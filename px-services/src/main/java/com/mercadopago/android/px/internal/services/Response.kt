package com.mercadopago.android.px.internal.services

sealed class Response {
    data class Success<T>(val result: T): Response()
    data class Failure<T>(val exception: T?): Response()
}