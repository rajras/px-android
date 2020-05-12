package com.mercadopago.android.px.internal.features.express.add_new_card;

import android.support.annotation.NonNull;

import com.mercadopago.android.px.internal.base.MvpView;
import com.mercadopago.android.px.internal.features.pay_button.PayButton;
import com.mercadopago.android.px.internal.viewmodel.AmountLocalized;
import com.mercadopago.android.px.internal.viewmodel.BusinessPaymentModel;
import com.mercadopago.android.px.internal.viewmodel.PaymentModel;

public interface OfflineMethods {

    interface OffMethodsView extends MvpView, PayButton.Handler {

        boolean isExploding();

        void updateTotalView(@NonNull final AmountLocalized amountLocalized);

        void onSlideSheet(final float offset);

        void startKnowYourCustomerFlow(@NonNull final String flowLink);

        void showPaymentResult(@NonNull PaymentModel model);

        void showBusinessResult(@NonNull BusinessPaymentModel model);
    }

    interface Actions {

        void updateModel();

        void selectMethod(@NonNull final OfflineMethodItem selectedItem);

        void trackAbort();

        void onPaymentFinished(PaymentModel paymentModel);
    }
}