package com.mercadopago.android.px.internal.features.review_payment_methods;

import com.mercadopago.android.px.mocks.PaymentMethodStub;
import com.mercadopago.android.px.model.PaymentMethod;
import com.mercadopago.android.px.model.Sites;
import com.mercadopago.android.px.tracking.internal.MPTracker;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@PrepareForTest(MPTracker.class)
@RunWith(PowerMockRunner.class)
public class ReviewPaymentMethodsPresenterTest {

    @Mock private ReviewPaymentMethods.View view;

    private ReviewPaymentMethodsPresenter presenter;
    private List<PaymentMethod> stubPaymentMethodList;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(MPTracker.class);
        when(MPTracker.getInstance()).thenReturn(mock(MPTracker.class));
        stubPaymentMethodList = PaymentMethodStub.getAllBySite(Sites.ARGENTINA.getId());
        presenter = new ReviewPaymentMethodsPresenter(stubPaymentMethodList);
        presenter.attachView(view);
    }

    @Test
    public void whenInitializeThenShowSupportedPaymentMethodsList() {
        presenter.initialize();
        verify(view).initializeSupportedPaymentMethods(stubPaymentMethodList);
    }
}