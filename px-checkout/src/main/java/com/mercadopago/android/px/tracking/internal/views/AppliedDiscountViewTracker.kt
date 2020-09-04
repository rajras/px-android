package com.mercadopago.android.px.tracking.internal.views

import com.mercadopago.android.px.model.DiscountConfigurationModel
import com.mercadopago.android.px.tracking.internal.TrackFactory
import com.mercadopago.android.px.tracking.internal.TrackWrapper
import com.mercadopago.android.px.tracking.internal.model.DiscountInfo

class AppliedDiscountViewTracker(private val discountInfo: DiscountInfo?) : TrackWrapper() {

    private val data = mutableMapOf<String, Any?>().also {
        discountInfo?.let { info ->
            it["discount"] = info.toMap()
        }
    }

    override fun getTrack() = TrackFactory.withView(PATH).addData(data).build()

    companion object {
        private const val PATH = "$BASE_PATH$PAYMENTS_PATH/applied_discount"
    }
}