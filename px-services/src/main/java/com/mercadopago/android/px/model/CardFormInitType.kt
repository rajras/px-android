package com.mercadopago.android.px.model

import com.google.gson.annotations.SerializedName

enum class CardFormInitType {
    @SerializedName("standard") STANDARD,
    @SerializedName("webpay_tbk") WEB_PAY
}