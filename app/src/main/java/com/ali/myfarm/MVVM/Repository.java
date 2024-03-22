package com.ali.myfarm.MVVM;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Data.Firebase;
import com.ali.myfarm.Models.Bag;
import com.ali.myfarm.Models.Chicken;
import com.ali.myfarm.Models.Feed;
import com.ali.myfarm.Models.Heating;
import com.ali.myfarm.Models.Period;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Repository {

    private static Repository instance;

    public static Repository getInstance() {
        if (instance == null) instance = new Repository();
        return instance;
    }

    public MutableLiveData<List<String>> getYears(Context context) {
        MutableLiveData<List<String>> mutableLiveData = new MutableLiveData<>();

        Firebase.getDatabase(context).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<String> stringList = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String string = snapshot.getKey();
                        assert string != null;
                        if (!string.equals(Common.RUNNING_ITEMS)) stringList.add(string);
                    }

                    mutableLiveData.setValue(stringList);
                } else mutableLiveData.setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });


        return mutableLiveData;
    }

    public MutableLiveData<List<String>> getPeriods(Context context, String year) {
        MutableLiveData<List<String>> mutableLiveData = new MutableLiveData<>();

        Firebase.getPeriods(context, year).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<String> stringList = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        try {
                            Period period = snapshot.getValue(Period.class);
                            assert period != null;
                            stringList.add(period.getName());
                        } catch (Exception ignored) {

                        }
                    }

                    mutableLiveData.setValue(stringList);
                } else mutableLiveData.setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });


        return mutableLiveData;
    }

    public MutableLiveData<Period> getPeriod(Context context, String year, String periodName) {
        MutableLiveData<Period> mutableLiveData = new MutableLiveData<>();

        Firebase.getPeriod(context, year, periodName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Period period = dataSnapshot.getValue(Period.class);

                    mutableLiveData.setValue(period);
                } else mutableLiveData.setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });


        return mutableLiveData;
    }

    public MutableLiveData<List<Chicken>> getChicken(Context context, String year, String periodName) {
        MutableLiveData<List<Chicken>> mutableLiveData = new MutableLiveData<>();

        Firebase.getChicken(context, year, periodName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Chicken> chickens = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        chickens.add(snapshot.getValue(Chicken.class));
                    }

                    Collections.reverse(chickens);
                    mutableLiveData.setValue(chickens);
                } else mutableLiveData.setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });


        return mutableLiveData;
    }

    public MutableLiveData<Feed> getFeed(Context context, String year, String periodName) {
        MutableLiveData<Feed> mutableLiveData = new MutableLiveData<>();

        Firebase.getFeed(context, year, periodName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Feed feed = dataSnapshot.getValue(Feed.class);

                    mutableLiveData.setValue(feed);
                } else mutableLiveData.setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });


        return mutableLiveData;
    }

    public MutableLiveData<List<Bag>> getGrowing(Context context, String year, String periodName) {
        MutableLiveData<List<Bag>> mutableLiveData = new MutableLiveData<>();

        Firebase.getGrowing(context, year, periodName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Bag> bags = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        bags.add(snapshot.getValue(Bag.class));

                    Collections.reverse(bags);
                    mutableLiveData.setValue(bags);
                } else mutableLiveData.setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });


        return mutableLiveData;
    }

    public MutableLiveData<List<Bag>> getBeginning(Context context, String year, String periodName) {
        MutableLiveData<List<Bag>> mutableLiveData = new MutableLiveData<>();

        Firebase.getInitialize(context, year, periodName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Bag> bags = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        bags.add(snapshot.getValue(Bag.class));

                    Collections.reverse(bags);
                    mutableLiveData.setValue(bags);
                } else mutableLiveData.setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });


        return mutableLiveData;
    }

    public MutableLiveData<List<Bag>> getEnd(Context context, String year, String periodName) {
        MutableLiveData<List<Bag>> mutableLiveData = new MutableLiveData<>();

        Firebase.getEnd(context, year, periodName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Bag> bags = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        bags.add(snapshot.getValue(Bag.class));

                    Collections.reverse(bags);
                    mutableLiveData.setValue(bags);
                } else mutableLiveData.setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });


        return mutableLiveData;
    }

    public MutableLiveData<List<Heating>> getHeating(Context context, String year, String periodName) {
        MutableLiveData<List<Heating>> mutableLiveData = new MutableLiveData<>();

        Firebase.getHeating(context, year, periodName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Heating> heatingList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        heatingList.add(snapshot.getValue(Heating.class));

                    Collections.reverse(heatingList);
                    mutableLiveData.setValue(heatingList);
                } else mutableLiveData.setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });


        return mutableLiveData;
    }
}
