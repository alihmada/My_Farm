package com.ali.myfarm.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.ali.myfarm.Classes.DateAndTime;
import com.ali.myfarm.Data.Firebase;
import com.ali.myfarm.Dialogs.NewPeriod;
import com.ali.myfarm.Fragments.Months;
import com.ali.myfarm.Fragments.Years;
import com.ali.myfarm.Interfaces.OnResume;
import com.ali.myfarm.MVVM.NameViewModel;
import com.ali.myfarm.Models.Period;
import com.ali.myfarm.R;

public class Main extends AppCompatActivity implements OnResume {

    private NameViewModel nameViewModel;
    private ImageButton menu;
    private String farmName;
    private boolean isYears;
    private TextView name;
    private Months months;
    private Years years;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setStatusBarColor();
        setupFragments();
        setupHeader();
        initializeButtons();
        setupSearch();
    }

    private int getPrimaryColor() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    private void setStatusBarColor() {
        getWindow().setStatusBarColor(getPrimaryColor());
    }

    private void setupHeader() {
        name = findViewById(R.id.name);
        setupNameViewModel();
        setName();
    }

    private void setupFragments() {
        years = new Years(this, year -> {
            months = new Months(year);
            replaceFragment(months, "FRAGMENT_MONTHS_TAG");
            name.setText(year);
            isYears = false;
        });

        replaceFragment(years, "FRAGMENT_YEARS_TAG");

    }

    private void replaceFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_down,   // enter animation
                R.anim.slide_out_down,  // exit animation
                R.anim.slide_in_up,     // popEnter animation
                R.anim.slide_out_up     // popExit animation
        ).replace(R.id.container, fragment, tag).addToBackStack(null).commit();
    }

    private void checkCurrentFragment() {
        Fragment years = getSupportFragmentManager().findFragmentByTag("FRAGMENT_YEARS_TAG");

        if (years != null && years.isVisible()) {
            name.setText(farmName);
            isYears = true;
        }
    }

    private void initializeButtons() {
        ImageButton addPeriod = findViewById(R.id.add_period);
        addPeriod.setOnClickListener(view -> new Handler(Looper.getMainLooper()).post(() -> {
            NewPeriod period = new NewPeriod((count, price) -> Firebase.setPeriod(this, DateAndTime.getYear(), new Period(DateAndTime.getArabicNameOfMonth(), String.valueOf(DateAndTime.getCurrentMonth() + 1), DateAndTime.getCurrentDateTime(), "", Integer.parseInt(count), 0, 0, 0, Double.parseDouble(price)), getSupportFragmentManager()));
            period.show(getSupportFragmentManager(), "");
        }));

        menu = findViewById(R.id.menu);
        menu.setOnClickListener(view -> startActivity(new Intent(this, Menu.class)));
    }

    private void setupSearch() {
        SearchView search = findViewById(R.id.search_view);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (isYears) {
                    if (years != null)
                        years.search(newText);
                } else {
                    if (months != null)
                        months.search(newText);
                }
                return false;
            }
        });

        search.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                menu.setVisibility(View.INVISIBLE);
            } else {
                menu.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setName() {
        nameViewModel.getName().observe(this, name -> {
            if (name != null) {
                this.name.setText(name);
            } else {
                this.name.setText(getString(R.string.app_name));
            }
            farmName = this.name.getText().toString();
        });

    }

    private void setupNameViewModel() {
        nameViewModel = new ViewModelProvider(this).get(NameViewModel.class);
        nameViewModel.initialize(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
        } else if (fragmentManager.getBackStackEntryCount() == 0) {
            finish();
        }
    }

    @Override
    public void onResumeFragment() {
        checkCurrentFragment();
    }
}