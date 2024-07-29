package com.ali.myfarm.MVVM;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SoldViewModel extends ViewModel {
    private MutableLiveData<Integer> mutableLiveData;

    public void initialize(Context context, String year, String periodName) {
        if (mutableLiveData != null) {
            return;
        }
        Repository repository = Repository.getInstance();
        mutableLiveData = repository.getSold(context, year, periodName);
    }

    public LiveData<Integer> getSold() {
        return mutableLiveData;
    }
}
