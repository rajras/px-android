package com.mercadopago.android.px.addons;

import android.content.Context;
import android.support.annotation.NonNull;
import java.util.Locale;

public final class FakeLocaleBehaviourImpl implements LocaleBehaviour {

    @NonNull
    @Override
    public Context attachBaseContext(@NonNull final Context context) {
        final Locale locale = new Locale("en", "US");
        return LocaleContextWrapper.wrap(context, locale);
    }
}