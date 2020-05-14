package com.mercadopago.android.px.feature.custom_initialize

data class InitializationData(
    var locale: InitializationDataType.Locale,
    var publicKey: InitializationDataType.PublicKey,
    var preferenceId: InitializationDataType.PreferenceId,
    var accessToken: InitializationDataType.AccessToken) {

    fun updateModel(locale: String, publicKey: String, preferenceId: String, accessToken: String) {
        this.locale = InitializationDataType.Locale(locale)
        this.publicKey = InitializationDataType.PublicKey(publicKey)
        this.preferenceId = InitializationDataType.PreferenceId(preferenceId)
        this.accessToken = InitializationDataType.AccessToken(accessToken)
    }

    fun updateModel(data: InitializationDataType) {
        when (data) {
            is InitializationDataType.Locale -> locale = data
            is InitializationDataType.PublicKey -> publicKey = data
            is InitializationDataType.PreferenceId -> preferenceId = data
            is InitializationDataType.AccessToken -> accessToken = data
        }
    }
}