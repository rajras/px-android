package com.mercadopago.android.px.internal.util

internal object PayerCostHelper {
    fun getRatePercent(rateMap: Map<String, String>?, rate: RateType) = rateMap?.run { this[rate.name] }
}