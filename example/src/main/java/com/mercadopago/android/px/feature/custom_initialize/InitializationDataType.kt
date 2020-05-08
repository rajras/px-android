package com.mercadopago.android.px.feature.custom_initialize

sealed class InitializationDataType(val value: String) {
    class Locale(value: String): InitializationDataType(value)
    class PublicKey(value: String): InitializationDataType(value)
    class PreferenceId(value: String): InitializationDataType(value)
    class AccessToken(value: String): InitializationDataType(value)
}