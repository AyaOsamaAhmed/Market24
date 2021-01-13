package com.ka8eem.market24.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ka8eem.market24.R;
import com.ka8eem.market24.adapters.AutoImageSliderAdapter;
import com.ka8eem.market24.adapters.ObjectAdapter;
import com.ka8eem.market24.adapters.SearchAdapter;
import com.ka8eem.market24.models.ObjectModel;
import com.ka8eem.market24.models.PaymentAdsModel;
import com.ka8eem.market24.models.ProductModel;
import com.ka8eem.market24.viewmodel.ProductViewModel;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class AdsByCategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProductViewModel productViewModel;
    ArrayList<PaymentAdsModel> list;
    ObjectAdapter objectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_by_category);
        Intent intent = getIntent();
        String id = intent.getStringExtra("cat_id");
        recyclerView = findViewById(R.id.ads_cat_recycler_view);
        list = new ArrayList<>(MainActivity.paymentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        productViewModel.getAdsByCategory(id);
        productViewModel.adsCategoryList.observe(this, new Observer<ArrayList<ProductModel>>() {
            @Override
            public void onChanged(ArrayList<ProductModel> productModels) {
                if (productModels != null) {
                    ArrayList<ObjectModel> tempList = new ArrayList<>();
                    int cnt1 = 0, cnt2 = 0, cur = 0, lastPaidAdded = 0;
                    while (cnt1 < productModels.size()) {
                        if (cur % 3 == 0 && cur > 0) {
                            tempList.add(new ObjectModel(list.get(cnt2), true));
                            cnt2++;
                            cnt2 %= list.size();
                            lastPaidAdded++;
                        } else {
                            tempList.add(new ObjectModel(productModels.get(cnt1), false));
                            cnt1++;
                        }
                        cur++;
                    }
                    while (lastPaidAdded < list.size()) {
                        tempList.add(new ObjectModel(list.get(lastPaidAdded), true));
                        lastPaidAdded++;
                    }
                    objectAdapter = new ObjectAdapter(tempList);
                    recyclerView.setAdapter(objectAdapter);
                } else
                    Toast.makeText(AdsByCategoryActivity.this, getString(R.string.no_ads), Toast.LENGTH_SHORT).show();
            }
        });
    }
}