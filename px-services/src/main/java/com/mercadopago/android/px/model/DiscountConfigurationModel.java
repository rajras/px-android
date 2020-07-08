package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;

public class DiscountConfigurationModel implements Parcelable {

    public static final DiscountConfigurationModel NONE = new DiscountConfigurationModel(null, null, true);

    private final Discount discount;
    private final Campaign campaign;
    private final boolean isAvailable;
    private final Reason reason;
    @SerializedName("discount_overview")
    private final AmountDescriptor discountAmountDescriptor;
    @SerializedName("discount_description")
    private final DiscountDescriptionDetail discountDescriptionDetail;

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
        discountDescriptionDetail = null;
    }

    protected DiscountConfigurationModel(final Parcel in) {
        discount = in.readParcelable(Discount.class.getClassLoader());
        campaign = in.readParcelable(Campaign.class.getClassLoader());
        isAvailable = in.readByte() != 0;
        reason = in.readParcelable(Reason.class.getClassLoader());
        discountAmountDescriptor = in.readParcelable(AmountDescriptor.class.getClassLoader());
        discountDescriptionDetail = in.readParcelable(DiscountDescriptionDetail.class.getClassLoader());
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(discount, flags);
        dest.writeParcelable(campaign, flags);
        dest.writeByte((byte) (isAvailable ? 1 : 0));
        dest.writeParcelable(reason, flags);
        dest.writeParcelable(discountAmountDescriptor, flags);
        dest.writeParcelable(discountDescriptionDetail, flags);
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
        return discountAmountDescriptor;
    }

    public DiscountDescriptionDetail getDiscountDescriptionDetail() {
        return discountDescriptionDetail;
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