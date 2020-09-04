package com.mercadopago.android.px.internal.features.express

data class OneTapModel(
    val paymentTypeId: String,
    val hasSplit: Boolean,
    val id: String,
    val discountModel: DiscountModel
)