package com.ali.myfarm.Dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ali.myfarm.Adapters.PersonsAdapter;
import com.ali.myfarm.Classes.FirstItemMarginDecoration;
import com.ali.myfarm.Data.Firebase;
import com.ali.myfarm.Intenet.Internet;
import com.ali.myfarm.Interfaces.ViewOnClickListener;
import com.ali.myfarm.MVVM.PersonsViewModel;
import com.ali.myfarm.Models.Person;
import com.ali.myfarm.R;

import java.util.List;

public class SelectPerson extends DialogFragment implements ViewOnClickListener {

    private Person.Type type;
    private PersonsViewModel model;
    private PersonsAdapter personAdapter;
    private SelectPersonListener listener;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ConstraintLayout alert;
    private ImageView imageView;
    private TextView textView;
    private EditText search;
    private Handler handler;

    public SelectPerson() {
    }

    public SelectPerson(Person.Type type, SelectPersonListener listener) {
        this.listener = listener;
        this.type = type;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_person_dialog);

        handler = new Handler(Looper.getMainLooper());

        initializeViews(dialog);
        setupAddButton(dialog);
        setupSearch(dialog);
        setupViewModel();
        setupView();
        setupSwipeRefreshLayout(dialog);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.dialog_animation);
        }
        return dialog;
    }

    private void initializeViews(Dialog dialog) {
        alert = dialog.findViewById(R.id.alert);
        imageView = dialog.findViewById(R.id.alert_image);
        textView = dialog.findViewById(R.id.alert_text);
        progressBar = dialog.findViewById(R.id.progressBar);
        recyclerView = dialog.findViewById(R.id.persons_recycler);
        recyclerView.addItemDecoration(new FirstItemMarginDecoration(getResources().getDimensionPixelSize(R.dimen.margin)));
    }

    private void setupView() {
        if (Internet.isConnectedWithoutMessage(requireContext())) {
            alert.setVisibility(View.GONE);
            if (Internet.isNetworkLimited(requireContext())) {
                setupWifi(getString(R.string.internet_limited));
            } else {
                setRecyclerView();
            }
        } else {
            setupWifi(getString(R.string.no_internet));
        }
    }

    private void setupWifi(String message) {
        alert.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        imageView.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.wifi_off));
        recyclerView.setAdapter(null);
        textView.setText(message);
    }

    private void setupSwipeRefreshLayout(Dialog dialog) {
        swipeRefreshLayout = dialog.findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            setupView();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setRecyclerView() {
        model.getPersons().observe(requireActivity(), personList -> {
            if (personList != null) {
                setupRecyclerViewData(personList);
                if (!personList.isEmpty()) {
                    search.setEnabled(true);
                    alert.setVisibility(View.GONE);
                } else {
                    alert.setVisibility(View.VISIBLE);
                }
            } else {
                search.setEnabled(false);
                alert.setVisibility(View.VISIBLE);
                textView.setText(getString(R.string.data_not_found));
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    private void setupRecyclerViewData(List<Person> personList) {
        personAdapter = new PersonsAdapter(this, personList);
        recyclerView.setAdapter(personAdapter);
    }

    private void setupSearch(Dialog dialog) {
        search = dialog.findViewById(R.id.search_view);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (personAdapter != null) {
                    personAdapter.getFilter().filter(s);
                }
            }
        });
    }

    private void setupAddButton(Dialog dialog) {
        ImageButton add = dialog.findViewById(R.id.add);
        add.setOnClickListener(v -> handler.post(() -> {
            AddNewUser newUser = new AddNewUser((firstName, lastName, phone, type) -> Firebase.setPerson(requireContext(), new Person(firstName, lastName, phone, type, 0.0, 0.0)));
            newUser.show(getParentFragmentManager(), "");
        }));
    }

    private void setupViewModel() {
        model = new ViewModelProvider(requireActivity()).get(PersonsViewModel.class);
        model.initialize(requireContext(), type);
    }

    @Override
    public void onClickListener(String name) {
        listener.onDataEntered(name);
        dismiss();
    }

    public interface SelectPersonListener {
        void onDataEntered(String name);
    }
}
