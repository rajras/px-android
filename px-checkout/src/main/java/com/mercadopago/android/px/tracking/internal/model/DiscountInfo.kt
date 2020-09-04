package com.mercadopago.android.px.tracking.internal.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.mercadopago.android.px.internal.extensions.*
import com.mercadopago.android.px.internal.extensions.readBigDecimal
import com.mercadopago.android.px.internal.extensions.readOptionalInt
import com.mercadopago.android.px.internal.extensions.writeBigDecimal
import com.mercadopago.android.px.internal.extensions.writeOptionalInt
import com.mercadopago.android.px.internal.util.KParcelable
import com.mercadopago.android.px.internal.util.ParcelableUtil
import com.mercadopago.android.px.internal.util.parcelableCreator
import com.mercadopago.android.px.model.Campaign
import com.mercadopago.android.px.model.Discount
import java.math.BigDecimal

@Keep
class DiscountInfo private constructor(private val isAvailable: Boolean) : TrackingMapModel(), KParcelable {

    private var couponAmount: BigDecimal? = null
    private var amountOff: BigDecimal? = null
    private var percentOff: BigDecimal? = null
    private var maxCouponAmount: BigDecimal? = null
    private var maxRedeemPerUser: Int? = null
    private var campaignId: String? = null

    private constructor(discount: Discount?, campaign: Campaign?,
        isAvailable: Boolean) : this(isAvailable) {
        if (campaign == null || discount == null) {
            couponAmount = null
            amountOff = null
            maxCouponAmount = null
            maxRedeemPerUser = null
            campaignId = null
            percentOff = null
        } else {
            couponAmount = discount.couponAmount
            amountOff = discount.amountOff
            percentOff = discount.percentOff
            maxCouponAmount = campaign.maxCouponAmount
            maxRedeemPerUser = campaign.maxRedeemPerUser
            campaignId = discount.id
        }
    }

    private constructor(parcel: Parcel) : this(parcel.readBool()) {
        couponAmount = parcel.readBigDecimal()
        amountOff = parcel.readBigDecimal()
        percentOff = parcel.readBigDecimal()
        maxCouponAmount = parcel.readBigDecimal()
        maxRedeemPerUser = parcel.readOptionalInt()
        campaignId = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        writeBool(isAvailable)
        writeBigDecimal(couponAmount)
        writeBigDecimal(amountOff)
        writeBigDecimal(percentOff)
        writeBigDecimal(maxCouponAmount)
        writeOptionalInt(maxRedeemPerUser)
        writeString(campaignId)
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::DiscountInfo)

        @JvmStatic
        fun with(discount: Discount?, campaign: Campaign?, isAvailable: Boolean): DiscountInfo? {
            return if (isAvailable && (discount == null || campaign == null)) {
                null
            } else DiscountInfo(discount, campaign, isAvailable)
        }
    }
}