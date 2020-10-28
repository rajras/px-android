package com.mercadopago.android.px.internal.features.checkout

import com.mercadopago.android.px.internal.features.dummy_result.RedirectHelper
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository
import com.mercadopago.android.px.internal.viewmodel.BusinessPaymentModel
import com.mercadopago.android.px.internal.viewmodel.PaymentModel

class PostPaymentDriver(builder: Builder) {

    private val paymentSettingRepository = builder.paymentSettingRepository
    private val paymentModel = builder.paymentModel
    private val action = builder.action!!

    fun execute() {
        val hasRedirect = paymentModel.payment?.let {
            RedirectHelper.hasRedirect(paymentSettingRepository.checkoutPreference?.redirectUrls, it.paymentStatus)
        } ?: false
        when {
            hasRedirect -> action.skipCongrats(paymentModel)
            paymentModel is BusinessPaymentModel -> action.showCongrats(paymentModel)
            else -> action.showCongrats(paymentModel)
        }
    }

    class Builder(internal val paymentSettingRepository: PaymentSettingRepository, internal var paymentModel: PaymentModel) {
        internal var action: Action? = null
            private set

        fun action(action: Action) = apply { this.action = action }
        fun build(): PostPaymentDriver {
            checkNotNull(action) { "Missing action listener" }
            return PostPaymentDriver(this)
        }
    }

    interface Action {
        fun showCongrats(model: PaymentModel)
        fun showCongrats(model: BusinessPaymentModel)
        fun skipCongrats(model: PaymentModel)
    }
}
