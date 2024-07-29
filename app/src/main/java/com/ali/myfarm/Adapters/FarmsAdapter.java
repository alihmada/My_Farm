package com.ali.myfarm.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.myfarm.Classes.Animation;
import com.ali.myfarm.Classes.DiffCallback;
import com.ali.myfarm.Interfaces.ViewOnClickListener;
import com.ali.myfarm.Models.Farm;
import com.ali.myfarm.R;

import java.util.ArrayList;
import java.util.List;

public class FarmsAdapter extends RecyclerView.Adapter<FarmsAdapter.ViewHolder> implements Filterable {

    static List<Farm> filteredFarms;
    private final ViewOnClickListener viewOnClickListener;
    private final List<Farm> farms;

    public FarmsAdapter(ViewOnClickListener viewOnClickListener, List<Farm> farms) {
        this.viewOnClickListener = viewOnClickListener;
        filteredFarms = new ArrayList<>(farms);
        this.farms = farms;
    }

    @NonNull
    @Override
    public FarmsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.farm_row, parent, false);
        return new ViewHolder(view, viewOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(filteredFarms.get(position).getName());

        Animation.startAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return filteredFarms.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase();

                List<Farm> filteredList = new ArrayList<>();

                for (Farm farm : farms) {
                    if (farm.getName().toLowerCase().contains(query)) {
                        filteredList.add(farm);
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.values instanceof List) {
                    List<?> resultList = (List<?>) results.values;
                    if (!resultList.isEmpty() && resultList.get(0) instanceof Farm) {
                        @SuppressWarnings("unchecked") List<Farm> filteredList = (List<Farm>) resultList;

                        // Calculate the differences between the previous and new filtered lists
                        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(filteredFarms, filteredList));

                        // Update the filtered customers list
                        filteredFarms.clear();
                        filteredFarms.addAll(filteredList);

                        // Dispatch the specific change events to the adapter
                        diffResult.dispatchUpdatesTo(FarmsAdapter.this);
                    }
                }
            }
        };
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, number;

        public ViewHolder(@NonNull View itemView, ViewOnClickListener viewOnClickListener) {
            super(itemView);

            name = itemView.findViewById(R.id.name);

            itemView.setOnClickListener(v -> viewOnClickListener.onClickListener(String.valueOf(filteredFarms.get(getBindingAdapterPosition()).getRoot())));
        }
    }
}
