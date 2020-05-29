package com.mercadopago.android.px.feature.custom_initialize

internal sealed class ConfigurationDataType

internal sealed class ConfigurationStringData(val value: String) : ConfigurationDataType() {
    class Locale(value: String) : ConfigurationStringData(value)
    class PublicKey(value: String) : ConfigurationStringData(value)
    class PreferenceId(value: String) : ConfigurationStringData(value)
    class AccessToken(value: String) : ConfigurationStringData(value)
}

internal sealed class ConfigurationBooleanData(val value: Boolean) : ConfigurationDataType() {
    class OneTap(value: Boolean) : ConfigurationBooleanData(value)
}