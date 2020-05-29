package com.mercadopago.android.px.internal.di

import android.content.Context
import com.mercadopago.android.px.internal.core.ApplicationModule
import com.mercadopago.android.px.internal.core.FlowIdProvider
import com.mercadopago.android.px.internal.core.ProductIdProvider
import com.mercadopago.android.px.internal.core.SessionIdProvider
import com.mercadopago.android.px.internal.util.RetrofitUtil
import retrofit2.Retrofit

class NetworkModule(context: Context) : ApplicationModule(context) {

    val retrofitClient: Retrofit by lazy { RetrofitUtil.getRetrofitClient(applicationContext) }
    val flowIdProvider by lazy { FlowIdProvider(applicationContext) }
    val productIdProvider by lazy { ProductIdProvider(sharedPreferences) }
    val sessionIdProvider by lazy { SessionIdProvider(sharedPreferences) }

    fun clear() {
        //TODO: need to refactor how flow is setted to be able to clear it between sessions
        //flowIdProvider.clear()
        productIdProvider.clear()
        sessionIdProvider.clear()
    }

    companion object {
        lateinit var INSTANCE: NetworkModule
        @JvmStatic fun initialize(context: Context) {
            INSTANCE = NetworkModule(context.applicationContext ?: context)
        }
    }
}