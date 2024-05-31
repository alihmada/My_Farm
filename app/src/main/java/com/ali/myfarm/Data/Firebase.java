package com.ali.myfarm.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.ali.myfarm.Classes.Calculation;
import com.ali.myfarm.Classes.Ciphering;
import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Dialogs.Alert;
import com.ali.myfarm.Models.Bag;
import com.ali.myfarm.Models.Buyer;
import com.ali.myfarm.Models.Electricity;
import com.ali.myfarm.Models.Feed;
import com.ali.myfarm.Models.Heating;
import com.ali.myfarm.Models.Period;
import com.ali.myfarm.Models.Person;
import com.ali.myfarm.Models.Sale;
import com.ali.myfarm.Models.Trader;
import com.ali.myfarm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Firebase {
    private final static Handler handler = new Handler(Looper.getMainLooper());
    private static SharedPreferences sharedPreferences;

    public static FirebaseDatabase getInstance() {
        return FirebaseDatabase.getInstance();
    } // End getInstance()

    public static DatabaseReference getRoot(Context context) {
        rootHaveValue(context);
        return getInstance().getReference(Common.getROOT());
    } // End getRoot()

    public static DatabaseReference getDatabase(Context context) {
        return getRoot(context).child(Common.DATABASE_NAME);
    } // End getDatabase()

    public static DatabaseReference getUsers(Context context) {
        return getRoot(context).child(Common.FIREBASE_USERS);
    } // End getDatabase()

    public static DatabaseReference getAllPeriods(Context context, String year) {
        return getDatabase(context).child(year);
    } // End getPeriods()

    public static DatabaseReference getSpecificPeriod(Context context, String year, String periodName) {
        return getAllPeriods(context, year).child(periodName);
    } // End getPeriod()

    public static DatabaseReference haveRunningPeriod(Context context) {
        return getDatabase(context).child(Common.RUNNING_ITEMS);
    }

    public static void setRunningItemValue(Context context, boolean b) {
        getDatabase(context).child(Common.RUNNING_ITEMS).setValue(b);
    }

    public static void setPeriod(Context context, String year, Period period, FragmentManager fragmentManager) {
        haveRunningPeriod(context).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (Boolean.FALSE.equals(snapshot.getValue(boolean.class))) {
                        getAllPeriods(context, year).child(period.getName()).setValue(period);
                        setFeed(context, year, period.getName(), new Feed(0, 0.0, 0, 0.0, 0, 0.0));
                    } else {
                        showAlert(fragmentManager, R.drawable.wrong, context.getString(R.string.running_period));
                    }
                } else {
                    getAllPeriods(context, year).child(period.getName()).setValue(period);
                    setFeed(context, year, period.getName(), new Feed(0, 0.0, 0, 0.0, 0, 0.0));
                    setRunningItemValue(context, true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    } // End getPeriod()

    public static DatabaseReference getChicken(Context context, String year, String periodName) {
        return getSpecificPeriod(context, year, periodName).child(Common.CHICKEN);
    }

    public static DatabaseReference getFeed(Context context, String year, String periodName) {
        return getSpecificPeriod(context, year, periodName).child(Common.FEED);
    }

    public static void setFeed(Context context, String year, String periodName, Feed feed) {
        getFeed(context, year, periodName).setValue(feed);
    }

    public static DatabaseReference getGrowing(Context context, String year, String periodName) {
        return getFeed(context, year, periodName).child(Common.GROWING);
    }

    public static DatabaseReference getInitialize(Context context, String year, String periodName) {
        return getFeed(context, year, periodName).child(Common.INITIALIZE);
    }

    public static DatabaseReference getEnd(Context context, String year, String periodName) {
        return getFeed(context, year, periodName).child(Common.END);
    }

    public static void setGrowing(Context context, String year, String periodName, Bag bag) {
        getGrowing(context, year, periodName).push().setValue(bag);
        operationHandler(context, year, periodName, bag, Feed.Type.GROWING);
    }

    public static void setInitialize(Context context, String year, String periodName, Bag bag) {
        getInitialize(context, year, periodName).push().setValue(bag);
        operationHandler(context, year, periodName, bag, Feed.Type.BEGGING);
    }

    public static void setEnd(Context context, String year, String periodName, Bag bag) {
        getEnd(context, year, periodName).push().setValue(bag);
        operationHandler(context, year, periodName, bag, Feed.Type.END);
    }

    public static DatabaseReference getHeating(Context context, String year, String periodName) {
        return getSpecificPeriod(context, year, periodName).child(Common.HEATING);
    }

    public static void setHeating(Context context, String year, String periodName, Heating heating) {
        getHeating(context, year, periodName).push().setValue(heating);
    }

    public static DatabaseReference getElectricity(Context context, String year, String periodName) {
        return getSpecificPeriod(context, year, periodName).child(Common.ELECTRICITY);
    }

    public static void setElectricity(Context context, String year, String periodName, Electricity electricity) {
        getElectricity(context, year, periodName).push().setValue(electricity);
    }

    public static DatabaseReference getTransactionBranchThatPeriodHave(Context context, String year, String periodName) {
        return getSpecificPeriod(context, year, periodName).child(Common.TRANSACTION);
    }

    public static DatabaseReference getTraderBranchFromTransactionBranchThatPeriodHave(Context context, String year, String periodName) {
        return getTransactionBranchThatPeriodHave(context, year, periodName).child(Common.TRADERS);
    }

    public static void setTraderInTraderBranchFromTransactionBranchThatPeriodHave(Context context, String year, String periodName, Trader trader) {
        getTraderBranchFromTransactionBranchThatPeriodHave(context, year, periodName).push().setValue(trader);
    }

    public static DatabaseReference getBuyerBranchFromTransactionBranchThatPeriodHave(Context context, String year, String periodName) {
        return getTransactionBranchThatPeriodHave(context, year, periodName).child(Common.BUYERS);
    }

    public static void setBuyerInTransactionBranchThatPeriodHave(Context context, String year, String periodName, Buyer buyer) {
        getBuyerBranchFromTransactionBranchThatPeriodHave(context, year, periodName).push().setValue(buyer);
    }

    public static DatabaseReference getPersons(Context context) {
        return getRoot(context).child(Common.PERSONS);
    }

    public static void setPerson(Context context, Person person) {
        getPersons(context).push().setValue(person);
    }

    public static DatabaseReference getTransactionBranchThatPersonHave(DatabaseReference person) {
        return person.child(Common.TRANSACTION);
    }

    public static void setTransactionValueInTraderThatPersonHave(DatabaseReference person, String year, String periodName) {
        String value = String.format("%s - %s", year, periodName);
        Query query = getTransactionBranchThatPersonHave(person).orderByValue().equalTo(value);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists())
                    getTransactionBranchThatPersonHave(person).push().setValue(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void setTransactionValueInBuyerThatPersonHave(DatabaseReference person, String year, String periodName) {
        String value = String.format("%s - %s", year, periodName);
        Query query = getTransactionBranchThatPersonHave(person).orderByValue().equalTo(value);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists())
                    getTransactionBranchThatPersonHave(person).push().setValue(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static void updateSoldValue(Context context, String year, String periodName, int oldValue, int newValue) {
        getSpecificPeriod(context, year, periodName).child(Common.SOLD).setValue(oldValue + newValue);
    }

    public static DatabaseReference getSales(Context context, String year, String periodName) {
        return getSpecificPeriod(context, year, periodName).child(Common.SALES);
    }

    public static void setSale(Context context, String year, String periodName, Sale sale) {
        getSpecificPeriod(context, year, periodName).child(Common.SALES).push().setValue(sale);
    }

    private static void operationHandler(Context context, String year, String periodName, Bag bag, Feed.Type type) {
        getFeed(context, year, periodName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    Feed feed = snapshot.getValue(Feed.class);
                    if (feed == null) throw new IllegalArgumentException("Feed data is null");

                    boolean isAddOperation = bag.getOperation() == Bag.Operation.ADD;
                    int bagNumber = bag.getNumber();
                    double bagPrice = Calculation.getBagsPrice(bagNumber, bag.getPriceOfTon());

                    switch (type) {
                        case GROWING:
                            updateFeedAttributes(context, year, periodName, "growing", isAddOperation, bagNumber, bagPrice, feed.getGrowing(), feed.getPriceOfGrowing());
                            break;
                        case BEGGING:
                            updateFeedAttributes(context, year, periodName, "beginning", isAddOperation, bagNumber, bagPrice, feed.getBeginning(), feed.getPriceOfBeginning());
                            break;
                        case END:
                            updateFeedAttributes(context, year, periodName, "end", isAddOperation, bagNumber, bagPrice, feed.getEnd(), feed.getPriceOfEnd());
                            break;
                    }

                    getSpecificPeriod(context, year, periodName).child("numberOfFeedBags").setValue(feed.getBags());
                } catch (Exception e) {
                    Log.e("operationHandler", "Error handling feed operation", e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("operationHandler", "Database operation cancelled", error.toException());
            }
        });
    }

    private static void updateFeedAttributes(Context context, String year, String periodName, String attributePrefix, boolean isAddOperation, int bagNumber, double bagPrice, int currentBags, double currentPrice) {
        if (isAddOperation) {
            currentBags += bagNumber;
            currentPrice += bagPrice;
            changeAttributes(context, year, periodName, attributePrefix, currentBags);
            changeAttributes(context, year, periodName, "priceOf" + capitalize(attributePrefix), currentPrice);
        } else {
            currentBags -= bagNumber;
            if (currentBags >= 0) {
                changeAttributes(context, year, periodName, attributePrefix, currentBags);
            }
        }
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    } // End of getFirebaseAuth()

    public static FirebaseUser getCurrentUser() {
        return getFirebaseAuth().getCurrentUser();
    } // End of getCurrentUser()

    public static String getPhoneNumber() {
        return getCurrentUser().getPhoneNumber();
    } // End of getPhoneNumber()

    public static void rootHaveValue(Context context) {
        try {
            sharedPreferences = context.getSharedPreferences(Ciphering.decrypt(Common.SHARED_PREFERENCE_NAME), Context.MODE_PRIVATE);
        } catch (Exception ignored) {
        }

        if (sharedPreferences != null) {
            String root = sharedPreferences.getString(Common.FARM_ID, "");
            if (!Objects.equals(Common.getROOT(), root)) {
                Common.setROOT(root);
            }
        }
    }

    private static void changeAttributes(Context context, String year, String periodName, String childName, double value) {
        getFeed(context, year, periodName).child(childName).setValue(value);
    }

    private static void showAlert(FragmentManager fragmentManager, int iconResId, String message) {
        handler.post(() -> new Alert(iconResId, message).show(fragmentManager, ""));
    }
} //End Firebase