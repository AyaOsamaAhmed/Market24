package com.ka8eem.market24.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListAdapter;
import android.widget.Spinner;

import com.ka8eem.market24.R;
import com.ka8eem.market24.models.CategoryModel;
import com.ka8eem.market24.models.CityModel;
import com.ka8eem.market24.models.SubCategoryModel;
import com.ka8eem.market24.ui.fragments.HomeFragment;
import com.ka8eem.market24.util.Constants;
import com.ka8eem.market24.viewmodel.CategoryViewModel;
import com.ka8eem.market24.viewmodel.ProductViewModel;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity {

    // widgets
    EditText textSearch;
    Spinner categorySpinner, subCategorySpinner, areaSpinner, subAreaSpinner;

    // vars
    ArrayAdapter<String> catAdapter, subCatAdapter, areaAdapter, subAreaAdapter;
    ArrayList<CityModel> areaList, subAreaList;
    ArrayList<CategoryModel> catList;
    ArrayList<SubCategoryModel> subCatList;
    String selectedCatId, selectedSubCatId, selectedAreaId, selectedSubAreaId, searchQuery;
    ProductViewModel productViewModel;
    CategoryViewModel categoryVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        init();
    }

    private void init() {
        catList = new ArrayList<>();
        subCatList = new ArrayList<>();
        areaList = new ArrayList<>();
        subAreaList = new ArrayList<>();
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        categoryVM = ViewModelProviders.of(this).get(CategoryViewModel.class);
        textSearch = findViewById(R.id.search_text);
        categorySpinner = findViewById(R.id.category_spinner);
        subCategorySpinner = findViewById(R.id.sub_category_spinner);
        areaSpinner = findViewById(R.id.area_spinner);
        subAreaSpinner = findViewById(R.id.sub_area_spinner);
        categoryVM.getAllCategories();
        categoryVM.getAllCities();
        categoryVM.mutableCategoryList.observe(this, new Observer<ArrayList<CategoryModel>>() {
            @Override
            public void onChanged(ArrayList<CategoryModel> categoryModels) {
                catList = new ArrayList<>();
                String curLang = "AR";
                curLang = Constants.getLocal(FilterActivity.this);
                ArrayList<String> list = new ArrayList<>();
                for (CategoryModel it : categoryModels) {
                    catList.add(it);
                    if (curLang.equals("AR"))
                        list.add(it.getCategoryName());
                    else
                        list.add(it.getCatNameEn());
                }
                String all = getString(R.string.all_categories);
                list.add(0, all);
                catList.add(0, new CategoryModel(0, all, "0"));
                list.remove(list.size() - 1);
                catList.remove(catList.size() - 1);
                catAdapter = new ArrayAdapter<>(FilterActivity.this, R.layout.spinner_textview, list);
                catAdapter.setDropDownViewResource(R.layout.text_drop);
                categorySpinner.setAdapter(catAdapter);
            }
        });

        categoryVM.mutableCityList.observe(this, new Observer<ArrayList<CityModel>>() {
            @Override
            public void onChanged(ArrayList<CityModel> cityModels) {
                areaList = new ArrayList<>();
                ArrayList<String> list = new ArrayList<>();
                String curLang = "AR";
                curLang = Constants.getLocal(FilterActivity.this);
                for (CityModel model : cityModels) {
                    areaList.add(model);
                    if (curLang.equals("AR"))
                        list.add(model.getCityName());
                    else
                        list.add(model.getAreaNameEn());
                }
                String all = getString(R.string.all_cities);
                list.add(0, all);
                areaList.add(0, new CityModel(0, all));
                list.remove(list.size() - 1);
                areaList.remove(areaList.size() - 1);
                areaAdapter = new ArrayAdapter<>(FilterActivity.this, R.layout.spinner_textview, list);
                areaAdapter.setDropDownViewResource(R.layout.text_drop);
                areaSpinner.setAdapter(areaAdapter);
            }
        });
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    subCategorySpinner.setVisibility(View.GONE);
                    selectedCatId = "0";
                } else {
                    selectedCatId = catList.get(position).getCategoryId() + "";
                    categoryVM.getSubCategory(selectedCatId);
                    categoryVM.subCategoryList.observe(FilterActivity.this, new Observer<ArrayList<SubCategoryModel>>() {
                        @Override
                        public void onChanged(ArrayList<SubCategoryModel> subCategoryModels) {
                            subCatList = new ArrayList<>(subCategoryModels);
                            String curLang = "AR";
                            curLang = Constants.getLocal(FilterActivity.this);
                            ArrayList<String> listNames = new ArrayList<>();
                            listNames.add(getString(R.string.all_cat));
                            for (SubCategoryModel it : subCategoryModels) {
                                if (curLang.equals("AR"))
                                    listNames.add(it.getCubCatName());
                                else
                                    listNames.add(it.getSubCatNameEn());
                            }
                            subCatAdapter = new ArrayAdapter<>(FilterActivity.this, R.layout.spinner_textview, listNames);
                            subCatAdapter.setDropDownViewResource(R.layout.text_drop);
                            subCategorySpinner.setAdapter(subCatAdapter);
                            subCategorySpinner.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    subAreaSpinner.setVisibility(View.GONE);
                    selectedAreaId = "0";
                } else {
                    selectedAreaId = areaList.get(position).getCityID() + "";
                    categoryVM.getSubArea(selectedAreaId);
                    categoryVM.subAreaList.observe(FilterActivity.this, new Observer<ArrayList<CityModel>>() {
                        @Override
                        public void onChanged(ArrayList<CityModel> cityModels) {
                            subAreaList = new ArrayList<>(cityModels);
                            String curLang = "AR";
                            ArrayList<String> listNames = new ArrayList<>();
                            listNames.add(getString(R.string.all_area));
                            curLang = Constants.getLocal(FilterActivity.this);
                            for (CityModel it : cityModels) {
                                if (curLang.equals("AR"))
                                    listNames.add(it.getCityName());
                                else
                                    listNames.add(it.getAreaNameEn());
                            }
                            subAreaAdapter = new ArrayAdapter<>(FilterActivity.this, R.layout.spinner_textview, listNames);
                            subAreaAdapter.setDropDownViewResource(R.layout.text_drop);
                            subAreaSpinner.setAdapter(subAreaAdapter);
                            subAreaSpinner.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        subCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    selectedSubCatId = "0";
                else
                    selectedSubCatId = subCatList.get(position).getSubCatId() + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        subAreaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    selectedSubAreaId = "0";
                else {
                    selectedSubAreaId = subAreaList.get(position).getCityID() + "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void search(View view) {
        searchQuery = textSearch.getText().toString();
        HomeFragment.getProducts(selectedCatId, selectedAreaId, selectedSubCatId, selectedSubAreaId, searchQuery);
        finish();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}