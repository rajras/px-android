package com.mercadopago.android.px.internal.datasource.cache

import com.mercadopago.android.px.internal.callbacks.MPCall
import com.mercadopago.android.px.model.exceptions.ApiException
import com.mercadopago.android.px.model.internal.InitResponse
import com.mercadopago.android.px.services.Callback

class InitMemCache : Cache<InitResponse> {
    private var initResponse: InitResponse? = null

    override fun get() = MPCall { callback: Callback<InitResponse> -> this.resolve(callback) }

    private fun resolve(callback: Callback<InitResponse>) {
        if (isCached) callback.success(initResponse) else callback.failure(ApiException())
    }

    override fun put(initResponse: InitResponse) {
        this.initResponse = initResponse
    }

    override fun evict() {
        initResponse = null
    }

    override fun isCached() = initResponse != null
}