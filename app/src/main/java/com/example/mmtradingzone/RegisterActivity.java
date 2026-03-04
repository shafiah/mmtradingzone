package com.example.mmtradingzone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mmtradingzone.database.AppDatabase;
import com.example.mmtradingzone.database.User;
import com.example.mmtradingzone.database.UserDao;

public class RegisterActivity extends AppCompatActivity {

    EditText etUsername, etMobile, etPassword;
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize DB
        AppDatabase db = AppDatabase.getInstance(this);
        UserDao userDao = db.userDao();

        // Initialize Views (XML IDs ke according)
        etUsername = findViewById(R.id.etUsername);
        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(v -> {

            String username = etUsername.getText().toString().trim();
            String mobile = etMobile.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || mobile.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegisterActivity.this,
                        "All fields required",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if mobile already exists (since email field nahi hai)
            User existingUser = userDao.checkMobileExists(mobile);

            if (existingUser != null) {
                Toast.makeText(RegisterActivity.this,
                        "User already registered with this mobile",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Insert new user
            User newUser = new User(username, mobile, password);
            userDao.registerUser(newUser);

            Toast.makeText(RegisterActivity.this,
                    "Registration Successful",
                    Toast.LENGTH_SHORT).show();

            // Go back to Auth page
            Intent intent = new Intent(RegisterActivity.this, AuthChoiceActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}