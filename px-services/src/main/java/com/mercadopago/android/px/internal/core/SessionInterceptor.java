package com.mercadopago.android.px.internal.core;

import android.content.Context;
import android.support.annotation.NonNull;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public final class SessionInterceptor implements Interceptor {

    private static final String SESSION_ID_HEADER = "X-Session-Id";

    @NonNull private final SessionIdProvider sessionIdProvider;

    public SessionInterceptor(@NonNull final Context context) {
        sessionIdProvider = new ApplicationModule(context).getSessionIdProvider();
    }

    @Override
    public Response intercept(@NonNull final Chain chain) throws IOException {
        final Request originalRequest = chain.request();
        final Request request = originalRequest.newBuilder()
            .header(SESSION_ID_HEADER, sessionIdProvider.getSessionId())
            .build();
        return chain.proceed(request);
    }
}