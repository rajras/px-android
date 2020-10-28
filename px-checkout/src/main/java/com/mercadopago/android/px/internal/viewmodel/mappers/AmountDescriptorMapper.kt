package com.mercadopago.android.px.internal.viewmodel.mappers

import com.mercadopago.android.px.internal.experiments.KnownVariant
import com.mercadopago.android.px.internal.repository.ExperimentsRepository
import com.mercadopago.android.px.internal.view.experiments.ExperimentHelper
import com.mercadopago.android.px.internal.viewmodel.AmountDescriptor
import com.mercadopago.android.px.model.DiscountOverview

internal class AmountDescriptorMapper(private val experimentsRepository: ExperimentsRepository)
    : Mapper<DiscountOverview, AmountDescriptor>() {

    override fun map(model: DiscountOverview) = AmountDescriptor(
        model.description,
        model.amount,
        model.brief.takeIf {
            ExperimentHelper.getVariantFrom(experimentsRepository.experiments, KnownVariant.SCROLLED).isDefault()
        },
        model.url
    )
}
