package com.ali.myfarm.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.ali.myfarm.Classes.Capture;
import com.ali.myfarm.Classes.Ciphering;
import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Data.Firebase;
import com.ali.myfarm.Dialogs.Loading;
import com.ali.myfarm.MVVM.NameViewModel;
import com.ali.myfarm.Models.Farm;
import com.ali.myfarm.Models.User;
import com.ali.myfarm.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

public class FarmID extends AppCompatActivity {

    private ImageView userImage;
    private NameViewModel nameViewModel;
    private SharedPreferences sharedPreferences;
    private EditText firstName;
    private EditText lastName;
    private EditText farmID;
    private final ActivityResultLauncher<ScanOptions> scanOptionsActivityResultLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            farmID.setText(result.getContents());
        }
    });
    private Uri uri;
    private String id;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_id);

        initViews();
        Loading.progressDialogConstructor(this);
        setupSharedPreferences();

        findViewById(R.id.select_image).setOnClickListener(view -> checkPermission());

        findViewById(R.id.getMain).setOnClickListener(v -> handleMainButtonClick());

        findViewById(R.id.qr_scanner).setOnClickListener(v -> {
            ScanOptions scanOptions = new ScanOptions().setBeepEnabled(true).setOrientationLocked(true).setCaptureActivity(Capture.class);
            scanOptionsActivityResultLauncher.launch(scanOptions);
        });
    }

    private void initViews() {
        gson = new Gson();

        userImage = findViewById(R.id.user_image);
        farmID = findViewById(R.id.farm_id);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
    }

    private void setupSharedPreferences() {
        try {
            sharedPreferences = getSharedPreferences(Ciphering.decrypt(Common.SHARED_PREFERENCE_NAME), MODE_PRIVATE);
        } catch (Exception ignored) {
        }
    }

    private void handleMainButtonClick() {
        Loading.showProgressDialog();
        String _1stName = firstName.getText().toString().trim();
        String _2ndName = lastName.getText().toString().trim();
        id = farmID.getText().toString().trim();

        try {
            id = Ciphering.decrypt(id);
        } catch (Exception e) {
            showToast(getString(R.string.error_in_inputs));
            Loading.dismissProgressDialog();
            return;
        }

        if (_1stName.isEmpty() || _2ndName.isEmpty() || id.isEmpty()) {
            showToast(getString(R.string.error_in_inputs));
            Loading.dismissProgressDialog();
            return;
        }

        validateFarmID(_1stName, _2ndName);
    }

    private void validateFarmID(String firstName, String lastName) {
        Query query = Firebase.getInstance().getReference(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    sharedPreferences.edit().putString(Common.FARM_ID, id).apply();
                    setupNameViewModel();
                    setName();
                    validateUser(firstName, lastName);
                } else {
                    Loading.dismissProgressDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Loading.dismissProgressDialog();
            }
        });
    }

    private void validateUser(String firstName, String lastName) {
        Query userQuery = Firebase.getUsers(FarmID.this).orderByChild("phoneNumber").equalTo(Firebase.getPhoneNumber());
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = null;
                if (!snapshot.exists()) {
                    user = new User(firstName, lastName, Firebase.getPhoneNumber());
                    Firebase.getUsers(FarmID.this).push().setValue(user);
                } else {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        user = dataSnapshot.getValue(User.class);
                        dataSnapshot.getRef().setValue(new User(firstName, lastName, Firebase.getPhoneNumber()));
                    }
                }
                sharedPreferences.edit().putString(Common.USER_DATA, new Gson().toJson(user)).apply();

                if (userImage.getDrawable() != null) {
                    uploadImage();
                }

                navigateToMainScreen();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Loading.dismissProgressDialog();
            }
        });
    }

    private void uploadImage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("images");

        if (uri != null) {
            StorageReference imageRef = storageRef.child(Firebase.getPhoneNumber());
            UploadTask uploadTask = imageRef.putFile(uri);

            uploadTask.addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> sharedPreferences.edit().putString(Common.IMAGE, uri.toString()).apply())).addOnFailureListener(exception -> {
            });
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 2);
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        } else {
            pickImagesFromGallery();
        }
    }

    private void pickImagesFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, 1);
    }

    private void navigateToMainScreen() {
        Loading.dismissProgressDialog();
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void setupNameViewModel() {
        nameViewModel = new ViewModelProvider(this).get(NameViewModel.class);
        nameViewModel.initialize(this);
    }

    private void setName() {
        nameViewModel.getName().observe(this, name -> {
            if (name != null) {
                List<Farm> farms = new ArrayList<>();
                farms.add(new Farm(id, name));
                sharedPreferences.edit().putString(Common.ACCOUNTS, gson.toJson(farms)).apply();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1 && data != null && data.getData() != null) {
            uri = data.getData();
            userImage.setImageURI(uri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 2 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImagesFromGallery();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}