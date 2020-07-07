package com.mercadopago.android.px.internal.view;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.features.TermsAndConditionsActivity;
import com.mercadopago.android.px.internal.util.ViewUtils;
import com.mercadopago.android.px.model.internal.DiscountDescriptionDetail;
import com.mercadopago.android.px.model.internal.TextUrl;

public class DiscountDetail extends CompactComponent<DiscountDetail.Props, Void> {

    public static class Props {
        @NonNull /* default */ final DiscountDescriptionDetail discountDescriptionDetail;

        public Props(@NonNull final DiscountDescriptionDetail discountDescriptionDetail) {
            this.discountDescriptionDetail = discountDescriptionDetail;
        }
    }

    /* default */ DiscountDetail(@NonNull final Props props) {
        super(props);
    }

    @Override
    public View render(@NonNull final ViewGroup parent) {
        final View mainContainer = ViewUtils.inflate(parent, R.layout.px_view_discount_detail);
        configureSummary(mainContainer);
        configureDescription(mainContainer);
        configureTermsAndConditions(mainContainer);
        return mainContainer;
    }

    private void configureSummary(@NonNull final View mainContainer) {
        final MPTextView summary = mainContainer.findViewById(R.id.summary);
        summary.setText(props.discountDescriptionDetail.getSummary());
    }

    private void configureDescription(@NonNull final View mainContainer) {
        final MPTextView description = mainContainer.findViewById(R.id.description);
        description.setText(props.discountDescriptionDetail.getDescription());
    }

    private void configureTermsAndConditions(@NonNull final View mainContainer) {
        final MPTextView linkText = mainContainer.findViewById(R.id.legal_terms);
        final TextUrl legalTerms = props.discountDescriptionDetail.getLegalTerms();
        linkText.setText(legalTerms.getContent());
        linkText.setOnClickListener(v -> TermsAndConditionsActivity
            .start(mainContainer.getContext(), legalTerms.getUrl()));
    }
}