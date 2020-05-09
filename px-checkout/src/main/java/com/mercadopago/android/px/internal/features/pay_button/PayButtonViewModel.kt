package com.mercadopago.android.px.internal.features.pay_button

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations.map
import android.os.Bundle
import com.mercadopago.android.px.addons.model.SecurityValidationData
import com.mercadopago.android.px.internal.base.BaseViewModel
import com.mercadopago.android.px.internal.callbacks.Event
import com.mercadopago.android.px.internal.core.ConnectionHelper
import com.mercadopago.android.px.internal.core.ProductIdProvider
import com.mercadopago.android.px.internal.features.explode.ExplodeDecoratorMapper
import com.mercadopago.android.px.internal.features.pay_button.PayButton.OnReadyForPaymentCallback
import com.mercadopago.android.px.internal.features.pay_button.UIProgress.*
import com.mercadopago.android.px.internal.features.pay_button.UIResult.VisualProcessorResult
import com.mercadopago.android.px.internal.repository.CongratsRepository
import com.mercadopago.android.px.internal.repository.PaymentRepository
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository
import com.mercadopago.android.px.internal.util.ApiUtil
import com.mercadopago.android.px.internal.util.SecurityValidationDataFactory
import com.mercadopago.android.px.internal.viewmodel.PaymentModel
import com.mercadopago.android.px.internal.viewmodel.mappers.PayButtonViewModelMapper
import com.mercadopago.android.px.model.Card
import com.mercadopago.android.px.model.PaymentRecovery
import com.mercadopago.android.px.model.exceptions.MercadoPagoError
import com.mercadopago.android.px.model.exceptions.NoConnectivityException
import com.mercadopago.android.px.model.internal.PaymentConfiguration
import com.mercadopago.android.px.tracking.internal.events.ConfirmEvent
import com.mercadopago.android.px.tracking.internal.events.FrictionEventTracker
import com.mercadopago.android.px.tracking.internal.model.ConfirmData
import com.mercadopago.android.px.tracking.internal.model.Reason
import com.mercadopago.android.px.tracking.internal.views.OneTapViewTracker
import com.mercadopago.android.px.internal.viewmodel.PayButtonViewModel as ButtonConfig

internal class PayButtonViewModel(
    private var paymentService: PaymentRepository,
    private var productIdProvider: ProductIdProvider,
    private var connectionHelper: ConnectionHelper,
    private var congratsRepository: CongratsRepository,
    paymentSettingRepository: PaymentSettingRepository) : BaseViewModel(), PayButton.ViewModel {

    val buttonTextLiveData = MutableLiveData<ButtonConfig>()
    private var buttonConfig: ButtonConfig = PayButtonViewModelMapper().map(
        paymentSettingRepository.advancedConfiguration.customStringConfiguration)

    init {
        buttonTextLiveData.value = buttonConfig
    }

    private var handler: PayButton.Handler? = null
    private var confirmTrackerData: ConfirmData? = null
    private var paymentConfiguration: PaymentConfiguration? = null

    private val serviceLiveData = paymentService.observableEvents

    private val paymentErrorLiveData: LiveData<ButtonLoadingCanceled?> =
        transform(serviceLiveData.paymentErrorLiveData) { error ->
            val shouldHandleError = error.isInternalServerError || error.isNoConnectivityError
            if (shouldHandleError) handleError(error) else handler?.onPaymentError(error)
            ButtonLoadingCanceled
        }

    private val visualPaymentLiveData: LiveData<VisualProcessorResult?> =
        transform(serviceLiveData.visualPaymentLiveData) { VisualProcessorResult }

    private val paymentFinishedLiveData: LiveData<ButtonLoadingFinished?> =
        transform(serviceLiveData.paymentFinishedLiveData) { paymentModel ->
            ButtonLoadingFinished(ExplodeDecoratorMapper().map(paymentModel))
        }

    val cvvRequiredLiveData: LiveData<Pair<Card, Reason>?> = transform(serviceLiveData.requireCvvLiveData) { it }

    val recoverRequiredLiveData: LiveData<PaymentRecovery?> =
        transform(serviceLiveData.recoverInvalidEscLiveData) { it.takeIf { it.shouldAskForCvv() } }

    val stateUILiveData = MediatorLiveData<PayButtonState>().also {
        it.addSource(paymentErrorLiveData) { value -> it.value = value }
        it.addSource(visualPaymentLiveData) { value -> it.value = value }
        it.addSource(paymentFinishedLiveData) { value -> it.value = value }
    }

    private fun <T : Event<X>, X, I> transform(liveData: LiveData<T>, block: (content: X) -> I): LiveData<I?> {
        return map(liveData) { event -> event.getContentIfNotHandled()?.let { block(it) } }
    }

    override fun attach(handler: PayButton.Handler) {
        this.handler = handler
    }

    override fun detach() {
        handler = null
    }

    override fun preparePayment() {
        paymentConfiguration = null
        confirmTrackerData = null
        if (connectionHelper.checkConnection()) {
            handler?.prePayment(object : OnReadyForPaymentCallback {
                override fun call(paymentConfiguration: PaymentConfiguration, confirmTrackerData: ConfirmData?) {
                    startSecuredPayment(paymentConfiguration, confirmTrackerData)
                }
            })
        } else {
            manageNoConnection()
        }
    }

    private fun startSecuredPayment(paymentConfiguration: PaymentConfiguration, confirmTrackerData: ConfirmData?) {
        this.paymentConfiguration = paymentConfiguration
        this.confirmTrackerData = confirmTrackerData
        val data: SecurityValidationData = SecurityValidationDataFactory
            .create(productIdProvider, paymentConfiguration)
        stateUILiveData.value = FingerprintRequired(data)
    }

    override fun handleBiometricsResult(isSuccess: Boolean) {
        if (isSuccess) {
            startPayment()
        } else {
            FrictionEventTracker
                .with(OneTapViewTracker.PATH_REVIEW_ONE_TAP_VIEW, FrictionEventTracker.Id.GENERIC,
                    FrictionEventTracker.Style.CUSTOM_COMPONENT).track()
        }
    }

    override fun startPayment() {
        if (paymentService.isExplodingAnimationCompatible) {
            stateUILiveData.postValue(ButtonLoadingStarted(paymentService.paymentTimeout, buttonConfig))
        }
        handler?.enqueueOnExploding(object : PayButton.OnEnqueueResolvedCallback {
            override fun success() {
                paymentService.startExpressPayment(paymentConfiguration!!)

                confirmTrackerData?.let { ConfirmEvent(it).track() }
            }

            override fun failure() {
                stateUILiveData.value = (ButtonLoadingCanceled)
            }
        })
    }

    override fun onPostPayment(paymentModel: PaymentModel) {
        stateUILiveData.value = ButtonLoadingFinished(ExplodeDecoratorMapper().map(paymentModel))
    }

    override fun onRecoverPaymentEscInvalid(recovery: PaymentRecovery) {
        recoverPayment(recovery)
    }

    override fun recoverPayment() {
        recoverPayment(paymentService.createPaymentRecovery())
    }

    private fun recoverPayment(recovery: PaymentRecovery) {
        // hack but this stimulation could come from service or front, so we redirect it to one source
        serviceLiveData.recoverInvalidEscLiveData.value = Event(recovery)
    }

    private fun manageNoConnection() {
        val exception = NoConnectivityException()
        val apiException = ApiUtil.getApiException(exception)
        val error = MercadoPagoError(apiException, null)
        handleError(error)
    }

    private fun handleError(error: MercadoPagoError) {
        FrictionEventTracker.with(OneTapViewTracker.PATH_REVIEW_ONE_TAP_VIEW,
            FrictionEventTracker.Id.GENERIC, FrictionEventTracker.Style.CUSTOM_COMPONENT, error).track()
        stateUILiveData.value = UIError.ConnectionError(error)
    }

    override fun hasFinishPaymentAnimation() {
        paymentService.payment?.let { handler?.onPaymentFinished(it) }
    }

    override fun storeInBundle(bundle: Bundle) {
        bundle.putParcelable(BUNDLE_PAYMENT_CONFIGURATION, paymentConfiguration)
        bundle.putParcelable(BUNDLE_CONFIRM_DATA, confirmTrackerData)
    }

    override fun recoverFromBundle(bundle: Bundle) {
        paymentConfiguration = bundle.getParcelable(BUNDLE_PAYMENT_CONFIGURATION)
        confirmTrackerData = bundle.getParcelable(BUNDLE_CONFIRM_DATA)
    }

    companion object {
        const val BUNDLE_PAYMENT_CONFIGURATION = "BUNDLE_PAYMENT_CONFIGURATION"
        const val BUNDLE_CONFIRM_DATA = "BUNDLE_CONFIRM_DATA"
    }
}