package com.ali.myfarm.MVVM;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class PeriodsViewModel extends ViewModel {
    private MutableLiveData<List<String>> mutableLiveData;

    public void initialize(Context context, String year) {
        if (mutableLiveData != null) {
            return;
        }
        Repository repository = Repository.getInstance();
        mutableLiveData = repository.getPeriods(context, year);
    }

    public LiveData<List<String>> getPeriods() {
        return mutableLiveData;
    }
}
