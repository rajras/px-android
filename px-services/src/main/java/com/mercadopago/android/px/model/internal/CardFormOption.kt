package com.mercadopago.android.px.model.internal

import android.os.Parcelable
import com.mercadopago.android.px.model.CardFormInitType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CardFormOption(
    val cardFormInitType: CardFormInitType,
    val imageUrl: String?,
    val title: Text,
    val subtitle: Text?): Parcelable