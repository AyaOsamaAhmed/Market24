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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.navigation.NavigationView;
import com.ka8eem.market24.R;
import com.ka8eem.market24.adapters.ImageProductAdapter;
import com.ka8eem.market24.databinding.FragmentAddProductBinding;
import com.ka8eem.market24.models.AdsModel;
import com.ka8eem.market24.models.CategoryModel;
import com.ka8eem.market24.models.AreaModel;
import com.ka8eem.market24.models.MainModel;
import com.ka8eem.market24.models.SubAreaModel;
import com.ka8eem.market24.models.SubCategoryModel;
import com.ka8eem.market24.models.UserModel;
import com.ka8eem.market24.ui.activities.HomeActivity;
import com.ka8eem.market24.util.Constants;
import com.ka8eem.market24.util.Keys;
import com.ka8eem.market24.viewmodel.CategoryViewModel;
import com.ka8eem.market24.viewmodel.ProductViewModel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.MultipartBody;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class AddProductFragment extends Fragment {

    public AddProductFragment() {
        // Required empty public constructor
    }

    FragmentAddProductBinding fragmentAddProductBinding ;
    LinearLayout searchView;
    private DrawerLayout drawerLayout;
    // vars
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String curLang = "AR";
    public static ArrayList<Uri> imageUris;
    public static ArrayList<File> imageFiles;

    int areaIndex, subAreaIndex, categoryIndex, subCategoryIntex;
    ImageProductAdapter imageProductAdapter;
    CategoryViewModel categoryViewModel;
    ProductViewModel  viewModel ;
    ArrayList<CategoryModel> catList = new ArrayList<>();
    ArrayList<AreaModel> areaList = new ArrayList<>();
    ArrayAdapter<String> catAdapter, cityAdapter, subAreaAdapter, subCatAdapter;
    ArrayList<SubCategoryModel> subCategoryList = new ArrayList<>();
    ArrayList<SubAreaModel> subAreaList = new ArrayList<>();
    public static ArrayList<MultipartBody.Part> images;
    public static ArrayList<String> encodedImages;
      ProgressDialog progressDialog ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentAddProductBinding =  FragmentAddProductBinding.inflate(inflater, container, false);
        initViews();
        return fragmentAddProductBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //You need to add the following line for this solution to work; thanks skayred
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    //  FragmentTransaction tx = getFragmentManager().beginTransaction();
                    //  tx.replace(R.id.fragment_container, new AddProductFragment() ).addToBackStack( "tag" ).commit();

                    return true;
                }
                return false;
            }
        } );

    }

    private void initViews() {

        searchView = (LinearLayout) getActivity().findViewById(R.id.relative1);
        searchView.setVisibility(View.GONE);
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        //-------------------
        imageUris = new ArrayList<>();
        imageFiles =new ArrayList<>();
        encodedImages = new ArrayList<>();
        preferences = getContext().getSharedPreferences(Constants.SHARED, Context.MODE_PRIVATE);
        editor = preferences.edit();
        curLang = Constants.getLocal(getContext());
        //-----------------
        areaIndex = subAreaIndex = categoryIndex = subCategoryIntex = -1;
        categoryViewModel = ViewModelProviders.of(getActivity()).get(CategoryViewModel.class);
        viewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);
        //-------------
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        //----------------
        if(! preferences.getString(Keys.longtitude,"").equals("")){
            fragmentAddProductBinding.editLocation.setText(preferences.getString(Keys.latitude,"") +"--"+preferences.getString(Keys.longtitude,""));
        }
        //---------
        categoryViewModel.subCategoryList.observe(getActivity(), new Observer<List<SubCategoryModel>>() {
            @Override
            public void onChanged(List<SubCategoryModel> subCategoryModels) {
                if (getActivity() != null && getContext() != null) {
                    subCategoryList = new ArrayList<>(subCategoryModels);
                    ArrayList<String> listNames = new ArrayList<>();
                    // subCategoryList.add(0, new SubCategoryModel(-1, "", ""));
                    //  listNames.add(getString(R.string.choose_sub_category));
                    for (SubCategoryModel it : subCategoryModels) {
                        if (curLang.equals("AR"))
                            listNames.add(it.getCubCatName());
                        else
                            listNames.add(it.getSubCatNameEn());
                    }

                    subCatAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_textview, listNames);
                    subCatAdapter.setDropDownViewResource(R.layout.text_drop);
                    fragmentAddProductBinding.subCategorySpinner.setAdapter(subCatAdapter);
                    subCategoryIntex = subCategoryList.get(0).getSub_id();
                }
            }
        });

        categoryViewModel.subAreaList.observe(getActivity(), new Observer<List<SubAreaModel>>() {
                    @Override
                    public void onChanged(List<SubAreaModel> subAreaModels) {
                        if (getActivity() != null && getContext() != null) {
                            subAreaList = new ArrayList<>(subAreaModels);
                            List<String> listNames = new ArrayList<>();
                            //  listNames.add(getString(R.string.choose_sub_area));
                            // subAreaList.add(0, new AreaModel(-1, ""));

                            for (SubAreaModel it : subAreaModels) {
                                if (curLang.equals("AR"))
                                    listNames.add(it.getSubAreaName());
                                else
                                    listNames.add(it.getSubAreaNameEn());
                            }
                            subAreaAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_textview, listNames);
                            subAreaAdapter.setDropDownViewResource(R.layout.text_drop);
                         //   fragmentAddProductBinding.subAreaSpinner.setAdapter(subAreaAdapter);
                            subAreaIndex  = subAreaList.get(0).getSubAreaID();
                        }
                    }
                }
        );

        //------------
     /*   fragmentAddProductBinding.areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (getActivity() != null && areaList != null && areaList.size() > 0) {
                    areaIndex = position;
                    if (position != 0) {
                        areaIndex = areaList.get(position).getAreaID() ;
                        subAreaList = new ArrayList<>();
                        categoryViewModel.getSubArea(areaIndex+"");
                     }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

      */

        fragmentAddProductBinding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (getActivity() != null && catList != null && catList.size() > 0) {
                    categoryIndex = position;
                    ArrayList<String> list = new ArrayList<>();
                    if (position != 0) {
                        subCategoryList = new ArrayList<>();
                        categoryIndex = catList.get(position).getCategoryId() ;
                        categoryViewModel.getSubCategory(categoryIndex+"");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fragmentAddProductBinding.subCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subCategoryIntex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

/*        fragmentAddProductBinding.subAreaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
                subAreaIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

 */

        fragmentAddProductBinding.editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null && getContext() != null) {
                    NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                    navigationView.setCheckedItem(R.id.nav_home);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();
                }
            }
        });

        fragmentAddProductBinding.btnPublishProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validInput()) {
                    progressDialog.show();
                    UserModel userModel = Constants.getUser(getContext());

                    String userId = userModel.getUserId() + "";
                   String price = fragmentAddProductBinding.editPrice.getText().toString().trim();
                   String name = fragmentAddProductBinding.editProductName.getText().toString();
                    String catId = catList.get(categoryIndex).getCategoryId() + "";
                    String subCatId = subCategoryList.get(subCategoryIntex).getSubCatId() + "";
                    String areaId = areaList.get(areaIndex).getAreaID() + "";
                    String subAreaId = subAreaList.get(subAreaIndex).getSubAreaID() + "";
                    String description = fragmentAddProductBinding.editDescription.getText().toString();
                    String latitude = preferences.getString(Keys.latitude,"");
                    String longtitude =preferences.getString(Keys.longtitude,"");

                    AdsModel adsModel = new AdsModel(userId, catId, subCatId , areaId, subAreaId,name, price, description , imageUris,latitude,longtitude);
                    Log.i(TAG, "onClick: "+ userId +"..."+catId +".."+subCatId+".."+areaId +".."+subAreaId+".."+price+"..."+description);
                    viewModel.uploadProduct(adsModel);

                 /*
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
               //     bundle.putSerializable("ads_model", adsModel);
                    bundle.putSerializable("images", null);
                    intent.putExtras(bundle);
                //    startActivity(intent);
                  */

                }
            }
        });

        fragmentAddProductBinding.takeImg.setOnClickListener(new View.OnClickListener() {
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

        fragmentAddProductBinding.takeImg2.setOnClickListener(new View.OnClickListener() {
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

        viewModel.mutableUploadProduct.observe((LifecycleOwner) getContext(), new Observer<MainModel>() {
            @Override
            public void onChanged(MainModel resultMainModel) {
                if (resultMainModel.getStatus()) {
                    Toast.makeText(getContext(), getString(R.string.upload_ads_sucess), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Intent intent = new Intent(getContext(), HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                     Toast.makeText(getContext(), resultMainModel.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

              //  Log.e("encodedImagesSize", AddProductFragment.encodedImages.size() + "");
               /* UploadImageModel imageModel = new UploadImageModel( AddProductFragment.encodedImages);
                viewModel.uploadImageAsString(imageModel);
                viewModel.uploadImageAsString.observe(getContext(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s.contains("-1-2-3") || s.contains("error")) {
                            Toast.makeText(getContext(), getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;
                        }
                        Toast.makeText(getContext(), getString(R.string.ads_uploaded), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                });
                */
            }
        });

        initSpinners();


    }

    private void initSpinners() {
        catList = new ArrayList<>();
        categoryViewModel.getAllCategories();
        categoryViewModel.mutableCategoryList.observe(getActivity(), new Observer<List<CategoryModel>>() {
            @Override
            public void onChanged(List<CategoryModel> categoryModels) {
                if (getActivity() != null && getContext() != null) {
                    catList = new ArrayList<>();
                     ArrayList<String> list = new ArrayList<>();
                    for (CategoryModel it : categoryModels) {
                        catList.add(it);
                        if (curLang.equals("AR"))
                            list.add(it.getCategoryName());
                        else
                            list.add(it.getCatNameEn());
                    }
                 //   String all = getContext().getString(R.string.all_categories);
                  //  list.add(0, all);
                  //  catList.add(0, new CategoryModel(0, all, "0"));
                 //   list.remove(list.size() - 1);
                    catAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_textview, list);
                    catAdapter.setDropDownViewResource(R.layout.text_drop);
                    fragmentAddProductBinding.categorySpinner.setAdapter(catAdapter);

                    categoryIndex = catList.get(0).getCategoryId();
                    categoryViewModel.getSubCategory(categoryIndex+"");
                }
            }
        });
        areaList = new ArrayList<>();
        categoryViewModel.getAllCities();
        categoryViewModel.mutableAreaList.observe(getActivity(), new Observer<List<AreaModel>>() {
            @Override
            public void onChanged(List<AreaModel> cityModels) {
                if (getActivity() != null && getContext() != null) {
                    areaList = new ArrayList<>();
                    ArrayList<String> list = new ArrayList<>();
                    for (AreaModel model : cityModels) {
                        areaList.add(model);
                        if (curLang.equals("AR"))
                            list.add(model.getAreaName());
                        else
                            list.add(model.getAreaNameEn());
                    }
                //    String all = getContext().getString(R.string.all_cities);
                //    list.add(0, all);
                //    areaList.add(0, new AreaModel(0, all));
                //    list.remove(list.size() - 1);
                    cityAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_textview, list);
                    cityAdapter.setDropDownViewResource(R.layout.text_drop);
                  //  fragmentAddProductBinding.areaSpinner.setAdapter(cityAdapter);
                    areaIndex = areaList.get(0).getAreaID();
                    categoryViewModel.getSubArea(areaIndex+"");
                }
            }
        });
        progressDialog.dismiss();
    }

    private Boolean validInput() {
        if (fragmentAddProductBinding.editProductName.getText().toString().isEmpty()) {
            fragmentAddProductBinding.editProductName.setError("");
            fragmentAddProductBinding.editProductName.requestFocus();
            return false;
        }
        else if (fragmentAddProductBinding.editPrice.getText().toString().isEmpty()) {
            fragmentAddProductBinding.editPrice.setError("");
            fragmentAddProductBinding.editPrice.requestFocus();
            return false;
        }
        else if ( preferences.getString(Keys.latitude,"").isEmpty()  ) {
            fragmentAddProductBinding.editLocation.requestFocus();
            return false;
        }
       /* else if (areaIndex != 0) {
            fragmentAddProductBinding.areaSpinner.requestFocus();
            Toast.makeText(getContext(), getString(R.string.choose_city), Toast.LENGTH_SHORT).show();
            return false;
        }
        */


        else if (categoryIndex !=0) {
            fragmentAddProductBinding.categorySpinner.requestFocus();
            Toast.makeText(getContext(), getString(R.string.choose_cat), Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (subCategoryIntex != 0 ) {
            fragmentAddProductBinding.subCategorySpinner.requestFocus();
            Toast.makeText(getContext(), getString(R.string.choose_sub_category), Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (imageUris == null || imageUris.size() == 0 || imageUris.size() > 5) {
            Toast.makeText(getContext(), getString(R.string.max_images_uploaded), Toast.LENGTH_SHORT).show();
            return false;
        }

      /*  else if (subAreaIndex != 0 ) {
            fragmentAddProductBinding.subAreaSpinner.requestFocus();
            Toast.makeText(getContext(), getString(R.string.choose_sub_area), Toast.LENGTH_SHORT).show();
            return false;
        }

       */
        return true ;
    }

    public void openDrawer(View view) {
        String lang = Constants.getLocal(getContext());
        if (lang.equals("AR"))
            drawerLayout.openDrawer(Gravity.RIGHT);
        else
            drawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri uri = null;
            File filePath = null ;
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    imageUris.add(clipData.getItemAt(i).getUri());
                    filePath = new File (clipData.getItemAt(i).getUri().getPath());
                    imageFiles.add(filePath);
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
                        filePath = new File (data.getData().getPath());
                        imageFiles.add(filePath);
                    }
                } else
                    encodedImages = new ArrayList<>();
            }
            GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
            fragmentAddProductBinding.recyclerViewProductImage.setLayoutManager(manager);
            imageProductAdapter = new ImageProductAdapter(false);
            imageProductAdapter.setList(imageUris);
            fragmentAddProductBinding.recyclerViewProductImage.setVisibility(View.VISIBLE);
            fragmentAddProductBinding.empty.setVisibility(View.GONE);
            fragmentAddProductBinding.emptyDetails.setVisibility(View.GONE);
            fragmentAddProductBinding.takeImg.setVisibility(View.GONE);
            fragmentAddProductBinding.takeImg2.setVisibility(View.VISIBLE);
            fragmentAddProductBinding.recyclerViewProductImage.setAdapter(imageProductAdapter);
        }
    }


}
