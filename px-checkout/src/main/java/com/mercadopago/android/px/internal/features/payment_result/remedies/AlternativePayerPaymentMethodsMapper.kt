package com.mercadopago.android.px.internal.features.payment_result.remedies

import com.mercadopago.android.px.internal.model.EscStatus
import com.mercadopago.android.px.internal.viewmodel.mappers.Mapper
import com.mercadopago.android.px.model.CustomSearchItem
import com.mercadopago.android.px.model.SecurityCode
import com.mercadopago.android.px.model.internal.remedies.Installment
import com.mercadopago.android.px.model.internal.remedies.RemedyPaymentMethod

internal class AlternativePayerPaymentMethodsMapper(private val escCardIds: Set<String>) :
    Mapper<Triple<SecurityCode?, String, CustomSearchItem>, RemedyPaymentMethod>() {

    override fun map(it: Triple<SecurityCode?, String, CustomSearchItem>): RemedyPaymentMethod {
        val (securityCode, customOptionId, searchItem) = it
        return RemedyPaymentMethod(
            customOptionId,
            null,
            searchItem.issuer?.name,
            searchItem.lastFourDigits,
            searchItem.paymentMethodId,
            searchItem.type,
            securityCode?.length,
            securityCode?.cardLocation,
            null,
            searchItem.getAmountConfiguration(searchItem.defaultAmountConfiguration!!)
                .payerCosts
                .map { payerCost -> Installment(payerCost.installments, payerCost.totalAmount) },
            searchItem.escStatus ?: EscStatus.NOT_AVAILABLE,
            escCardIds.contains(customOptionId))
    }

    override fun map(data: Iterable<Triple<SecurityCode?, String, CustomSearchItem>>) = data.map { map(it) }
}
