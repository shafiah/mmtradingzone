package com.example.mmtradingzone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mmtradingzone.base.BaseActivity;
import com.example.mmtradingzone.database.AppDatabase;
import com.example.mmtradingzone.database.User;
import com.example.mmtradingzone.database.UserDao;
import com.example.mmtradingzone.network.ApiClient;
import com.example.mmtradingzone.network.ApiService;
import com.example.mmtradingzone.network.UserRequest;
import com.example.mmtradingzone.network.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {

    EditText etUsername, etMobile, etPassword;
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize DB
        AppDatabase db = AppDatabase.getInstance(this);
        UserDao userDao = db.userDao();

        // Initialize Views
        etUsername = findViewById(R.id.etUsername);
        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(v -> {

            String username = etUsername.getText().toString().trim();
            String mobile = etMobile.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Empty field check
            if (username.isEmpty() || mobile.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegisterActivity.this,
                        "All fields required",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Mobile validation
            if (!mobile.matches("[6-9][0-9]{9}")) {
                etMobile.setError("Enter valid mobile number");
                etMobile.requestFocus();
                return;
            }

            // Check if mobile already exists in local DB
            User existingUser = userDao.getUserByMobile(mobile);

            if (existingUser != null) {
                Toast.makeText(RegisterActivity.this,
                        "User already registered with this mobile",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Get device ID
            @SuppressLint("HardwareIds")
            String deviceId = android.provider.Settings.Secure.getString(
                    getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID
            );

            // 🔵 API SERVICE
            ApiService apiService = ApiClient.getClient().create(ApiService.class);

            UserRequest request = new UserRequest(
                    username,
                    null,
                    mobile,
                    deviceId,
                    password
            );

            apiService.registerUser(request).enqueue(new Callback<UserResponse>() {

                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                    if (response.isSuccessful()) {

                        // Save in local DB also
                        User newUser = new User(username, mobile, password, deviceId);
                        userDao.registerUser(newUser);

                        Toast.makeText(RegisterActivity.this,
                                "Registration Successful",
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(RegisterActivity.this,
                                AuthChoiceActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    else {

                        Toast.makeText(RegisterActivity.this,
                                "Server Error",
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {

                    Toast.makeText(RegisterActivity.this,
                            "API Error: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

        });
    }
}