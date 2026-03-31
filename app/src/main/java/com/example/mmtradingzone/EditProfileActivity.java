package com.example.mmtradingzone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mmtradingzone.base.BaseActivity;
import com.example.mmtradingzone.network.ApiClient;
import com.example.mmtradingzone.network.ApiService;
import com.example.mmtradingzone.network.Users;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends BaseActivity {

    EditText etName;
    Button btnUpdate;

    TextView txtMobile;

    SharedPreferences prefs;
    Long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ⭐ IMPORTANT
        setContentLayout(R.layout.activity_edit_profile);

       // setContentView(R.layout.activity_edit_profile);

        etName = findViewById(R.id.etName);
        txtMobile = findViewById(R.id.txtMobile);
        btnUpdate = findViewById(R.id.btnUpdate);




        prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        // ✅ GET DATA FROM PREFS
        userId = prefs.getLong("userId", -1);
        String userName = prefs.getString("userName", "");
        String mobile = prefs.getString("phoneNumber","");
        Log.d("PREF_MOBILE", mobile);
        // DEBUG
        Log.d("PREF_MOBILE", "Mobile: " + mobile);

        // ✅ SET EXISTING NAME
        etName.setText(userName);
        txtMobile.setText(mobile);
        // DEBUG
        Log.d("PREF_MOBILE", "Mobile: " + mobile);

        // ============================
        // 🔥 UPDATE BUTTON CLICK
        // ============================
        btnUpdate.setOnClickListener(v -> {

            String newName = etName.getText().toString().trim();

            if (newName.isEmpty()) {
                Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
                return;
            }

            updateUserName(newName);
        });
    }

    // ============================
    // 🔥 UPDATE API CALL
    // ============================
    private void updateUserName(String name) {


        ApiService apiService =
                ApiClient.getClient(this).create(ApiService.class);

        Users user = new Users();
        user.setUserName(name); // ⭐ ONLY NAME UPDATE

        Call<Users> call = apiService.updateUser(userId, user);

        call.enqueue(new Callback<Users>() {

            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {



                if (response.isSuccessful()) {

                    // ✅ SAVE UPDATED NAME LOCALLY
                    prefs.edit()
                            .putString("userName", name)
                            .apply();

                    Toast.makeText(EditProfileActivity.this,
                            "Profile Updated",
                            Toast.LENGTH_SHORT).show();

                    finish(); // 🔙 BACK TO MAIN
                } else {
                    Toast.makeText(EditProfileActivity.this,
                            "Update Failed",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {


                Toast.makeText(EditProfileActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}