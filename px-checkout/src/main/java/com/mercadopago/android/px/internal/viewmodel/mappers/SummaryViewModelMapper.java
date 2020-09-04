package com.mercadopago.android.px.internal.viewmodel.mappers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.mercadopago.android.px.internal.features.express.DiscountModel;
import com.mercadopago.android.px.internal.features.express.OneTapModel;
import com.mercadopago.android.px.internal.repository.AmountConfigurationRepository;
import com.mercadopago.android.px.internal.repository.AmountRepository;
import com.mercadopago.android.px.internal.repository.ChargeRepository;
import com.mercadopago.android.px.internal.repository.CustomTextsRepository;
import com.mercadopago.android.px.internal.repository.DiscountRepository;
import com.mercadopago.android.px.internal.util.ChargeRuleHelper;
import com.mercadopago.android.px.internal.view.AmountDescriptorView;
import com.mercadopago.android.px.internal.view.ElementDescriptorView;
import com.mercadopago.android.px.internal.view.SummaryDetailDescriptorFactory;
import com.mercadopago.android.px.internal.view.SummaryView;
import com.mercadopago.android.px.internal.viewmodel.AmountLocalized;
import com.mercadopago.android.px.internal.viewmodel.SummaryViewDefaultColor;
import com.mercadopago.android.px.internal.viewmodel.TotalLocalized;
import com.mercadopago.android.px.model.Currency;
import com.mercadopago.android.px.model.commission.PaymentTypeChargeRule;
import com.mercadopago.android.px.model.internal.SummaryInfo;
import java.util.List;
import java.util.Objects;

public class SummaryViewModelMapper extends CacheableMapper<OneTapModel, SummaryView.Model,
    SummaryViewModelMapper.Key> {

    @NonNull private final Currency currency;
    @NonNull private final DiscountRepository discountRepository;
    @NonNull private final AmountRepository amountRepository;
    @NonNull private final ElementDescriptorView.Model elementDescriptorModel;
    @NonNull private final AmountDescriptorView.OnClickListener listener;
    @NonNull private final SummaryInfo summaryInfo;
    @NonNull private final ChargeRepository chargeRepository;
    @NonNull private final AmountConfigurationRepository amountConfigurationRepository;
    @NonNull private final CustomTextsRepository customTextsRepository;

    public SummaryViewModelMapper(@NonNull final Currency currency,
        @NonNull final DiscountRepository discountRepository, @NonNull final AmountRepository amountRepository,
        @NonNull final ElementDescriptorView.Model elementDescriptorModel,
        @NonNull final AmountDescriptorView.OnClickListener listener,
        @NonNull final SummaryInfo summaryInfo, @NonNull final ChargeRepository chargeRepository,
        @NonNull final AmountConfigurationRepository amountConfigurationRepository,
        @NonNull final CustomTextsRepository customTextsRepository) {
        this.currency = currency;
        this.discountRepository = discountRepository;
        this.amountRepository = amountRepository;
        this.elementDescriptorModel = elementDescriptorModel;
        this.listener = listener;
        this.summaryInfo = summaryInfo;
        this.chargeRepository = chargeRepository;
        this.amountConfigurationRepository = amountConfigurationRepository;
        this.customTextsRepository = customTextsRepository;
    }

    @Override
    protected Key getKey(
        @NonNull final OneTapModel oneTapModel) {
        final PaymentTypeChargeRule chargeRule =
            chargeRepository.getChargeRule(oneTapModel.getPaymentTypeId());
        return new Key(oneTapModel.getDiscountModel(), chargeRule, oneTapModel.getHasSplit());
    }

    @Override
    public SummaryView.Model map(@NonNull final OneTapModel oneTapModel) {
        return createModel(oneTapModel.getPaymentTypeId(), oneTapModel.getDiscountModel(),
            oneTapModel.getHasSplit());
    }

    @NonNull
    private SummaryView.Model createModel(@NonNull final String paymentTypeId,
        @NonNull final DiscountModel discountModel, final boolean hasSplit) {
        final PaymentTypeChargeRule chargeRule = chargeRepository.getChargeRule(paymentTypeId);
        final List<AmountDescriptorView.Model> summaryDetailList =
            new SummaryDetailDescriptorFactory(listener, discountModel, amountRepository, summaryInfo, currency,
                chargeRule, hasSplit).create();

        final AmountDescriptorView.Model totalRow = new AmountDescriptorView.Model(
            new TotalLocalized(customTextsRepository),
            new AmountLocalized(amountRepository.getAmountToPay(paymentTypeId, discountModel.getDiscountAmount()),
                currency),
            new SummaryViewDefaultColor());

        return new SummaryView.Model(elementDescriptorModel, summaryDetailList, totalRow);
    }

    static final class Key {
        private final DiscountModel discountModel;
        private final PaymentTypeChargeRule paymentTypeChargeRule;
        private final Boolean hasSplit;

        Key(@NonNull final DiscountModel discountModel,
            final PaymentTypeChargeRule paymentTypeChargeRule, final boolean hasSplit) {
            this.discountModel = discountModel;
            this.paymentTypeChargeRule =
                ChargeRuleHelper.isHighlightCharge(paymentTypeChargeRule) ? null : paymentTypeChargeRule;
            this.hasSplit = hasSplit;
        }

        @Override
        public int hashCode() {
            return (discountModel == null ? 0 : discountModel.hashCode()) ^
                (paymentTypeChargeRule == null ? 0 : paymentTypeChargeRule.hashCode()) ^
                (hasSplit == null ? 0 : hasSplit.hashCode());
        }

        @Override
        public boolean equals(@Nullable final Object obj) {
            if (!(obj instanceof Key)) {
                return false;
            }
            final Key k = (Key) obj;
            return Objects.equals(k.discountModel, discountModel) &&
                Objects.equals(k.paymentTypeChargeRule, paymentTypeChargeRule)
                && Objects.equals(k.hasSplit, hasSplit);
        }
    }
}