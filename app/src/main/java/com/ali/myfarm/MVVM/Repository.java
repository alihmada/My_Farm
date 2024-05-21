package com.ali.myfarm.MVVM;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Data.Firebase;
import com.ali.myfarm.Models.Bag;
import com.ali.myfarm.Models.Buyer;
import com.ali.myfarm.Models.Chicken;
import com.ali.myfarm.Models.Electricity;
import com.ali.myfarm.Models.Feed;
import com.ali.myfarm.Models.Heating;
import com.ali.myfarm.Models.Period;
import com.ali.myfarm.Models.Person;
import com.ali.myfarm.Models.Sale;
import com.ali.myfarm.Models.Trader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        Firebase.getAllPeriods(context, year).addValueEventListener(new ValueEventListener() {
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

        Firebase.getSpecificPeriod(context, year, periodName).addValueEventListener(new ValueEventListener() {
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

    public MutableLiveData<List<Electricity>> getElectricity(Context context, String year, String periodName) {
        MutableLiveData<List<Electricity>> mutableLiveData = new MutableLiveData<>();

        Firebase.getElectricity(context, year, periodName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Electricity> electricityList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        electricityList.add(snapshot.getValue(Electricity.class));

                    Collections.reverse(electricityList);
                    mutableLiveData.setValue(electricityList);
                } else mutableLiveData.setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });


        return mutableLiveData;
    }

    public MutableLiveData<List<Person>> getPersons(Context context, Person.Type type) {
        MutableLiveData<List<Person>> mutableLiveData = new MutableLiveData<>();

        Query query = Firebase.getPersons(context).orderByChild("type").equalTo(String.valueOf(type));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Person> person = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        person.add(snapshot.getValue(Person.class));
                    mutableLiveData.setValue(person);
                } else mutableLiveData.setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });


        return mutableLiveData;
    }

    public MutableLiveData<Map<Person, DatabaseReference>> getPerson(Context context, String name) {
        MutableLiveData<Map<Person, DatabaseReference>> mutableLiveData = new MutableLiveData<>();
        Map<Person, DatabaseReference> map = new HashMap<>();

        Query query = Firebase.getPersons(context).orderByChild("fullName").equalTo(name);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DatabaseReference personRef = null;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        personRef = snapshot.getRef();
                        break;
                    }

                    if (personRef != null) {
                        DatabaseReference finalPersonRef = personRef;
                        personRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Person person = null;
                                    try {
                                        person = snapshot.getValue(Person.class);
                                    } catch (Exception ignored) {
                                    }
                                    map.put(person, finalPersonRef);
                                    mutableLiveData.setValue(map);
                                } else {
                                    mutableLiveData.setValue(null);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                } else mutableLiveData.setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });


        return mutableLiveData;
    }

    public MutableLiveData<List<String>> getPersonTransaction(Context context, String name) {
        MutableLiveData<List<String>> mutableLiveData = new MutableLiveData<>();

        Query query = Firebase.getPersons(context).orderByChild("name").equalTo(name);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DatabaseReference personRef = null;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        personRef = snapshot.getRef();
                        break;
                    }

                    assert personRef != null;
                    personRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                List<String> stringList = new ArrayList<>();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    stringList.add(dataSnapshot.getValue(String.class));
                                    break;
                                }
                                mutableLiveData.setValue(stringList);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else mutableLiveData.setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });

        return mutableLiveData;
    }

    public MutableLiveData<List<Trader>> getTraders(Context context, String year, String periodName) {
        MutableLiveData<List<Trader>> mutableLiveData = new MutableLiveData<>();

        Firebase.getTraderBranchFromTransactionBranchThatPeriodHave(context, year, periodName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Trader> traders = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        traders.add(snapshot.getValue(Trader.class));

                    Collections.reverse(traders);
                    mutableLiveData.setValue(traders);
                } else mutableLiveData.setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });


        return mutableLiveData;
    }

    public MutableLiveData<List<Buyer>> getBuyers(Context context, String year, String periodName) {
        MutableLiveData<List<Buyer>> mutableLiveData = new MutableLiveData<>();

        Firebase.getBuyerBranchFromTransactionBranchThatPeriodHave(context, year, periodName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Buyer> buyers = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        buyers.add(snapshot.getValue(Buyer.class));

                    Collections.reverse(buyers);
                    mutableLiveData.setValue(buyers);
                } else mutableLiveData.setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });


        return mutableLiveData;
    }

    public MutableLiveData<List<Sale>> getSales(Context context, String year, String periodName) {
        MutableLiveData<List<Sale>> mutableLiveData = new MutableLiveData<>();

        Firebase.getSales(context, year, periodName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Sale> sales = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        sales.add(snapshot.getValue(Sale.class));

                    Collections.reverse(sales);
                    mutableLiveData.setValue(sales);
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
