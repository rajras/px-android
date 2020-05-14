package com.mercadopago.android.px.internal.core

import android.content.SharedPreferences

class SessionIdProvider(private val sharedPreferences: SharedPreferences) {

    private var internalSessionId: String? = null
    val sessionId: String
        get() {
            if (internalSessionId == null) {
                internalSessionId = sharedPreferences.getString(PREF_SESSION_ID, DEFAULT)
            }
            return internalSessionId!!
        }

    fun configure(sessionId: String) {
        internalSessionId = sessionId
        sharedPreferences.edit().putString(PREF_SESSION_ID, sessionId).apply()
    }

    fun clear() {
        internalSessionId = DEFAULT
        sharedPreferences.edit().remove(PREF_SESSION_ID).apply()
    }

    companion object {
        private const val DEFAULT = "no-value"
        private const val PREF_SESSION_ID = "PREF_SESSION_ID"
    }
}