package com.mercadopago.android.px.testcheckout.assertions;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.test.espresso.matcher.ViewMatchers;
import android.view.View;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.model.Campaign;
import com.mercadopago.android.px.model.Discount;
import com.mercadopago.android.px.testcheckout.pages.InstallmentsPage;
import com.mercadopago.android.px.testcheckout.pages.OneTapPage;
import com.mercadopago.android.px.testcheckout.pages.PaymentMethodPage;
import com.mercadopago.android.px.testcheckout.pages.ReviewAndConfirmPage;
import org.hamcrest.Matcher;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasTextColor;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public abstract class DiscountValidator extends DefaultValidator {
    @NonNull protected final Campaign campaign;
    @NonNull protected final Discount discount;

    public DiscountValidator(@NonNull final Campaign campaign, @NonNull final Discount discount) {
        this.campaign = campaign;
        this.discount = discount;
    }

    @Override
    public void validate(@NonNull final PaymentMethodPage paymentMethodPage) {
        validateDiscountRow();
    }

    @Override
    public void validate(@NonNull final InstallmentsPage installmentsPage) {
        validateDiscountRow();
    }

    @Override
    public void validate(@NonNull final ReviewAndConfirmPage reviewAndConfirmPage) {
        final Matcher<View> discountDescription = withText(R.string.px_review_summary_discount);
        onView(discountDescription).check(matches(hasTextColor(R.color.px_discount_description)));
    }

    @Override
    public void validate(@NonNull final OneTapPage oneTapPage) {
        // TODO implement
    }

    private void validateDiscountRow() {
        final Matcher<View> amountDescription = withId(com.mercadopago.android.px.R.id.amount_description);
        final Matcher<View> maxCouponAmount = withId(com.mercadopago.android.px.R.id.max_coupon_amount);
        final Matcher<View> amountBeforeDiscount =
            withId(com.mercadopago.android.px.R.id.amount_before_discount);
        final Matcher<View> finalAmount = withId(com.mercadopago.android.px.R.id.final_amount);
        onView(amountDescription).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(maxCouponAmount).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(amountBeforeDiscount)
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(finalAmount).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(amountDescription).check(matches(withText(getAmountDescription())));
    }

    private String getAmountDescription() {
        final String amountDescriptionMessage;
        final Context context = getInstrumentation().getTargetContext();
        if (discount.hasPercentOff()) {
            amountDescriptionMessage = TextUtil
                .format(context, com.mercadopago.android.px.R.string.px_discount_percent_off,
                    discount.getPercentOff().toString());
        } else {
            amountDescriptionMessage = TextUtil
                .format(context, com.mercadopago.android.px.R.string.px_discount_amount_off_with_minus,
                    discount.getAmountOff().toString());
        }
        return amountDescriptionMessage;
    }
}