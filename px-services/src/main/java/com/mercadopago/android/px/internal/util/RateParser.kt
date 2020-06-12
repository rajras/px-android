package com.mercadopago.android.px.internal.util

import java.util.HashMap

object RateParser {

    private const val CFT = "CFT"
    private val CFTNA = "CFTNA"
    private val TEA = "TEA"

    fun getRates(labels: List<String>?): Map<String, String> {
        val ratesMap: MutableMap<String, String> = HashMap()
        if (!labels.isNullOrEmpty()) {
            for (label in labels) {
                if (label.contains(CFTNA) || label.contains(CFT) || label.contains(TEA)) {
                    val ratesRaw = label.split("|").toTypedArray()
                    for (rate in ratesRaw) {
                        val rates = rate.split("_").toTypedArray()
                        ratesMap[rates[0]] = rates[1]
                    }
                }
            }
        }
        return ratesMap
    }
}