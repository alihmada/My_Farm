package com.ali.myfarm.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.myfarm.Classes.Animation;
import com.ali.myfarm.Classes.Calculation;
import com.ali.myfarm.Classes.DateAndTime;
import com.ali.myfarm.Models.Sale;
import com.ali.myfarm.R;

import java.util.List;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.ViewHolder> implements Filterable {
    private final List<Sale> sales;

    public SalesAdapter(List<Sale> sales) {
        this.sales = sales;
    }

    @NonNull
    @Override
    public SalesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sale_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesAdapter.ViewHolder holder, int position) {
        holder.number.setText(String.valueOf(position + 1));
        holder.dateTime.setText(DateAndTime.getDate(holder.dateTime.getContext(), sales.get(position).getDateTime()));
        holder.sales.setText(Calculation.formatNumberWithCommas(sales.get(position).getSale()));
        holder.remaining.setText(Calculation.formatNumberWithCommas(sales.get(position).getRemaining()));

        Animation.startAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return sales.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView number, dateTime, sales, remaining;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.num);
            dateTime = itemView.findViewById(R.id.date_time);
            sales = itemView.findViewById(R.id.sales);
            remaining = itemView.findViewById(R.id.remaining);
        }
    }
}
