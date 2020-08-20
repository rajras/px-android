package com.mercadopago.android.px.testcheckout.idleresources;

import androidx.test.core.app.ApplicationProvider;
import com.mercadopago.android.px.internal.util.HttpClientUtil;
import com.mercadopago.android.testlib.HttpResource;
import okhttp3.OkHttpClient;

public class CheckoutResource extends HttpResource {
    @Override
    protected OkHttpClient getClient() {
        return HttpClientUtil.getClient(ApplicationProvider.getApplicationContext(), 10, 10, 10);
    }
}