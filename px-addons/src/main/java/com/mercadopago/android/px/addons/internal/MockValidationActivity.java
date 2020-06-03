package com.mercadopago.android.px.addons.internal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.mercadopago.android.px.addons.BehaviourProvider;

public class MockValidationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent data = new Intent();
        data.putExtra(BehaviourProvider.getSecurityBehaviour().getExtraResultKey(), false);
        setResult(RESULT_OK, data);
        finish();
    }
}