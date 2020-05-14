package com.mercadopago.android.px.di.module

import android.content.Context
import android.content.SharedPreferences
import com.mercadopago.android.px.di.preference.InitializationDataPreferences

internal class LocalRepositoryModule(applicationContext: Context) {

    private val preferences: SharedPreferences
        by lazy { applicationContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE) }

    val initializationDataPreferences: InitializationDataPreferences
        by lazy { InitializationDataPreferences(preferences) }

    companion object {
        private const val PREFERENCES_NAME = "com.mercadolibre.android.testapp.store"
    }
}