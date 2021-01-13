package com.ka8eem.market24.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ka8eem.market24.R;
import com.ka8eem.market24.models.AdsModel;
import com.ka8eem.market24.models.BuildingConstantDetailsModel;
import com.ka8eem.market24.models.OtherConstantDetailsModel;
import com.ka8eem.market24.models.UploadImageModel;
import com.ka8eem.market24.models.VehiclesConstantDetailsModel;
import com.ka8eem.market24.ui.fragments.AddProductFragment;
import com.ka8eem.market24.viewmodel.ProductViewModel;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class VehiclesActivity extends AppCompatActivity {

    //widgets
    EditText textType, textModel, textColor, textTrans, textStatus, textDesc, textEngine, textYear, textKilometer;
    Button btnUpload, btnCancel;

    // vars
    String type, model, color, trans, status, desc, engine, year, kilometers;
    boolean valid;
    Intent intent;
    Bundle bundle;
    AdsModel adsModel;
    ArrayList<MultipartBody.Part> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);
        intent = getIntent();
        if (intent != null) {
            bundle = intent.getExtras();
            adsModel = (AdsModel) bundle.getSerializable("ads_model");
        }
        initViews();
    }

    private void initViews() {
        valid = true;
        textType = findViewById(R.id.vehicle_type);
        textModel = findViewById(R.id.vehicle_model);
        textColor = findViewById(R.id.vehicle_color);
        textTrans = findViewById(R.id.vehicle_trans_type);
        textStatus = findViewById(R.id.vehicle_status);
        textDesc = findViewById(R.id.desc);
        textEngine = findViewById(R.id.vehicle_motor_power);
        textYear = findViewById(R.id.vehicle_year);
        textKilometer = findViewById(R.id.vehicle_km);
        btnUpload = findViewById(R.id.ok_go);
        btnCancel = findViewById(R.id.cancel);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valid = true;
                type = textType.getText().toString();
                model = textModel.getText().toString();
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
                            model,
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
                    ProductViewModel viewModel = ViewModelProviders.of(VehiclesActivity.this).
                            get(ProductViewModel.class);
                    viewModel.uploadProduct(model);
                    final ProgressDialog progressDialog = new ProgressDialog(VehiclesActivity.this);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    progressDialog.setCancelable(false);
                    viewModel.mutableUploadProduct.observe(VehiclesActivity.this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            if (s != null) {
                                if (s.contains("error") || s.equals("-1")) {
                                    Toast.makeText(VehiclesActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    return;
                                }
                            }
                            UploadImageModel imageModel = new UploadImageModel(s, AddProductFragment.encodedImages);
                            viewModel.uploadImageAsString(imageModel);
                            viewModel.uploadImageAsString.observe(VehiclesActivity.this, new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    if (s.contains("-1-2-3") || s.contains("error")) {
                                        Toast.makeText(VehiclesActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        return;
                                    }
                                    Toast.makeText(VehiclesActivity.this, getString(R.string.ads_uploaded), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    startHomeActivity();
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
                startHomeActivity();
            }
        });
    }

    private void validInput() {
        if (type.isEmpty()) {
            textType.setError("");
            textType.requestFocus();
            valid = false;
        }
        if (model.isEmpty()) {
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