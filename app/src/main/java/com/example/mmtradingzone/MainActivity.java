package com.example.mmtradingzone;

import com.example.mmtradingzone.base.BaseActivity;
import com.example.mmtradingzone.network.ApiClient;
import com.example.mmtradingzone.network.ApiService;
import com.example.mmtradingzone.network.LoginRequest;
import com.example.mmtradingzone.network.LoginResponse;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ⭐ IMPORTANT
        setContentLayout(R.layout.activity_main);
        setSelectedTab(R.id.nav_home);

        prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        // ===============================
        // ⭐ NEW: FREE MATERIAL CLICK
        // ===============================
        TextView txtFreeMaterial = findViewById(R.id.txtFreeMaterial);

        txtFreeMaterial.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FreeVideosActivity.class));
        });

        // ===============================
        // ⭐ EDIT PROFILE CLICK
        // ===============================
        TextView txtEditProfile = findViewById(R.id.txtEditProfile);

        txtEditProfile.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, EditProfileActivity.class));
        });

        // ===============================
        // ⭐ SETTINGS CLICK
        // ===============================
        TextView txtSettings = findViewById(R.id.txtSettings);

        txtSettings.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ComingSoonActivity.class));
        });

        // ===============================
        // ⭐ HOW TO USE APP
        // ===============================
        TextView txtHowToUse = findViewById(R.id.txtHowToUse);

        txtHowToUse.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        // ===============================
        // ⭐ PRIVACY POLICY
        // ===============================
        TextView txtPrivacyPolicy = findViewById(R.id.txtPrivacyPolicy);

        txtPrivacyPolicy.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ComingSoonActivity.class));
        });

        // ===============================
        // ⭐ LOGOUT
        // ===============================
        Button btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(MainActivity.this, AuthChoiceActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

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
        MaterialCardView cardFileUpload = findViewById(R.id.cardFileUpload);

        cardFileUpload.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, UploadChoiceActivity.class));
        });

        // ===============================
        // ⭐ ALL COURSE CLICK
        // ===============================
        findViewById(R.id.layoutAllCourse).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FreeVideosActivity.class));
        });

        // ===============================
        // 🔗 CONNECT WITH US
        // ===============================
        ImageView ivYoutube = findViewById(R.id.ivBottomYoutube);
        ImageView ivInstagram = findViewById(R.id.ivBottomInstagram);
        ImageView ivTelegram = findViewById(R.id.ivBottomTelegram);

        ivYoutube.setOnClickListener(v ->
                openLink("https://youtube.com/@muzammilpathan-e4g?si=FgblTSgwn2JL6G8I")
        );

        ivTelegram.setOnClickListener(v ->
                openLink("https://t.me/MuftiMuzammil")
        );

        ivInstagram.setOnClickListener(v ->
                openLink("https://www.instagram.com/")
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