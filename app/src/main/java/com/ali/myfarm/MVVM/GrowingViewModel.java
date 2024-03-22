package com.ali.myfarm.MVVM;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ali.myfarm.Models.Bag;

import java.util.List;

public class GrowingViewModel extends ViewModel {
    private MutableLiveData<List<Bag>> mutableLiveData;

    public void initialize(Context context, String year, String periodName) {
        if (mutableLiveData != null) {
            return;
        }
        Repository repository = Repository.getInstance();
        mutableLiveData = repository.getGrowing(context, year, periodName);
    }

    public LiveData<List<Bag>> getGrowing() {
        return mutableLiveData;
    }
}