package com.mercadopago.testcheckout.pages;


import android.view.View;

import com.mercadopago.testcheckout.assertions.CheckoutValidator;
import com.mercadopago.testlib.pages.PageObject;

import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class CashPage extends PageObject<CheckoutValidator> {

    public CashPage() {
        // This constructor is intentionally empty. Nothing special is needed here.
    }

    public CashPage(CheckoutValidator validator) {
        super(validator);
    }

    public ReviewAndConfirmPage selectMethod(final String paymentMethodName) {
        Matcher<View> paymentCell = withText(paymentMethodName);
        onView(paymentCell).perform(click());
        return new ReviewAndConfirmPage(validator);
    }

    @Override
    public CashPage validate(CheckoutValidator validator) {
        validator.validate(this);
        return this;
    }

}