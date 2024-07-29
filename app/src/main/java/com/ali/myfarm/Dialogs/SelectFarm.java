package com.ali.myfarm.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ali.myfarm.Adapters.FarmsAdapter;
import com.ali.myfarm.Classes.Ciphering;
import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Classes.FirstItemMarginDecoration;
import com.ali.myfarm.Intenet.Internet;
import com.ali.myfarm.Interfaces.ViewOnClickListener;
import com.ali.myfarm.Models.Farm;
import com.ali.myfarm.R;
import com.google.gson.Gson;

import java.util.List;

public class SelectFarm extends DialogFragment implements ViewOnClickListener {

    private FarmsAdapter farmsAdapter;
    private SelectFarmListener listener;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ConstraintLayout alert;
    private ImageView imageView;
    private TextView textView;
    private List<Farm> farms;
    private EditText search;
    private Handler handler;

    public SelectFarm() {
    }

    public SelectFarm(List<Farm> farms, SelectFarmListener listener) {
        this.farms = farms;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_item_dialog);

        handler = new Handler(Looper.getMainLooper());

        initializeViews(dialog);
        setupAddButton(dialog);
        setupSearch(dialog);
        setupView();
        setupSwipeRefreshLayout(dialog);
        setupSharedPreferences();

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
        recyclerView = dialog.findViewById(R.id.items_recycler);
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
        if (farms != null) {
            setupRecyclerViewData(farms);
            if (!farms.isEmpty()) {
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
    }

    private void setupRecyclerViewData(List<Farm> farms) {
        farmsAdapter = new FarmsAdapter(this, farms);
        recyclerView.setAdapter(farmsAdapter);
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
                if (farmsAdapter != null) {
                    farmsAdapter.getFilter().filter(s);
                }
            }
        });
    }

    private void setupSharedPreferences() {
        try {
            sharedPreferences = requireContext().getSharedPreferences(Ciphering.decrypt(Common.SHARED_PREFERENCE_NAME), Context.MODE_PRIVATE);
        } catch (Exception ignored) {
        }
    }

    private void setupAddButton(Dialog dialog) {
        ImageButton add = dialog.findViewById(R.id.add);
        add.setOnClickListener(v -> handler.post(() -> {
            AddNewFarm addNewFarm = new AddNewFarm((root, name) -> {
                farms.add(new Farm(root, name));
                sharedPreferences.edit().putString(Common.ACCOUNTS, new Gson().toJson(farms)).apply();
                setupRecyclerViewData(farms);
            });
            addNewFarm.show(getParentFragmentManager(), "");
        }));
    }

    @Override
    public void onClickListener(String root) {
        listener.onDataEntered(root);
        dismiss();
    }

    public interface SelectFarmListener {
        void onDataEntered(String root);
    }
}
