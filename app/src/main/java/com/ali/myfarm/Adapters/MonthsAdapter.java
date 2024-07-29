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

import com.ali.myfarm.Classes.DiffCallback;
import com.ali.myfarm.Interfaces.ViewOnClickListener;
import com.ali.myfarm.R;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class MonthsAdapter extends RecyclerView.Adapter<MonthsAdapter.ViewHolder> implements Filterable {

    static List<AbstractMap.SimpleEntry<String, String>> filteredPeriods;
    private final ViewOnClickListener viewOnClickListener;
    private final List<AbstractMap.SimpleEntry<String, String>> periods;

    public MonthsAdapter(ViewOnClickListener viewOnClickListener, List<AbstractMap.SimpleEntry<String, String>> periods) {
        this.viewOnClickListener = viewOnClickListener;
        filteredPeriods = new ArrayList<>(periods);
        this.periods = periods;
    }

    @NonNull
    @Override
    public MonthsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.year_row, parent, false);
        return new MonthsAdapter.ViewHolder(view, viewOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthsAdapter.ViewHolder holder, int position) {
        holder.number.setText(filteredPeriods.get(position).getKey());
        holder.period.setText(filteredPeriods.get(position).getValue());
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

                List<AbstractMap.SimpleEntry<String, String>> filteredList = new ArrayList<>();

                for (AbstractMap.SimpleEntry<String, String> period : periods) {
                    if (period.getValue().toLowerCase().contains(query) ||
                            period.getKey().toLowerCase().contains(query)) {
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
                    if (!resultList.isEmpty() && resultList.get(0) instanceof AbstractMap.SimpleEntry) {
                        @SuppressWarnings("unchecked") List<AbstractMap.SimpleEntry<String, String>> filteredList = (List<AbstractMap.SimpleEntry<String, String>>) resultList;

                        // Calculate the differences between the previous and new filtered lists
                        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(filteredPeriods, filteredList));

                        // Update the filtered customers list
                        filteredPeriods.clear();
                        filteredPeriods.addAll(filteredList);

                        // Dispatch the specific change events to the adapter
                        diffResult.dispatchUpdatesTo(MonthsAdapter.this);
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

            itemView.setOnClickListener(v -> viewOnClickListener.onClickListener(number.getText().toString()));
        }
    }
}
