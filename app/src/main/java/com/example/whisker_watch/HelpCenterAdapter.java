package com.example.whisker_watch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HelpCenterAdapter extends RecyclerView.Adapter<HelpCenterAdapter.ViewHolder> {

    private List<HelpCenter> helpCenters;

    public HelpCenterAdapter(List<HelpCenter> helpCenters) {
        this.helpCenters = helpCenters;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_help_center, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HelpCenter center = helpCenters.get(position);
        holder.tvName.setText(center.getName());
        holder.tvLocation.setText(center.getLocation());
        holder.tvContact.setText(center.getContact());

        holder.btnCall.setOnClickListener(v -> {
            String phoneNumber = center.getContact();
            // Basic cleaning of the number string for the dialer
            String dialUri = "tel:" + phoneNumber.replaceAll("[^0-9+]", "");
            
            Context context = v.getContext();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(dialUri));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return helpCenters.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvLocation, tvContact;
        ImageButton btnCall;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCenterName);
            tvLocation = itemView.findViewById(R.id.tvCenterLocation);
            tvContact = itemView.findViewById(R.id.tvCenterContact);
            btnCall = itemView.findViewById(R.id.btnCall);
        }
    }
}