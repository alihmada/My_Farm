package com.ali.myfarm.MVVM;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ali.myfarm.Models.Chicken;

import java.util.List;

public class ChickenViewModel extends ViewModel {
    private MutableLiveData<List<Chicken>> mutableLiveData;

    public void initialize(Context context, String year, String periodName) {
        if (mutableLiveData != null) {
            return;
        }
        Repository repository = Repository.getInstance();
        mutableLiveData = repository.getChicken(context, year, periodName);
    }

    public LiveData<List<Chicken>> getChicken() {
        return mutableLiveData;
    }
}
