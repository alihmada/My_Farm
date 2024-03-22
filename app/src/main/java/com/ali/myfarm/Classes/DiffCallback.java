package com.ali.myfarm.Classes;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class DiffCallback extends DiffUtil.Callback {
    private final List<String> oldList;
    private final List<String> newList;

    public DiffCallback(List<String> oldList, List<String> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        String oldString = oldList.get(oldItemPosition);
        String newString = newList.get(newItemPosition);
        // Compare unique identifiers of the items (e.g., String IDs)
        return oldString.equals(newString);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        String oldString = oldList.get(oldItemPosition);
        String newString = newList.get(newItemPosition);
        // Compare the content of the items
        return oldString.equals(newString);
    }
}
