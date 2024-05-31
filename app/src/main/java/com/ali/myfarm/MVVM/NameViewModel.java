package com.ali.myfarm.MVVM;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NameViewModel extends ViewModel {
    private MutableLiveData<String> mutableLiveData;

    public void initialize(Context context) {
        if (mutableLiveData != null) {
            return;
        }
        Repository repository = Repository.getInstance();
        mutableLiveData = repository.getName(context);
    }

    public LiveData<String> getName() {
        return mutableLiveData;
    }
}