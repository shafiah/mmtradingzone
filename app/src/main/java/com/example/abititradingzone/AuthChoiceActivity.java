package com.example.abititradingzone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.example.abititradingzone.base.BaseActivity;

public class AuthChoiceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        String phone = prefs.getString("phoneNumber", "");

// ✅ FIXED CONDITION
        if (isLoggedIn && phone != null && !phone.isEmpty()) {

            Intent intent = new Intent(AuthChoiceActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_auth_choice);

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v ->
                startActivity(new Intent(AuthChoiceActivity.this, LoginActivity.class))
        );

        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(AuthChoiceActivity.this, RegisterActivity.class))
        );
    }
}