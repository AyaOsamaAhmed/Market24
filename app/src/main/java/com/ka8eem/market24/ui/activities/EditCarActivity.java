package com.ka8eem.market24.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;

import okhttp3.MultipartBody;

public class EditCarActivity extends AppCompatActivity {

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
            type, modell, desc, color;
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
    VehiclesConstantDetailsModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_car);
        intent = getIntent();
        if (intent != null) {
            bundle = intent.getExtras();
            adsModel = (AdsModel) bundle.getSerializable("ads_model");
        }
        initViews();

    }

    private void initViews() {
        valid = true;
        model = adsModel.getVehiclesConstantDetailsModel();
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        yearOfCreate = findViewById(R.id.edit_car_text_year_of_create);
        textEnginePower = findViewById(R.id.edit_car_edit_text_engine_power);
        textEnginePower.setText(model.getEnginePower());
        textKilometers = findViewById(R.id.edit_car_edit_text_kilo_meters);
        textKilometers.setText(model.getKilometers());
        btnCancel = findViewById(R.id.edit_car_btn_cancel_add_car);
        btnUpload = findViewById(R.id.edit_car_btn_upload_car);
        numberPicker = findViewById(R.id.edit_car_year_of_create);
        colorSpinner = findViewById(R.id.edit_car_color_spinner);
        //colorSpinner.setFocusable(true);
        //  colorSpinner.setFocusableInTouchMode(true);
        typeSpinner = findViewById(R.id.edit_car_type_spinner);
        // typeSpinner.setFocusable(true);
        // typeSpinner.setFocusableInTouchMode(true);
        modelSpinner = findViewById(R.id.edit_car_model_spinner);
        textDesc = findViewById(R.id.edit_car_edit_text_details);
        textDesc.setText(model.getVehicleDetails());
        // modelSpinner.setFocusable(true);
        // modelSpinner.setFocusableInTouchMode(true);
        radioGroupEngineType = findViewById(R.id.edit_car_radio_group_engine_type);
        if (model.getTransmissionType().equals("auto")) {
          //  RadioButton br = (RadioButton) radioGroupEngineType.findViewById(R.id.edit_car_radio_btn_auto);
         //   br.setChecked(true);
            radioGroupEngineType.check(R.id.edit_car_radio_btn_auto);
            trans = "auto";
        } else {
           // RadioButton br =  (RadioButton) radioGroupEngineType.findViewById(R.id.edit_car_radio_btn_manual);
            radioGroupEngineType.check(R.id.edit_car_radio_btn_manual);
           // br.setChecked(true);
            trans = "manual";
        }
        radioGroupStatus = findViewById(R.id.edit_car_radio_group_car_status);
        if (model.getVehicleStatus().equals("new")) {
          //  RadioButton br =  (RadioButton) radioGroupEngineType.findViewById(R.id.edit_car_radio_btn_new);
         //   br.setChecked(true);
            radioGroupEngineType.check(R.id.edit_car_radio_btn_new);

            carStatus = "new";
        } else {
           // RadioButton br =  (RadioButton) radioGroupEngineType.findViewById(R.id.edit_car_radio_btn_used);
           // br.setChecked(true);
            radioGroupEngineType.check(R.id.edit_car_radio_btn_used);

            carStatus = "used";
        }
        modelSpinnerContainer = findViewById(R.id.edit_car_model_spinner_container);
        numberPicker.setMinValue(1990);
        numberPicker.setMaxValue(2021);
        if(model !=null) {
            if(model.getProductionYear()!=null && !model.getProductionYear().isEmpty()){
            numberPicker.setValue(Integer.parseInt(model.getProductionYear()));
            year = model.getProductionYear();
        }else{ year = "2010";}
        }
        else
        {year = "2010";}

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
                String lang = Constants.getLocal(EditCarActivity.this);
                if (lang.equals("AR")) {
                    type = typeCarModelList.get(typeIndex).getName_type();
                    modell = modelsCarList.get(modelIndex).getName_model();
                    color = colorModelList.get(colorIndex).getName_color();
                } else {
                    type = typeCarModelList.get(typeIndex).getName_type_E();
                    modell = modelsCarList.get(modelIndex).getName_model_E();
                    color = colorModelList.get(colorIndex).getName_color_E();
                }
                if (valid) {
                    VehiclesConstantDetailsModel details = new VehiclesConstantDetailsModel(
                            type,
                            modell,
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
                    model.setID_ADS(adsModel.getID_ADS());
                    ProductViewModel viewModel = ViewModelProviders.of(EditCarActivity.this).
                            get(ProductViewModel.class);
                    viewModel.updateProduct(model);
                    final ProgressDialog progressDialog = new ProgressDialog(EditCarActivity.this);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    progressDialog.setCancelable(false);
                    viewModel._updateProduct.observe(EditCarActivity.this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            if (s != null) {
                                if (s.contains("error") || s.equals("-1") || s.equals("-1-1")) {
                                    Toast.makeText(EditCarActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    return;
                                }
                            }
                            UploadImageModel imageModel = new UploadImageModel(s, EditProductActivity.encodedImages);
                            viewModel.updateImageAsString(imageModel);
                            viewModel.updateImageAsString.observe(EditCarActivity.this, new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    if (s.contains("-1-2-3") || s.contains("error")) {
                                        Toast.makeText(EditCarActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        return;
                                    }
                                    Toast.makeText(EditCarActivity.this, getString(R.string.ads_uploaded), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                   // onBackPressed();
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
                onBackPressed();
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
                    curLang = Constants.getLocal(EditCarActivity.this);
                    for (ColorModel it : colorModels) {
                        if (curLang.equals("AR"))
                            listNames.add(it.getName_color());
                        else
                            listNames.add(it.getName_color_E());
                    }
                    colorAdapter = new ArrayAdapter<>(EditCarActivity.this, R.layout.spinner_textview, listNames);
                    colorAdapter.setDropDownViewResource(R.layout.text_drop);
                    colorSpinner.setAdapter(colorAdapter);
                    String lang = Constants.getLocal(EditCarActivity.this);
                    int idx = 0;
                    for (int i = 1; i < colorModelList.size(); i++) {
                        if (lang.equals("AR")) {
                          //  Log.e("error_color",(colorModelList.get(i).getName_color()==null?"yes":"no"));
                            String dd = model.getColor();
                            if (colorModelList.get(i).getName_color().equals(dd)) {
                                idx = i;
                                break;
                            }
                        } else {
                            if (colorModelList.get(i).getName_color_E().equals(model.getColor())) {
                                idx = i;
                                break;
                            }
                        }
                    }
                    colorSpinner.setSelection(idx);
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
                    curLang = Constants.getLocal(EditCarActivity.this);
                    for (TypeCarModel it : typeCarModels) {
                        if (curLang.equals("AR"))
                            listNames.add(it.getName_type());
                        else
                            listNames.add(it.getName_type_E());
                    }
                    typeAdapter = new ArrayAdapter<>(EditCarActivity.this, R.layout.spinner_textview, listNames);
                    typeAdapter.setDropDownViewResource(R.layout.text_drop);
                    typeSpinner.setAdapter(typeAdapter);
                    String lang = Constants.getLocal(EditCarActivity.this);
                    int idx = 0;
                    for (int i = 1; i < typeCarModelList.size(); i++) {
                        if (lang.equals("AR")) {
                            if (typeCarModelList.get(i).getName_type().equals(model.getVehicleType())) {
                                idx = i;
                                break;
                            }
                        } else {
                            if (typeCarModelList.get(i).getName_type_E().equals(model.getVehicleType())) {
                                idx = i;
                                break;
                            }
                        }
                    }
                    typeSpinner.setSelection(idx);
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
                        categoryViewModel.modelTypeList.observe(EditCarActivity.this, new Observer<ArrayList<ModelsOfCarModel>>() {
                            @Override
                            public void onChanged(ArrayList<ModelsOfCarModel> modelsOfCarModels) {
                                if (modelsOfCarModels != null) {
                                    modelsCarList = new ArrayList<>(modelsOfCarModels);

                                    ArrayList<String> listNames = new ArrayList<>();

                                    String curLang = "AR";
                                    listNames.add(getString(R.string.choose_model_Car));
                                    modelsCarList.add(0, new ModelsOfCarModel());
                                    curLang = Constants.getLocal(EditCarActivity.this);
                                    for (ModelsOfCarModel it : modelsOfCarModels) {
                                        if (curLang.equals("AR"))
                                            listNames.add(it.getName_model());
                                        else
                                            listNames.add(it.getName_model_E());
                                    }
                                    modelSpinnerContainer.setVisibility(View.VISIBLE);
                                    modelAdapter = new ArrayAdapter<>(EditCarActivity.this, R.layout.spinner_textview, listNames);
                                    modelAdapter.setDropDownViewResource(R.layout.text_drop);
                                    modelSpinner.setAdapter(modelAdapter);
                                    String lang = Constants.getLocal(EditCarActivity.this);
                                    int idx = -1;
                                    for (int i = 1; i < modelsCarList.size(); i++) {
                                        if (lang.equals("AR")) {
                                            if (modelsCarList.get(i).getName_model().equals(model.getVehicleModel())) {
                                                idx = i;
                                                break;
                                            }
                                        } else {
                                            if (modelsCarList.get(i).getName_model_E().equals(model.getVehicleModel())) {
                                                idx = i;
                                                break;
                                            }
                                        }
                                    }
                                    modelSpinner.setSelection(idx);
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
            Toast.makeText(this, getString(R.string.choose_color), Toast.LENGTH_SHORT).show();
        }
        if (modelSpinner.getSelectedItemPosition() == -1 || modelSpinner.getSelectedItemPosition() == 0) {
            modelSpinner.requestFocus();
            valid = false;
            Toast.makeText(this, getString(R.string.choose_model_Car), Toast.LENGTH_SHORT).show();
        }
        if (typeSpinner.getSelectedItemPosition() == -1 || typeSpinner.getSelectedItemPosition() == 0) {
            typeSpinner.requestFocus();
            valid = false;
            Toast.makeText(this, getString(R.string.choose_Type_Car), Toast.LENGTH_SHORT).show();
        }
    }
}