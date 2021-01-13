package com.ka8eem.market24.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.ka8eem.market24.R;
import com.ka8eem.market24.adapters.CategoryAdapter;
import com.ka8eem.market24.models.CategoryModel;
import com.ka8eem.market24.viewmodel.CategoryViewModel;

import java.util.ArrayList;

public class AllCategories extends AppCompatActivity {

    LinearLayout layout;
    RecyclerView recyclerView;
    CategoryAdapter categoryAdapter;
    CategoryViewModel categoryVM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_categories);
        initView();
    }

    private void initView() {
        layout = findViewById(R.id.linear);
        recyclerView = findViewById(R.id.all_category_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryAdapter = new CategoryAdapter();
        categoryVM = ViewModelProviders.of(this).get(CategoryViewModel.class);
        categoryVM.getAllCategories();
        categoryVM.mutableCategoryList.observe(this, new Observer<ArrayList<CategoryModel>>() {
            @Override
            public void onChanged(ArrayList<CategoryModel> categoryModels) {
                ArrayList<CategoryModel> list = new ArrayList<>(categoryModels);
                CategoryModel cat = list.get(list.size() - 1);
                list.remove(list.size() - 1);
                categoryAdapter.setList(list);
                recyclerView.setAdapter(categoryAdapter);
            }
        });
    }
}