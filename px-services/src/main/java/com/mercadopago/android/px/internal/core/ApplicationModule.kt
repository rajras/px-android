package com.mercadopago.android.px.internal.core

import android.content.Context
import android.content.SharedPreferences

open class ApplicationModule(val applicationContext: Context) : PreferenceComponent {

    override fun getSharedPreferences(): SharedPreferences {
        return applicationContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    val fileManager: FileManager
        get() = FileManager(applicationContext.cacheDir)

    companion object {
        private const val SHARED_PREFERENCE_NAME = "com.mercadopago.checkout.store"
    }
}