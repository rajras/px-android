package com.mercadopago.android.px.internal.features.express

import android.os.Parcel
import com.mercadopago.android.px.internal.extensions.readBigDecimal
import com.mercadopago.android.px.internal.extensions.writeBigDecimal
import com.mercadopago.android.px.internal.util.KParcelable
import com.mercadopago.android.px.internal.util.parcelableCreator
import com.mercadopago.android.px.internal.viewmodel.AmountDescriptor
import com.mercadopago.android.px.internal.viewmodel.DiscountDetailModel
import com.mercadopago.android.px.tracking.internal.model.DiscountInfo
import java.math.BigDecimal

class DiscountModel(
    val discountAmount: BigDecimal,
    val discountInfo: DiscountInfo?,
    val discountDetailModel: DiscountDetailModel,
    val discountOverview: AmountDescriptor?

) : KParcelable {

    constructor(parcel: Parcel) : this(
        parcel.readBigDecimal()!!,
        parcel.readParcelable(DiscountInfo::class.java.classLoader)!!,
        parcel.readParcelable(DiscountDetailModel::class.java.classLoader)!!,
        parcel.readParcelable(AmountDescriptor::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        writeBigDecimal(discountAmount)
        writeParcelable(discountInfo, flags)
        writeParcelable(discountDetailModel, flags)
        writeParcelable(discountOverview, flags)
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::DiscountModel)
    }
}