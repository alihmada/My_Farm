package com.ali.myfarm.MVVM;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class PersonTransactionViewModel extends ViewModel {
    private MutableLiveData<List<String>> mutableLiveData;

    public void initialize(Context context, String name) {
        if (mutableLiveData != null) {
            return;
        }
        Repository repository = Repository.getInstance();
        mutableLiveData = repository.getPersonTransaction(context, name);
    }

    public LiveData<List<String>> getPersonTransaction() {
        return mutableLiveData;
    }
}
