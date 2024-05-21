package com.ali.myfarm.MVVM;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ali.myfarm.Models.Buyer;

import java.util.List;

public class BuyersViewModel extends ViewModel {
    private MutableLiveData<List<Buyer>> mutableLiveData;

    public void initialize(Context context, String year, String periodName) {
        if (mutableLiveData != null) {
            return;
        }
        Repository repository = Repository.getInstance();
        mutableLiveData = repository.getBuyers(context, year, periodName);
    }

    public LiveData<List<Buyer>> getBuyers() {
        return mutableLiveData;
    }
}
