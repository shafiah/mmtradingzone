package com.example.mmtradingzone;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;

import com.example.mmtradingzone.network.ApiClient;
import com.example.mmtradingzone.network.ApiService;
import com.example.mmtradingzone.network.LoginRequest;
import com.example.mmtradingzone.network.LoginResponse;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs; // ✅ global banaya taaki sab jagah use ho

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE); // ✅ prefs init

        // Drawer user name show
        TextView txtName = findViewById(R.id.txtName);

        //UserName Fetch
        String userName = prefs.getString("userName","User");
        txtName.setText(userName);

        Button btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(v -> {

            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(MainActivity.this, AuthChoiceActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        Button btnBuyNow = findViewById(R.id.btnBuyNow);

        btnBuyNow.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    BuyActivity.class
            );
            startActivity(intent);
        });

        MaterialCardView cardFileUpload = findViewById(R.id.cardFileUpload);

        cardFileUpload.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UploadChoiceActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.layoutAllCourse).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FreeVideosActivity.class);
            startActivity(intent);
        });

        View root = findViewById(android.R.id.content);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });

        // 🔹 DRAWER OPEN CODE
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START)
        );

        // 🔗 CONNECT WITH US ICONS
        ImageView ivYoutube = findViewById(R.id.ivBottomYoutube);
        ImageView ivInstagram = findViewById(R.id.ivBottomInstagram);
        ImageView ivTelegram = findViewById(R.id.ivBottomTelegram);

        ivYoutube.setOnClickListener(v ->
                openLink("https://youtube.com/@muzammilpathan-e4g?si=FgblTSgwn2JL6G8I")
        );

        ivTelegram.setOnClickListener(v ->
                openLink("https://t.me/MuftiMuzammil")
        );

        ivInstagram.setOnClickListener(v ->
                openLink("https://www.instagram.com/")
        );
    }

    // ✅ IMPORTANT: Activity resume hone par session check hoga
    @Override
    protected void onResume() {
        super.onResume();
        checkSession();
    }

    // ✅ SESSION CHECK FUNCTION
    private void checkSession() {

        String phone = prefs.getString("PHONE", "");
        String password = prefs.getString("PASSWORD", "");
        long localVersion = prefs.getLong("LOGIN_VERSION", 0);

        String deviceId = Settings.Secure.getString(
                getContentResolver(),
                Settings.Secure.ANDROID_ID
        );

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        LoginRequest request = new LoginRequest(phone, password, deviceId);

        apiService.loginUser(request).enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    long serverVersion = response.body().getLoginVersion();

                    if (localVersion != serverVersion) {

                        prefs.edit().clear().apply();

                        Toast.makeText(MainActivity.this,
                                "Logged in on another device",
                                Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_bar_menu, menu);
        return true;
    }

    // 🔔 TOOLBAR BELL ICON CLICK HANDLE
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_notification) {
            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}