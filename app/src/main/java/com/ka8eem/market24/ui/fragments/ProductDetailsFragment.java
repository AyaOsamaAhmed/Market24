package com.ka8eem.market24.ui.fragments;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ka8eem.market24.R;
import com.ka8eem.market24.adapters.ProductImageSliderAdapter;
import com.ka8eem.market24.databinding.ActivityProductDetailsBinding;
import com.ka8eem.market24.models.ImageModel;
import com.ka8eem.market24.models.MainModel;
import com.ka8eem.market24.models.ProductModel;
import com.ka8eem.market24.models.UserModel;
import com.ka8eem.market24.ui.activities.ChattingActivity;
import com.ka8eem.market24.ui.activities.LoginActivity;
import com.ka8eem.market24.ui.activities.RegisterActivity;
import com.ka8eem.market24.util.Constants;
import com.ka8eem.market24.util.Keys;
import com.ka8eem.market24.viewmodel.ProductViewModel;
import com.squareup.picasso.Picasso;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.loader.ImageLoader;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailsFragment extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 1 ;
    String product_id  , type_user;
    ProductViewModel productViewModel ;
    ActivityProductDetailsBinding binding ;
    ProductModel productModel ;
    LinearLayout toolbar ;
    ProgressDialog progressDialog ;
    SharedPreferences preferences;
    NavController navController ;
    ProductImageSliderAdapter setSliderAdapter ;
    UserModel userModel ;
     int is_favourite = 0 ,seller_id , buyer_id;
    private List<ImageModel> images;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        product_id = bundle.getString("product_id","");
     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =  ActivityProductDetailsBinding.inflate(inflater, container, false);

        toolbar = getActivity().findViewById(R.id.relative1);
        toolbar.setVisibility(View.GONE);
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        preferences = getContext().getSharedPreferences(Constants.SHARED, Context.MODE_PRIVATE);
        navController = Navigation.findNavController(getActivity(),R.id.fragment_container);

        String json = preferences.getString("USER_MODEL", null);
        Gson gson1 = new Gson();
        Type type1 = new TypeToken<UserModel>() {
        }.getType();
         userModel = gson1.fromJson(json, type1);

        initView();


        return binding.getRoot();
    }

    void loading(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
    }
    private void initView() {

        loading();
        if (!checkLoggedIn()) {
            productViewModel.getProductById(product_id);
        }
        else
            productViewModel.getProductByIdAndUser(product_id,userModel.getUserId());

        images = new ArrayList<>();

        productViewModel.productById.observe(getActivity(), new Observer<ProductModel>() {
            @Override
            public void onChanged(ProductModel model) {
                if (model != null) {
                    productModel = model;
                    images = model.getListAdsImage();
                    setData();
                    progressDialog.dismiss();

                }
            }
        });
         binding.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (!checkLoggedIn()) {
                    startLoginActivity();
                } else {
                    makeCall();
                }
            }
        });

        binding.callPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkLoggedIn()) {
                    startLoginActivity();
                } else {
                    if(productModel.getPhone() != null)
                    makePhoneCall(productModel.getPhone());
                    else
                        Toast.makeText(getContext(),"لا يوجد رقم متاح للاتصال",Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLoggedIn()) {
                    productViewModel.updateItemFavourite(userModel.getUserId(), product_id);
                }else
                    Toast.makeText(getContext(),"يجب تسجيل الدخول اولا",Toast.LENGTH_LONG).show();
             }
        });
        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareProduct();
             }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
             }
        });

        binding.imageProductSlider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StfalconImageViewer.Builder <>(getContext(), images, new ImageLoader<ImageModel>() {
                    @Override
                    public void loadImage(ImageView imageView, ImageModel image) {
                        Picasso.get().load(image.getImgUrl()).into(imageView);
                    }
                }).show();



            }
        });
        productViewModel.mutableUpdateFavourite.observe(getActivity(), new Observer<MainModel>() {
            @Override
            public void onChanged(MainModel mainModel) {
                if(mainModel.getStatus()) {
                    Toast.makeText(getContext(), "Success favourite", Toast.LENGTH_LONG).show();
                    if(is_favourite == 1 ){
                        is_favourite = 0;
                        binding.like.setImageResource(R.drawable.ic_like);
                    }else {
                        is_favourite = 1;
                        binding.like.setImageResource(R.drawable.ic_like_full);
                    }

                }
                }
        });



    }

    void setData(){
        String price = productModel.getPrice();
        String lang = Constants.getLocal(getContext());
        if (lang.equals("AR"))
            price = price + " ل.س";
        else {
            price = price + " L.S";
        }
        binding.productName.setText(productModel.getProduct_name());
        binding.productDetails.setText(productModel.getDescription());
        binding.productPrice.setText(price);
        binding.productDate.setText(productModel.getDate());
        binding.productAddress.setText(productModel.getAddress());

        if(productModel.getAble_disscussion() == 1)
        binding.productCheck.setVisibility(View.VISIBLE);

        if(productModel.getIs_favourite() == 1 ){

            binding.like.setImageResource(R.drawable.ic_like_full);
            is_favourite = 1 ;
        }

        if(userModel.getUserId() == Integer.parseInt(productModel.getUserID())){

            binding.call.setVisibility(View.GONE);
        }else {
            type_user = Keys.buyer;
            seller_id = Integer.parseInt(productModel.getUserID());
            buyer_id = userModel.getUserId();
        }


        setSliderAdapter = new ProductImageSliderAdapter(getContext() );
        setSliderAdapter.newItems(productModel.getListAdsImage());
        binding.imageProductSlider.setSliderAdapter(setSliderAdapter);

    }

    private void deleteProduct(String productID) {
        ProductViewModel viewModel = ViewModelProviders.of(ProductDetailsFragment.this).get(ProductViewModel.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.are_u_sure));
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        ).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.deleteProduct(productID);
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                progressDialog.setCancelable(false);
                viewModel.mutableDeleteProduct.observe(ProductDetailsFragment.this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        progressDialog.dismiss();
                        String res = null;
                        if (s != null && s.equals("1")) {
                            res = getString(R.string.delete_done);

                        } else
                            res = getString(R.string.something_wrong);
                        Toast.makeText(getContext(), res, Toast.LENGTH_SHORT).show();
                     //   finish();
                    }
                });
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }


    private void makeCall() {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        Intent i = new Intent(getContext() , ChattingActivity.class);
        i.putExtra("id_user" , userModel.getUserId());
        i.putExtra("ID_ADS",Integer.parseInt(product_id));
        i.putExtra("IMG_ADS", productModel.getListAdsImage().get(0).getImgUrl());
        //i.putExtra("name_owner", user.getName());
        i.putExtra("name_ADS",productModel.getProduct_name() );
        i.putExtra("type_user" , type_user);
        i.putExtra("buyer_id" , buyer_id);
        i.putExtra("seller_id" , seller_id);
        progressDialog.cancel();
        startActivity(i);

    }

    private void makePhoneCall(String phone ) {
        String phoneNum = phone;
        phoneNum = phoneNum.trim();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
         } else {
            String dial = "tel:" + phoneNum;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }
    private void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.CALL_PHONE)){}
        else {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},PERMISSION_REQUEST_CODE);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makePhoneCall(productModel.getPhone());
                }
                break;
        }
    }
    private void startRegisterActivity() {
        Intent intent = new Intent(getContext(), RegisterActivity.class);
        startActivity(intent);
    }

    private void startLoginActivity() {
        Intent intent = new Intent( getContext() , LoginActivity.class);
        startActivity(intent);
    }

    private boolean checkRegister() {
        boolean ret = preferences.getBoolean("REGISTERED", false);
        return ret;
    }

    private boolean checkLoggedIn() {
        boolean ret = preferences.getBoolean("LOGGED_IN", false);
        return ret;
    }


    private void shareProduct() {
        String url = "url for product";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(Intent.createChooser(intent, "Share Via"));
    }

}