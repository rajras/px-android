package com.mercadopago.android.px.internal.features.payment_result

import android.os.CountDownTimer

object CongratsAutoReturn {

    private const val ALL = "all"
    private const val APPROVED = "approved"

    fun isValid(value: String?) = value == ALL || value == APPROVED

    class Timer(private val callback: () -> Unit) : CountDownTimer(5000, 1000) {
        override fun onFinish() = callback.invoke()
        override fun onTick(p0: Long) = Unit
    }
}