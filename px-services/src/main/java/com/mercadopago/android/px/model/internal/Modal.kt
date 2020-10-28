package com.mercadopago.android.px.model.internal

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Modal(
    val title: Text,
    val description: Text,
    val mainButton: Button,
    val secondaryButton: Button?,
    val imageUrl: String?
) : Parcelable