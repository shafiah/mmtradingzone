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

        AppDatabase db = AppDatabase.getInstance(this);
        UserDao userDao = db.userDao();

        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);
        btnLoginSubmit = findViewById(R.id.btnLoginSubmit);

        btnLoginSubmit.setOnClickListener(v -> {

            String mobile = etMobile.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (mobile.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this,
                        "All fields required",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            User user = userDao.loginUser(mobile, password);

            if (user != null) {

                // Save login session
                SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putString("loggedInMobile", mobile);
                editor.apply();

                Toast.makeText(LoginActivity.this,
                        "Login Successful",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(LoginActivity.this,
                        "Wrong mobile or password",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}