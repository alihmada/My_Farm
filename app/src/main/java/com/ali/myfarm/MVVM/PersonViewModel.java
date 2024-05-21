package com.ali.myfarm.MVVM;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ali.myfarm.Models.Person;
import com.google.firebase.database.DatabaseReference;

import java.util.Map;

public class PersonViewModel extends ViewModel {
    private MutableLiveData<Map<Person, DatabaseReference>> mutableLiveData;

    public void initialize(Context context, String name) {
        if (mutableLiveData != null) {
            return;
        }
        Repository repository = Repository.getInstance();
        mutableLiveData = repository.getPerson(context, name);
    }

    public LiveData<Map<Person, DatabaseReference>> getPerson() {
        return mutableLiveData;
    }
}
