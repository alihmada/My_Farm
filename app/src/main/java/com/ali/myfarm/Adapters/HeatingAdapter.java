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
import com.ali.myfarm.Models.Heating;
import com.ali.myfarm.R;

import java.util.List;

public class HeatingAdapter extends RecyclerView.Adapter<HeatingAdapter.ViewHolder> {

    private final List<Heating> heatingList;

    public HeatingAdapter(List<Heating> heatingList) {
        this.heatingList = heatingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.heating_row, parent, false);
        return new HeatingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.number.setText(String.valueOf(position + 1));
        holder.dateTime.setText(heatingList.get(position).getDate());
        if (heatingList.get(position).getType() == Heating.Type.GAS) {
            holder.type.setText(holder.type.getContext().getString(R.string.num_of_gas_cylinders));
            holder.item.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.gas));
        } else {
            holder.type.setText(holder.type.getContext().getString(R.string.num_of_diesel_bottles));
            holder.item.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.gasoline));
        }
        holder.numberOfItems.setText(String.valueOf(heatingList.get(position).getNumber()));
        holder.price.setText(String.valueOf(Calculation.getHeatingPrice(heatingList.get(position).getNumber(), heatingList.get(position).getPrice())));

        Animation.startAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return heatingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView number, dateTime, type, numberOfItems, price;
        ImageView item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            number = itemView.findViewById(R.id.num);
            dateTime = itemView.findViewById(R.id.date_time);
            numberOfItems = itemView.findViewById(R.id.number_of_items);
            type = itemView.findViewById(R.id.type);
            price = itemView.findViewById(R.id.price);

            item = itemView.findViewById(R.id.heating_item);
        }
    }
}
