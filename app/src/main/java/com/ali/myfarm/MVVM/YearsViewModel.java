package com.ali.myfarm.MVVM;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class YearsViewModel extends ViewModel {
    private MutableLiveData<List<String>> mutableLiveData;

    public void initialize(Context context) {
        if (mutableLiveData != null) {
            return;
        }
        Repository repository = Repository.getInstance();
        mutableLiveData = repository.getYears(context);
    }

    public LiveData<List<String>> getYears() {
        return mutableLiveData;
    }
}
