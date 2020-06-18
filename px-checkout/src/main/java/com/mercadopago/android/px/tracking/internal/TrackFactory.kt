package com.mercadopago.android.px.tracking.internal

import com.mercadopago.android.px.addons.model.Track
import com.mercadopago.android.px.addons.tracking.Tracker

object TrackFactory {

    private const val APPLICATION_CONTEXT = "PX"

    @JvmStatic
    fun withView(path: String) =
        Track.Builder(Tracker.MELIDATA, APPLICATION_CONTEXT, Track.Type.VIEW, path)
            .addTracker(Tracker.GOOGLE_ANALYTICS)

    @JvmStatic
    fun withEvent(path: String) =
        Track.Builder(Tracker.MELIDATA, APPLICATION_CONTEXT, Track.Type.EVENT, path)
            .addTracker(Tracker.GOOGLE_ANALYTICS)
}