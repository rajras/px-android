package com.mercadopago.android.px.internal.core

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ProductIdInterceptor(private val productIdProvider: ProductIdProvider) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder()
            .header(HEADER_KEY, productIdProvider.productId)
            .build()
        return chain.proceed(request)
    }

    companion object {
        private const val HEADER_KEY = "X-Product-Id"
    }
}