package com.example.mmtradingzone.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.Menu; // ⭐ NEW ADD
import android.view.MenuItem; // ⭐ NEW ADD
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.mmtradingzone.AuthChoiceActivity;
import com.example.mmtradingzone.BuyActivity;
import com.example.mmtradingzone.FreeVideosActivity;
import com.example.mmtradingzone.MainActivity;
import com.example.mmtradingzone.NotificationActivity;
import com.example.mmtradingzone.ProfileActivity;
import com.example.mmtradingzone.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;
import com.example.mmtradingzone.EditProfileActivity;



public class BaseActivity extends AppCompatActivity {

    protected FrameLayout contentFrame;
    protected BottomNavigationView bottomNav;
    protected DrawerLayout drawerLayout;
    protected MaterialToolbar toolbar;

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


        // ⭐ NEW TOOLBAR + DRAWER (FINAL)
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.topAppBar);

        if (toolbar != null && drawerLayout != null) {

            // ⭐ VERY IMPORTANT
            setSupportActionBar(toolbar);

            // ☰ MENU CLICK → OPEN DRAWER
            toolbar.setNavigationOnClickListener(v -> {
                drawerLayout.openDrawer(GravityCompat.START);
            });

            // 🔔 NOTIFICATION CLICK (KEEP THIS)
            toolbar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_notification) {
                    startActivity(new Intent(this, NotificationActivity.class));
                    return true;
                }
                return false;
            });
        }

        contentFrame = findViewById(R.id.contentFrame);
        bottomNav = findViewById(R.id.bottomNav);


        // ===============================
        // 🔥 NEW: DRAWER MENU SHIFTED HERE
        // ===============================

        // ===============================
// 🔥 DRAWER CLICK FINAL FIX
// ===============================

// USER NAME
        TextView txtName = findViewById(R.id.txtName);
        if (txtName != null) {
            SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            String userName = prefs.getString("userName", "User");
            txtName.setText(userName);
        }

// 🔥 IMPORTANT: CORRECT IDS USE
        TextView txtFreeMaterial = findViewById(R.id.txtFreeMaterial);
        TextView txtEditProfile = findViewById(R.id.txtEditProfile);
        TextView txtSettings = findViewById(R.id.txtSettings);
        TextView txtPrivacyPolicy = findViewById(R.id.txtPrivacyPolicy);

// FREE MATERIAL
        if (txtFreeMaterial != null) {
            txtFreeMaterial.setOnClickListener(v -> {
                startActivity(new Intent(this, FreeVideosActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            });
        }

// EDIT PROFILE
        if (txtEditProfile != null) {
            txtEditProfile.setOnClickListener(v -> {
                startActivity(new Intent(this, EditProfileActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            });
        }

// SETTINGS
        if (txtSettings != null) {
            txtSettings.setOnClickListener(v -> {
                Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawer(GravityCompat.START);
            });
        }

// PRIVACY
        if (txtPrivacyPolicy != null) {
            txtPrivacyPolicy.setOnClickListener(v -> {
                Toast.makeText(this, "Privacy Policy", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawer(GravityCompat.START);
            });
        }

        // ===============================
// ⭐ LOGOUT (FROM MAINACTIVITY - SHIFTED)
// ===============================

        Button btnLogout = findViewById(R.id.btnLogout);

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {

                // 🔥 SAME LOGIC AS MAINACTIVITY
                getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                        .edit()
                        .clear()
                        .apply();

                Intent intent = new Intent(BaseActivity.this, AuthChoiceActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                // 🔥 CLOSE DRAWER
                if (drawerLayout != null) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            });
        }

        // ===============================
        // BOTTOM NAV (UNCHANGED)
        // ===============================


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

    // ⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐
    // 🔥🔥🔥 FINAL FIX (NEW ADD - DO NOT DELETE)
    // ⭐ MENU LOAD FOR ALL PAGES
    // ⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_bar_menu, menu);
        return true;
    }

    // ⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐
    // 🔔 CLICK HANDLE (GLOBAL)
    // ⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_notification) {
            startActivity(new Intent(this, NotificationActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
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