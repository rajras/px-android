package com.mercadopago.android.px.internal.features.payment_result.mappers;

import androidx.annotation.NonNull;
import com.mercadopago.android.px.configuration.PaymentResultScreenConfiguration;
import com.mercadopago.android.px.internal.features.business_result.BusinessPaymentResultTracker;
import com.mercadopago.android.px.internal.features.business_result.PaymentCongratsResponseMapper;
import com.mercadopago.android.px.internal.features.payment_congrats.model.PaymentCongratsResponse;
import com.mercadopago.android.px.internal.features.payment_congrats.model.CongratsViewModelMapper;
import com.mercadopago.android.px.internal.view.PaymentResultBody;
import com.mercadopago.android.px.internal.view.PaymentResultMethod;
import com.mercadopago.android.px.internal.viewmodel.PaymentModel;
import com.mercadopago.android.px.internal.mappers.Mapper;
import com.mercadopago.android.px.model.PaymentData;
import com.mercadopago.android.px.model.PaymentResult;
import com.mercadopago.android.px.tracking.internal.MPTracker;
import java.util.ArrayList;
import java.util.List;

public class PaymentResultBodyModelMapper extends Mapper<PaymentModel, PaymentResultBody.Model> {

    @NonNull private final PaymentResultScreenConfiguration configuration;
    @NonNull private final MPTracker tracker;

    public PaymentResultBodyModelMapper(@NonNull final PaymentResultScreenConfiguration configuration,
        @NonNull final MPTracker tracker) {
        this.configuration = configuration;
        this.tracker = tracker;
    }

    @Override
    public PaymentResultBody.Model map(@NonNull final PaymentModel model) {
        final PaymentResult paymentResult = model.getPaymentResult();
        final List<PaymentResultMethod.Model> methodModels = new ArrayList<>();
        final PaymentCongratsResponse paymentCongratsResponse = new PaymentCongratsResponseMapper()
            .map(model.getCongratsResponse());
        for (final PaymentData paymentData : paymentResult.getPaymentDataList()) {
            final String imageUrl =
                model.getCongratsResponse().getPaymentMethodsImages().get(paymentData.getPaymentMethod().getId());
            methodModels.add(PaymentResultMethod.Model.with(imageUrl, paymentData, model.getCurrency()));
        }

        return new PaymentResultBody.Model.Builder()
            .setMethodModels(methodModels)
            .setCongratsViewModel(new CongratsViewModelMapper(new BusinessPaymentResultTracker(tracker))
                .map(paymentCongratsResponse))
            .setReceiptId(String.valueOf(paymentResult.getPaymentId()))
            .setTopFragment(configuration.getTopFragment())
            .setBottomFragment(configuration.getBottomFragment())
            .build();
    }
}
