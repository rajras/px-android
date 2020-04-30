package com.mercadopago.android.px.internal.features.payment_result.remedies

import com.mercadopago.android.px.internal.features.pay_button.PayButton
import com.mercadopago.android.px.model.IPaymentDescriptor

internal interface Remedies {
    interface View {
        fun onPrePayment(callback: PayButton.OnReadyForPaymentCallback)
        fun onPayButtonPressed(callback: PayButton.OnEnqueueResolvedCallback)
        fun onPaymentFinished(payment: IPaymentDescriptor)
    }

    interface ViewModel {
        fun onPrePayment(callback: PayButton.OnReadyForPaymentCallback)
        fun onPayButtonPressed(callback: PayButton.OnEnqueueResolvedCallback)
        fun onCvvFilled(cvv: String)
        fun onButtonPressed(action: RemedyButton.Action)
    }
}