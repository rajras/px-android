package com.mercadopago.android.px.internal.core;

import android.content.Context;
import android.support.annotation.NonNull;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public final class ProductIdInterceptor implements Interceptor {

    private static final String HEADER_KEY = "X-Product-Id";

    @NonNull private final ProductIdProvider productIdProvider;

    public ProductIdInterceptor(@NonNull final Context context) {
        productIdProvider = new ApplicationModule(context).getProductIdProvider();
    }

    @Override
    public Response intercept(@NonNull final Chain chain) throws IOException {
        final Request originalRequest = chain.request();
        final Request request = originalRequest.newBuilder()
            .header(HEADER_KEY, productIdProvider.getProductId())
            .build();
        return chain.proceed(request);
    }
}