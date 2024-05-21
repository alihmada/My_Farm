package com.ali.myfarm.MVVM;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ali.myfarm.Models.Sale;

import java.util.List;

public class SalesViewMadel extends ViewModel {
    private MutableLiveData<List<Sale>> mutableLiveData;

    public void initialize(Context context, String year, String periodName) {
        if (mutableLiveData != null) {
            return;
        }
        Repository repository = Repository.getInstance();
        mutableLiveData = repository.getSales(context, year, periodName);
    }

    public LiveData<List<Sale>> getSales() {
        return mutableLiveData;
    }
}
