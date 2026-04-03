package com.example.mmtradingzone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mmtradingzone.base.BaseActivity;
import com.example.mmtradingzone.network.ApiClient;
import com.example.mmtradingzone.network.ApiService;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyActivity extends BaseActivity implements PaymentResultListener {

    private String currentOrderId = ""; // ⭐ NEW: store orderId for verification

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ⭐ IMPORTANT
        setContentLayout(R.layout.activity_buy);
        setSelectedTab(R.id.nav_store);

        // setContentView(R.layout.activity_buy);

        Checkout.preload(getApplicationContext());

        Button btn = findViewById(R.id.btnProceedPayment);

        btn.setOnClickListener(v -> {
            Toast.makeText(this, "Opening Payment...", Toast.LENGTH_SHORT).show(); // ⭐ NEW UX
            createOrderFromBackend();
        });
    }

    private void createOrderFromBackend() {

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        Call<Map<String, Object>> call = apiService.createOrder();

        call.enqueue(new Callback<Map<String, Object>>() {

            @Override
            public void onResponse(Call<Map<String, Object>> call,
                                   Response<Map<String, Object>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    Map<String, Object> result = response.body();

                    // 🔥 Backend me key "id"
                    String orderId = result.get("id").toString();

                    currentOrderId = orderId; // ⭐ NEW: save orderId

                    startPayment(orderId, result);

                } else {
                    Toast.makeText(BuyActivity.this,
                            "Order creation failed",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call,
                                  Throwable t) {

                Toast.makeText(BuyActivity.this,
                        "API Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // ===============================
    // ⭐ UPDATED PAYMENT METHOD
    // ===============================
    private void startPayment(String orderId, Map<String, Object> result) {

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_SMOMZAdWKVM1nh");

        try {
            JSONObject options = new JSONObject();

            options.put("name", "ABITI Trading Zone");
            options.put("description", "Premium Trading Plan");
            options.put("currency", "INR");
            options.put("order_id", orderId);

            // ⭐ NEW: dynamic amount from backend
            options.put("amount", result.get("amount"));

            // ===============================
            // ⭐ NEW: PREFILL USER DATA
            // ===============================
            JSONObject prefill = new JSONObject();
            prefill.put("email", "test@gmail.com");
            prefill.put("contact", "9999999999");

            options.put("prefill", prefill);

            checkout.open(BuyActivity.this, options);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===============================
    // ❌ OLD METHOD (COMMENTED - INSECURE)
    // ===============================
    /*
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        Toast.makeText(this,
                "Payment Successful",
                Toast.LENGTH_LONG).show();

        Intent intent = new Intent(BuyActivity.this, PaidVideoListActivity.class);
        intent.putExtra("paymentSuccess", true);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        finish();
    }
    */

    // ===============================
    // ✅ NEW SECURE FLOW
    // ===============================
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {

        // 🔥 NEW: send to backend for verification
        verifyPaymentWithBackend(razorpayPaymentID);
    }

    // ===============================
    // ⭐ NEW METHOD: VERIFY PAYMENT
    // ===============================
    private void verifyPaymentWithBackend(String paymentId) {

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String phoneNumber = prefs.getString("PHONE","");
        Call<String> call = apiService.verifyPayment(paymentId,currentOrderId,phoneNumber);

        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {

                    Toast.makeText(BuyActivity.this,
                            "Payment Verified ✅",
                            Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(BuyActivity.this, PaidVideoListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(BuyActivity.this,
                            "Verification Failed ❌",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(BuyActivity.this,
                        "API Error",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(this,
                "Payment Failed",
                Toast.LENGTH_LONG).show();
    }
}