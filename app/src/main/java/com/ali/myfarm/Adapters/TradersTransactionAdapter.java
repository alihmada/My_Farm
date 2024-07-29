package com.ali.myfarm.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.myfarm.Classes.Animation;
import com.ali.myfarm.Classes.Calculation;
import com.ali.myfarm.Classes.DateAndTime;
import com.ali.myfarm.Interfaces.ViewOnClickListener;
import com.ali.myfarm.Models.Trader;
import com.ali.myfarm.R;
import com.google.gson.Gson;

import java.util.List;

public class TradersTransactionAdapter extends RecyclerView.Adapter<TradersTransactionAdapter.ViewHolder> {
    public final List<Trader> traders;
    private final ViewOnClickListener viewOnClickListener;

    public TradersTransactionAdapter(List<Trader> traders, ViewOnClickListener viewOnClickListener) {
        this.traders = traders;
        this.viewOnClickListener = viewOnClickListener;
    }

    @NonNull
    @Override
    public TradersTransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_row, parent, false);
        return new ViewHolder(view, viewOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.number.setText(String.valueOf(position + 1));
        holder.dateTime.setText(DateAndTime.getDate(holder.dateTime.getContext(), traders.get(position).getDate()));
        holder.name.setText(traders.get(position).getName());
        holder.count.setText(String.valueOf(traders.get(position).getNumberOfChickens()));
        double w = Calculation.getChickensWeight(traders.get(position).getTotalWeightOfCages(), traders.get(position).getTotalWeightOfEmptyCages());
        holder.weight.setText(Calculation.formatNumberWithCommas(w));
        holder.price.setText(Calculation.formatNumberWithCommas(Calculation.getTotalPrice(w, traders.get(position).getPrice())));


        Animation.startAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return traders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView number, dateTime, name, count, weight, price;
        Gson gson;

        public ViewHolder(@NonNull View itemView, ViewOnClickListener viewOnClickListener) {
            super(itemView);
            gson = new Gson();

            number = itemView.findViewById(R.id.num);
            dateTime = itemView.findViewById(R.id.date_time);
            name = itemView.findViewById(R.id.person_name);
            count = itemView.findViewById(R.id.chicken_count);
            weight = itemView.findViewById(R.id.chicken_weight);
            price = itemView.findViewById(R.id.price);


            itemView.setOnClickListener(v -> viewOnClickListener.onClickListener(gson.toJson(traders.get(getLayoutPosition()))));
        }
    }
}
