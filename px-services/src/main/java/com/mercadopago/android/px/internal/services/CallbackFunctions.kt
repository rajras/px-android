package com.mercadopago.android.px.internal.services

import com.mercadopago.android.px.internal.callbacks.MPCall
import com.mercadopago.android.px.model.exceptions.ApiException
import com.mercadopago.android.px.services.Callback
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun <T> MPCall<T>.awaitCallback(): Response =
        suspendCancellableCoroutine { cont ->
            enqueue(object : Callback<T>() {
                override fun success(result: T) {
                    cont.resume(Response.Success(result))
                }

                override fun failure(apiException: ApiException?) {
                    apiException?.let {
                        cont.resume(Response.Failure(it))
                    }
                }
            })
        }