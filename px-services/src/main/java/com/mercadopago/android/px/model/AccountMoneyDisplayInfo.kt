package com.mercadopago.android.px.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class AccountMoneyDisplayInfo(
    val sliderTitle: String?,
    val type: Type?,
    val color: String,
    val paymentMethodImageUrl: String?,
    val message: String?,
    val gradientColors: List<String>?
) : Serializable, Parcelable {

    enum class Type {
        @SerializedName("default") DEFAULT,
        @SerializedName("hybrid") HYBRID
    }
}
