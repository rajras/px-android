package com.mercadopago.android.px.feature.custom_initialize

import com.mercadopago.android.px.core.MercadoPagoCheckout

internal sealed class CustomInitializeState {
    data class LoadData(val initializationData: InitializationData) : CustomInitializeState()
    data class InitCheckout(val builder: MercadoPagoCheckout.Builder) : CustomInitializeState()
}