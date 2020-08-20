package com.mercadopago.android.px.internal.repository

import com.mercadopago.android.px.internal.callbacks.MPCall
import com.mercadopago.android.px.model.internal.InitResponse

interface InitRepository {
    fun init(): MPCall<InitResponse>
    fun cleanRefresh(): MPCall<InitResponse>
    fun refresh(): MPCall<InitResponse>
    fun refreshWithNewCard(cardId: String): MPCall<InitResponse>
    fun lazyConfigure(initResponse: InitResponse)
    fun addOnChangedListener(listener: OnChangedListener)
    suspend fun loadInitResponse(): InitResponse?
    interface OnChangedListener {
        fun onInitResponseChanged(response: InitResponse)
    }
}