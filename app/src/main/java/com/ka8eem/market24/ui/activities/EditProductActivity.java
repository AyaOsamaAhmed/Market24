package com.ka8eem.market24.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.ka8eem.market24.R;
import com.ka8eem.market24.adapters.EditImageProductAdapter;
import com.ka8eem.market24.adapters.ImageProductAdapter;
import com.ka8eem.market24.models.AdsModel;
import com.ka8eem.market24.models.BuildingConstantDetailsModel;
import com.ka8eem.market24.models.CategoryModel;
import com.ka8eem.market24.models.CityModel;
import com.ka8eem.market24.models.OtherConstantDetailsModel;
import com.ka8eem.market24.models.ProductModel;
import com.ka8eem.market24.models.SubCategoryModel;
import com.ka8eem.market24.models.UserModel;
import com.ka8eem.market24.models.VehiclesConstantDetailsModel;
import com.ka8eem.market24.ui.fragments.HomeFragment;
import com.ka8eem.market24.util.Constants;
import com.ka8eem.market24.viewmodel.CategoryViewModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MultipartBody;

public class EditProductActivity extends AppCompatActivity {

    //widgets
    RecyclerView recyclerViewProductImages, recyclerViewDeleteImages;
    Spinner categorySpinner, subCategorySpinner, areaSpinner, subAreaSpinner;
    Button btnCancel, btnNext;
    EditText editTextPrice, otherArea, productName;
    TextView empty, textCurArea, textCurSubArea, textCurCat, textCurSubCat;
    ImageView imageView;
    CheckBox priceSwitch;
    CheckBox checkBoxAddArea;

    // vars
    int id_c  ;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public static ArrayList<Uri> imageUris;
    boolean valid;
    String price, priceType, subArea, name;
    int areaIndex, subAreaIndex, categoryIndex, subCategoryIndex;
    ImageProductAdapter imageProductAdapter;
    EditImageProductAdapter editImageProductAdapter;
    CategoryViewModel categoryViewModel;
    ArrayList<CategoryModel> catList = new ArrayList<>();
    ArrayList<CityModel> cityList = new ArrayList<>();
    ArrayAdapter<String> catAdapter, cityAdapter, subAreaAdapter, subCatAdapter ,subCatAdapter2;
    ArrayList<SubCategoryModel> subCategoryList = new ArrayList<>();
    ArrayList<SubCategoryModel> subCategoryList2= new ArrayList<>();
    ArrayList<CityModel> subAreaList = new ArrayList<>();
    public static ArrayList<MultipartBody.Part> images;
    public static ArrayList<String> encodedImages;
    Intent intent;
    ProductModel productModel;
    String catId, subCatId, areaId, subAreaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        initViews();
    }

    private void initViews() {
        intent = getIntent();
        if (intent != null) {
            productModel = (ProductModel) intent.getSerializableExtra("product_model");
        }
        imageUris = new ArrayList<>();
        encodedImages = new ArrayList<>();
        preferences = getSharedPreferences(Constants.SHARED, Context.MODE_PRIVATE);
        editor = preferences.edit();
        valid = true;
        priceType = "0";
        areaIndex = subAreaIndex = categoryIndex = subCategoryIndex = -1;
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        recyclerViewProductImages = findViewById(R.id.edit_product_recycler_view_product_image);
        recyclerViewDeleteImages = findViewById(R.id.edit_product_delete_image_recycler_view);
        GridLayoutManager manager = new GridLayoutManager(getBaseContext(), 3);
        recyclerViewDeleteImages.setLayoutManager(manager);
        //recyclerViewDeleteImages.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        editImageProductAdapter = new EditImageProductAdapter();
        editImageProductAdapter.setList(productModel.getProductImages());
        recyclerViewDeleteImages.setAdapter(editImageProductAdapter);
        imageUris = new ArrayList<>();
        checkBoxAddArea = findViewById(R.id.edit_product_check_other_area);

        priceSwitch = findViewById(R.id.edit_product_switch_price);
        if (productModel.getPriceType().equals("1")) {
            priceSwitch.setChecked(true);
        } else
            priceSwitch.setChecked(false);
        otherArea = findViewById(R.id.edit_product_other_area);
        imageView = findViewById(R.id.edit_product_take_img);
        empty = findViewById(R.id.edit_product_empty);
        editTextPrice = findViewById(R.id.edit_product_edit_text_price);
        editTextPrice.setText(productModel.getPrice());
        btnNext = findViewById(R.id.edit_product_btn_next);
        btnCancel = findViewById(R.id.edit_product_btn_cancel_add_product);
        productName = findViewById(R.id.edit_product_edit_text_ads_name);
        productName.setText(productModel.getProductName());
        categorySpinner = findViewById(R.id.edit_product_category_spinner);
//        textCurArea = findViewById(R.id.edit_product_cur_area_text);
//        textCurSubArea = findViewById(R.id.edit_product_cur_sub_area_text);
//        textCurCat = findViewById(R.id.edit_product_cur_cat_text);
//        textCurSubCat = findViewById(R.id.edit_product_cur_sub_cat_text);
        String lang = Constants.getLocal(this);
        // categorySpinner.setFocusable(true);
        // categorySpinner.setFocusableInTouchMode(true);
        subCategorySpinner = findViewById(R.id.edit_product_sub_category_spinner);
        // subCategorySpinner.setFocusable(true);
        // subCategorySpinner.setFocusableInTouchMode(true);
        areaSpinner = findViewById(R.id.edit_product_area_spinner);
        // areaSpinner.setFocusable(true);
        // areaSpinner.setFocusableInTouchMode(true);
        subAreaSpinner = findViewById(R.id.edit_product_sub_area_spinner);
        // subAreaSpinner.setFocusable(true);
        // subAreaSpinner.setFocusableInTouchMode(true);

        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (cityList != null && cityList.size() > 0) {
                    areaIndex = position;
                    if (position != 0) {
                        String _id = cityList.get(position).getCityID() + "";
                        subAreaList = new ArrayList<>();
                        categoryViewModel.getSubArea(_id);
                        categoryViewModel.subAreaList.observe(EditProductActivity.this, new Observer<ArrayList<CityModel>>() {
                                    @Override
                                    public void onChanged(ArrayList<CityModel> cityModels) {
                                        subAreaList = new ArrayList<>(cityModels);
                                        String curLang = "AR";
                                        ArrayList<String> listNames = new ArrayList<>();
                                        listNames.add(getString(R.string.choose_sub_area));
                                        subAreaList.add(0, new CityModel(-1, ""));
                                        curLang = Constants.getLocal(EditProductActivity.this);
                                        for (CityModel it : cityModels) {
                                            if (curLang.equals("AR"))
                                                listNames.add(it.getCityName());
                                            else
                                                listNames.add(it.getAreaNameEn());
                                        }
                                        subAreaAdapter = new ArrayAdapter<>(EditProductActivity.this, R.layout.spinner_textview, listNames);
                                        subAreaAdapter.setDropDownViewResource(R.layout.text_drop);
                                        subAreaSpinner.setAdapter(subAreaAdapter);

                                        for (int i = 1; i < subAreaList.size(); i++) {
                                            String s = subAreaList.get(i).getCityID()+"";
                                            if (productModel.getSubAreaId().equals(s)) {
                                                subAreaSpinner.setSelection(i);
                                                break;
                                            }
                                        }
                                    }
                                }
                        );
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (catList != null && catList.size() > 0) {
                    categoryIndex = position;
                    ArrayList<String> list = new ArrayList<>();
                    if (position != 0) {
                        subCategoryList = new ArrayList<>();
                        String _id = catList.get(position).getCategoryId() + "";
                        categoryViewModel.getSubCategory(_id);
                        categoryViewModel.subCategoryList.observe(EditProductActivity.this, new Observer<ArrayList<SubCategoryModel>>() {
                            @Override
                            public void onChanged(ArrayList<SubCategoryModel> subCategoryModels) {
                                subCategoryList = new ArrayList<>(subCategoryModels);
                                String curLang = "AR";
                                curLang = Constants.getLocal(EditProductActivity.this);
                                ArrayList<String> listNames = new ArrayList<>();
                                subCategoryList.add(0, new SubCategoryModel(-1, "", ""));
                                listNames.add(getString(R.string.choose_sub_category));
                                for (SubCategoryModel it : subCategoryModels) {
                                    if (curLang.equals("AR"))
                                        listNames.add(it.getCubCatName());
                                    else
                                        listNames.add(it.getSubCatNameEn());
                                }

                                subCatAdapter = new ArrayAdapter<>(EditProductActivity.this, R.layout.spinner_textview, listNames);
                                subCatAdapter.setDropDownViewResource(R.layout.text_drop);
                                subCategorySpinner.setAdapter(subCatAdapter);
                                for (int i = 1; i < subCategoryList.size(); i++) {
                                    String s = subCategoryList.get(i).getSubCatId()+"";
                                    if (productModel.getSubCatId().equals(s)) {
                                        subCategorySpinner.setSelection(i);
                                        break;
                                    }
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        subCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subCategoryIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        subAreaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
                subAreaIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        checkBoxAddArea.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    otherArea.setVisibility(View.VISIBLE);
                    subArea = otherArea.getText().toString();
                } else {
                    subArea = "-1";
                    otherArea.setVisibility(View.GONE);
                }
            }
        });

        priceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    priceType = "1";
                else
                    priceType = "0";
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valid = true;
                price = editTextPrice.getText().toString().trim();
                name = productName.getText().toString();
                validInput();
                if (valid) {
                    catId = catList.get(categoryIndex).getCategoryId() + "";
                    subCatId = subCategoryList.get(subCategoryIndex).getSubCatId() + "";
                    areaId = cityList.get(areaIndex).getCityID() + "";
                    subAreaId = subAreaList.get(subAreaIndex).getCityID() + "";
                    if (checkBoxAddArea.isChecked()) {
                        subArea = otherArea.getText().toString();
                    } else
                        subArea = "-1";
                    UserModel userModel = Constants.getUser(EditProductActivity.this);
                    if (userModel == null) {
                        Toast.makeText(EditProductActivity.this, "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String userId = userModel.getUserId() + "";
                    OtherConstantDetailsModel otherConstantDetailsModel = productModel.getOtherConstantDetails();
                    VehiclesConstantDetailsModel vehiclesConstantDetailsModel = productModel.getVehiclesConstantDetails();
                    BuildingConstantDetailsModel buildingConstantDetailsModel = productModel.getBuildingConstantDetails();
                    AdsModel adsModel = new AdsModel(name, catId, subCatId, userId, areaId, subAreaId, subArea, price, priceType ,  vehiclesConstantDetailsModel , buildingConstantDetailsModel , otherConstantDetailsModel );
                    adsModel.setID_ADS(productModel.getProductID()+"");
                   // AdsModel adsModel2 = new AdsModel(adsModel, vehiclesConstantDetailsModel , buildingConstantDetailsModel , otherConstantDetailsModel ); );
                    Intent intent = null;

                        String productType = catList.get(categoryIndex).getIsVehicles();
                        if (productType.equals("1")) {
                            if (subCategoryList.get(subCategoryIndex).getIsCar().equals("1"))
                                intent = new Intent(EditProductActivity.this, EditCarActivity.class);
                            else
                                intent = new Intent(EditProductActivity.this, EditVehiclesActivity.class);
                        } else if (productType.equals("2")) {
                            intent = new Intent(EditProductActivity.this, EditBuildingActivity.class);
                        } else if (productType.equals("0"))
                            intent = new Intent(EditProductActivity.this, EditOtherDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ads_model", adsModel);
                    intent.putExtras(bundle);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUris != null && imageUris.size() >= 5) {
                    Toast.makeText(EditProductActivity.this, getString(R.string.max_images_uploaded), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ActivityCompat.checkSelfPermission(EditProductActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EditProductActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, getString(R.string.choose_images)), 1);
            }
        });

        initSpinners();
    }

    private void initSpinners() {

        catList = new ArrayList<>();
        categoryViewModel.getAllCategories();
        categoryViewModel.mutableCategoryList.observe(this, new Observer<ArrayList<CategoryModel>>() {
            @Override
            public void onChanged(ArrayList<CategoryModel> categoryModels) {
                catList = new ArrayList<>();
                String curLang = "AR";
                curLang = Constants.getLocal(EditProductActivity.this);
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
                catAdapter = new ArrayAdapter<>(EditProductActivity.this, R.layout.spinner_textview, list);
                catAdapter.setDropDownViewResource(R.layout.text_drop);
                categorySpinner.setAdapter(catAdapter);
                for (int i = 1; i < catList.size(); i++) {
                    if (catList.get(i).getCategoryId() == productModel.getCategoryID()) {


                        categorySpinner.setSelection(i);

                        break;
                    }
                }
            }
        });


        cityList = new ArrayList<>();
        categoryViewModel.getAllCities();
        categoryViewModel.mutableCityList.observe(this, new Observer<ArrayList<CityModel>>() {
            @Override
            public void onChanged(ArrayList<CityModel> cityModels) {
                cityList = new ArrayList<>();
                ArrayList<String> list = new ArrayList<>();
                String curLang = "AR";
                curLang = Constants.getLocal(EditProductActivity.this);
                for (CityModel model : cityModels) {
                    cityList.add(model);
                    if (curLang.equals("AR"))
                        list.add(model.getCityName());
                    else
                        list.add(model.getAreaNameEn());
                }
                String all = getString(R.string.all_cities);
                list.add(0, all);
                cityList.add(0, new CityModel(0, all));
                list.remove(list.size() - 1);
                //***//
                cityAdapter = new ArrayAdapter<>(EditProductActivity.this, R.layout.spinner_textview, list);
                cityAdapter.setDropDownViewResource(R.layout.text_drop);
                areaSpinner.setAdapter(cityAdapter);
                for (int i = 1; i < cityList.size(); i++) {
                    if (productModel.getAreaId().equals(cityList.get(i).getCityID() + "")) {
                        areaSpinner.setSelection(i);
                        break;
                    }
                }
            }
        });
    }

    private void validInput() {
        if (name.isEmpty()) {
            productName.setError("");
            productName.requestFocus();
            valid = false;
        }
        if (price == null || price.isEmpty()) {
            editTextPrice.setError("");
            editTextPrice.requestFocus();
            valid = false;
        }
        if (areaSpinner.getSelectedItemPosition() == -1 || areaSpinner.getSelectedItemPosition() == 0) {
            areaSpinner.requestFocus();
            Toast.makeText(this, getString(R.string.choose_city), Toast.LENGTH_SHORT).show();
            valid = false;
        }


        if (categorySpinner.getSelectedItemPosition() == -1 || categorySpinner.getSelectedItemPosition() == 0) {
            categorySpinner.requestFocus();
            Toast.makeText(this, getString(R.string.choose_cat), Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (subCategorySpinner.getSelectedItemPosition() == -1 || subCategorySpinner.getSelectedItemPosition() == 0) {
            subCategorySpinner.requestFocus();
            Toast.makeText(this, getString(R.string.choose_sub_category), Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (editImageProductAdapter.getItemCount() == 0
                || editImageProductAdapter.getItemCount() + imageUris.size() > 5) {
            valid = false;
            Toast.makeText(this, getString(R.string.max_images_uploaded), Toast.LENGTH_SHORT).show();
        }
        if (checkBoxAddArea.isChecked()) {
            subAreaIndex = 0;
            otherArea.setEnabled(true);
            otherArea.setVisibility(View.VISIBLE);
            subArea = otherArea.getText().toString();
            if (subArea == null || subArea.isEmpty()) {
                otherArea.setError("");
                otherArea.requestFocus();
                valid = false;
            }
        } else if (subAreaSpinner.getSelectedItemPosition() == -1 || subAreaSpinner.getSelectedItemPosition() == 0) {
            subAreaSpinner.requestFocus();
            Toast.makeText(this, getString(R.string.choose_sub_area), Toast.LENGTH_SHORT).show();
            valid = false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri uri = null;
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    imageUris.add(clipData.getItemAt(i).getUri());
                }
                encodedImages = new ArrayList<>();
                Bitmap bitmap = null;
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                progressDialog.setCancelable(false);
                for (int i = 0; i < imageUris.size(); i++) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUris.get(i));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    String encoded = "";
                    if (bitmap != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos); // bm is the bitmap object
                        byte[] byteArrayImage = baos.toByteArray();
                        encoded = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                        encodedImages.add(encoded);
                    }
                }
                progressDialog.dismiss();
            } else {
                if (data.getData() != null) {
                    Bitmap bitmap = null;
                    imageUris.add(data.getData());
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUris.get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    String encoded = "";
                    if (bitmap != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos); // bm is the bitmap object
                        byte[] byteArrayImage = baos.toByteArray();
                        encoded = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                        encodedImages.add(encoded);
                    }
                } else
                    encodedImages = new ArrayList<>();
            }
            GridLayoutManager manager = new GridLayoutManager(this, 3);
            recyclerViewProductImages.setLayoutManager(manager);
            imageProductAdapter = new ImageProductAdapter(true);
            imageProductAdapter.setList(imageUris);
            recyclerViewProductImages.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
            recyclerViewProductImages.setAdapter(imageProductAdapter);
        }
    }
}