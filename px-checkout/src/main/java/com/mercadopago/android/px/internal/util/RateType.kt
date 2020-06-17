package com.mercadopago.android.px.internal.util

import com.mercadopago.android.px.R

enum class RateType {

    TEA {
        override fun getResource(): Int {
            throw NotImplementedError()
        }
    },

    CFT {
        override fun getResource() = R.string.px_installments_cft

    },
    CFTNA {
        override fun getResource() = R.string.px_installments_cftna
    };

    abstract fun getResource(): Int
}