package com.mercadopago.android.px.internal.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.util.ViewUtils;
import com.mercadopago.android.px.model.internal.DiscountDescriptionDetail;
import com.mercadopago.android.px.model.internal.TextUrl;
import javax.annotation.Nonnull;

public class DiscountDetailContainer extends CompactComponent<DiscountDetailContainer.Props, Void> {

    public static final class Props {
        @NonNull /* default */ final DiscountDescriptionDetail discountDescriptionDetail;

        public Props(@Nonnull final DiscountDescriptionDetail discountDescriptionDetail) {
            this.discountDescriptionDetail = discountDescriptionDetail;
        }
    }

    /* default */ DiscountDetailContainer(@NonNull final DiscountDetailContainer.Props props) {
        super(props);
    }

    @Nullable
    @Override
    public View render(@NonNull final ViewGroup parent) {
        addDiscountTitle(parent);
        addDiscountDetail(parent);
        return parent;
    }

    private void addDiscountDetail(@NonNull final ViewGroup parent) {
        final View discountView = new DiscountDetail(new DiscountDetail.Props(props.discountDescriptionDetail)).render(parent);
        parent.addView(discountView);
    }

    private void addDiscountTitle(@NonNull final ViewGroup parent) {
        final View headerView = ViewUtils.inflate(parent, R.layout.px_view_big_modal_title);
        final DiscountDescriptionDetail discountDescriptionDetail = props.discountDescriptionDetail;
        final TextUrl textUrl = props.discountDescriptionDetail.getBadge();
        ((MPTextView) headerView.findViewById(R.id.title)).setText(discountDescriptionDetail.getTitle());
        ViewUtils.loadOrGone(discountDescriptionDetail.getSubtitle(), (headerView.findViewById(R.id.subtitle)));

        if (textUrl != null) {
            final Badge badge = headerView.findViewById(R.id.badge);
            badge.setText(textUrl.getContent());
            badge.setIconUrl(textUrl.getUrl());
            badge.setVisibility(View.VISIBLE);
        }

        parent.addView(headerView);
    }
}