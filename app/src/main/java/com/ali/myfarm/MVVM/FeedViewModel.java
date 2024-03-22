package com.ali.myfarm.MVVM;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ali.myfarm.Models.Feed;

public class FeedViewModel extends ViewModel {
    private MutableLiveData<Feed> mutableLiveData;

    public void initialize(Context context, String year, String periodName) {
        if (mutableLiveData != null) {
            return;
        }
        Repository repository = Repository.getInstance();
        mutableLiveData = repository.getFeed(context, year, periodName);
    }

    public LiveData<Feed> getFeed() {
        return mutableLiveData;
    }
}
