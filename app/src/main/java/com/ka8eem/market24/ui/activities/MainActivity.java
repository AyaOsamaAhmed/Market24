package com.ka8eem.market24.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ka8eem.market24.R;
import com.ka8eem.market24.models.PaymentAdsModel;
import com.ka8eem.market24.viewmodel.ProductViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1500;
    public static ArrayList<PaymentAdsModel> paymentList;
    ProductViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        productViewModel.getPaymentAds();
        productViewModel.paymentAdsList.observe(this, new Observer<ArrayList<PaymentAdsModel>>() {
            @Override
            public void onChanged(ArrayList<PaymentAdsModel> paymentAdsModels) {
                if (paymentAdsModels != null) {
                    paymentList = new ArrayList<>(paymentAdsModels);
                }
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}