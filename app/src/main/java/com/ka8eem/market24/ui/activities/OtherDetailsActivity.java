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
import com.ka8eem.market24.util.Constants;
import com.ka8eem.market24.viewmodel.ProductViewModel;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class OtherDetailsActivity extends AppCompatActivity {

    // widgets
    EditText textName, textPrice, textDesc, textOther;
    Button btnCancel, btnUpload;

    // vars
    String name, price, desc, status;
    boolean valid;
    Intent intent;
    Bundle bundle;
    AdsModel adsModel;
    ArrayList<MultipartBody.Part> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_details);
        intent = getIntent();
        if (intent != null) {
            bundle = intent.getExtras();
            adsModel = (AdsModel) bundle.getSerializable("ads_model");
        }
        initViews();
    }

    private void initViews() {
        valid = true;
        textName = findViewById(R.id.name);
        textPrice = findViewById(R.id.price);
        textDesc = findViewById(R.id.desc);
        textOther = findViewById(R.id.other);
        btnUpload = findViewById(R.id.ok_go);
        btnCancel = findViewById(R.id.cancel);
        textName.setText(adsModel.getAdsTitle());
        String curLang = Constants.getLocal(this);
        if (curLang.equals("AR")) {
            price = adsModel.getAdsPrice() + " ل.س";
        } else {
            price = adsModel.getAdsPrice() + " L.S";
        }
        textPrice.setText(price);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valid = true;
                status = textOther.getText().toString();
                desc = textDesc.getText().toString();
                validInput();
                if (valid) {
                    OtherConstantDetailsModel detailsModel = new OtherConstantDetailsModel(
                            status,
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
                            new VehiclesConstantDetailsModel(),
                            new BuildingConstantDetailsModel(),
                            detailsModel
                    );
                    ProductViewModel viewModel = ViewModelProviders.of(OtherDetailsActivity.this).
                            get(ProductViewModel.class);
                    viewModel.uploadProduct(model);
                    final ProgressDialog progressDialog = new ProgressDialog(OtherDetailsActivity.this);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    progressDialog.setCancelable(false);
                    viewModel.mutableUploadProduct.observe(OtherDetailsActivity.this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            if (s != null) {
                                if (s.contains("error") || s.equals("-1")) {
                                    Toast.makeText(OtherDetailsActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    return;
                                }
                            }
                            Log.e("encodedImagesSize", AddProductFragment.encodedImages.size() + "");
                            UploadImageModel imageModel = new UploadImageModel(s, AddProductFragment.encodedImages);
                            viewModel.uploadImageAsString(imageModel);
                            viewModel.uploadImageAsString.observe(OtherDetailsActivity.this, new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    if (s.contains("-1-2-3") || s.contains("error")) {
                                        Toast.makeText(OtherDetailsActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        return;
                                    }
                                    Toast.makeText(OtherDetailsActivity.this, getString(R.string.ads_uploaded), Toast.LENGTH_SHORT).show();
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
        if (desc.isEmpty()) {
            textDesc.setError("");
            textDesc.requestFocus();
            valid = false;
        }
        if (status.isEmpty()) {
            textOther.setError("");
            textOther.requestFocus();
            valid = false;
        }
    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
/*
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
    }*/
}