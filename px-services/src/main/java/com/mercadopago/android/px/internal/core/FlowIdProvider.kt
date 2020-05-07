package com.mercadopago.android.px.internal.core

import android.content.Context

class FlowIdProvider(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

    internal constructor(context: Context, flowId: String?): this(context) {
        if (flowId.isNullOrEmpty()) {
            clear()
        } else {
            sharedPreferences.edit().putString(PREF_FLOW_ID, flowId).apply()
        }
    }

    val flowId: String
        get() = sharedPreferences.getString(PREF_FLOW_ID, DEFAULT_FLOW)!!

    private fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        private const val SHARED_PREFERENCE_NAME = "com.mercadopago.checkout.store.flow"
        private const val PREF_FLOW_ID = "PREF_FLOW_ID"
        private const val DEFAULT_FLOW = "unknown"
    }
}