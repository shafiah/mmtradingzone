package com.example.mmtradingzone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;

import com.example.mmtradingzone.base.BaseActivity;

public class ProfileActivity extends BaseActivity {

    TextView tvName, tvPhone, tvStatus;
    Button btnEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_profile);

        // ⭐ Bottom nav highlight
        setSelectedTab(R.id.nav_profile);

        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvStatus = findViewById(R.id.tvStatus);
        btnEditProfile = findViewById(R.id.btnEditProfile);

        // 🔹 SharedPreferences se data lo
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        String name = prefs.getString("userName", "User Name");
        String phone = prefs.getString("phoneNumber", "Not Available");
        boolean isPrime = prefs.getBoolean("isPrime", false);

        tvName.setText(name);
        tvPhone.setText(phone);
        tvStatus.setText(isPrime ? "Premium User" : "Free User");

        // 🔹 Edit button
        btnEditProfile.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
        });
    }
}