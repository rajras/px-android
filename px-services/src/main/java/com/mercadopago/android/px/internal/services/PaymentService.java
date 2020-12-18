package com.mercadopago.android.px.internal.services;

import com.mercadopago.android.px.internal.callbacks.MPCall;
import com.mercadopago.android.px.model.Payment;
import java.util.Map;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface PaymentService {

    String PAYMENTS_VERSION = "2.0";

    @POST("{environment}/px_mobile/payments?api_version=" + PAYMENTS_VERSION)
    MPCall<Payment> createPayment(
        @Path(value = "environment", encoded = true) String environment,
        @Header("X-Idempotency-Key") String transactionId, @Header("X-Security") String securityType,
        @Body Map<String, Object> additionalInfo, @QueryMap Map<String, String> query);
}
