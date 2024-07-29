package com.ali.myfarm.MVVM;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ali.myfarm.Models.Expenses;

import java.util.List;

public class ExpensesViewModel extends ViewModel {
    private MutableLiveData<List<Expenses>> mutableLiveData;

    public void initialize(Context context, String year, String periodName) {
        if (mutableLiveData != null) {
            return;
        }
        Repository repository = Repository.getInstance();
        mutableLiveData = repository.getExpenses(context, year, periodName);
    }

    public LiveData<List<Expenses>> getExpenses() {
        return mutableLiveData;
    }
}
