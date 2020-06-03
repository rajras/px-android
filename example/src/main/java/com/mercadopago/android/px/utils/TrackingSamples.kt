package com.mercadopago.android.px.utils

import com.mercadopago.android.px.tracking.PXTrackingListener

object TrackingSamples {
    fun getTracker() = object : PXTrackingListener {
        override fun onView(path: String, data: Map<String?, *>) = Unit
        override fun onEvent(path: String, data: Map<String?, *>) = Unit
    }
}