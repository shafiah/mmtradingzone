package com.example.mmtradingzone;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;

import com.example.mmtradingzone.database.AppDatabase;
import com.example.mmtradingzone.database.User;
import com.example.mmtradingzone.database.UserDao;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Drawer user name show

        TextView txtName = findViewById(R.id.txtName);

        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        String mobile = prefs.getString("mobile", null);

        if (mobile != null) {

            AppDatabase db = AppDatabase.getInstance(this);
            UserDao userDao = db.userDao();

            User user = userDao.getUserByMobile(mobile);

            if (user != null) {
                txtName.setText(user.getName());
            }
        }

        Button btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(v -> {

         //   SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(MainActivity.this, AuthChoiceActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });



        Button btnBuyNow = findViewById(R.id.btnBuyNow);

        btnBuyNow.setOnClickListener(v -> {
            // 🔜 Next step: Payment / Plan page
            // Abhi dummy navigation (baad me Razorpay yahin se lagega)

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

        // 🔹 DRAWER OPEN CODE (YAHI PASTE KARNA HAI)
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);

        // 🔹 VERY IMPORTANT (WITHOUT THIS 🔔 WON'T WORK)
        setSupportActionBar(toolbar);



        toolbar.setNavigationOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START)
        );



        // 🔗 CONNECT WITH US ICONS (BOTTOM SECTION)
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