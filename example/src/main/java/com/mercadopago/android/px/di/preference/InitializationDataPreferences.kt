package com.mercadopago.android.px.di.preference

import android.content.SharedPreferences
import com.mercadopago.android.px.feature.custom_initialize.InitializationData
import com.mercadopago.android.px.feature.custom_initialize.InitializationDataType
import com.mercadopago.android.px.internal.util.TextUtil

internal class InitializationDataPreferences(private val preferences: SharedPreferences) {

    fun saveInitializationData(data: InitializationData) {
        preferences.edit()?.apply {
            putString(LOCALE, data.locale.value)
            putString(PUBLIC_KEY, data.publicKey.value)
            putString(PREFERENCE_ID, data.preferenceId.value)
            putString(ACCESS_TOKEN, data.accessToken.value)
            apply()
        }
    }

    fun getInitializationData() = InitializationData(
        InitializationDataType.Locale(preferences.getString(LOCALE, DEFAULT_LOCALE) ?: DEFAULT_LOCALE),
        InitializationDataType.PublicKey(preferences.getString(PUBLIC_KEY, TextUtil.EMPTY) ?: TextUtil.EMPTY),
        InitializationDataType.PreferenceId(preferences.getString(PREFERENCE_ID, TextUtil.EMPTY) ?: TextUtil.EMPTY),
        InitializationDataType.AccessToken(preferences.getString(ACCESS_TOKEN, TextUtil.EMPTY) ?: TextUtil.EMPTY))

    companion object {
        private const val DEFAULT_LOCALE = "en-US"
        private const val LOCALE = "locale"
        private const val PUBLIC_KEY = "public_key"
        private const val PREFERENCE_ID = "preference_id"
        private const val ACCESS_TOKEN = "access_token"
    }
}