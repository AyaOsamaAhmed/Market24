package com.ka8eem.market24.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
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
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class BuildingActivity extends AppCompatActivity {

    // widgets
    Button btnUpload, btnCancel;
    EditText textNumberOfRooms, textSpace, textNumberOfBathroom, textFinishing, textLuxuries, textFloorNumber, textFurnished, textDetails;

    // vars
    String numOfRooms, space, numOfBathroom, finishing, luxuries, floorNum, furnished, desc;
    boolean valid;
    Intent intent;
    Bundle bundle;
    AdsModel adsModel;
    ArrayList<MultipartBody.Part> images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
        intent = getIntent();
        if (intent != null) {
            bundle = intent.getExtras();
            adsModel = (AdsModel) bundle.getSerializable("ads_model");
        }
        initViews();
    }

    private void initViews() {
        valid = true;
        btnUpload = findViewById(R.id.upload_building);
        btnCancel = findViewById(R.id.cancel_building);
        textNumberOfRooms = findViewById(R.id.building_room_number);
        textNumberOfBathroom = findViewById(R.id.building_bathroom_number);
        textSpace = findViewById(R.id.building_space);
        textDetails = findViewById(R.id.desc);
        textFinishing = findViewById(R.id.building_finish);
        textLuxuries = findViewById(R.id.building_luxuries);
        textFloorNumber = findViewById(R.id.building_floor);
        textFurnished = findViewById(R.id.building_furnished);
        /*
         * images to uploaded
         * */
        btnUpload.setOnClickListener(new View.OnClickListener() {
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
                ProductViewModel viewModel = ViewModelProviders.of(BuildingActivity.this).
                        get(ProductViewModel.class);
                viewModel.uploadProduct(model);
                final ProgressDialog progressDialog = new ProgressDialog(BuildingActivity.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                progressDialog.setCancelable(false);
                viewModel.mutableUploadProduct.observe(BuildingActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {

                        if (s != null) {
                            if (s.contains("error") || s.equals("-1")) {
                                Toast.makeText(BuildingActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                return;
                            }
                        }
                        UploadImageModel imageModel = new UploadImageModel(s, AddProductFragment.encodedImages);
                        viewModel.uploadImageAsString(imageModel);
                        viewModel.uploadImageAsString.observe(BuildingActivity.this, new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                if (s.contains("-1-2-3") || s.contains("error")) {
                                    Toast.makeText(BuildingActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    return;
                                }
                                Toast.makeText(BuildingActivity.this, getString(R.string.ads_uploaded), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                startHomeActivity();
                            }
                        });

                    }
                });
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
        final ProgressDialog progressDialog = new ProgressDialog(BuildingActivity.this);
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