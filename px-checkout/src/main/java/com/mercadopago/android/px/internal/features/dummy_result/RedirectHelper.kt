package com.mercadopago.android.px.internal.features.dummy_result

import com.mercadopago.android.px.internal.extensions.isNotNullNorEmpty
import com.mercadopago.android.px.internal.extensions.runIfNotNullNorEmpty
import com.mercadopago.android.px.internal.features.payment_result.CongratsAutoReturn
import com.mercadopago.android.px.model.Payment
import com.mercadopago.android.px.preferences.PostPaymentUrls

typealias Callback = (String) -> Unit

object RedirectHelper {

    fun hasRedirect(postPaymentUrls: PostPaymentUrls?, statusCode: String) : Boolean {
        return postPaymentUrls?.let {
            when (statusCode) {
                Payment.StatusCodes.STATUS_APPROVED -> it.success.isNotNullNorEmpty()
                Payment.StatusCodes.STATUS_REJECTED -> it.failure.isNotNullNorEmpty()
                else -> it.pending.isNotNullNorEmpty()
            }
        } ?: false
    }

    fun resolveRedirect(paymentStatus: String, vararg postPaymentUrls: Pair<PostPaymentUrls?, Callback>) {
        var resolved = false
        val iterator = postPaymentUrls.iterator()
        while(iterator.hasNext() && !resolved) {
            iterator.next().also {
                val (postPaymentUrl, callback) = it
                resolved = when (paymentStatus) {
                    Payment.StatusCodes.STATUS_APPROVED -> postPaymentUrl?.success.runIfNotNullNorEmpty(callback)
                    Payment.StatusCodes.STATUS_REJECTED -> postPaymentUrl?.failure.runIfNotNullNorEmpty(callback)
                    else -> postPaymentUrl?.pending.runIfNotNullNorEmpty(callback)
                }
            }
        }
    }

    fun shouldAutoReturn(autoReturn: String?, statusCode: String): Boolean {
        return statusCode == Payment.StatusCodes.STATUS_APPROVED && CongratsAutoReturn.isValid(autoReturn)
    }
}