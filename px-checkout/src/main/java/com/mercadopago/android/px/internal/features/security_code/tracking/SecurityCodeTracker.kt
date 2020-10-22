package com.mercadopago.android.px.internal.features.security_code.tracking

class SecurityCodeTracker(
    private val securityCodeViewTrack: SecurityCodeViewTrack,
    private val confirmSecurityCodeTrack: ConfirmSecurityCodeTrack,
    private val abortSecurityCodeTrack: AbortSecurityCodeTrack,
    private val securityCodeFrictions: SecurityCodeFrictions) {

    fun trackConfirmSecurityCode() {
        confirmSecurityCodeTrack.track()
    }

    fun trackSecurityCode() {
        securityCodeViewTrack.track()
    }

    fun trackAbortSecurityCode() {
        abortSecurityCodeTrack.track()
    }

    fun trackPaymentApiError() {
        securityCodeFrictions.paymentApiErrorFriction().track()
    }

    fun trackTokenApiError() {
        securityCodeFrictions.tokenApiErrorFriction().track()
    }
}