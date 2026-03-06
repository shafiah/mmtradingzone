package com.example.mmtradingzone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mmtradingzone.database.AppDatabase;
import com.example.mmtradingzone.database.User;
import com.example.mmtradingzone.database.UserDao;

public class LoginActivity extends AppCompatActivity {

    EditText etMobile, etPassword;
    Button btnLoginSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);
        btnLoginSubmit = findViewById(R.id.btnLoginSubmit);

        AppDatabase db = AppDatabase.getInstance(this);
        UserDao userDao = db.userDao();

        btnLoginSubmit.setOnClickListener(v -> {

            String mobile = etMobile.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (mobile.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Enter Mobile and Password", Toast.LENGTH_SHORT).show();
                return;
            }

            // get user by mobile
            User user = userDao.getUserByMobile(mobile);

            if (user != null && user.getPassword().equals(password)) {

                String deviceId = Settings.Secure.getString(
                        getContentResolver(),
                        Settings.Secure.ANDROID_ID
                );

                if (user.getDeviceId() != null && !user.getDeviceId().equals(deviceId)) {

                    // new device login
                    userDao.updateDeviceId(mobile, deviceId);

                    Toast.makeText(LoginActivity.this, "Logged in from new device", Toast.LENGTH_SHORT).show();
                }

                // SAVE LOGIN SESSION
                SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putString("mobile", mobile);
                editor.apply();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            } else {

                Toast.makeText(LoginActivity.this, "Invalid Mobile or Password", Toast.LENGTH_SHORT).show();
            }

        });
    }
}