package com.mercadopago.android.px.addons.model.internal

data class Configuration(val trackingMode: String?) {
    enum class TrackingMode {
        CONDITIONAL,
        NO_CONDITIONAL;

        fun match(trackingMode: String?): Boolean {
            return name.equals(trackingMode, ignoreCase = true)
        }
    }
}