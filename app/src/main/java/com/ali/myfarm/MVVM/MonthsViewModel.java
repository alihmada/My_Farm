package com.ali.myfarm.MVVM;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.AbstractMap;
import java.util.List;

public class MonthsViewModel extends ViewModel {
    private MutableLiveData<List<AbstractMap.SimpleEntry<String, String>>> mutableLiveData;

    public void initialize(Context context, String year) {
        if (mutableLiveData != null) {
            return;
        }
        Repository repository = Repository.getInstance();
        mutableLiveData = repository.getPeriods(context, year);
    }

    public LiveData<List<AbstractMap.SimpleEntry<String, String>>> getPeriods() {
        return mutableLiveData;
    }
}
