package com.example.mmtradingzone.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mmtradingzone.BuyActivity;
import com.example.mmtradingzone.MainActivity;
import com.example.mmtradingzone.ProfileActivity;
import com.example.mmtradingzone.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseActivity extends AppCompatActivity {

    protected FrameLayout contentFrame;
    protected BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 🔒 SECURITY (UNCHANGED)
        // getWindow().setFlags(
        //         WindowManager.LayoutParams.FLAG_SECURE,
        //         WindowManager.LayoutParams.FLAG_SECURE
        // );

        // ⭐ BASE LAYOUT
        super.setContentView(R.layout.activity_base);

        contentFrame = findViewById(R.id.contentFrame);
        bottomNav = findViewById(R.id.bottomNav);

        // ⭐ NAVIGATION CLICK
        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            // 🏠 HOME
            if (id == R.id.nav_home) {
                if (!(this instanceof MainActivity)) {

                    Intent intent = new Intent(this, MainActivity.class);

                    // 🔥 NEW: BACK STACK CLEAN
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    startActivity(intent);
                    overridePendingTransition(0,0);
                    finish(); // 🔥 NEW: current activity close
                }
                return true;
            }

            // 🛒 STORE
            if (id == R.id.nav_store) {
                if (!(this instanceof BuyActivity)) {

                    Intent intent = new Intent(this, BuyActivity.class);

                    // 🔥 NEW: BACK STACK CLEAN
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    startActivity(intent);
                    overridePendingTransition(0,0);
                    finish(); // 🔥 NEW
                }
                return true;
            }

            // 👤 PROFILE
            if (id == R.id.nav_profile) {
                if (!(this instanceof ProfileActivity)) {

                    Intent intent = new Intent(this, ProfileActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    startActivity(intent);
                    overridePendingTransition(0,0);
                    finish();
                }
                return true;
            }

            return false;
        });
    }

    // ⭐ CHILD LAYOUT SET (UNCHANGED)
    protected void setContentLayout(int layoutId) {
        LayoutInflater.from(this).inflate(layoutId, contentFrame, true);
    }

    // ⭐🔥 MAIN FIX: SELECTED TAB CONTROL (UNCHANGED)
    protected void setSelectedTab(int itemId) {
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(itemId);
        }
    }
}