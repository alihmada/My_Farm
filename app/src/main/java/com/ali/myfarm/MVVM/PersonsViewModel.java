package com.ali.myfarm.MVVM;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ali.myfarm.Models.Person;

import java.util.List;

public class PersonsViewModel extends ViewModel {
    private MutableLiveData<List<Person>> mutableLiveData;

    public void initialize(Context context, Person.Type type) {
        if (mutableLiveData != null) {
            return;
        }
        Repository repository = Repository.getInstance();
        mutableLiveData = repository.getPersons(context, type);
    }

    public LiveData<List<Person>> getPersons() {
        return mutableLiveData;
    }
}
