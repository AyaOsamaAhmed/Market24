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
import android.os.Build;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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
    List<MultipartBody.Part> imagesPart ;
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
    ProgressDialog take_image_progressDialog;
    NavController navController ;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

        navController = Navigation.findNavController(getActivity(),R.id.fragment_container);

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
                    navController.navigate(R.id.HomeFragment);
                    return true;
                }
                return false;
            }
        } );

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initViews() {

        searchView = (LinearLayout) getActivity().findViewById(R.id.relative1);
        searchView.setVisibility(View.GONE);
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        //-------------------
        imageUris = new ArrayList<>();
        imagesPart = new ArrayList<>();
        imageFiles =new ArrayList<>();
        encodedImages = new ArrayList<>();
        preferences = getContext().getSharedPreferences(Constants.SHARED, Context.MODE_PRIVATE);
        editor = preferences.edit();
        curLang = Constants.getLocal(getContext());
        //-----------------
        categoryViewModel = ViewModelProviders.of(getActivity()).get(CategoryViewModel.class);
        viewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);
        //-------------
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        //----------------
        if(! preferences.getString(Keys.Address,"").equals("")){
            fragmentAddProductBinding.editLocation.setText(preferences.getString(Keys.Address,"") );

        }
        //---------
        categoryViewModel.subCategoryList.observe(getActivity(), new Observer<List<SubCategoryModel>>() {
            @Override
            public void onChanged(List<SubCategoryModel> subCategoryModels) {
                if (getActivity() != null && getContext() != null) {
                    subCategoryList = new ArrayList<>(subCategoryModels);
                    ArrayList<String> listNames = new ArrayList<>();

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
                    String catId = categoryIndex+"";
                    String subCatId = subCategoryList.get(subCategoryIntex).getSub_id() + "";
                    String description = fragmentAddProductBinding.editDescription.getText().toString();
                    String latitude = preferences.getString(Keys.latitude,"");
                    String longtitude =preferences.getString(Keys.longtitude,"");
                    String address  = preferences.getString(Keys.Address,"");
                    String phone = fragmentAddProductBinding.editNumber.getText().toString();
                       String negotiable;
                     if(fragmentAddProductBinding.negotiable.getText().toString() == null)
                           negotiable ="0";
                     else
                          negotiable = "1" ;

                    AdsModel adsModel = new AdsModel(userId, catId, subCatId ,name, price, description ,latitude,longtitude,address,negotiable,phone);
                    Log.i(TAG, "onClick: "+ userId +"..."+categoryIndex+"--"+catId +".."+subCatId+".." +".."+price+"..."+description+"... phone:"+phone);

                    viewModel.uploadProduct(adsModel,imagesPart);

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
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE )
                        != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    return;
                }
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE )
                        != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                    return;
                }

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA )
                        != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CAMERA}, 100);
                    return;
                }
                take_image_progressDialog = new ProgressDialog(getContext());
                take_image_progressDialog.show();
                take_image_progressDialog.setContentView(R.layout.choose_image_dialog);
                take_image_progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                take_image_progressDialog.setCancelable(false);
                Button take_image = take_image_progressDialog.findViewById(R.id.take_image);
                Button choose_image = take_image_progressDialog.findViewById(R.id.choose_image);

                take_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePicture.resolveActivity(getActivity().getPackageManager()) != null)
                        startActivityForResult(takePicture, 0);//zero can be replaced with any action code (called requestCode)
                    }
                });
                choose_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, getString(R.string.choose_images)), 1);
                    }
                });


            }
        });

        fragmentAddProductBinding.takeImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUris != null && imageUris.size() >= 5) {
                    Toast.makeText(getContext(), getString(R.string.max_images_uploaded), Toast.LENGTH_SHORT).show();
                    return;
                }
                take_image_progressDialog = new ProgressDialog(getContext());
                take_image_progressDialog.show();
                take_image_progressDialog.setContentView(R.layout.choose_image_dialog);
                take_image_progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                take_image_progressDialog.setCancelable(false);
                Button take_image = take_image_progressDialog.findViewById(R.id.take_image);
                Button choose_image = take_image_progressDialog.findViewById(R.id.choose_image);

                take_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePicture.resolveActivity(getActivity().getPackageManager()) != null)
                            startActivityForResult(takePicture, 0);//zero can be replaced with any action code (called requestCode)
                    }
                });
                choose_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, getString(R.string.choose_images)), 1);
                    }
                });

            }
        });

        viewModel.error.observe((LifecycleOwner) getContext(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
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

                    catAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_textview, list);
                    catAdapter.setDropDownViewResource(R.layout.text_drop);
                    fragmentAddProductBinding.categorySpinner.setAdapter(catAdapter);

                    categoryIndex = catList.get(0).getCategoryId();
                    categoryViewModel.getSubCategory(categoryIndex+"");
                }
            }
        });
        areaList = new ArrayList<>();
     //   categoryViewModel.getAllCities();
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


         else if (fragmentAddProductBinding.editNumber.getText().toString().isEmpty()) {
            fragmentAddProductBinding.editNumber.requestFocus();
            Toast.makeText(getContext(), getString(R.string.num), Toast.LENGTH_SHORT).show();
            return false;
        }
 */

        else if (fragmentAddProductBinding.editDescription.getText().toString().isEmpty() ) {
            fragmentAddProductBinding.editDescription.requestFocus();
            Toast.makeText(getContext(), getString(R.string.description), Toast.LENGTH_SHORT).show();
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
            File filePath = null;
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    imageUris.add(clipData.getItemAt(i).getUri());
                    imagesPart.add(Keys.compressImage(getContext(), clipData.getItemAt(i).getUri(), "images[]"));
                    filePath = new File(clipData.getItemAt(i).getUri().getPath());
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

                    imageUris.add(data.getData());
                    imagesPart.add(Keys.compressImage(getContext(), data.getData(), "images[]"));

                   /*
                     Bitmap bitmap = null;
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
                        filePath = new File(data.getData().getPath());
                        imageFiles.add(filePath);
                    }
                    */

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

        } else if (requestCode == 0 && resultCode == RESULT_OK) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos); // bm is the bitmap object

            String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, "Title", null);

            Uri selectedImage = Uri.parse(path);
                imageUris.add(selectedImage);
            imagesPart.add(Keys.compressImage(getContext(), selectedImage, "images[]"));

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

        take_image_progressDialog.dismiss();
    }

}
