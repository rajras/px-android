package com.mercadopago.android.px.internal.features.payment_result.mappers

import com.mercadopago.android.px.configuration.PaymentResultScreenConfiguration
import com.mercadopago.android.px.internal.features.payment_result.viewmodel.PaymentResultLegacyViewModel
import com.mercadopago.android.px.internal.features.payment_result.viewmodel.PaymentResultViewModel
import com.mercadopago.android.px.internal.viewmodel.PaymentModel
import com.mercadopago.android.px.internal.viewmodel.mappers.Mapper
import com.mercadopago.android.px.model.Instruction

internal class PaymentResultViewModelMapper(
    private val configuration: PaymentResultScreenConfiguration,
    private val instruction: Instruction?,
    private val autoReturnFromPreference: String?) : Mapper<PaymentModel, PaymentResultViewModel>() {

    override fun map(model: PaymentModel): PaymentResultViewModel {
        val legacyViewModel = PaymentResultLegacyViewModel(
            model, configuration, instruction)
        val remediesModel = PaymentResultRemediesModelMapper.map(model.remedies)
        return PaymentResultViewModel(
            PaymentResultHeaderModelMapper(configuration, instruction, remediesModel).map(model),
            remediesModel,
            PaymentResultFooterModelMapper.map(model),
            PaymentResultBodyModelMapper(configuration).map(model), legacyViewModel,
            CongratsAutoReturnMapper(autoReturnFromPreference, model.paymentResult.paymentStatus)
                .map(model.congratsResponse.autoReturn))
    }
}
