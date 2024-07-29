package com.ali.myfarm.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Fragments.BuyerBill;
import com.ali.myfarm.Fragments.TraderBill;
import com.ali.myfarm.R;

import java.util.Objects;

public class Bill extends AppCompatActivity implements TraderBill.OnFragmentInteractionListener, BuyerBill.OnFragmentInteractionListener {

    private String mainID;
    private String periodID;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        bundle = new Bundle();

        mainID = Objects.requireNonNull(getIntent().getExtras()).getString(Common.YEAR);
        periodID = getIntent().getExtras().getString(Common.MONTH);
        boolean isTrader = getIntent().getBooleanExtra(Common.IS_TRADER, false);

        String movedData = getIntent().getExtras().getString(Common.MOVED_DATA);

        Fragment fragment = isTrader ? new TraderBill(mainID, periodID, movedData) : new BuyerBill(mainID, periodID, movedData);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

        findViewById(R.id.back).setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void onFinishActivity() {
        Intent intent = new Intent(this, Transaction.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        bundle.putString(Common.YEAR, mainID);
        bundle.putString(Common.MONTH, periodID);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}