package com.ka8eem.market24.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.ka8eem.market24.util.Constants;
import com.ka8eem.market24.viewmodel.ProductViewModel;

public class EditOtherDetailsActivity extends AppCompatActivity {

    // widgets
    EditText textName, textPrice, textDesc, textOther;
    Button btnCancel, btnUpload;

    // vars
    String name, price, desc, status;
    boolean valid;
    Intent intent;
    Bundle bundle;
    AdsModel adsModel;
    OtherConstantDetailsModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_other_details);
        intent = getIntent();
        if (intent != null) {
            bundle = intent.getExtras();
            adsModel = (AdsModel) bundle.getSerializable("ads_model");
        }
        initViews();
    }

    private void initViews() {
        valid = true;
        textName = findViewById(R.id.edit_other_name);
        textPrice = findViewById(R.id.edit_other_price);
        textDesc = findViewById(R.id.edit_other_desc);
        textOther = findViewById(R.id.edit_other);
        btnUpload = findViewById(R.id.edit_other_ok_go);
        btnCancel = findViewById(R.id.edit_other_cancel);
        textName.setText(adsModel.getAdsTitle());
        String curLang = Constants.getLocal(this);
        if (curLang.equals("AR")) {
            price = adsModel.getAdsPrice() + " ل.س";
        } else {
            price = adsModel.getAdsPrice() + " L.S";
        }

        model = adsModel.getOtherConstantDetailsModel();
        textPrice.setText(price);
        if(model != null) {

            textDesc.setText(model.getDetails());
            textOther.setText(model.getStatus());
        }
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
                    model.setID_ADS(adsModel.getID_ADS());
                    ProductViewModel viewModel = ViewModelProviders.of(EditOtherDetailsActivity.this).
                            get(ProductViewModel.class);
                    viewModel.updateProduct(model);
                    final ProgressDialog progressDialog = new ProgressDialog(EditOtherDetailsActivity.this);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    progressDialog.setCancelable(false);
                    viewModel._updateProduct.observe(EditOtherDetailsActivity.this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            if (s != null) {
                                if (s.contains("error") || s.equals("-1") || s.equals("-1-1")) {
                                    Toast.makeText(EditOtherDetailsActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    return;
                                }
                            }
                            Log.e("encodedImagesSize", EditProductActivity.encodedImages.size() + "");
                            UploadImageModel imageModel = new UploadImageModel(s, EditProductActivity.encodedImages);
                            viewModel.updateImageAsString(imageModel);
                            viewModel.updateImageAsString.observe(EditOtherDetailsActivity.this, new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    if (s.contains("-1-2-3") || s.contains("error")) {
                                        Toast.makeText(EditOtherDetailsActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        return;
                                    }
                                    Toast.makeText(EditOtherDetailsActivity.this, getString(R.string.ads_uploaded), Toast.LENGTH_SHORT).show();
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

}