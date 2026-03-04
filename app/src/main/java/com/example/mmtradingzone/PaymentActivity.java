package com.example.mmtradingzone;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity
        implements PaymentResultListener {

    Button btnPay;
    int amount; // rupees

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        btnPay = findViewById(R.id.btnPay);

        // ✅ Amount receive from previous screen
        amount = getIntent().getIntExtra("amount", 0);

        btnPay.setOnClickListener(v -> openRazorpay());
    }

    private void openRazorpay() {

        if (amount <= 0) {
            Toast.makeText(this,
                    "Invalid amount",
                    Toast.LENGTH_LONG).show();
            return;
        }

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_SMOMZAdWKVM1nh"); // apni test key

        try {
            JSONObject options = new JSONObject();
            options.put("name", "MM Trading Zone");
            options.put("description", "Premium Plan");
            options.put("currency", "INR");

            // 🔥 Convert rupees → paise
            options.put("amount", amount * 100);

            checkout.open(PaymentActivity.this, options);

        } catch (Exception e) {
            Toast.makeText(this,
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentId) {
        Toast.makeText(this,
                "Payment Success: " + razorpayPaymentId,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(this,
                "Payment Failed: " + response,
                Toast.LENGTH_LONG).show();
    }
}