package com.mercadopago.android.px.internal.datasource;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.mercadopago.android.px.internal.repository.DiscountRepository;
import com.mercadopago.android.px.internal.repository.InitRepository;
import com.mercadopago.android.px.internal.repository.UserSelectionRepository;
import com.mercadopago.android.px.model.Card;
import com.mercadopago.android.px.model.DiscountConfigurationModel;
import com.mercadopago.android.px.model.PaymentMethod;
import java.util.Map;

public class DiscountServiceImp implements DiscountRepository {

    /* default */ ConfigurationSolver configurationSolver;
    /* default */ Map<String, DiscountConfigurationModel> discountsConfigurations;
    private final UserSelectionRepository userSelectionRepository;

    public DiscountServiceImp(@NonNull final InitRepository initRepository,
        @NonNull final UserSelectionRepository userSelectionRepository) {
        this.userSelectionRepository = userSelectionRepository;
        initRepository.addOnChangedListener(initResponse -> {
            configurationSolver = new ConfigurationSolverImpl(initResponse.getDefaultAmountConfiguration(),
                initResponse.getCustomSearchItems());
            discountsConfigurations = initResponse.getDiscountsConfigurations();
        });
    }

    @NonNull
    @Override
    public DiscountConfigurationModel getCurrentConfiguration() {
        final Card card = userSelectionRepository.getCard();
        // Remember to prioritize the selected discount over the rest when the selector feature is added.
        if (card != null) {
            return getConfiguration(configurationSolver.getConfigurationHashFor(card.getId()));
        } else {
            final PaymentMethod paymentMethod = userSelectionRepository.getPaymentMethod();
            if (paymentMethod == null) {
                // The user did not select any payment method, thus the dominant discount is the general config
                return getConfiguration(configurationSolver.getDefaultSelectedAmountConfiguration());
            } else {
                return getConfiguration(configurationSolver.getConfigurationHashFor(paymentMethod.getId()));
            }
        }
    }

    @Override
    public DiscountConfigurationModel getConfigurationFor(@NonNull final String customOptionId) {
        return getConfiguration(configurationSolver.getConfigurationHashFor(customOptionId));
    }

    private DiscountConfigurationModel getConfiguration(@Nullable final String hash) {
        final DiscountConfigurationModel discountModel = discountsConfigurations.get(hash);
        final DiscountConfigurationModel defaultConfig =
            discountsConfigurations.get(configurationSolver.getDefaultSelectedAmountConfiguration());
        if (discountModel == null && defaultConfig == null) {
            return DiscountConfigurationModel.NONE;
        }
        return discountModel == null ? defaultConfig : discountModel;
    }
}
