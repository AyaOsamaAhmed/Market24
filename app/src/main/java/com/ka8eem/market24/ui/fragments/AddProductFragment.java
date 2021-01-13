package com.ka8eem.market24.ui.fragments;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.navigation.NavigationView;
import com.ka8eem.market24.R;
import com.ka8eem.market24.adapters.ImageProductAdapter;
import com.ka8eem.market24.models.AdsModel;
import com.ka8eem.market24.models.CategoryModel;
import com.ka8eem.market24.models.CityModel;
import com.ka8eem.market24.models.SubCategoryModel;
import com.ka8eem.market24.models.UserModel;
import com.ka8eem.market24.ui.activities.OtherDetailsActivity;
import com.ka8eem.market24.ui.activities.BuildingActivity;
import com.ka8eem.market24.ui.activities.CarActivity;
import com.ka8eem.market24.ui.activities.VehiclesActivity;
import com.ka8eem.market24.util.Constants;
import com.ka8eem.market24.viewmodel.CategoryViewModel;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Struct;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class AddProductFragment extends Fragment {

    public AddProductFragment() {
        // Required empty public constructor
    }

    //widgets
    RecyclerView recyclerViewProductImages;
    Spinner categorySpinner, subCategorySpinner, areaSpinner, subAreaSpinner;
    Button btnCancel, btnNext;
    EditText editTextPrice, otherArea, productName;
    TextView empty;
    ImageView imageView;
    CheckBox priceSwitch;
    CheckBox checkBoxAddArea;
    SearchView searchView;
    ImageView filterImage;
    // vars
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public static ArrayList<Uri> imageUris;
    boolean valid;
    String price, priceType, subArea, name;
    int areaIndex, subAreaIndex, categoryIndex, subCategoryIntex;
    ImageProductAdapter imageProductAdapter;
    CategoryViewModel categoryViewModel;
    ArrayList<CategoryModel> catList = new ArrayList<>();
    ArrayList<CityModel> cityList = new ArrayList<>();
    ArrayAdapter<String> catAdapter, cityAdapter, subAreaAdapter, subCatAdapter;
    ArrayList<SubCategoryModel> subCategoryList = new ArrayList<>();
    ArrayList<CityModel> subAreaList = new ArrayList<>();
    public static ArrayList<MultipartBody.Part> images;
    public static ArrayList<String> encodedImages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_product, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        searchView = (SearchView) getActivity().findViewById(R.id.search_view);
        searchView.setVisibility(View.GONE);
        filterImage = (ImageView) getActivity().findViewById(R.id.filter_icon);
        filterImage.setVisibility(View.GONE);
        imageUris = new ArrayList<>();
        encodedImages = new ArrayList<>();
        preferences = getContext().getSharedPreferences(Constants.SHARED, Context.MODE_PRIVATE);
        editor = preferences.edit();
        valid = true;
        priceType = "0";
        areaIndex = subAreaIndex = categoryIndex = subCategoryIntex = -1;
        categoryViewModel = ViewModelProviders.of(getActivity()).get(CategoryViewModel.class);
        recyclerViewProductImages = view.findViewById(R.id.recycler_view_product_image);
        imageUris = new ArrayList<>();
        checkBoxAddArea = view.findViewById(R.id.check_other_area);
        priceSwitch = view.findViewById(R.id.switch_price);
        otherArea = view.findViewById(R.id.other_area);
        imageView = view.findViewById(R.id.take_img);
        empty = view.findViewById(R.id.empty);
        editTextPrice = view.findViewById(R.id.edit_text_price);
        btnNext = view.findViewById(R.id.btn_next);
        btnCancel = view.findViewById(R.id.btn_cancel_add_product);
        productName = view.findViewById(R.id.edit_text_ads_name);
        categorySpinner = view.findViewById(R.id.add_product_category_spinner);
        // categorySpinner.setFocusable(true);
        // categorySpinner.setFocusableInTouchMode(true);
        subCategorySpinner = view.findViewById(R.id.add_product_sub_category_spinner);
        // subCategorySpinner.setFocusable(true);
        // subCategorySpinner.setFocusableInTouchMode(true);
        areaSpinner = view.findViewById(R.id.add_product_area_spinner);
        // areaSpinner.setFocusable(true);
        // areaSpinner.setFocusableInTouchMode(true);
        subAreaSpinner = view.findViewById(R.id.add_product_sub_area_spinner);
        // subAreaSpinner.setFocusable(true);
        // subAreaSpinner.setFocusableInTouchMode(true);

        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (getActivity() != null && cityList != null && cityList.size() > 0) {
                    areaIndex = position;
                    if (position != 0) {
                        String _id = cityList.get(position).getCityID() + "";
                        subAreaList = new ArrayList<>();
                        categoryViewModel.getSubArea(_id);
                        categoryViewModel.subAreaList.observe(getActivity(), new Observer<ArrayList<CityModel>>() {
                                    @Override
                                    public void onChanged(ArrayList<CityModel> cityModels) {
                                        if (getActivity() != null && getContext() != null) {
                                            subAreaList = new ArrayList<>(cityModels);
                                            String curLang = "AR";
                                            ArrayList<String> listNames = new ArrayList<>();
                                            listNames.add(getString(R.string.choose_sub_area));
                                            subAreaList.add(0, new CityModel(-1, ""));
                                            curLang = Constants.getLocal(getContext());
                                            for (CityModel it : cityModels) {
                                                if (curLang.equals("AR"))
                                                    listNames.add(it.getCityName());
                                                else
                                                    listNames.add(it.getAreaNameEn());
                                            }
                                            subAreaAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_textview, listNames);
                                            subAreaAdapter.setDropDownViewResource(R.layout.text_drop);
                                            subAreaSpinner.setAdapter(subAreaAdapter);
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
                if (getActivity() != null && catList != null && catList.size() > 0) {
                    categoryIndex = position;
                    ArrayList<String> list = new ArrayList<>();
                    if (position != 0) {
                        subCategoryList = new ArrayList<>();
                        String _id = catList.get(position).getCategoryId() + "";
                        categoryViewModel.getSubCategory(_id);

                        categoryViewModel.subCategoryList.observe(getActivity(), new Observer<ArrayList<SubCategoryModel>>() {
                            @Override
                            public void onChanged(ArrayList<SubCategoryModel> subCategoryModels) {
                                if (getActivity() != null && getContext() != null) {
                                    subCategoryList = new ArrayList<>(subCategoryModels);
                                    String curLang = "AR";
                                    curLang = Constants.getLocal(getContext());
                                    ArrayList<String> listNames = new ArrayList<>();
                                    subCategoryList.add(0, new SubCategoryModel(-1, "", ""));
                                    listNames.add(getString(R.string.choose_sub_category));
                                    for (SubCategoryModel it : subCategoryModels) {
                                        if (curLang.equals("AR"))
                                            listNames.add(it.getCubCatName());
                                        else
                                            listNames.add(it.getSubCatNameEn());
                                    }

                                    subCatAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_textview, listNames);
                                    subCatAdapter.setDropDownViewResource(R.layout.text_drop);
                                    subCategorySpinner.setAdapter(subCatAdapter);
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
                subCategoryIntex = position;
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
                    otherArea.setEnabled(true);
                    //  otherArea.setFocusable(true);
                } else {
                    subArea = "-1";
                    otherArea.setVisibility(View.GONE);
                    otherArea.setEnabled(false);
                    // otherArea.setFocusable(false);
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
                if (getActivity() != null && getContext() != null) {
                    NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                    navigationView.setCheckedItem(R.id.nav_home);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                }
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
                    String catId = catList.get(categoryIndex).getCategoryId() + "";
                    String subCatId = subCategoryList.get(subCategoryIntex).getSubCatId() + "";
                    String areaId = cityList.get(areaIndex).getCityID() + "";
                    String subAreaId = subAreaList.get(subAreaIndex).getCityID() + "";
                    String _otherArea = null;
                    if (checkBoxAddArea.isChecked()) {
                        _otherArea = otherArea.getText().toString();
                    } else
                        _otherArea = "-1";
                    UserModel userModel = Constants.getUser(getContext());

                    if (userModel == null) {
                        Toast.makeText(getContext(), "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String userId = userModel.getUserId() + "";
                    AdsModel adsModel = new AdsModel(name, catId, subCatId, userId, areaId, subAreaId, _otherArea, price, priceType);
                    Intent intent = null;
                    String productType = catList.get(categoryIndex).getIsVehicles();
                    if (productType.equals("1")) {
                        if (subCategoryList.get(subCategoryIntex).getIsCar().equals("1"))
                            intent = new Intent(getContext(), CarActivity.class);
                        else
                            intent = new Intent(getContext(), VehiclesActivity.class);
                    } else if (productType.equals("2")) {
                        intent = new Intent(getContext(), BuildingActivity.class);
                    } else if (productType.equals("0"))
                        intent = new Intent(getContext(), OtherDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ads_model", adsModel);
                    bundle.putSerializable("images", null);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUris != null && imageUris.size() >= 5) {
                    Toast.makeText(getContext(), getString(R.string.max_images_uploaded), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
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
        categoryViewModel.mutableCategoryList.observe(getActivity(), new Observer<ArrayList<CategoryModel>>() {
            @Override
            public void onChanged(ArrayList<CategoryModel> categoryModels) {
                if (getActivity() != null && getContext() != null) {
                    catList = new ArrayList<>();
                    String curLang = "AR";
                    curLang = Constants.getLocal(getContext());
                    ArrayList<String> list = new ArrayList<>();
                    for (CategoryModel it : categoryModels) {
                        catList.add(it);
                        if (curLang.equals("AR"))
                            list.add(it.getCategoryName());
                        else
                            list.add(it.getCatNameEn());
                    }
                    String all = getContext().getString(R.string.all_categories);
                    list.add(0, all);
                    catList.add(0, new CategoryModel(0, all, "0"));
                    list.remove(list.size() - 1);
                    catAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_textview, list);
                    catAdapter.setDropDownViewResource(R.layout.text_drop);
                    categorySpinner.setAdapter(catAdapter);
                }
            }
        });
        cityList = new ArrayList<>();
        categoryViewModel.getAllCities();
        categoryViewModel.mutableCityList.observe(getActivity(), new Observer<ArrayList<CityModel>>() {
            @Override
            public void onChanged(ArrayList<CityModel> cityModels) {
                if (getActivity() != null && getContext() != null) {
                    cityList = new ArrayList<>();
                    ArrayList<String> list = new ArrayList<>();
                    String curLang = "AR";
                    curLang = Constants.getLocal(getContext());
                    for (CityModel model : cityModels) {
                        cityList.add(model);
                        if (curLang.equals("AR"))
                            list.add(model.getCityName());
                        else
                            list.add(model.getAreaNameEn());
                    }
                    String all = getContext().getString(R.string.all_cities);
                    list.add(0, all);
                    cityList.add(0, new CityModel(0, all));
                    list.remove(list.size() - 1);
                    cityAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_textview, list);
                    cityAdapter.setDropDownViewResource(R.layout.text_drop);
                    areaSpinner.setAdapter(cityAdapter);
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
            Toast.makeText(getContext(), getString(R.string.choose_city), Toast.LENGTH_SHORT).show();
            valid = false;
        }


        if (categorySpinner.getSelectedItemPosition() == -1 || categorySpinner.getSelectedItemPosition() == 0) {
            categorySpinner.requestFocus();
            Toast.makeText(getContext(), getString(R.string.choose_cat), Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (subCategorySpinner.getSelectedItemPosition() == -1 || subCategorySpinner.getSelectedItemPosition() == 0) {
            subCategorySpinner.requestFocus();
            Toast.makeText(getContext(), getString(R.string.choose_sub_category), Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (imageUris == null || imageUris.size() == 0 || imageUris.size() > 5) {
            valid = false;
            Toast.makeText(getContext(), getString(R.string.max_images_uploaded), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), getString(R.string.choose_sub_area), Toast.LENGTH_SHORT).show();
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
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                progressDialog.setCancelable(false);
                for (int i = 0; i < imageUris.size(); i++) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUris.get(i));
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
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUris.get(0));
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
            GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
            recyclerViewProductImages.setLayoutManager(manager);
            imageProductAdapter = new ImageProductAdapter(false);
            imageProductAdapter.setList(imageUris);
            recyclerViewProductImages.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
            recyclerViewProductImages.setAdapter(imageProductAdapter);
        }
    }
}
