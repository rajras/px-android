package com.mercadopago.android.px.internal.datasource.cache

import com.mercadopago.android.px.internal.callbacks.MPCall
import com.mercadopago.android.px.internal.util.ApiUtil
import com.mercadopago.android.px.model.exceptions.ApiException
import com.mercadopago.android.px.model.internal.InitResponse
import com.mercadopago.android.px.services.Callback

class InitCacheCoordinator(private val initDiskCache: InitDiskCache,
    private val initMemCache: InitMemCache) : Cache<InitResponse> {

    override fun get() = MPCall { callback: Callback<InitResponse> -> internalGet().enqueue(getCallback(callback)) }

    private fun internalGet(): MPCall<InitResponse> {
        return if (initMemCache.isCached) {
            initMemCache.get()
        } else {
            MPCall { callback: Callback<InitResponse> -> initDiskCache.get().enqueue(getCallbackDisk(callback)) }
        }
    }

    private fun getCallbackDisk(callback: Callback<InitResponse>): Callback<InitResponse> {
        return object : Callback<InitResponse>() {
            override fun success(initResponse: InitResponse) {
                initMemCache.put(initResponse)
                callback.success(initResponse)
            }

            override fun failure(apiException: ApiException) {
                callback.failure(apiException)
            }
        }
    }

    private fun getCallback(callback: Callback<InitResponse>): Callback<InitResponse> {
        return object : Callback<InitResponse>() {
            override fun success(initResponse: InitResponse) {
                callback.success(initResponse)
            }

            override fun failure(apiException: ApiException) {
                apiException.status = ApiUtil.StatusCodes.CACHE_FAIL
                callback.failure(apiException)
            }
        }
    }

    override fun put(initResponse: InitResponse) {
        initMemCache.put(initResponse)
        initDiskCache.put(initResponse)
    }

    override fun evict() {
        initDiskCache.evict()
        initMemCache.evict()
    }

    override fun isCached() = initMemCache.isCached || initDiskCache.isCached
}