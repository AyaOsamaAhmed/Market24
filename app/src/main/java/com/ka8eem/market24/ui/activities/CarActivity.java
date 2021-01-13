package com.ka8eem.market24.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ka8eem.market24.R;
import com.ka8eem.market24.models.AdsModel;
import com.ka8eem.market24.models.BuildingConstantDetailsModel;
import com.ka8eem.market24.models.ColorModel;
import com.ka8eem.market24.models.ModelsOfCarModel;
import com.ka8eem.market24.models.OtherConstantDetailsModel;
import com.ka8eem.market24.models.TypeCarModel;
import com.ka8eem.market24.models.UploadImageModel;
import com.ka8eem.market24.models.VehiclesConstantDetailsModel;
import com.ka8eem.market24.ui.fragments.AddProductFragment;
import com.ka8eem.market24.util.Constants;
import com.ka8eem.market24.viewmodel.CategoryViewModel;
import com.ka8eem.market24.viewmodel.ProductViewModel;

import java.io.File;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CarActivity extends AppCompatActivity {

    // widget
    NumberPicker numberPicker;
    EditText textKilometers, textEnginePower, textDesc;
    TextView yearOfCreate;
    Button btnUpload, btnCancel;
    Spinner colorSpinner, modelSpinner, typeSpinner;
    RadioGroup radioGroupStatus, radioGroupEngineType;
    RelativeLayout modelSpinnerContainer;

    // vars
    String year, kilometers, engine, trans = "N/A", carStatus = "N/A",
            type, model, desc, color;
    CategoryViewModel categoryViewModel;
    ArrayList<ColorModel> colorModelList = new ArrayList<>();
    ArrayList<TypeCarModel> typeCarModelList = new ArrayList<>();
    ArrayList<ModelsOfCarModel> modelsCarList = new ArrayList<>();
    ArrayAdapter<String> colorAdapter, modelAdapter, typeAdapter;
    int colorIndex, typeIndex, modelIndex;
    boolean valid;
    Intent intent;
    Bundle bundle;
    AdsModel adsModel;
    ArrayList<MultipartBody.Part> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        intent = getIntent();
        if (intent != null) {
            bundle = intent.getExtras();
            adsModel = (AdsModel) bundle.getSerializable("ads_model");
        }
        initViews();

    }

    private void initViews() {
        valid = true;
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        yearOfCreate = findViewById(R.id.text_year_of_create);
        textEnginePower = findViewById(R.id.edit_text_engine_power);
        textKilometers = findViewById(R.id.edit_text_kilo_meters);
        btnCancel = findViewById(R.id.btn_cancel_add_car);
        btnUpload = findViewById(R.id.btn_upload_car);
        numberPicker = findViewById(R.id.year_of_create);
        colorSpinner = findViewById(R.id.color_spinner);
        //colorSpinner.setFocusable(true);
      //  colorSpinner.setFocusableInTouchMode(true);
        typeSpinner = findViewById(R.id.type_spinner);
       // typeSpinner.setFocusable(true);
       // typeSpinner.setFocusableInTouchMode(true);
        modelSpinner = findViewById(R.id.model_spinner);
        textDesc = findViewById(R.id.edit_text_details);
       // modelSpinner.setFocusable(true);
       // modelSpinner.setFocusableInTouchMode(true);
        radioGroupEngineType = findViewById(R.id.radio_group_engine_type);
        radioGroupStatus = findViewById(R.id.radio_group_car_status);
        modelSpinnerContainer = findViewById(R.id.model_spinner_container);
        numberPicker.setMinValue(1990);
        numberPicker.setMaxValue(2021);
        numberPicker.setValue(2010);
        year = "2010";
        String text = getString(R.string.year_of_create);
        yearOfCreate.setText(text + ": " + year);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                year = newVal + "";
                yearOfCreate.setText(text + ": " + year);
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // upload product
                engine = textEnginePower.getText().toString();
                kilometers = textKilometers.getText().toString();
                desc = textDesc.getText().toString();
                valid = true;
                validInput();
                String lang = Constants.getLocal(CarActivity.this);
                if (lang.equals("AR")) {
                    type = typeCarModelList.get(typeIndex).getName_type();
                    model = modelsCarList.get(modelIndex).getName_model();
                    color = colorModelList.get(colorIndex).getName_color();
                } else {
                    type = typeCarModelList.get(typeIndex).getName_type_E();
                    model = modelsCarList.get(modelIndex).getName_model_E();
                    color = colorModelList.get(colorIndex).getName_color_E();
                }
                if (valid) {
                    VehiclesConstantDetailsModel details = new VehiclesConstantDetailsModel(
                            type,
                            model,
                            year,
                            trans,
                            carStatus,
                            engine,
                            kilometers,
                            color,
                            desc
                    );
                    AdsModel model = new AdsModel(
                            adsModel.getAdsTitle(),
                            adsModel.getAdsCatId(),
                            adsModel.getAdsSubCategoryId(),
                            adsModel.getAdsUserId(),
                            adsModel.getAdsAreaId(),
                            adsModel.getAdsSubAreaId(),
                            adsModel.getOtherArea(),
                            adsModel.getAdsPrice(),
                            adsModel.getType_price(),
                            details,
                            new BuildingConstantDetailsModel(),
                            new OtherConstantDetailsModel()
                    );
                    ProductViewModel viewModel = ViewModelProviders.of(CarActivity.this).
                            get(ProductViewModel.class);
                    viewModel.uploadProduct(model);
                    final ProgressDialog progressDialog = new ProgressDialog(CarActivity.this);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    progressDialog.setCancelable(false);
                    viewModel.mutableUploadProduct.observe(CarActivity.this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            if (s != null) {
                                if (s.contains("error") || s.equals("-1")) {
                                    Toast.makeText(CarActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    return;
                                }
                            }
                            UploadImageModel imageModel = new UploadImageModel(s, AddProductFragment.encodedImages);
                            viewModel.uploadImageAsString(imageModel);
                            viewModel.uploadImageAsString.observe(CarActivity.this, new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    if (s.contains("-1-2-3") || s.contains("error")) {
                                        Toast.makeText(CarActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        return;
                                    }
                                    Toast.makeText(CarActivity.this, getString(R.string.ads_uploaded), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    startHomeActivity();
                                }
                            });
                        }
                    });
                }
            }
        });
        initSpinners();
        radioGroupStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_btn_new:
                        carStatus = "new";
                        break;
                    case R.id.radio_btn_used:
                        carStatus = "used";
                        break;
                }
            }
        });
        radioGroupEngineType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_btn_auto:
                        trans = "auto";
                        break;
                    case R.id.radio_btn_manual:
                        trans = "manual";
                        break;
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHomeActivity();
            }
        });
    }

    private void initSpinners() {
        colorModelList = new ArrayList<>();
        typeCarModelList = new ArrayList<>();
        modelsCarList = new ArrayList<>();
        categoryViewModel.getAllColorCar();
        categoryViewModel.getAllTypeCar();
        categoryViewModel.colorList.observe(this, new Observer<ArrayList<ColorModel>>() {
            @Override
            public void onChanged(ArrayList<ColorModel> colorModels) {
                if (colorModels != null) {
                    colorModelList = new ArrayList<>(colorModels);
                    ArrayList<String> listNames = new ArrayList<>();
                    String curLang = "AR";
                    listNames.add(getString(R.string.choose_color));
                    colorModelList.add(0, new ColorModel());
                    curLang = Constants.getLocal(CarActivity.this);
                    for (ColorModel it : colorModels) {
                        if (curLang.equals("AR"))
                            listNames.add(it.getName_color());
                        else
                            listNames.add(it.getName_color_E());
                    }
                    colorAdapter = new ArrayAdapter<>(CarActivity.this, R.layout.spinner_textview, listNames);
                    colorAdapter.setDropDownViewResource(R.layout.text_drop);
                    colorSpinner.setAdapter(colorAdapter);
                }
            }
        });
        categoryViewModel.typecarList.observe(this, new Observer<ArrayList<TypeCarModel>>() {
            @Override
            public void onChanged(ArrayList<TypeCarModel> typeCarModels) {
                if (typeCarModels != null) {
                    typeCarModelList = new ArrayList<>(typeCarModels);
                    ArrayList<String> listNames = new ArrayList<>();
                    listNames.add(getString(R.string.choose_Type_Car));
                    String curLang = "AR";
                    typeCarModelList.add(0, new TypeCarModel());
                    curLang = Constants.getLocal(CarActivity.this);
                    for (TypeCarModel it : typeCarModels) {
                        if (curLang.equals("AR"))
                            listNames.add(it.getName_type());
                        else
                            listNames.add(it.getName_type_E());
                    }
                    typeAdapter = new ArrayAdapter<>(CarActivity.this, R.layout.spinner_textview, listNames);
                    typeAdapter.setDropDownViewResource(R.layout.text_drop);
                    typeSpinner.setAdapter(typeAdapter);
                }
            }
        });

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getContext() != null) {
                    typeIndex = position;
                    if (position > 0) {
                        String _id = typeCarModelList.get(position).getId_type() + "";

                        categoryViewModel.getModelsCar(_id);
                        categoryViewModel.modelTypeList.observe(CarActivity.this, new Observer<ArrayList<ModelsOfCarModel>>() {
                            @Override
                            public void onChanged(ArrayList<ModelsOfCarModel> modelsOfCarModels) {
                                if (modelsOfCarModels != null) {
                                    modelsCarList = new ArrayList<>(modelsOfCarModels);
                                    ArrayList<String> listNames = new ArrayList<>();
                                    String curLang = "AR";
                                    listNames.add(getString(R.string.choose_model_Car));
                                    modelsCarList.add(0, new ModelsOfCarModel());
                                    curLang = Constants.getLocal(CarActivity.this);
                                    for (ModelsOfCarModel it : modelsOfCarModels) {
                                        if (curLang.equals("AR"))
                                            listNames.add(it.getName_model());
                                        else
                                            listNames.add(it.getName_model_E());
                                    }
                                    modelSpinnerContainer.setVisibility(View.VISIBLE);
                                    modelAdapter = new ArrayAdapter<>(CarActivity.this, R.layout.spinner_textview, listNames);
                                    modelAdapter.setDropDownViewResource(R.layout.text_drop);
                                    modelSpinner.setAdapter(modelAdapter);
                                }
                            }
                        });
                    } else
                        modelSpinnerContainer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getContext() != null) {
                    colorIndex = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getContext() != null) {
                    modelIndex = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void validInput() {
        if (desc == null || desc.isEmpty()) {
            textDesc.setError("");
            textDesc.requestFocus();
            valid = false;
        }
        if (engine.isEmpty()) {
            textEnginePower.setError("");
            textEnginePower.requestFocus();
            valid = false;
        }
        if (kilometers.isEmpty()) {
            textKilometers.setError("");
            textKilometers.requestFocus();
            valid = false;
        }
        if (radioGroupEngineType.getCheckedRadioButtonId() == -1) {
            valid = false;
            radioGroupEngineType.requestFocus();
            Toast.makeText(this, getString(R.string.choose_trans), Toast.LENGTH_SHORT).show();
        }
        if (radioGroupStatus.getCheckedRadioButtonId() == -1) {
            valid = false;
            radioGroupStatus.requestFocus();
            Toast.makeText(this, getString(R.string.choose_status), Toast.LENGTH_SHORT).show();
        }
        if (colorSpinner.getSelectedItemPosition() == -1 || colorSpinner.getSelectedItemPosition() == 0) {
            colorSpinner.requestFocus();
            valid = false;
            Toast.makeText(CarActivity.this, getString(R.string.choose_color), Toast.LENGTH_SHORT).show();
        }
        if (modelSpinner.getSelectedItemPosition() == -1 || modelSpinner.getSelectedItemPosition() == 0) {
            modelSpinner.requestFocus();
            valid = false;
            Toast.makeText(CarActivity.this, getString(R.string.choose_model_Car), Toast.LENGTH_SHORT).show();
        }
        if (typeSpinner.getSelectedItemPosition() == -1 || typeSpinner.getSelectedItemPosition() == 0) {
            typeSpinner.requestFocus();
            valid = false;
            Toast.makeText(CarActivity.this, getString(R.string.choose_Type_Car), Toast.LENGTH_SHORT).show();
        }

    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @NonNull
    private RequestBody createPartFromString(String id) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, id);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        File file = new File(fileUri.getPath());
        Log.e("before compress", file.length() + "");

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/*"),
                        file);
        //Log.e("after compress", compressed.length() + "");
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private void createImages() {
        // Map is used to multipart the file using okhttp3.RequestBody
        ArrayList<Uri> uris = AddProductFragment.imageUris;
        images = new ArrayList<>();
        int i = 0;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        for (Uri uri : uris) {
            File file = new File(uri.getPath());

            String fileName = file.getName();
            //very important files[]
            MultipartBody.Part imageRequest = prepareFilePart("file[]", uri);
            images.add(imageRequest);
        }
        progressDialog.dismiss();
    }
}