package com.mercadopago.android.px.addons.tracking

enum class Tracker(val bit: Long) {
    MELIDATA(1 shl 0),
    GOOGLE_ANALYTICS(1 shl 1);

    fun shouldTrack(mask: Long) = mask and bit == bit
}