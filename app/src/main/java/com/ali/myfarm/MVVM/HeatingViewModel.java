package com.ali.myfarm.MVVM;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ali.myfarm.Models.Heating;

import java.util.List;

public class HeatingViewModel extends ViewModel {
    private MutableLiveData<List<Heating>> mutableLiveData;

    public void initialize(Context context, String year, String periodName) {
        if (mutableLiveData != null) {
            return;
        }
        Repository repository = Repository.getInstance();
        mutableLiveData = repository.getHeating(context, year, periodName);
    }

    public LiveData<List<Heating>> getHeating() {
        return mutableLiveData;
    }
}
