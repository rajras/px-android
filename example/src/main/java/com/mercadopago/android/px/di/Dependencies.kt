package com.mercadopago.android.px.di

import android.content.Context
import com.mercadopago.android.px.di.module.LocalRepositoryModule
import com.mercadopago.android.px.di.module.ViewModelModule

internal class Dependencies {

    var viewModelModule: ViewModelModule? = null
        private set
    var localPreferences: LocalRepositoryModule? = null
        private set

    fun initialize(context: Context) {
        localPreferences = LocalRepositoryModule(context.applicationContext)
        viewModelModule = ViewModelModule()
    }

    fun clean() {
        viewModelModule = null
        localPreferences =  null
    }

    companion object {
        @JvmStatic val instance: Dependencies by lazy { Dependencies() }
    }
}