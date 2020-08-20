package com.mercadopago.android.px.internal.features.express.offline_methods;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mercadopago.android.px.R;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* default */ final class OfflineMethodsAdapter extends RecyclerView.Adapter<OfflineMethodsRowHolder> {

    private final List<OfflineMethodItem> offlineItems = new ArrayList<>();
    private OfflineMethodsRowHolder lastHolder;
    private int lastPositionSelected = -1;
    private final OfflineMethodsFragment.OnMethodSelectedListener onMethodSelectedListener;

    public OfflineMethodsAdapter(final OfflineMethodsFragment.OnMethodSelectedListener onMethodSelectedListener) {
        this.onMethodSelectedListener = onMethodSelectedListener;
    }

    @NonNull
    @Override
    public OfflineMethodsRowHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int i) {
        final View offlineMethodView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.px_view_offline_item, parent, false);

        return new OfflineMethodsRowHolder(offlineMethodView);
    }

    @Override
    public void onBindViewHolder(@NonNull final OfflineMethodsRowHolder holder,
        @SuppressLint("RecyclerView") final int position) {
        holder.setChecked(lastPositionSelected >= 0 && lastPositionSelected == position);

        holder.populate(offlineItems.get(position), () -> {
            if (lastHolder != null) {
                lastHolder.setChecked(false);
            }

            holder.setChecked(true);
            lastHolder = holder;
            lastPositionSelected = position;

            onMethodSelectedListener.onItemSelected(offlineItems.get(position));
        });
    }

    public void setItems(@NonNull final Collection<OfflineMethodItem> items) {
        offlineItems.clear();
        offlineItems.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return offlineItems.size();
    }

    interface OnItemClicked {
        void onClick();
    }
}
