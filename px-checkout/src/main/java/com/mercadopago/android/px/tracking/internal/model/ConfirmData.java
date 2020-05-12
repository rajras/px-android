package com.mercadopago.android.px.tracking.internal.model;

import android.os.Parcel;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import com.mercadopago.android.px.tracking.internal.events.ConfirmEvent;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@Keep
public class ConfirmData extends AvailableMethod {

    private final String reviewType;
    private int paymentMethodSelectedIndex;

    public static final Creator<ConfirmData> CREATOR = new Creator<ConfirmData>() {
        @Override
        public ConfirmData createFromParcel(final Parcel in) {
            return new ConfirmData(in);
        }

        @Override
        public ConfirmData[] newArray(final int size) {
            return new ConfirmData[size];
        }
    };

    public static ConfirmData from(String paymentTypeId, String paymentMethodId, boolean isCompliant, boolean hasAdditionalInfoNeeded) {
        final Map<String, Object> extraInfo = new HashMap<>();
        extraInfo.put("has_payer_information", isCompliant);
        extraInfo.put("additional_information_needed", hasAdditionalInfoNeeded);
        return new ConfirmData(ConfirmEvent.ReviewType.ONE_TAP, new AvailableMethod(paymentMethodId, paymentTypeId, extraInfo));
    }

    public ConfirmData(@NonNull final ConfirmEvent.ReviewType reviewType, final int paymentMethodSelectedIndex,
        @NonNull final AvailableMethod availableMethod) {
        super(availableMethod.paymentMethodId, availableMethod.paymentMethodType, availableMethod.extraInfo);
        this.reviewType = reviewType.value;
        this.paymentMethodSelectedIndex = paymentMethodSelectedIndex;
    }

    public ConfirmData(@NonNull final ConfirmEvent.ReviewType reviewType,
        @NonNull final AvailableMethod availableMethod) {
        super(availableMethod.paymentMethodId, availableMethod.paymentMethodType, availableMethod.extraInfo);
        this.reviewType = reviewType.value;
    }

    @SuppressWarnings("WeakerAccess")
    protected ConfirmData(final Parcel in) {
        super(in);
        reviewType = in.readString();
        paymentMethodSelectedIndex = in.readInt();
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(reviewType);
        dest.writeInt(paymentMethodSelectedIndex);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}