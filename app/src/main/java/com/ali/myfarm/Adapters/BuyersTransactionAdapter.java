package com.ali.myfarm.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.myfarm.Classes.Animation;
import com.ali.myfarm.Classes.Calculation;
import com.ali.myfarm.Interfaces.ViewOnClickListener;
import com.ali.myfarm.Models.Buyer;
import com.ali.myfarm.R;
import com.google.gson.Gson;

import java.util.List;

public class BuyersTransactionAdapter extends RecyclerView.Adapter<BuyersTransactionAdapter.ViewHolder> {
    public final List<Buyer> buyers;
    private final ViewOnClickListener viewOnClickListener;

    public BuyersTransactionAdapter(List<Buyer> buyers, ViewOnClickListener viewOnClickListener) {
        this.buyers = buyers;
        this.viewOnClickListener = viewOnClickListener;
    }

    @NonNull
    @Override
    public BuyersTransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_row, parent, false);
        return new ViewHolder(view, viewOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.number.setText(String.valueOf(position + 1));
        holder.dateTime.setText(buyers.get(position).getDate());
        holder.header.setText(holder.header.getContext().getString(R.string.buyer_header));
        holder.name.setText(buyers.get(position).getName());
        holder.count.setText(String.valueOf(buyers.get(position).getNumberOfChickens()));
        holder.weight.setText(Calculation.formatNumberWithCommas(buyers.get(position).getWeightOfChickens()));
        holder.price.setText(Calculation.formatNumberWithCommas(Calculation.getTotalPrice(buyers.get(position).getWeightOfChickens(), buyers.get(position).getPrice())));

        Animation.startAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return buyers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView number, dateTime, header, name, count, weight, price;
        Gson gson;

        public ViewHolder(@NonNull View itemView, ViewOnClickListener viewOnClickListener) {
            super(itemView);
            gson = new Gson();

            number = itemView.findViewById(R.id.num);
            dateTime = itemView.findViewById(R.id.date_time);
            name = itemView.findViewById(R.id.person_name);
            header = itemView.findViewById(R.id.person_head);
            count = itemView.findViewById(R.id.chicken_count);
            weight = itemView.findViewById(R.id.chicken_weight);
            price = itemView.findViewById(R.id.price);


            itemView.setOnClickListener(v -> viewOnClickListener.onClickListener(gson.toJson(buyers.get(getLayoutPosition()))));
        }
    }
}
