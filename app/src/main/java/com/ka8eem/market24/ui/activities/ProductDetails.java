package com.ka8eem.market24.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ka8eem.market24.R;
import com.ka8eem.market24.adapters.ImageAdapter;
import com.ka8eem.market24.adapters.PhotoAdapter;
import com.ka8eem.market24.models.BuildingConstantDetailsModel;
import com.ka8eem.market24.models.OtherConstantDetailsModel;
import com.ka8eem.market24.models.ProductModel;
import com.ka8eem.market24.models.RequestModel;
import com.ka8eem.market24.models.UserModel;
import com.ka8eem.market24.models.VehiclesConstantDetailsModel;
import com.ka8eem.market24.ui.fragments.MessageDialog;
import com.ka8eem.market24.ui.fragments.ReportDialog;
import com.ka8eem.market24.util.Constants;
import com.ka8eem.market24.viewmodel.ProductViewModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.ka8eem.market24.util.Constants.SHARED;

public class ProductDetails extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;


    FirebaseDatabase firebaseDatabase;
    String owner_ad = "";
    String ID_ADS, one_img, name_ad;
    CollapsingToolbarLayout toolbarLayout;
    Toolbar toolbar;
    PhotoAdapter photoAdapter;
    TextView textCounter;

    private static ViewPager mPager;
    TextView textTimeDate, textProductName, textSalary, textLocation, textDesc, textDetails, textNumReq;
    LinearLayout callContainer, messageContainer, detailsContainer, buildingContainer, otherContainer;
    Button btnReport, btnDelete, btnEdit;

    // vars
    private ArrayList<ProductModel> favouriteList;
    private int ICON_ADD_TO_FAV_ID;
    private ProductModel productModel;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ImageAdapter imageAdapter;
    private String isVehicle;
    UserModel userModel;
    ProductViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        preferences = getSharedPreferences(Constants.SHARED, MODE_PRIVATE);
        editor = preferences.edit();
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        Gson gson = new Gson();
        String json = preferences.getString("USER_MODEL", null);
        Type type = new TypeToken<UserModel>() {
        }.getType();
        userModel = gson.fromJson(json, type);
        Intent intent = getIntent();
        String _id = intent.getStringExtra("product_id");
        productViewModel.getProductById(_id);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        productViewModel.productById.observe(this, new Observer<ProductModel>() {
            @Override
            public void onChanged(ProductModel model) {
                if (model != null) {
                    productModel = model;
                    progressDialog.dismiss();
                    isVehicle = productModel.getIsVehicles();
                    if (getApplicationContext() != null) {
                        initView(savedInstanceState);
                        if (productModel != null) {
                            if (productModel.getProductName() != null && !productModel.getProductName().equals(""))
                                getSupportActionBar().setTitle(productModel.getProductName());
                        }

                    }
                }
            }
        });
    }

    private void initView(Bundle savedInstanceState) {
        Gson gson = new Gson();
        String json = preferences.getString("listFav", null);
        Type type = new TypeToken<ArrayList<ProductModel>>() {
        }.getType();
        favouriteList = gson.fromJson(json, type);
        if (favouriteList == null)
            favouriteList = new ArrayList<>();
        toolbarLayout = findViewById(R.id.collapsing_Toolbar);
        toolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        toolbarLayout.setCollapsedTitleTextColor(Color.rgb(255, 255, 255));
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textDesc = findViewById(R.id.desc_details);
        detailsContainer = findViewById(R.id.details_container);
        buildingContainer = findViewById(R.id.building_details_container);
        otherContainer = findViewById(R.id.other_details_container);
        ID_ADS = productModel.getProductID() + "";
        one_img = productModel.getProductImages().get(0).getImgUrl() + "";
        name_ad = productModel.getProductName();
        if (isVehicle.equals("0")) {
            detailsContainer.setVisibility(View.GONE);
            buildingContainer.setVisibility(View.GONE);
            otherContainer.setVisibility(View.VISIBLE);
            TextView textView = otherContainer.findViewById(R.id.text_other_status);
            OtherConstantDetailsModel model = productModel.getOtherConstantDetails();
            textView.setText(model.getStatus());
            textDesc.setText(model.getDetails());

        } else if (isVehicle.equals("1")) {
            detailsContainer.setVisibility(View.VISIBLE);
            buildingContainer.setVisibility(View.GONE);
            otherContainer.setVisibility(View.GONE);
            VehiclesConstantDetailsModel model = productModel.getVehiclesConstantDetails();
            TextView textView = detailsContainer.findViewById(R.id.car_color);
            textView.setText(model.getColor());
            textView = detailsContainer.findViewById(R.id.car_distance);
            textView.setText(model.getKilometers());
            textView = detailsContainer.findViewById(R.id.car_engine);
            textView.setText(model.getEnginePower());
            textView = detailsContainer.findViewById(R.id.car_model);
            textView.setText(model.getVehicleModel());
            textView = detailsContainer.findViewById(R.id.car_status);
            textView.setText(model.getVehicleStatus());
            textView = detailsContainer.findViewById(R.id.car_trans);
            textView.setText(model.getTransmissionType());
            textView = detailsContainer.findViewById(R.id.car_type);
            textView.setText(model.getVehicleType());
            textView = detailsContainer.findViewById(R.id.car_year);
            textView.setText(model.getProductionYear());
            textDesc.setText(model.getVehicleDetails());
        } else if (isVehicle.equals("2")) {
            detailsContainer.setVisibility(View.GONE);
            buildingContainer.setVisibility(View.VISIBLE);
            otherContainer.setVisibility(View.GONE);
            TextView textView = buildingContainer.findViewById(R.id.text_building_area);
            BuildingConstantDetailsModel model = productModel.getBuildingConstantDetails();
            textView.setText(model.getSpace());
            textView = buildingContainer.findViewById(R.id.text_building_no_rooms);
            textView.setText(model.getNumOfRooms());
            textView = buildingContainer.findViewById(R.id.text_building_finishing);
            textView.setText(model.getFinishing());
            textView = buildingContainer.findViewById(R.id.text_building_floor_number);
            textView.setText(model.getFloorNumber());
            textView = buildingContainer.findViewById(R.id.text_building_furnished);
            textView.setText(model.getFurnished());
            textView = buildingContainer.findViewById(R.id.text_building_lux);
            textView.setText(model.getLux());
            textView = buildingContainer.findViewById(R.id.text_building_no_bath);
            textView.setText(model.getNumOfBathroom());
            textDesc.setText(model.getDetails());
        }


        mPager = (ViewPager) findViewById(R.id.pager);
        photoAdapter = new PhotoAdapter(this, productModel.getProductImages());
        mPager.setAdapter(photoAdapter);

        callContainer = findViewById(R.id.container_req);
        callContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkRegister()) {
                    startRegisterActivity();
                } else if (!checkLoggedIn()) {
                    startLoginActivity();
                } else {
                    makeRequest();
                }
            }
        });


        imageAdapter = new ImageAdapter();
        String price = productModel.getPrice();
        String category = productModel.getCategoryName();
        String city = productModel.getCityName();
        if (Constants.getLocal(ProductDetails.this).equals("AR"))
            price = price + " ل.س";
        else {
            price = price + " L.S";
            category = productModel.getCategoryNameEn();
            city = productModel.getCityNameEn();
        }

        textCounter = findViewById(R.id.text_counter);
        textProductName = findViewById(R.id.product_name_details);
        textProductName.setText(productModel.getProductName());
        textLocation = findViewById(R.id.text_location);
        textLocation.setText(city);
        textSalary = findViewById(R.id.product_price_details);
        textSalary.setText(price);
        textTimeDate = findViewById(R.id.date);
        textTimeDate.setText(productModel.getDateTime());
        btnDelete = findViewById(R.id.delete_ads_btn);
        LinearLayout layoutContainerEdit = findViewById(R.id.edit_product_container);

        btnReport = findViewById(R.id.btn_report);
        textNumReq = findViewById(R.id.num_req);
        if ("1".equals(productModel.getPriceType()))
            textNumReq.setText(getString(R.string.negotiable));
        else
            textNumReq.setText(getString(R.string.non_negotiable));
        if (checkLoggedIn()) {
            if (productModel.getUserID() == userModel.getUserId()) {
                btnReport.setVisibility(View.GONE);
                LinearLayout layout = findViewById(R.id.line6);
                layout.setVisibility(View.GONE);
                layoutContainerEdit.setVisibility(View.VISIBLE);
            } else {
                btnReport.setVisibility(View.VISIBLE);
                LinearLayout layout = findViewById(R.id.line6);
                layout.setVisibility(View.VISIBLE);
                layoutContainerEdit.setVisibility(View.GONE);
            }
        }
        btnEdit = findViewById(R.id.edit_ads_btn);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetails.this, EditProductActivity.class);
                intent.putExtra("product_model", productModel);
                startActivity(intent);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct(productModel.getProductID() + "");
            }
        });
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportDialog reportDialog = new ReportDialog(productModel.getProductID());
                reportDialog.setCancelable(false);
                reportDialog.show(getSupportFragmentManager(), "Report");
            }
        });
        if (savedInstanceState != null) {
            ICON_ADD_TO_FAV_ID = savedInstanceState.getInt("found", -1);
        } else {
            if (isFavProduct() >= 0)
                ICON_ADD_TO_FAV_ID = R.drawable.ic_favorite;
            else
                ICON_ADD_TO_FAV_ID = R.drawable.ic_favorite_border;
        }
    }

    private void deleteProduct(String productID) {
        ProductViewModel viewModel = ViewModelProviders.of(ProductDetails.this).get(ProductViewModel.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.are_u_sure));
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        ).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.deleteProduct(productID);
                final ProgressDialog progressDialog = new ProgressDialog(ProductDetails.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                progressDialog.setCancelable(false);
                viewModel.mutableDeleteProduct.observe(ProductDetails.this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        progressDialog.dismiss();
                        String res = null;
                        if (s != null && s.equals("1")) {
                            res = getString(R.string.delete_done);
                            int fav = isFav(productModel.getProductID());
                            if (fav > -1) {
                                removeItem(fav);
                            }
                        } else
                            res = getString(R.string.something_wrong);
                        Toast.makeText(ProductDetails.this, res, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    public void removeItem(int pos) {
        if (pos < 0 || pos >= favouriteList.size()) {
            return;
        }
        Gson gson = new Gson();
        String json = preferences.getString("listFav", null);
        Type type = new TypeToken<ArrayList<ProductModel>>() {
        }.getType();
        favouriteList = gson.fromJson(json, type);
        favouriteList.remove(pos);
        json = gson.toJson(favouriteList);
        editor.putString("listFav", json);
        editor.commit();
        editor.apply();
    }


    private int isFav(int _id) {
        for (int i = 0; i < favouriteList.size(); i++)
            if (favouriteList.get(i).getProductID() == _id)
                return i;
        return -1;
    }

    private void makeRequest() {
        String json = preferences.getString("USER_MODEL", null);
        Gson gson1 = new Gson();
        Type type1 = new TypeToken<UserModel>() {
        }.getType();
        UserModel userModel = gson1.fromJson(json, type1);
        ProductViewModel productViewModel = ViewModelProviders.of(ProductDetails.this).get(ProductViewModel.class);
        RequestModel requestModel = new RequestModel(productModel.getProductID() + "", userModel.getUserId() + "", productModel.getUserID() + "", productModel.getPrice());
        final ProgressDialog progressDialog = new ProgressDialog(ProductDetails.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        productViewModel.requestProduct(requestModel);
        productViewModel.mutableRequestProduct.observe(ProductDetails.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(ProductDetails.this, s, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                makePhoneCall();
            }
        });
    }

    private void makePhoneCall() {
        String phoneNum = productModel.getPhoneNumber();
        phoneNum = phoneNum.trim();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            String dial = "tel:" + phoneNum;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    private void startRegisterActivity() {
        Intent intent = new Intent(ProductDetails.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void startLoginActivity() {
        Intent intent = new Intent(ProductDetails.this, LoginActivity.class);
        startActivity(intent);
    }

    private boolean checkRegister() {
        boolean ret = preferences.getBoolean("REGISTERED", false);
        return ret;
    }

    private boolean checkLoggedIn() {
        boolean ret = preferences.getBoolean("LOGGED_IN", false);
        return ret;
    }


    private void shareProduct() {
        String url = "url for product";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(Intent.createChooser(intent, "Share Via"));
    }


    private int isFavProduct() {
        int idx = -1;
        for (int i = 0; i < favouriteList.size(); i++) {
            ProductModel model = favouriteList.get(i);
            if (model.getProductID() == productModel.getProductID()) {
                idx = i;
                break;
            }
        }
        return idx;
    }

    private void addToFavourite(MenuItem item) {
        String toast = "";
        editor = preferences.edit();
        int idx = isFavProduct();
        if (idx >= 0) {
            toast = getString(R.string.remove_from_fav);
            favouriteList.remove(idx);
            ICON_ADD_TO_FAV_ID = R.drawable.ic_favorite_border;
        } else {
            toast = getString(R.string.add_to_fav);
            favouriteList.add(productModel);
            ICON_ADD_TO_FAV_ID = R.drawable.ic_favorite;
        }
        item.setIcon(ICON_ADD_TO_FAV_ID);
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
        Gson gson = new Gson();
        String json = gson.toJson(favouriteList);
        editor.putString("listFav", json);
        editor.commit();
        editor.apply();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("found", ICON_ADD_TO_FAV_ID);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_product_details, menu);
        menu.getItem(0).setIcon(ICON_ADD_TO_FAV_ID);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.share:
//                shareProduct();
//                break;
            case R.id.add_to_fav:
                addToFavourite(item);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}