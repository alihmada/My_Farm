package com.ali.myfarm.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.myfarm.Classes.Animation;
import com.ali.myfarm.Classes.Calculation;
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
        holder.dateTime.setText(electricityList.get(position).getDate());
        holder.price.setText(Calculation.getNumber(electricityList.get(position).getPrice()));

        Animation.startAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return electricityList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView number, dateTime, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            number = itemView.findViewById(R.id.num);
            dateTime = itemView.findViewById(R.id.date_time);
            price = itemView.findViewById(R.id.price);
        }
    }
}
