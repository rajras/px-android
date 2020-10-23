package com.mercadopago.android.px.internal.viewmodel.drawables

import android.os.Parcel
import com.meli.android.carddrawer.configuration.CardDrawerStyle
import com.mercadopago.android.px.internal.util.parcelableCreator
import com.mercadopago.android.px.internal.viewmodel.CardUiConfiguration

internal class AccountMoneyDrawableFragmentItem : DrawableFragmentItem {

    val cardConfiguration: CardUiConfiguration?
    val cardStyle: CardDrawerStyle?

    constructor(parameters: Parameters, cardConfiguration: CardUiConfiguration) : super(parameters) {
        this.cardConfiguration = cardConfiguration
        cardStyle = null
    }

    constructor(parameters: Parameters, cardStyle: CardDrawerStyle) : super(parameters) {
        this.cardStyle = cardStyle
        cardConfiguration = null
    }

    private constructor(parcel: Parcel): super(parcel) {
        cardConfiguration = parcel.readParcelable(CardUiConfiguration::class.java.classLoader)
        cardStyle = parcel.readString()?.let {
            CardDrawerStyle.valueOf(it)
        }
    }

    override fun draw(drawer: PaymentMethodFragmentDrawer) = drawer.draw(this)

    override fun describeContents() = 0

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeParcelable(cardConfiguration, flags)
        parcel.writeString(cardStyle?.name)
    }

    companion object {
        @JvmField val CREATOR = parcelableCreator(::AccountMoneyDrawableFragmentItem)
    }
}
