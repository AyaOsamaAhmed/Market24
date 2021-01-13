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
import com.ka8eem.market24.models.UploadImageModel;
import com.ka8eem.market24.ui.fragments.AddProductFragment;
import com.ka8eem.market24.viewmodel.ProductViewModel;

import java.util.ArrayList;

import okhttp3.MultipartBody;

public class EditBuildingActivity extends AppCompatActivity {

    // widgets
    Button btnEdit, btnCancel;
    EditText textNumberOfRooms, textSpace, textNumberOfBathroom, textFinishing, textLuxuries, textFloorNumber, textFurnished, textDetails;

    // vars
    String numOfRooms, space, numOfBathroom, finishing, luxuries, floorNum, furnished, desc;
    boolean valid;
    Intent intent;
    Bundle bundle;
    AdsModel adsModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_building);
        intent = getIntent();
        if (intent != null) {
            bundle = intent.getExtras();
            adsModel = (AdsModel) bundle.getSerializable("ads_model");
        }
        initViews();
    }

    private void initViews() {
        BuildingConstantDetailsModel model = adsModel.getBuildingConstantDetailsModel();
        valid = true;
        btnEdit = findViewById(R.id.upload_edit_building);
        btnCancel = findViewById(R.id.cancel_edit_building);
        textNumberOfRooms = findViewById(R.id.edit_building_room_number);
        textNumberOfBathroom = findViewById(R.id.edit_building_bathroom_number);
        textSpace = findViewById(R.id.edit_building_space);
        textDetails = findViewById(R.id.edit_desc);
        textFinishing = findViewById(R.id.edit_building_finish);
        textLuxuries = findViewById(R.id.edit_building_luxuries);
        textFloorNumber = findViewById(R.id.edit_building_floor);
        textFurnished = findViewById(R.id.edit_building_furnished);
if(model != null)
{
    textNumberOfRooms.setText(model.getNumOfRooms());
    textNumberOfBathroom.setText(model.getNumOfBathroom());
    textSpace.setText(model.getSpace());
    textDetails.setText(model.getDetails());
    textFinishing.setText(model.getFinishing());
    textLuxuries.setText(model.getLux());
    textFloorNumber.setText(model.getFloorNumber());
    textFurnished.setText(model.getFurnished());
}

        /*
         * images to uploaded
         * */
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numOfRooms = textNumberOfRooms.getText().toString().trim();
                space = textSpace.getText().toString().trim();
                numOfBathroom = textNumberOfBathroom.getText().toString().trim();
                finishing = textFinishing.getText().toString();
                luxuries = textLuxuries.getText().toString();
                floorNum = textFloorNumber.getText().toString().trim();
                furnished = textFurnished.getText().toString();
                desc = textDetails.getText().toString();
                valid = true;
                validInput();
                if (!valid)
                    return;
                BuildingConstantDetailsModel details = new BuildingConstantDetailsModel(
                        space,
                        numOfRooms,
                        numOfBathroom,
                        finishing,
                        luxuries,
                        floorNum,
                        furnished,
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
                        null,
                        details,
                        null
                );
                model.setID_ADS(adsModel.getID_ADS());
                ProductViewModel viewModel = ViewModelProviders.of(EditBuildingActivity.this).
                        get(ProductViewModel.class);
                viewModel.updateProduct(model);
                final ProgressDialog progressDialog = new ProgressDialog(EditBuildingActivity.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                progressDialog.setCancelable(false);
                viewModel._updateProduct.observe(EditBuildingActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {

                        if (s != null) {
                            if (s.contains("error") || s.equals("-1") || s.equals("-1-1")) {
                                Toast.makeText(EditBuildingActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                return;
                            }
                        }
                        UploadImageModel imageModel = new UploadImageModel(s, EditProductActivity.encodedImages);
                        viewModel.updateImageAsString(imageModel);
                        viewModel.updateImageAsString.observe(EditBuildingActivity.this, new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                if (s.contains("-1-2-3") || s.contains("error")) {
                                    Toast.makeText(EditBuildingActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    return;
                                }
                                Toast.makeText(EditBuildingActivity.this, getString(R.string.ads_uploaded), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                onBackPressed();
                            }
                        });

                    }
                });
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
        if (numOfRooms.isEmpty()) {
            textNumberOfRooms.setError("");
            textNumberOfRooms.requestFocus();
            valid = false;
        }
        if (numOfBathroom.isEmpty()) {
            textNumberOfBathroom.setError("");
            textNumberOfBathroom.requestFocus();
            valid = false;
        }

        if (space.isEmpty()) {
            textSpace.setError("");
            textSpace.requestFocus();
            valid = false;
        }

        if (luxuries.isEmpty()) {
            textLuxuries.setError("");
            textLuxuries.requestFocus();
            valid = false;
        }
        if (finishing.isEmpty()) {
            textFinishing.setError("");
            textFinishing.requestFocus();
            valid = false;
        }

        if (furnished.isEmpty()) {
            textFurnished.setError("");
            textFurnished.requestFocus();
            valid = false;
        }
        if (floorNum.isEmpty()) {
            textFloorNumber.setError("");
            textFloorNumber.requestFocus();
            valid = false;
        }
        if (desc.isEmpty()) {
            textDetails.setError("");
            textDetails.requestFocus();
            valid = false;
        }
    }
}