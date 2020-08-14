package com.mercadopago.android.testlib;

import androidx.test.core.app.ApplicationProvider;
import java.io.InputStream;

public final class MockTestUtils {

    private MockTestUtils() {
    }

    public static String getBody(final int rawId) {
        String body = "";
        final InputStream inputStream = ApplicationProvider.getApplicationContext().getResources().openRawResource(rawId);
        try {
            final byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            body = new String(b);
        } catch (final Exception ignored) {
        }
        return body;
    }
}