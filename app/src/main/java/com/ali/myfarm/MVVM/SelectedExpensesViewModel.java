package com.ali.myfarm.MVVM;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ali.myfarm.Models.Expenses;

public class SelectedExpensesViewModel extends ViewModel {
    private MutableLiveData<Expenses> mutableLiveData;

    public void initialize(Context context, String year, String periodName, String id) {
        if (mutableLiveData != null) {
            return;
        }
        Repository repository = Repository.getInstance();
        mutableLiveData = repository.getExpensesItem(context, year, periodName, id);
    }

    public LiveData<Expenses> getExpensesItem() {
        return mutableLiveData;
    }
}
