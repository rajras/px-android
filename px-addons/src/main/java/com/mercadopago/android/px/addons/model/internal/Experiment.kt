package com.mercadopago.android.px.addons.model.internal

import java.io.Serializable

data class Experiment(val id: Int, val name: String, val variant: Variant) : Serializable