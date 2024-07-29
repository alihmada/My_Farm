package com.ali.myfarm.MVVM;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ali.myfarm.Models.Transaction;

public class TransactionViewModel extends ViewModel {
    private MutableLiveData<Transaction> mutableLiveData;

    public void initialize(Context context, String year, String periodName) {
        if (mutableLiveData != null) {
            return;
        }
        Repository repository = Repository.getInstance();
        mutableLiveData = repository.getTransaction(context, year, periodName);
    }

    public LiveData<Transaction> getTransaction() {
        return mutableLiveData;
    }
}
