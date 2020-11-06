package com.mercadopago.android.px.tracking.internal.model

import com.mercadopago.android.px.model.PaymentData

class PaymentDataExtraInfo private constructor(val selectedInstallment: PayerCostInfo?,
    val cardId: String?,
    val hasEsc: Boolean,
    val issuerId: Long?) : TrackingMapModel() {

    companion object {
        @JvmStatic
        fun resultPaymentDataExtraInfo(paymentData: PaymentData): PaymentDataExtraInfo {
            val payerCost = paymentData.payerCost
            val token = paymentData.token
            val issuerId = paymentData.issuer?.id

            return PaymentDataExtraInfo(
                payerCost?.let { PayerCostInfo(it) },
                token?.cardId,
                token?.esc != null,
                issuerId)
        }
    }
}