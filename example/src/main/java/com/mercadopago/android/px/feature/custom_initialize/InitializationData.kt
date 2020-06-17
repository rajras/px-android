package com.mercadopago.android.px.feature.custom_initialize

internal data class InitializationData(
    var locale: ConfigurationStringData.Locale,
    var publicKey: ConfigurationStringData.PublicKey,
    var preferenceId: ConfigurationStringData.PreferenceId,
    var accessToken: ConfigurationStringData.AccessToken,
    var oneTap: ConfigurationBooleanData.OneTap,
    var processor: ConfigurationDataType.Processor) {

    fun updateModel(locale: String, publicKey: String, preferenceId: String, accessToken: String, oneTap: Boolean,
        processor: String) {
        this.locale = ConfigurationStringData.Locale(locale)
        this.publicKey = ConfigurationStringData.PublicKey(publicKey)
        this.preferenceId = ConfigurationStringData.PreferenceId(preferenceId)
        this.accessToken = ConfigurationStringData.AccessToken(accessToken)
        this.oneTap = ConfigurationBooleanData.OneTap(oneTap)
        this.processor = ConfigurationDataType.Processor(ProcessorType.valueOf(processor))
    }

    fun updateModel(data: ConfigurationDataType) {
        when (data) {
            is ConfigurationStringData.Locale -> locale = data
            is ConfigurationStringData.PublicKey -> publicKey = data
            is ConfigurationStringData.PreferenceId -> preferenceId = data
            is ConfigurationStringData.AccessToken -> accessToken = data
            is ConfigurationBooleanData.OneTap -> oneTap = data
            is ConfigurationDataType.Processor -> processor = data
        }
    }
}