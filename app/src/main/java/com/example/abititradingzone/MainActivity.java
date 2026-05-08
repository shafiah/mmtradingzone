package com.example.abititradingzone;

import com.example.abititradingzone.base.BaseActivity;
import com.example.abititradingzone.network.ApiClient;
import com.example.abititradingzone.network.ApiService;
import com.example.abititradingzone.network.LoginRequest;
import com.example.abititradingzone.network.LoginResponse;
import com.google.android.material.card.MaterialCardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    SharedPreferences prefs;
    MaterialCardView cardFileUpload;

    // ⭐ NEW: PRIME UI
    MaterialCardView cardPrimeBanner;

    ImageView imgPrimeBanner;
    Button btnBuyNow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ⭐ IMPORTANT
        setContentLayout(R.layout.activity_main);
        setSelectedTab(R.id.nav_home);

        TextView txtUserNameTop = findViewById(R.id.txtWelcomeUser);
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userName = prefs.getString("userName", "User");

        if (txtUserNameTop != null) {
            txtUserNameTop.setText("Welcome," + userName);
        }

        cardFileUpload = findViewById(R.id.cardFileUpload);

        // ⭐ NEW: INIT VIEWS
      //  imgPrimeBanner = findViewById(R.id.imgPrimeBanner); // 🔥 add in XML
        cardPrimeBanner = findViewById(R.id.cardPrimeBanner);
        btnBuyNow = findViewById(R.id.btnBuyNow);


        // ⭐ GET USER TYPE
        prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        String userType = prefs.getString("USER_TYPE", "USER");

        // ⭐ LOGIC
        if (!"ADMIN".equals(userType)) {

            // ❌ HIDE FILE UPLOAD
            cardFileUpload.setVisibility(View.GONE);

        } else {

            // ✅ SHOW
            cardFileUpload.setVisibility(View.VISIBLE);
        }

        // =========================================================
        // 🔥 NEW CODE: PRIME USER LOGIC (MOST IMPORTANT)
        // =========================================================
        boolean isPrime = prefs.getBoolean("IS_PRIME", false);

        if (isPrime) {

            // ✅ PRIME USER
            cardPrimeBanner.setVisibility(View.VISIBLE); // show banner

            btnBuyNow.setVisibility(View.GONE); // hide buy button

        } else {

            // ❌ FREE USER
            cardPrimeBanner.setVisibility(View.GONE); // hide banner
            btnBuyNow.setVisibility(View.VISIBLE); // show buy button
        }
        // =========================================================


        // ===============================
        // ⭐ BUY NOW
        // ===============================
        Button btnBuyNow = findViewById(R.id.btnBuyNow);

        btnBuyNow.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, BuyActivity.class));
        });

        // ===============================
        // ⭐ FILE UPLOAD CARD
        // ===============================
        cardFileUpload = findViewById(R.id.cardFileUpload);

        cardFileUpload.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, UploadChoiceActivity.class));
        });

        // ===============================
        // ⭐ ALL COURSE CLICK
        // ===============================
        MaterialCardView layoutAllCourse = findViewById(R.id.layoutAllCourse);
        layoutAllCourse.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FreeVideosActivity.class));
        });

        // ===============================
        // 🔗 CONNECT WITH US
        // ===============================
        ImageView ivYoutube = findViewById(R.id.ivBottomYoutube);
        ImageView ivInstagram = findViewById(R.id.ivBottomInstagram);
        ImageView ivTelegram = findViewById(R.id.ivBottomTelegram);

        ivYoutube.setOnClickListener(v ->
                openLink("https://youtube.com/@albarakahinstituteoftrading?si=nwYf7GMk7la-NEgT")
        );

        ivTelegram.setOnClickListener(v ->
                openLink("https://t.me/brakahinstitute")
        );

        ivInstagram.setOnClickListener(v ->
                openLink("https://www.instagram.com/brakahinstitute?igsh=MWZndmRlZjVqZjIweA==")
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        // ⭐ USERNAME REFRESH (Drawer me show hoga BaseActivity se)
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userName = prefs.getString("userName", "User");

        TextView txtName = findViewById(R.id.txtName);
        if (txtName != null) {
            txtName.setText(userName);
        }
        // =========================================
        // 🔥 NEW FIX: PRIME UI REFRESH
        // =========================================
        boolean isPrime = prefs.getBoolean("IS_PRIME", false);

        if (isPrime) {
            cardPrimeBanner.setVisibility(View.VISIBLE);
            btnBuyNow.setVisibility(View.GONE);
        } else {
            cardPrimeBanner.setVisibility(View.GONE);
            btnBuyNow.setVisibility(View.VISIBLE);
        }

    }

    private void checkSession() {

        String phone = prefs.getString("PHONE", "");
        String password = prefs.getString("PASSWORD", "");
        long localVersion = prefs.getLong("LOGIN_VERSION", 0);

        String deviceId = Settings.Secure.getString(
                getContentResolver(),
                Settings.Secure.ANDROID_ID
        );

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        LoginRequest request = new LoginRequest(phone, password, deviceId);

        apiService.loginUser(request).enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    long serverVersion = response.body().getLoginVersion();

                    if (localVersion != serverVersion) {

                        prefs.edit().clear().apply();

                        Toast.makeText(MainActivity.this,
                                "Logged in on another device",
                                Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }

    private void openLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}