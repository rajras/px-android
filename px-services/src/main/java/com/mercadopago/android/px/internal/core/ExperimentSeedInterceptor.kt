package com.mercadopago.android.px.internal.core

import com.mercadopago.android.px.addons.TrackingBehaviour
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ExperimentSeedInterceptor(private val trackingBehaviour: TrackingBehaviour) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder()
            .header(EXPERIMENT_SEED_HEADER, trackingBehaviour.experimentSeed.orEmpty())
            .build()
        return chain.proceed(request)
    }

    companion object {
        private const val EXPERIMENT_SEED_HEADER = "x-experiment-seed"
    }
}