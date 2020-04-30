package com.mercadopago.android.px.internal.features.payment_result.mappers

import com.mercadopago.android.px.R
import com.mercadopago.android.px.internal.features.payment_result.remedies.RemedyButton
import com.mercadopago.android.px.internal.features.payment_result.remedies.view.PaymentResultFooter
import com.mercadopago.android.px.internal.viewmodel.LazyString
import com.mercadopago.android.px.internal.viewmodel.mappers.Mapper
import com.mercadopago.android.px.model.internal.remedies.RemediesResponse

internal object PaymentResultFooterModelMapper : Mapper<RemediesResponse, PaymentResultFooter.Model>() {
    override fun map(response: RemediesResponse): PaymentResultFooter.Model {
        return when {
            response.suggestedPaymentMethod != null || response.cvv != null -> PaymentResultFooter.Model(null,
                RemedyButton(LazyString(R.string.px_change_payment), RemedyButton.Action.CHANGE_PM))
            response.highRisk != null -> PaymentResultFooter.Model(
                RemedyButton(LazyString(response.highRisk!!.actionLoud.label), RemedyButton.Action.KYC),
                RemedyButton(LazyString(R.string.px_change_payment), RemedyButton.Action.CHANGE_PM), false
            )
            else -> PaymentResultFooter.Model(null, null)
        }
    }
}