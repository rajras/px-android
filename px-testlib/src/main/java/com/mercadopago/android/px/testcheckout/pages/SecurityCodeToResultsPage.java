package com.mercadopago.android.px.testcheckout.pages;

import androidx.annotation.NonNull;
import com.mercadopago.android.px.testcheckout.assertions.CheckoutValidator;
import com.mercadopago.android.testlib.pages.PageObject;

@Deprecated
public class SecurityCodeToResultsPage extends PageObject<CheckoutValidator> {

    public SecurityCodeToResultsPage() {
        // This constructor is intentionally empty. Nothing special is needed here.
    }

    public SecurityCodeToResultsPage(@NonNull final CheckoutValidator validator) {
        super(validator);
    }

    public CongratsPage enterSecurityCodeToCongratsPage(final String cvv) {
        return new CongratsPage(validator);
    }

    public PendingPage enterSecurityCodeToPendingPage(final String cvv) {
        return new PendingPage(validator);
    }

    public CallForAuthPage enterSecurityCodeToCallForAuthPage(final String cvv) {
        return new CallForAuthPage(validator);
    }

    @Override
    public SecurityCodeToResultsPage validate(final CheckoutValidator validator) {
        validator.validate(this);
        return this;
    }
}
