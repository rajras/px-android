package com.mercadopago.android.px.internal.viewmodel

import android.content.Context
import android.os.Parcel
import com.mercadopago.android.px.internal.extensions.orIfEmpty
import com.mercadopago.android.px.internal.util.KParcelable
import com.mercadopago.android.px.internal.util.TextUtil
import com.mercadopago.android.px.internal.util.parcelableCreator

internal class LazyString(private val text: CharSequence?, private val resId: Int?, private vararg val args: String) : KParcelable {
    constructor(text: CharSequence?, vararg args: String) : this(text, null, *args)
    constructor(resId: Int?, vararg args: String) : this(null, resId, *args)
    constructor(parcel: Parcel) : this(parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        *parcel.createStringArray()!!
    )

    fun get(context: Context): CharSequence {
        val value = text.orIfEmpty(resId?.let { context.getString(it) } ?: "")
        return if (args.isNotEmpty()) {
            TextUtil.format(value.toString(), *args)
        } else {
            value
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text?.toString())
        parcel.writeValue(resId)
        parcel.writeStringArray(args)
    }

    companion object {
        @JvmField val CREATOR = parcelableCreator(::LazyString)
    }
}