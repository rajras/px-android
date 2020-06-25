package com.mercadopago.android.px.tracking.internal.events

abstract class CongratsDeepLink : EventTracker() {
    protected abstract val congratsType: String
    protected abstract val deepLinkType: DeepLinkType
    protected abstract val deepLink: String

    override fun getEventPath() = "$BASE_PATH/congrats/$congratsType/deep_link"

    override fun getEventData() = mutableMapOf(
            "type" to deepLinkType.type,
            "deep_link" to deepLink
    )
}