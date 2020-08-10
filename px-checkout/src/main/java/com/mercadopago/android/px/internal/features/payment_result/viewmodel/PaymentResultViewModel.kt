package com.mercadopago.android.px.internal.features.payment_result.viewmodel

import com.mercadopago.android.px.internal.features.payment_result.remedies.RemediesModel
import com.mercadopago.android.px.internal.features.payment_result.remedies.view.PaymentResultFooter
import com.mercadopago.android.px.internal.view.PaymentResultBody
import com.mercadopago.android.px.internal.view.PaymentResultHeader

internal class PaymentResultViewModel(
    @JvmField val headerModel: PaymentResultHeader.Model,
    @JvmField val remediesModel: RemediesModel,
    @JvmField val footerModel: PaymentResultFooter.Model,
    @JvmField val bodyModel: PaymentResultBody.Model,
    @JvmField val legacyViewModel: PaymentResultLegacyViewModel,
    @JvmField val shouldAutoReturn: Boolean
)