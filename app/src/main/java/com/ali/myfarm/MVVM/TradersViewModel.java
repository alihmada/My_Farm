package com.ali.myfarm.MVVM;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ali.myfarm.Models.Trader;

import java.util.List;

public class TradersViewModel extends ViewModel {
    private MutableLiveData<List<Trader>> mutableLiveData;

    public void initialize(Context context, String year, String periodName) {
        if (mutableLiveData != null) {
            return;
        }
        Repository repository = Repository.getInstance();
        mutableLiveData = repository.getTraders(context, year, periodName);
    }

    public LiveData<List<Trader>> getTraders() {
        return mutableLiveData;
    }
}
