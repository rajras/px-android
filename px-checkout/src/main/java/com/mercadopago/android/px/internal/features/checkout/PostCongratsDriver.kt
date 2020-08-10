package com.mercadopago.android.px.internal.features.checkout

import com.mercadopago.android.px.internal.features.dummy_result.RedirectHelper
import com.mercadopago.android.px.internal.repository.PaymentRepository
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository
import com.mercadopago.android.px.model.Payment
import com.mercadopago.android.px.preferences.CheckoutPreference

class PostCongratsDriver(builder: Builder) {

    private val paymentSettingRepository = builder.paymentSettingRepository
    private val paymentRepository = builder.paymentRepository
    private val action = builder.action!!
    private val customResponseCode = builder.customResponseCode

    fun execute() {
        val payment = paymentRepository.payment
        paymentSettingRepository.checkoutPreference?.let { preference ->
            payment?.let { resolveRedirectUrls(preference, it.paymentStatus) }
        }
        //We only return the Payment object to respect our signature
        action.exitWith(customResponseCode, if (payment is Payment) payment else null)
    }

    private fun resolveRedirectUrls(preference: CheckoutPreference, paymentStatus: String) {
        RedirectHelper.resolveRedirect(paymentStatus,
            Pair(preference.redirectUrls, action::openInWebView),
            Pair(preference.backUrls, action::goToLink)
        )
    }

    class Builder(internal val paymentSettingRepository: PaymentSettingRepository,
        internal val paymentRepository: PaymentRepository) {
        internal var action: Action? = null
            private set
        internal var customResponseCode: Int? = null
            private set

        fun action(action: Action) = apply { this.action = action }
        fun customResponseCode(customResponseCode: Int?) = apply { this.customResponseCode = customResponseCode }
        fun build(): PostCongratsDriver {
            checkNotNull(action) { "Missing action listener" }
            return PostCongratsDriver(this)
        }
    }

    interface Action {
        fun openInWebView(link: String)
        fun goToLink(link: String)
        fun exitWith(customResponseCode: Int?, payment: Payment?)
    }
}