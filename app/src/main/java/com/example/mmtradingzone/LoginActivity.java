package com.example.mmtradingzone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mmtradingzone.base.BaseActivity;
import com.example.mmtradingzone.network.ApiClient;
import com.example.mmtradingzone.network.ApiService;
import com.example.mmtradingzone.network.LoginRequest;
import com.example.mmtradingzone.network.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    EditText etMobile, etPassword;

    Button btnLoginSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);
        btnLoginSubmit = findViewById(R.id.btnLoginSubmit);

        btnLoginSubmit.setOnClickListener(v -> {

            String mobile = etMobile.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (mobile.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Enter Mobile and Password", Toast.LENGTH_SHORT).show();
                return;
            }

            // DEVICE ID
            String deviceId = Settings.Secure.getString(
                    getContentResolver(),
                    Settings.Secure.ANDROID_ID
            );

            ApiService apiService = ApiClient.getClient().create(ApiService.class);

            LoginRequest request = new LoginRequest(
                    mobile,
                    password,
                    deviceId
            );

            apiService.loginUser(request).enqueue(new Callback<LoginResponse>() {

                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                    if (response.isSuccessful() && response.body() != null) {

                        LoginResponse apiUser = response.body();

                       // Toast.makeText(LoginActivity.this, apiUser.getUserName(), Toast.LENGTH_LONG).show();

                        // SAVE SESSION
                        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putLong("LOGIN_VERSION",apiUser.getLoginVersion());
                        editor.putString("PHONE",mobile);
                        editor.putString("PASSWORD",password);
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("userName", apiUser.getUserName());
                        editor.apply();

                        Toast.makeText(LoginActivity.this,
                                "Login Successful",
                                Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();


                    } else {

                        Toast.makeText(LoginActivity.this,
                                "Invalid credentials",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {

                    Toast.makeText(LoginActivity.this,
                            "API Error: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

        });
    }
}