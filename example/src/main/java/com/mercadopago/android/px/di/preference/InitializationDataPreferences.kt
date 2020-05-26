package com.mercadopago.android.px.di.preference

import android.content.SharedPreferences
import com.mercadopago.android.px.feature.custom_initialize.ConfigurationBooleanData
import com.mercadopago.android.px.feature.custom_initialize.ConfigurationStringData
import com.mercadopago.android.px.feature.custom_initialize.InitializationData
import com.mercadopago.android.px.internal.util.TextUtil

internal class InitializationDataPreferences(private val preferences: SharedPreferences) {

    fun saveInitializationData(data: InitializationData) {
        preferences.edit()?.apply {
            putString(LOCALE, data.locale.value)
            putString(PUBLIC_KEY, data.publicKey.value)
            putString(PREFERENCE_ID, data.preferenceId.value)
            putString(ACCESS_TOKEN, data.accessToken.value)
            putBoolean(ONE_TAP, data.oneTap.value)
            apply()
        }
    }

    fun getInitializationData() = InitializationData(
        ConfigurationStringData.Locale(preferences.getString(LOCALE, DEFAULT_LOCALE) ?: DEFAULT_LOCALE),
        ConfigurationStringData.PublicKey(preferences.getString(PUBLIC_KEY, TextUtil.EMPTY) ?: TextUtil.EMPTY),
        ConfigurationStringData.PreferenceId(preferences.getString(PREFERENCE_ID, TextUtil.EMPTY) ?: TextUtil.EMPTY),
        ConfigurationStringData.AccessToken(preferences.getString(ACCESS_TOKEN, TextUtil.EMPTY) ?: TextUtil.EMPTY),
        ConfigurationBooleanData.OneTap(preferences.getBoolean(ONE_TAP, true)))

    companion object {
        private const val DEFAULT_LOCALE = "en-US"
        private const val LOCALE = "locale"
        private const val PUBLIC_KEY = "public_key"
        private const val PREFERENCE_ID = "preference_id"
        private const val ACCESS_TOKEN = "access_token"
        private const val ONE_TAP = "one_tap"
    }
}