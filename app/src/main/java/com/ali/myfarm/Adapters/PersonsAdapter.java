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
import com.ali.myfarm.Models.Person;
import com.ali.myfarm.R;

import java.util.ArrayList;
import java.util.List;

public class PersonsAdapter extends RecyclerView.Adapter<PersonsAdapter.ViewHolder> implements Filterable {

    static List<Person> filteredPersons;
    private final ViewOnClickListener viewOnClickListener;
    private final List<Person> personList;

    public PersonsAdapter(ViewOnClickListener viewOnClickListener, List<Person> personList) {
        this.viewOnClickListener = viewOnClickListener;
        filteredPersons = new ArrayList<>(personList);
        this.personList = personList;
    }

    @NonNull
    @Override
    public PersonsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_row, parent, false);
        return new ViewHolder(view, viewOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(filteredPersons.get(position).getFullName());
        holder.phone.setText(filteredPersons.get(position).getPhoneNumber());

        Animation.startAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return filteredPersons.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase();

                List<Person> filteredList = new ArrayList<>();

                for (Person person : personList) {
                    if (person.getFullName().toLowerCase().contains(query)) {
                        filteredList.add(person);
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
                    if (!resultList.isEmpty() && resultList.get(0) instanceof Person) {
                        @SuppressWarnings("unchecked") List<Person> filteredList = (List<Person>) resultList;

                        // Calculate the differences between the previous and new filtered lists
                        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(filteredPersons, filteredList));

                        // Update the filtered customers list
                        filteredPersons.clear();
                        filteredPersons.addAll(filteredList);

                        // Dispatch the specific change events to the adapter
                        diffResult.dispatchUpdatesTo(PersonsAdapter.this);
                    }
                }
            }
        };
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, phone;

        public ViewHolder(@NonNull View itemView, ViewOnClickListener viewOnClickListener) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);

            itemView.setOnClickListener(v -> viewOnClickListener.onClickListener(name.getText().toString()));
        }
    }
}
