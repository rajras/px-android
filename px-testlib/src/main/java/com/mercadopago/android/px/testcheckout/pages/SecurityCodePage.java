package com.mercadopago.android.px.testcheckout.pages;

import androidx.test.espresso.action.ViewActions;
import com.mercadopago.android.px.testcheckout.assertions.CheckoutValidator;
import com.mercadopago.android.testlib.pages.PageObject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;

@Deprecated
public class SecurityCodePage extends PageObject<CheckoutValidator> {

    public SecurityCodePage() {
    }

    public SecurityCodePage(final CheckoutValidator validator) {
        super(validator);
    }

    @Override
    public SecurityCodePage validate(CheckoutValidator validator) {
        validator.validate(this);
        return this;
    }

    public IdentificationPage enterSecurityCodeForNewCard(final String cvvNumber) {
        return new IdentificationPage(validator);
    }

    public ReviewAndConfirmPage enterSecurityCodeForSavedCard(final String cvvNumber) {
        return new ReviewAndConfirmPage(validator);
    }

    public NoCheckoutPage pressBackWithExclusion() {
        onView(isRoot()).perform(ViewActions.pressBack());
        return new NoCheckoutPage(validator);
    }

    public PaymentMethodPage pressBack() {
        onView(isRoot()).perform(ViewActions.pressBack());
        return new PaymentMethodPage(validator);
    }

    public ExpiryDatePage pressPrevious() {
        return new ExpiryDatePage(validator);
    }
}
