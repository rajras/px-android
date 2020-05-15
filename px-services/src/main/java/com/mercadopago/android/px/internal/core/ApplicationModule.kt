package com.mercadopago.android.px.internal.core

import android.content.Context
import android.content.SharedPreferences
import java.io.File

open class ApplicationModule(val applicationContext: Context) : PreferenceComponent {

    override fun getSharedPreferences(): SharedPreferences {
        return applicationContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    val fileManager: FileManager
        get() = FileManager()

    val cacheDir: File
        get() = applicationContext.cacheDir

    companion object {
        private const val SHARED_PREFERENCE_NAME = "com.mercadopago.checkout.store"
    }
}