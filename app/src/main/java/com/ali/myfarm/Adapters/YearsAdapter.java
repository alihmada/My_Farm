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

public class YearsAdapter extends RecyclerView.Adapter<YearsAdapter.ViewHolder> implements Filterable {
    static List<String> filteredYears;
    private final ViewOnClickListener viewOnClickListener;
    private final List<String> years;


    public YearsAdapter(List<String> years, ViewOnClickListener viewOnClickListener) {
        this.viewOnClickListener = viewOnClickListener;
        filteredYears = new ArrayList<>(years);
        this.years = years;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.year_row, parent, false);
        return new ViewHolder(view, viewOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.number.setText(String.valueOf(position + 1));
        holder.period.setText(filteredYears.get(position));

        Animation.startAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return filteredYears.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase();

                List<String> filteredList = new ArrayList<>();

                for (String year : years) {
                    if (year.toLowerCase().contains(query)) {
                        filteredList.add(year);
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
                        @SuppressWarnings("unchecked")
                        List<String> filteredList = (List<String>) resultList;

                        // Calculate the differences between the previous and new filtered lists
                        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(filteredYears, filteredList));

                        // Update the filtered customers list
                        filteredYears.clear();
                        filteredYears.addAll(filteredList);

                        // Dispatch the specific change events to the adapter
                        diffResult.dispatchUpdatesTo(YearsAdapter.this);
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