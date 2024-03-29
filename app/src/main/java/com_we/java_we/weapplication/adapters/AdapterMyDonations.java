package com_we.java_we.weapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com_we.java_we.weapplication.R;
import com_we.java_we.weapplication.models.Donation;

public class AdapterMyDonations extends RecyclerView.Adapter<AdapterMyDonations.MyDonationViewHolder> {

    private List<Donation> donations;

    public AdapterMyDonations(List<Donation> donations) {
        this.donations = donations;
    }

    @NonNull
    @Override
    public MyDonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_donation, parent, false);
        return new MyDonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyDonationViewHolder holder, int position) {
        holder.date.setText(donations.get(position).getDate());
        holder.value.setText(donations.get(position).getValue() + " CAD");
    }

    @Override
    public int getItemCount() {
        return donations.size();
    }

    public class MyDonationViewHolder extends RecyclerView.ViewHolder {
        private TextView date, value;

        public MyDonationViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.text_view_date);
            value = itemView.findViewById(R.id.text_view_my_value);

        }
    }
}
