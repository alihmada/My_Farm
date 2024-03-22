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
import com.ali.myfarm.R;

import java.util.ArrayList;
import java.util.List;

public class PeriodsAdapter extends RecyclerView.Adapter<PeriodsAdapter.ViewHolder> implements Filterable {

    static List<String> filteredPeriods;
    private final ViewOnClickListener viewOnClickListener;
    private final List<String> periods;

    public PeriodsAdapter(ViewOnClickListener viewOnClickListener, List<String> periods) {
        this.viewOnClickListener = viewOnClickListener;
        filteredPeriods = new ArrayList<>(periods);
        this.periods = periods;
    }

    @NonNull
    @Override
    public PeriodsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.year_row, parent, false);
        return new PeriodsAdapter.ViewHolder(view, viewOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PeriodsAdapter.ViewHolder holder, int position) {
        holder.number.setText(String.valueOf(position + 1));
        holder.period.setText(filteredPeriods.get(position));

        Animation.startAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return filteredPeriods.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase();

                List<String> filteredList = new ArrayList<>();

                for (String period : periods) {
                    if (period.toLowerCase().contains(query)) {
                        filteredList.add(period);
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
                    if (!resultList.isEmpty() && resultList.get(0) instanceof String) {
                        @SuppressWarnings("unchecked") List<String> filteredList = (List<String>) resultList;

                        // Calculate the differences between the previous and new filtered lists
                        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(filteredPeriods, filteredList));

                        // Update the filtered customers list
                        filteredPeriods.clear();
                        filteredPeriods.addAll(filteredList);

                        // Dispatch the specific change events to the adapter
                        diffResult.dispatchUpdatesTo(PeriodsAdapter.this);
                    }
                }
            }
        };
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView number, period;

        public ViewHolder(@NonNull View itemView, ViewOnClickListener viewOnClickListener) {
            super(itemView);

            number = itemView.findViewById(R.id.num);
            period = itemView.findViewById(R.id.period);

            itemView.setOnClickListener(v -> viewOnClickListener.onClickListener(period.getText().toString()));
        }
    }
}
