package com.mercadopago.android.px.internal.features.payment_result.viewmodel

import com.mercadopago.android.px.internal.features.payment_result.CongratsAutoReturn
import com.mercadopago.android.px.internal.features.payment_result.remedies.RemediesModel
import com.mercadopago.android.px.internal.features.payment_result.presentation.PaymentResultFooter
import com.mercadopago.android.px.internal.view.PaymentResultBody
import com.mercadopago.android.px.internal.view.PaymentResultHeader

internal class PaymentResultViewModel(
    val headerModel: PaymentResultHeader.Model,
    val remediesModel: RemediesModel,
    val footerModel: PaymentResultFooter.Model?,
    val bodyModel: PaymentResultBody.Model,
    val legacyViewModel: PaymentResultLegacyViewModel,
    val autoReturnModel: CongratsAutoReturn.Model? = null
)
