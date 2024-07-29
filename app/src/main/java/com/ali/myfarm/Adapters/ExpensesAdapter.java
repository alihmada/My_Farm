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
import com.ali.myfarm.Models.Expenses;
import com.ali.myfarm.R;

import java.util.List;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ViewHolder> {
    private static List<Expenses> expenses;
    private final ViewOnClickListener viewOnClickListener;

    public ExpensesAdapter(List<Expenses> expenses, ViewOnClickListener viewOnClickListener) {
        ExpensesAdapter.expenses = expenses;
        this.viewOnClickListener = viewOnClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expenses_row, parent, false);
        return new ViewHolder(view, viewOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.dateTime.setText(DateAndTime.getDate(holder.dateTime.getContext(), expenses.get(position).getDate()));
        holder.reason.setText(String.valueOf(expenses.get(position).getReason()));
        holder.balance.setText(Calculation.formatNumberWithCommas(expenses.get(position).getBalance()));

        Animation.startAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView reason, dateTime, balance;

        public ViewHolder(@NonNull View itemView, ViewOnClickListener viewOnClickListener) {
            super(itemView);
            reason = itemView.findViewById(R.id.reason);
            dateTime = itemView.findViewById(R.id.date);
            balance = itemView.findViewById(R.id.balance);

            itemView.setOnClickListener(v -> viewOnClickListener.onClickListener(expenses.get(getBindingAdapterPosition()).getId()));
        }
    }
}
