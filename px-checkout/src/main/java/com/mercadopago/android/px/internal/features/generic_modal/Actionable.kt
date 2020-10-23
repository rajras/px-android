package com.mercadopago.android.px.internal.features.generic_modal

import android.os.Parcelable
import com.mercadopago.android.px.internal.extensions.isNotNullNorEmpty
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Actionable(
    val label: String,
    val deepLink: String?,
    @ActionType val action: String?) : Parcelable {

    init {
        check(deepLink.isNotNullNorEmpty() || action.isNotNullNorEmpty()) {
            "An ${javaClass.simpleName} should have a deepLink or an action to follow"
        }
    }
}
