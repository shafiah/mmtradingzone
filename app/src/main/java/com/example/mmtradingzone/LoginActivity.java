package com.example.mmtradingzone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
                Toast.makeText(this, "Enter Mobile and Password", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = userDao.loginUser(mobile, password);

            if (user != null) {

                // ✅ SAVE LOGIN SESSION
                SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putString("mobile", mobile);
                editor.apply();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(this, "Invalid Mobile or Password", Toast.LENGTH_SHORT).show();
            }

        });
    }
}