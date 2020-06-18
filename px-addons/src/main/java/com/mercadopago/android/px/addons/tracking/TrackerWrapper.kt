package com.mercadopago.android.px.addons.tracking

import com.mercadopago.android.px.addons.model.Track

abstract class TrackerWrapper {
    abstract val tracker: Tracker
    internal fun internalSend(track: Track) {
        if (track.shouldTrack(tracker)) {
            send(track)
        }
    }

    abstract fun send(track: Track)
}