package com.ali.myfarm.MVVM;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ali.myfarm.Models.Electricity;

import java.util.List;

public class ElectricityViewModel extends ViewModel {
    private MutableLiveData<List<Electricity>> mutableLiveData;

    public void initialize(Context context, String year, String periodName) {
        if (mutableLiveData != null) {
            return;
        }
        Repository repository = Repository.getInstance();
        mutableLiveData = repository.getElectricity(context, year, periodName);
    }

    public LiveData<List<Electricity>> getElectricity() {
        return mutableLiveData;
    }
}
