package com.mercadopago.android.px.internal.di

import com.mercadopago.android.px.internal.features.checkout.PostPaymentUrlsMapper
import com.mercadopago.android.px.internal.features.payment_congrats.model.PaymentCongratsModelMapper
import com.mercadopago.android.px.internal.viewmodel.drawables.PaymentMethodDrawableItemMapper
import com.mercadopago.android.px.internal.mappers.AmountDescriptorMapper
import com.mercadopago.android.px.internal.mappers.CardUiMapper
import com.mercadopago.android.px.internal.mappers.PaymentMethodDescriptorMapper

internal object MapperProvider {
    fun getPaymentMethodDrawableItemMapper(): PaymentMethodDrawableItemMapper {
        val session = Session.getInstance()
        return PaymentMethodDrawableItemMapper(
            session.configurationModule.chargeRepository,
            session.initRepository,
            session.configurationModule.disabledPaymentMethodRepository,
            CardUiMapper
        )
    }

    fun getPaymentMethodDescriptorMapper(): PaymentMethodDescriptorMapper {
        return PaymentMethodDescriptorMapper(
            Session.getInstance().configurationModule.paymentSettings,
            Session.getInstance().amountConfigurationRepository,
            Session.getInstance().configurationModule.disabledPaymentMethodRepository,
            Session.getInstance().amountRepository
        )
    }

    fun getPaymentCongratsMapper() : PaymentCongratsModelMapper {
        return PaymentCongratsModelMapper(
            Session.getInstance().configurationModule.paymentSettings,
            Session.getInstance().configurationModule.trackingRepository
        )
    }

    fun getAmountDescriptorMapper(): AmountDescriptorMapper {
        return AmountDescriptorMapper(
            Session.getInstance().experimentsRepository
        )
    }

    fun getPostPaymentUrlsMapper() = PostPaymentUrlsMapper
}
