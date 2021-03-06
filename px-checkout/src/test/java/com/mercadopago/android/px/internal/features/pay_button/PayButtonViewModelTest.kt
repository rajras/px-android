package com.mercadopago.android.px.internal.features.pay_button

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mercadopago.android.px.any
import com.mercadopago.android.px.argumentCaptor
import com.mercadopago.android.px.internal.callbacks.PaymentServiceEventHandler
import com.mercadopago.android.px.internal.core.ConnectionHelper
import com.mercadopago.android.px.internal.core.ProductIdProvider
import com.mercadopago.android.px.internal.datasource.CustomTextsRepositoryImpl
import com.mercadopago.android.px.internal.datasource.PaymentService
import com.mercadopago.android.px.internal.datasource.PaymentSettingService
import com.mercadopago.android.px.internal.features.PaymentResultViewModelFactory
import com.mercadopago.android.px.internal.features.checkout.PostPaymentUrlsMapper
import com.mercadopago.android.px.internal.features.explode.ExplodeDecorator
import com.mercadopago.android.px.internal.features.express.RenderMode
import com.mercadopago.android.px.internal.features.payment_congrats.model.PaymentCongratsModelMapper
import com.mercadopago.android.px.internal.features.payment_result.PaymentResultDecorator
import com.mercadopago.android.px.internal.features.payment_result.remedies.RemediesModel
import com.mercadopago.android.px.internal.features.security_code.model.SecurityCodeParams
import com.mercadopago.android.px.internal.livedata.MutableSingleLiveData
import com.mercadopago.android.px.internal.mappers.PayButtonViewModelMapper
import com.mercadopago.android.px.internal.model.SecurityType
import com.mercadopago.android.px.internal.viewmodel.BusinessPaymentModel
import com.mercadopago.android.px.internal.viewmodel.PaymentModel
import com.mercadopago.android.px.internal.viewmodel.PaymentResultType
import com.mercadopago.android.px.model.*
import com.mercadopago.android.px.model.exceptions.MercadoPagoError
import com.mercadopago.android.px.model.internal.CustomTexts
import com.mercadopago.android.px.model.internal.PaymentConfiguration
import com.mercadopago.android.px.model.internal.remedies.RemediesResponse
import com.mercadopago.android.px.preferences.CheckoutPreference
import com.mercadopago.android.px.tracking.internal.MPTracker
import com.mercadopago.android.px.tracking.internal.model.Reason
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.internal.matchers.apachecommons.ReflectionEquals
import org.mockito.junit.MockitoJUnitRunner
import com.mercadopago.android.px.internal.viewmodel.PayButtonViewModel as PayButtonTexts

@RunWith(MockitoJUnitRunner::class)
internal class PayButtonViewModelTest {

    private lateinit var payButtonViewModel: PayButtonViewModel

    @Mock
    private lateinit var paymentService: PaymentService
    @Mock
    private lateinit var productIdProvider: ProductIdProvider
    @Mock
    private lateinit var connectionHelper: ConnectionHelper
    @Mock
    private lateinit var paymentSettingService: PaymentSettingService
    @Mock
    private lateinit var customTextsRepositoryImpl: CustomTextsRepositoryImpl
    @Mock
    private lateinit var payButtonViewModelMapper: PayButtonViewModelMapper
    @Mock
    private lateinit var paymentCongratsMapper: PaymentCongratsModelMapper
    @Mock
    private lateinit var customTexts: CustomTexts
    @Mock
    private lateinit var payButtonTexts: PayButtonTexts
    @Mock
    private lateinit var handler: PayButton.Handler
    @Mock
    private lateinit var buttonTextObserver: Observer<PayButtonTexts>
    @Mock
    private lateinit var uiStateObserver: Observer<PayButtonUiState>
    @Mock
    private lateinit var cvvRequiredObserver: Observer<SecurityCodeParams>
    @Mock
    private lateinit var paymentResultViewModelFactory: PaymentResultViewModelFactory

    private val paymentErrorLiveData = MutableSingleLiveData<MercadoPagoError>()
    private val paymentFinishedLiveData = MutableSingleLiveData<PaymentModel>()
    private val requireCvvLiveData = MutableSingleLiveData<Pair<Card,Reason>>()
    private val recoverInvalidEscLiveData = MutableSingleLiveData<PaymentRecovery>()
    private val visualPaymentLiveData = MutableSingleLiveData<Unit>()

    /*
    * https://stackoverflow.com/questions/29945087/kotlin-and-new-activitytestrule-the-rule-must-be-public
    * */
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        `when`(customTextsRepositoryImpl.customTexts).thenReturn(customTexts)
        `when`(payButtonViewModelMapper.map(customTexts)).thenReturn(payButtonTexts)
        `when`(connectionHelper.checkConnection()).thenReturn(true)
        `when`(paymentSettingService.checkoutPreference).thenReturn(mock(CheckoutPreference::class.java))
        configurePaymentSettingServiceObservableEvents()

        payButtonViewModel = PayButtonViewModel(
            paymentService,
            productIdProvider,
            connectionHelper,
            paymentSettingService,
            customTextsRepositoryImpl,
            payButtonViewModelMapper,
            paymentCongratsMapper,
            mock(PostPaymentUrlsMapper::class.java),
            paymentResultViewModelFactory,
            mock(MPTracker::class.java))

        payButtonViewModel.stateUILiveData.observeForever(uiStateObserver)
        payButtonViewModel.buttonTextLiveData.observeForever(buttonTextObserver)
        payButtonViewModel.cvvRequiredLiveData.observeForever(cvvRequiredObserver)
        payButtonViewModel.attach(handler)

        val state = mock(PayButtonViewModel.State::class.java)
        `when`(state.paymentConfiguration).thenReturn(mock(PaymentConfiguration::class.java))
        payButtonViewModel.restoreState(state)

        verify(buttonTextObserver).onChanged(any(PayButtonTexts::class.java))
        assertNotNull(handler)
    }

    @Test
    fun preparePaymentWhenNonConnection() {
        `when`(connectionHelper.checkConnection()).thenReturn(false)
        payButtonViewModel.preparePayment()
        verify(uiStateObserver).onChanged(any(UIError.ConnectionError::class.java))
    }

    @Test
    fun preparePaymentWhenHasConnection() {
        val callback = argumentCaptor<PayButton.OnReadyForPaymentCallback>()
        payButtonViewModel.preparePayment()
        verify(handler).prePayment(callback.capture())
        callback.value.call(mock(PaymentConfiguration::class.java))
        verify(uiStateObserver).onChanged(any(UIProgress.FingerprintRequired::class.java))
    }

    @Test
    fun handleBiometricsResultWithoutFrictionAndSuccess() {
        val callback = argumentCaptor<PayButton.OnEnqueueResolvedCallback>()

        payButtonViewModel.handleBiometricsResult(isSuccess = true, securityRequested = true)

        verify(uiStateObserver).onChanged(any(UIProgress.ButtonLoadingStarted::class.java))
        verify(paymentSettingService).configure(SecurityType.SECOND_FACTOR)
        verify(handler).enqueueOnExploding(callback.capture())
        callback.value.success()
        verify(paymentService).startExpressPayment(any(PaymentConfiguration::class.java))
    }

    @Test
    fun handleBiometricsResultWithoutFrictionAndFailure() {
        val callback = argumentCaptor<PayButton.OnEnqueueResolvedCallback>()

        payButtonViewModel.handleBiometricsResult(isSuccess = true, securityRequested = true)

        verify(paymentSettingService).configure(SecurityType.SECOND_FACTOR)
        verify(handler).enqueueOnExploding(callback.capture())
        callback.value.failure()
        verify(uiStateObserver).onChanged(any(UIProgress.ButtonLoadingCanceled::class.java))
    }

    @Test
    fun startPaymentAndObserveServiceWhenIsErrorEventAndIsPaymentProcessingError() {
        val callback = argumentCaptor<PayButton.OnEnqueueResolvedCallback>()
        val error = mock(MercadoPagoError::class.java)
        val payButtonViewModelSpy = spy(payButtonViewModel)
        `when`(error.isPaymentProcessing).thenReturn(true)
        `when`(paymentSettingService.currency).thenReturn(mock(Currency::class.java))
        `when`(paymentService.paymentDataList).thenReturn(mock(MutableList::class.java) as MutableList<PaymentData>)
        `when`(paymentResultViewModelFactory.createPaymentResultDecorator(any())).thenReturn(mock(PaymentResultDecorator::class.java))

        payButtonViewModelSpy.startPayment()
        paymentErrorLiveData.value = error

        verify(handler).enqueueOnExploding(callback.capture())
        callback.value.success()
        verify(payButtonViewModelSpy).onPostPayment(any(PaymentModel::class.java))
        verify(uiStateObserver).onChanged(any(UIProgress.ButtonLoadingFinished::class.java))
        verify(handler).onPaymentError(error)
        verify(uiStateObserver).onChanged(any(UIProgress.ButtonLoadingCanceled::class.java))
    }

    @Test
    fun startPaymentAndObserveServiceWhenIsErrorEventAndIsNoRecoverableError() {
        val callback = argumentCaptor<PayButton.OnEnqueueResolvedCallback>()
        val error = mock(MercadoPagoError::class.java)

        `when`(error.isPaymentProcessing).thenReturn(false)

        payButtonViewModel.startPayment()
        paymentErrorLiveData.value = error

        verify(handler).enqueueOnExploding(callback.capture())
        callback.value.success()
        verify(uiStateObserver).onChanged(any(UIError.BusinessError::class.java))
        verify(handler).onPaymentError(error)
        verify(uiStateObserver).onChanged(any(UIProgress.ButtonLoadingCanceled::class.java))
    }

    @Test
    fun startPaymentAndObserveServiceWhenIsVisualPaymentEvent() {
        val callback = argumentCaptor<PayButton.OnEnqueueResolvedCallback>()

        payButtonViewModel.startPayment()
        visualPaymentLiveData.value = Unit

        verify(handler).enqueueOnExploding(callback.capture())
        callback.value.success()
        verify(uiStateObserver).onChanged(any(UIResult.VisualProcessorResult::class.java))
    }

    @Test
    fun startPaymentAndObserveServiceWhenIsPaymentFinishedEventAndIsRemedies() {
        val callback = argumentCaptor<PayButton.OnEnqueueResolvedCallback>()
        val paymentModel = mock(PaymentModel::class.java)
        `when`(paymentModel.remedies).thenReturn(mock(RemediesResponse::class.java))
        `when`(paymentModel.remedies.hasRemedies()).thenReturn(true)

        payButtonViewModel.startPayment()
        paymentFinishedLiveData.value = paymentModel

        verify(handler).enqueueOnExploding(callback.capture())
        callback.value.success()
        verify(uiStateObserver).onChanged(any(UIProgress.ButtonLoadingFinished::class.java))

        val actual = (payButtonViewModel.stateUILiveData.value as UIProgress.ButtonLoadingFinished)
        assertTrue(ReflectionEquals(actual.explodeDecorator).matches(ExplodeDecorator.from(RemediesModel.DECORATOR)))
    }

    @Test
    fun startPaymentAndObserveServiceWhenIsPaymentFinishedEventAndIsBusiness() {
        val callback = argumentCaptor<PayButton.OnEnqueueResolvedCallback>()
        val paymentModel = mock(BusinessPaymentModel::class.java)
        val payment = mock(BusinessPayment::class.java)
        `when`(paymentModel.remedies).thenReturn(mock(RemediesResponse::class.java))
        `when`(paymentModel.remedies.hasRemedies()).thenReturn(false)
        `when`(paymentModel.payment).thenReturn(payment)
        `when`(paymentModel.payment.decorator).thenReturn(BusinessPayment.Decorator.APPROVED)

        payButtonViewModel.startPayment()
        paymentFinishedLiveData.value = paymentModel

        verify(handler).enqueueOnExploding(callback.capture())
        callback.value.success()
        verify(uiStateObserver).onChanged(any(UIProgress.ButtonLoadingFinished::class.java))

        val actual = (payButtonViewModel.stateUILiveData.value as UIProgress.ButtonLoadingFinished)
        assertTrue(ReflectionEquals(actual.explodeDecorator).matches(ExplodeDecorator.from(PaymentResultType.from(payment.decorator))))
    }

    @Test
    fun startPaymentAndObserveServiceWhenIsPaymentFinishedEventAndIsPaymentResult() {
        val callback = argumentCaptor<PayButton.OnEnqueueResolvedCallback>()
        val paymentModel = mock(PaymentModel::class.java)
        val paymentResult = mock(PaymentResult::class.java)
        `when`(paymentModel.remedies).thenReturn(mock(RemediesResponse::class.java))
        `when`(paymentModel.remedies.hasRemedies()).thenReturn(false)
        `when`(paymentModel.paymentResult).thenReturn(paymentResult)
        `when`(paymentResultViewModelFactory.createPaymentResultDecorator(any())).thenReturn(mock(PaymentResultDecorator::class.java))

        payButtonViewModel.startPayment()
        paymentFinishedLiveData.value = paymentModel

        verify(handler).enqueueOnExploding(callback.capture())
        callback.value.success()
        verify(uiStateObserver).onChanged(any(UIProgress.ButtonLoadingFinished::class.java))
    }

    @Test
    fun startPaymentAndObserveServiceWhenIsPaymentCvvRequiredEvent() {
        val callback = argumentCaptor<PayButton.OnEnqueueResolvedCallback>()
        val paymentConfigurationCaptor = argumentCaptor<PaymentConfiguration>()
        val onChangeCaptor = argumentCaptor<SecurityCodeParams>()
        val reason = mock(Reason::class.java)
        val card = mock(Card::class.java)
        val cvvRequested = mock(PayButton.CvvRequestedModel::class.java)
        `when`(cvvRequested.renderMode).thenReturn(mock(RenderMode::class.java))
        `when`(handler.onCvvRequested()).thenReturn(cvvRequested)

        payButtonViewModel.startPayment()
        requireCvvLiveData.value = Pair(card,reason)

        verify(handler).enqueueOnExploding(callback.capture())
        callback.value.success()
        verify(paymentService).startExpressPayment(paymentConfigurationCaptor.capture())
        verify(handler).onCvvRequested()
        verify(cvvRequiredObserver).onChanged(onChangeCaptor.capture())
        val actualResult = onChangeCaptor.value
        assertTrue(ReflectionEquals(actualResult.paymentConfiguration).matches(paymentConfigurationCaptor.value))
        assertTrue(ReflectionEquals(actualResult.reason).matches(reason))
    }

    @Test
    fun startPaymentAndObserveServiceWhenIsInvalidEscEventAndShouldAskForCvv() {
        val callback = argumentCaptor<PayButton.OnEnqueueResolvedCallback>()
        val paymentConfigurationCaptor = argumentCaptor<PaymentConfiguration>()
        val onChangeCaptor = argumentCaptor<SecurityCodeParams>()
        val paymentRecoveryMock = mock(PaymentRecovery::class.java)
        `when`(paymentRecoveryMock.shouldAskForCvv()).thenReturn(true)
        val cvvRequested = mock(PayButton.CvvRequestedModel::class.java)
        `when`(cvvRequested.renderMode).thenReturn(mock(RenderMode::class.java))
        `when`(handler.onCvvRequested()).thenReturn(cvvRequested)

        payButtonViewModel.startPayment()
        recoverInvalidEscLiveData.value = paymentRecoveryMock

        verify(handler).enqueueOnExploding(callback.capture())
        callback.value.success()
        verify(paymentService).startExpressPayment(paymentConfigurationCaptor.capture())
        verify(handler).onCvvRequested()
        verify(cvvRequiredObserver).onChanged(onChangeCaptor.capture())
        val actualResult = onChangeCaptor.value
        assertTrue(ReflectionEquals(actualResult.paymentConfiguration).matches(paymentConfigurationCaptor.value))
        assertTrue(ReflectionEquals(actualResult.paymentRecovery).matches(paymentRecoveryMock))
    }

    private fun configurePaymentSettingServiceObservableEvents() {
        `when`(paymentService.observableEvents).thenReturn(mock(PaymentServiceEventHandler::class.java))
        `when`(paymentService.isExplodingAnimationCompatible).thenReturn(true)
        `when`(paymentService.observableEvents?.paymentErrorLiveData).thenReturn(paymentErrorLiveData)
        `when`(paymentService.observableEvents?.paymentFinishedLiveData).thenReturn(paymentFinishedLiveData)
        `when`(paymentService.observableEvents?.requireCvvLiveData).thenReturn(requireCvvLiveData)
        `when`(paymentService.observableEvents?.recoverInvalidEscLiveData).thenReturn(recoverInvalidEscLiveData)
        `when`(paymentService.observableEvents?.visualPaymentLiveData).thenReturn(visualPaymentLiveData)
    }
}
