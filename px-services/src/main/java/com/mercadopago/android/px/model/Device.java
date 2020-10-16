package com.mercadopago.android.px.model;

import android.content.Context;
import androidx.annotation.NonNull;
import com.mercadopago.android.px.addons.ESCManagerBehaviour;
import com.mercadopago.android.px.addons.internal.ESCManagerDefaultBehaviour;

public class Device {

    public Fingerprint fingerprint;

    /**
     *
     * @deprecated use {@link com.mercadopago.android.px.model.Device#Device(Context, ESCManagerBehaviour)}
     */
    @Deprecated
    public Device(final Context context) {
        fingerprint = new Fingerprint(context, new ESCManagerDefaultBehaviour());
    }

    public Device(final Context context, @NonNull final ESCManagerBehaviour escManagerBehaviour) {
        fingerprint = new Fingerprint(context, escManagerBehaviour);
    }
}
