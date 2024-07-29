package com.ali.myfarm.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.myfarm.Classes.Animation;
import com.ali.myfarm.Classes.Calculation;
import com.ali.myfarm.Classes.DateAndTime;
import com.ali.myfarm.Models.Electricity;
import com.ali.myfarm.R;

import java.util.List;

public class ElectricityAdapter extends RecyclerView.Adapter<ElectricityAdapter.ViewHolder> {
    private final List<Electricity> electricityList;

    public ElectricityAdapter(List<Electricity> electricityList) {
        this.electricityList = electricityList;
    }

    @NonNull
    @Override
    public ElectricityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.electricity_row, parent, false);
        return new ElectricityAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ElectricityAdapter.ViewHolder holder, int position) {
        holder.number.setText(String.valueOf(position + 1));
        holder.dateTime.setText(DateAndTime.getDate(holder.dateTime.getContext(), electricityList.get(position).getDate()));
        if (electricityList.get(position).getType() == Electricity.Type.RECEIPT) {
            holder.type.setText(holder.type.getContext().getString(R.string.num_of_receipts));
            holder.title.setText(ContextCompat.getString(holder.itemView.getContext(), R.string.receipt));
            holder.item.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.electricity_receipt));
        } else {
            holder.type.setText(holder.type.getContext().getString(R.string.num_of_petrol_bottles));
            holder.title.setText(ContextCompat.getString(holder.itemView.getContext(), R.string.liter));
            holder.item.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.petrol));
        }
        holder.numberOfItems.setText(Calculation.formatNumberWithCommas(electricityList.get(position).getNumber()));
        holder.price.setText(Calculation.formatNumberWithCommas(Calculation.getElectricityOrHeatingPrice(electricityList.get(position).getNumber(), electricityList.get(position).getPrice())));

        Animation.startAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return electricityList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView number, dateTime, type, numberOfItems, title, price;
        ImageView item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            number = itemView.findViewById(R.id.num);
            dateTime = itemView.findViewById(R.id.date_time);
            numberOfItems = itemView.findViewById(R.id.number_of_items);
            title = itemView.findViewById(R.id.title);
            type = itemView.findViewById(R.id.type);
            price = itemView.findViewById(R.id.price);

            item = itemView.findViewById(R.id.electricity_item);
        }
    }
}
