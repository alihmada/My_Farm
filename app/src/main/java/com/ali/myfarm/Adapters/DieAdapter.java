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
import com.ali.myfarm.Models.Chicken;
import com.ali.myfarm.R;

import java.util.List;

public class DieAdapter extends RecyclerView.Adapter<DieAdapter.ViewHolder> implements Filterable {
    private final List<Chicken> chickens;

    public DieAdapter(List<Chicken> chickens) {
        this.chickens = chickens;
    }

    @NonNull
    @Override
    public DieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checken_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DieAdapter.ViewHolder holder, int position) {
        holder.number.setText(String.valueOf(position + 1));
        holder.dateTime.setText(chickens.get(position).getDate());
        holder.alive.setText(String.valueOf(chickens.get(position).getAlive()));
        holder.dead.setText(String.valueOf(chickens.get(position).getDead()));

        Animation.startAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return chickens.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView number, dateTime, alive, dead;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.num);
            dateTime = itemView.findViewById(R.id.date_time);
            alive = itemView.findViewById(R.id.alive_num);
            dead = itemView.findViewById(R.id.dead_num);
        }
    }
}
