package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.internal.util.JsonUtil;
import com.mercadopago.android.px.model.internal.AmountDescriptor;
import java.math.BigDecimal;

public class DiscountConfigurationModel implements Parcelable {

    public static final DiscountConfigurationModel NONE = new DiscountConfigurationModel(null, null, true);

    private final Discount discount;
    private final Campaign campaign;
    private final boolean isAvailable;
    private final Reason reason;
    private final AmountDescriptor discountAmountDescriptor;

    public static final Creator<DiscountConfigurationModel> CREATOR = new Creator<DiscountConfigurationModel>() {
        @Override
        public DiscountConfigurationModel createFromParcel(final Parcel in) {
            return new DiscountConfigurationModel(in);
        }

        @Override
        public DiscountConfigurationModel[] newArray(final int size) {
            return new DiscountConfigurationModel[size];
        }
    };

    public DiscountConfigurationModel(@Nullable final Discount discount, @Nullable final Campaign campaign,
        final boolean isAvailable) {
        this.discount = discount;
        this.campaign = campaign;
        this.isAvailable = isAvailable;
        reason = null;
        discountAmountDescriptor = null;
    }

    protected DiscountConfigurationModel(final Parcel in) {
        discount = in.readParcelable(Discount.class.getClassLoader());
        campaign = in.readParcelable(Campaign.class.getClassLoader());
        isAvailable = in.readByte() != 0;
        reason = in.readParcelable(Reason.class.getClassLoader());
        discountAmountDescriptor = in.readParcelable(AmountDescriptor.class.getClassLoader());
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(discount, flags);
        dest.writeParcelable(campaign, flags);
        dest.writeByte((byte) (isAvailable ? 1 : 0));
        dest.writeParcelable(reason, flags);
        dest.writeParcelable(discountAmountDescriptor, flags);
    }

    public Discount getDiscount() {
        return discount;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public Reason getReason() {
        return reason;
    }

    public AmountDescriptor getDiscountAmountDescriptor() {
        return JsonUtil.fromJson("{\n" +
            "    \"descriptions\": [\n" +
            "      {\n" +
            "        \"message\": \"70% OFF \",\n" +
            "        \"background_color\": \"#ffffff\",\n" +
            "        \"text_color\": \"#ffffff\",\n" +
            "        \"weight\": \"semi_bold\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"message\": \"(Tope: $400)\",\n" +
            "        \"background_color\": \"#ffffff\",\n" +
            "        \"text_color\": \"#ffffff\",\n" +
            "        \"weight\": \"regular\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"amount\": {\n" +
            "      \"message\": \"- $400\",\n" +
            "      \"background_color\": \"#ffffff\",\n" +
            "      \"text_color\": \"#ffffff\",\n" +
            "      \"weight\": \"semi_bold\"\n" +
            "    },\n" +
            "    \"brief\": {\n" +
            "      \"message\": \"Podés usar este descuento una sola vez\",\n" +
            "      \"background_color\": \"#ffffff\",\n" +
            "      \"text_color\": \"#ccededed\",\n" +
            "      \"weight\": \"regular\"\n" +
            "    },\n" +
            "    \"icon_url\": \"\"\n" +
            "  }", AmountDescriptor.class);
    }

    public boolean hasValidDiscount() {
        return discount != null && campaign != null;
    }

    @Deprecated
    public BigDecimal getAmountWithDiscount(final BigDecimal amount) {
        if (hasValidDiscount()) {
            return discount.getAmountWithDiscount(amount);
        } else {
            return amount;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }
}