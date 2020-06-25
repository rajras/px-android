package com.mercadopago.android.px.addons.model.internal

import java.io.Serializable

data class Variant(val id: Int, val name: String, val availableFeatures: List<AvailableFeature>?) : Serializable