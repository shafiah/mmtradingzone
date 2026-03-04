package com.example.mmtradingzone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AuthChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_choice);

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v ->
                startActivity(new Intent(AuthChoiceActivity.this, LoginActivity.class))
        );

        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(AuthChoiceActivity.this, RegisterActivity.class))
        );
    }
}