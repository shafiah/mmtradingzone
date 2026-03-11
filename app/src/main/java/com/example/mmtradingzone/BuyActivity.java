package com.example.mmtradingzone;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mmtradingzone.network.ApiClient;
import com.example.mmtradingzone.network.ApiService;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyActivity extends AppCompatActivity implements PaymentResultListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        Checkout.preload(getApplicationContext());

        Button btn = findViewById(R.id.btnProceedPayment);
        btn.setOnClickListener(v -> createOrderFromBackend());
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

                    // 🔥 Backend me key "id" hai, "orderId" nahi
                    String orderId = result.get("id").toString();

                    startPayment(orderId);

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

    private void startPayment(String orderId) {

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_SMOMZAdWKVM1nh");

        try {
            JSONObject options = new JSONObject();

            options.put("name", "MM Trading Zone");
            options.put("description", "Premium Trading Plan");
            options.put("currency", "INR");
            options.put("order_id", orderId);
            options.put("amount",100);

            checkout.open(BuyActivity.this, options);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        Toast.makeText(this,
                "Payment Successful",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(this,
                "Payment Failed",
                Toast.LENGTH_LONG).show();
    }
}