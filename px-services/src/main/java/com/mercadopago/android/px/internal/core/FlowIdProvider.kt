package com.mercadopago.android.px.internal.core

import android.content.Context

class FlowIdProvider(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

    private var internalFlowId: String? = null
    val flowId: String
        get() {
            if (internalFlowId == null) {
                internalFlowId = sharedPreferences.getString(PREF_FLOW_ID, DEFAULT_FLOW)
            }
            return internalFlowId!!
        }

    fun configure(flowId: String?) {
        if (flowId.isNullOrEmpty()) {
            clear()
        } else {
            internalFlowId = flowId
            sharedPreferences.edit().putString(PREF_FLOW_ID, flowId).apply()
        }
    }

    fun clear() {
        internalFlowId = DEFAULT_FLOW
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        private const val SHARED_PREFERENCE_NAME = "com.mercadopago.checkout.store.flow"
        private const val PREF_FLOW_ID = "PREF_FLOW_ID"
        private const val DEFAULT_FLOW = "unknown"
    }
}