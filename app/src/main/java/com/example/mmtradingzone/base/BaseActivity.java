package com.example.mmtradingzone.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mmtradingzone.BuyActivity;
import com.example.mmtradingzone.EditProfileActivity;
import com.example.mmtradingzone.MainActivity;
import com.example.mmtradingzone.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseActivity extends AppCompatActivity {

    protected FrameLayout contentFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 🔒 EXISTING (UNCHANGED)
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
        );

        // ⭐ NEW
        super.setContentView(R.layout.activity_base);

        contentFrame = findViewById(R.id.contentFrame);

        // ⭐ NEW: Bottom nav handle
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            // 🏠 HOME
            if (id == R.id.nav_home) {

                // ⭐ FIX: already home me ho to kuch mat karo
                if (!(this instanceof MainActivity)) {
                    startActivity(new Intent(this, MainActivity.class));
                }
                return true;
            }

            // 🛒 STORE (🔥 NEW ADDED)
            if (id == R.id.nav_store) {

                if (!(this instanceof BuyActivity)) {
                    startActivity(new Intent(this, BuyActivity.class));
                }
                return true;
            }
            // future use
            if (id == R.id.nav_chats) {
                return true;
            }
            // future use
            if (id == R.id.nav_profile) {
                if (!(this instanceof EditProfileActivity)) {
                    startActivity(new Intent(this, EditProfileActivity.class));
                }
                return true;
            }

            return false;
        });
    }

    // ⭐ NEW METHOD
    protected void setContentLayout(int layoutId) {
        LayoutInflater.from(this).inflate(layoutId, contentFrame, true);
    }
}