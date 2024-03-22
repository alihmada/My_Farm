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
import com.ali.myfarm.Models.Bag;
import com.ali.myfarm.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class EndAdapter extends RecyclerView.Adapter<EndAdapter.ViewHolder> {
    private final List<Bag> bags;

    public EndAdapter(List<Bag> bags) {
        this.bags = bags;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.number.setText(String.valueOf(position + 1));
        holder.dateTime.setText(bags.get(position).getDateOfModification());
        holder.numberOfBags.setText(String.valueOf(bags.get(position).getNumber()));
        if (bags.get(position).getOperation() == Bag.Operation.ADD) {
            holder.operationType.setText(holder.itemView.getResources().getString(R.string.add));
            holder.operation.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.add));
            holder.price.setText(String.valueOf(Calculation.getBagsPrice(bags.get(position).getNumber(), bags.get(position).getPriceOfTon())));
        } else {
            holder.operationType.setText(holder.itemView.getResources().getString(R.string.remove));
            holder.operation.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.remove));
            holder.priceCard.setVisibility(View.GONE);
        }
        holder.feedBag.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.end));

        Animation.startAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return bags.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView priceCard;
        TextView number, dateTime, numberOfBags, operationType, price;
        ImageView feedBag, operation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            priceCard = itemView.findViewById(R.id.price_card);

            number = itemView.findViewById(R.id.num);
            dateTime = itemView.findViewById(R.id.date_time);
            numberOfBags = itemView.findViewById(R.id.number_of_bags);
            operationType = itemView.findViewById(R.id.operation_txt);
            price = itemView.findViewById(R.id.price);

            feedBag = itemView.findViewById(R.id.feed_bag);
            operation = itemView.findViewById(R.id.operation);
        }
    }
}
