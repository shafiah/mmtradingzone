package com.example.abititradingzone;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.abititradingzone.base.BaseActivity;
import com.example.abititradingzone.network.ApiClient;
import com.example.abititradingzone.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends BaseActivity {

    LinearLayout layoutContactUs;
    LinearLayout layoutDeleteAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentLayout(R.layout.activity_settings);

        layoutContactUs = findViewById(R.id.layoutContactUs);
        layoutDeleteAccount = findViewById(R.id.layoutDeleteAccount);

        // ===============================
        // CONTACT US
        // ===============================
        layoutContactUs.setOnClickListener(v -> {

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);

            emailIntent.setData(
                    Uri.parse("mailto:ziyarajput223@gmail.com")
            );

            emailIntent.putExtra(
                    Intent.EXTRA_SUBJECT,
                    "ABITI Trading Zone Support"
            );

            startActivity(
                    Intent.createChooser(emailIntent,
                            "Contact Us")
            );
        });

        // ===============================
        // DELETE ACCOUNT
        // ===============================
        layoutDeleteAccount.setOnClickListener(v -> {

            new AlertDialog.Builder(SettingsActivity.this)

                    .setTitle("Delete Account")

                    .setMessage(
                            "Are you sure you want to permanently delete your account?\n\n" +
                                    "This action cannot be undone."
                    )

                    .setPositiveButton("Delete", (dialog, which) -> {

                        deleteAccountFromBackend();

                    })

                    .setNegativeButton("Cancel", (dialog, which) -> {

                        dialog.dismiss();

                    })

                    .show();
        });
    }

    // ===============================
    // DELETE ACCOUNT API
    // ===============================
    private void deleteAccountFromBackend() {

        SharedPreferences preferences =
                getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        String phoneNumber =
                preferences.getString("PHONE", "");

        ApiService apiService =
                ApiClient.getClient(this)
                        .create(ApiService.class);

        Call<String> call =
                apiService.deleteAccount(phoneNumber);

        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call,
                                   Response<String> response) {

                if (response.isSuccessful()) {

                    // CLEAR SESSION
                    SharedPreferences.Editor editor =
                            preferences.edit();

                    editor.clear();
                    editor.apply();

                    Toast.makeText(
                            SettingsActivity.this,
                            "Your account has been permanently deleted.\nPlease register again to continue.",
                            Toast.LENGTH_LONG
                    ).show();

                    // OPEN LOGIN SCREEN
                    Intent intent =
                            new Intent(SettingsActivity.this,
                                    LoginActivity.class);

                    intent.setFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                    );

                    startActivity(intent);

                    finish();

                } else {

                    Toast.makeText(
                            SettingsActivity.this,
                            "Delete Failed",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<String> call,
                                  Throwable t) {

                Toast.makeText(
                        SettingsActivity.this,
                        "API Error: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}