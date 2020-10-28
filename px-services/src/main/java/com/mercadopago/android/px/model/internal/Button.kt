package com.mercadopago.android.px.model.internal

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Button(
    val action: Action?,
    val target: String?,
    val type: Type?,
    val label: String
) : Parcelable {

    enum class Action {
        @SerializedName("continue") CONTINUE,
        @SerializedName("kyc") KYC,
        @SerializedName("change_pm") CHANGE_PM,
        @SerializedName("pay") PAY
    }

    enum class Type {
        @SerializedName("loud") LOUD,
        @SerializedName("quiet") QUIET,
        @SerializedName("transparent") TRANSPARENT
    }
}