package com.mercadopago.android.px.internal.viewmodel.mappers

import com.mercadopago.android.px.internal.viewmodel.DiscountBody
import com.mercadopago.android.px.internal.viewmodel.DiscountDetailModel
import com.mercadopago.android.px.internal.viewmodel.DiscountHeader
import com.mercadopago.android.px.model.DiscountDescriptionDetail

object DiscountConfigurationMapper: Mapper<DiscountDescriptionDetail, DiscountDetailModel>() {

    override fun map(model: DiscountDescriptionDetail): DiscountDetailModel {
        return DiscountDetailModel(
                DiscountHeader(model.title, model.subtitle, model.badge),
                DiscountBody(model.summary, model.description, model.legalTerms)
        )
    }
}