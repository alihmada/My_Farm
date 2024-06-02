package com.ali.myfarm.MVVM;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ali.myfarm.Models.Period;

public class PeriodViewModel extends ViewModel {
    private MutableLiveData<Period> mutableLiveData;

    public void initialize(Context context, String year, String periodName) {
        if (mutableLiveData != null) {
            return;
        }
        Repository repository = Repository.getInstance();
        mutableLiveData = repository.getPeriod(context, year, periodName);
    }

    public LiveData<Period> getPeriod() {
        return mutableLiveData;
    }
}
