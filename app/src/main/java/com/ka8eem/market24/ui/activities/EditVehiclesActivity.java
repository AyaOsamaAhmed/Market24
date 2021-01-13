package com.ka8eem.market24.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ka8eem.market24.R;
import com.ka8eem.market24.models.AdsModel;
import com.ka8eem.market24.models.BuildingConstantDetailsModel;
import com.ka8eem.market24.models.OtherConstantDetailsModel;
import com.ka8eem.market24.models.UploadImageModel;
import com.ka8eem.market24.models.VehiclesConstantDetailsModel;
import com.ka8eem.market24.ui.fragments.AddProductFragment;
import com.ka8eem.market24.viewmodel.ProductViewModel;

public class EditVehiclesActivity extends AppCompatActivity {

    //widgets
    EditText textType, textModel, textColor, textTrans, textStatus, textDesc, textEngine, textYear, textKilometer;
    Button btnUpload, btnCancel;

    // vars
    String type, modell, color, trans, status, desc, engine, year, kilometers;
    boolean valid;
    Intent intent;
    Bundle bundle;
    AdsModel adsModel;
    VehiclesConstantDetailsModel  model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vehicles);
        intent = getIntent();
        if (intent != null) {
            bundle = intent.getExtras();
            adsModel = (AdsModel) bundle.getSerializable("ads_model");
        }
        initViews();
    }

    private void initViews() {
        model = adsModel.getVehiclesConstantDetailsModel();
        valid = true;

        textType = findViewById(R.id.edit_vehicle_type);
        textModel = findViewById(R.id.edit_vehicle_model);
        textColor = findViewById(R.id.edit_vehicle_color);
        textTrans = findViewById(R.id.edit_vehicle_trans_type);
        textStatus = findViewById(R.id.edit_vehicle_status);
        textDesc = findViewById(R.id.edit_desc);
        textEngine = findViewById(R.id.edit_vehicle_motor_power);
        textYear = findViewById(R.id.edit_vehicle_year);
        textKilometer = findViewById(R.id.edit_vehicle_km);

        btnUpload = findViewById(R.id.edit_ok_go);
        btnCancel = findViewById(R.id.edit_cancel);
        if(model != null){
            textKilometer.setText(model.getKilometers());
            textYear.setText(model.getProductionYear());
            textEngine.setText(model.getEnginePower());
            textType.setText(model.getVehicleType());
            textModel.setText(model.getVehicleModel());
            textColor.setText(model.getColor());
            textTrans.setText(model.getTransmissionType());
            textStatus.setText(model.getVehicleStatus());
            textStatus.setText(model.getVehicleStatus());
            textEngine.setText(model.getEnginePower());


        }
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valid = true;
                type = textType.getText().toString();
                modell = textModel.getText().toString();
                color = textColor.getText().toString();
                trans = textTrans.getText().toString();
                status = textStatus.getText().toString();
                desc = textDesc.getText().toString();
                engine = textEngine.getText().toString();
                year = textYear.getText().toString();
                kilometers = textKilometer.getText().toString();
                validInput();
                if (valid) {
                    VehiclesConstantDetailsModel details = new VehiclesConstantDetailsModel(
                            type,
                            modell,
                            year,
                            trans,
                            status,
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
                    ProductViewModel viewModel = ViewModelProviders.of(EditVehiclesActivity.this).
                            get(ProductViewModel.class);
                    viewModel.updateProduct(model);
                    final ProgressDialog progressDialog = new ProgressDialog(EditVehiclesActivity.this);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    progressDialog.setCancelable(false);
                    viewModel._updateProduct.observe(EditVehiclesActivity.this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            if (s != null) {
                                if (s.contains("error") || s.equals("-1") || s.equals("-1-1")) {
                                    Toast.makeText(EditVehiclesActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    return;
                                }
                            }
                            UploadImageModel imageModel = new UploadImageModel(s, EditProductActivity.encodedImages);
                            viewModel.updateImageAsString(imageModel);
                            viewModel.updateImageAsString.observe(EditVehiclesActivity.this, new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    if (s.contains("-1-2-3") || s.contains("error")) {
                                        Toast.makeText(EditVehiclesActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        return;
                                    }
                                    Toast.makeText(EditVehiclesActivity.this, getString(R.string.ads_uploaded), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    onBackPressed();
                                }
                            });

                        }
                    });
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

    private void validInput() {
        if (type.isEmpty()) {
            textType.setError("");
            textType.requestFocus();
            valid = false;
        }
        if (modell.isEmpty()) {
            textModel.setError("");
            textModel.requestFocus();
            valid = false;
        }
        if (trans.isEmpty()) {
            textTrans.setError("");
            textTrans.requestFocus();
            valid = false;
        }
        if (color.isEmpty()) {
            textColor.setError("");
            textColor.requestFocus();
            valid = false;
        }
        if (desc.isEmpty()) {
            textDesc.setError("");
            textDesc.requestFocus();
            valid = false;
        }
        if (engine.isEmpty()) {
            textEngine.setError("");
            textEngine.requestFocus();
            valid = false;
        }
        if (status.isEmpty()) {
            textStatus.setError("");
            textStatus.requestFocus();
            valid = false;
        }

        if (year.isEmpty()) {
            textYear.setError("");
            textYear.requestFocus();
            valid = false;
        }
        if (kilometers.isEmpty()) {
            textKilometer.setError("");
            textKilometer.requestFocus();
            valid = false;
        }
    }
}