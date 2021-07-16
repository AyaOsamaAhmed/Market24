package com.ka8eem.market24.ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.ka8eem.market24.R;
import com.ka8eem.market24.adapters.AdsAdapter;
import com.ka8eem.market24.adapters.AutoImageSliderAdapter;
import com.ka8eem.market24.adapters.CategoryAdapter;
import com.ka8eem.market24.adapters.ImageSliderAdapter;
import com.ka8eem.market24.adapters.PaymentAdsAdapter;
import com.ka8eem.market24.adapters.SearchAdapter;
import com.ka8eem.market24.models.CategoryModel;
import com.ka8eem.market24.models.PannerModel;
import com.ka8eem.market24.models.PaymentAdsModel;
import com.ka8eem.market24.models.ProductModel;
import com.ka8eem.market24.ui.activities.AdsByCategoryActivity;
import com.ka8eem.market24.ui.activities.LoginActivity;
import com.ka8eem.market24.ui.activities.RegisterActivity;
import com.ka8eem.market24.util.Constants;
import com.ka8eem.market24.viewmodel.CategoryViewModel;
import com.ka8eem.market24.viewmodel.ProductViewModel;
import com.opensooq.pluto.PlutoView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.ka8eem.market24.util.Keys.image_domain;

public class HomeFragment extends Fragment {
    public HomeFragment() {
        // Required empty public constructor
    }

    RecyclerView recommendedRecyclerView, paymentAdsRecyclerView;
    LinearLayout furnishedText, all_product, cat_one, cat_two, cat_four ,cat_three  ,allCatText;
    ImageView cat_one_img  , cat_two_img  , cat_three_img  , cat_four_img ;
    TextView cat_one_text  , cat_two_text  , cat_three_text  , cat_four_text ;
    static ProductViewModel productViewModel;
    static CategoryViewModel categoryViewModel ;
    SharedPreferences preferences;
    TextView textUploadAdsFree;
    ArrayList<PaymentAdsModel> paymentAdsList;
    ArrayList<ProductModel> productList;
    List<CategoryModel> list_category;
 //   SliderView sliderView;
    PlutoView pluto_flipper;
    PaymentAdsAdapter paymentAdsAdapter;
    AdsAdapter adsAdapter ;
    SearchView searchView;
    ImageView filterImage;
    LinearLayout toolbar ;
     NavController navController ;
    String lang ;
    private int PERMISSION_REQUEST= 10 ;
    private LocationManager service ;
    private  Boolean enabled = false ;
    private DrawerLayout drawerLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        drawerLayout = getActivity().findViewById(R.id.drawer_layout);
        toolbar = getActivity().findViewById(R.id.relative1);
        toolbar.setVisibility(View.VISIBLE);
          lang = Constants.getLocal(getContext());

         //You need to add the following line for this solution to work; thanks skayred
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                 //   @SuppressLint("RestrictedApi")
               //     String lastFragment =  Navigation.findNavController(v).getPreviousBackStackEntry().getDestination().getDisplayName(); //com.ka8eem.market24:id/FavouriteFragment
                    @SuppressLint("RestrictedApi")
                    String currentFragment = Navigation.findNavController(v).getCurrentBackStackEntry().getDestination().getDisplayName(); //com.ka8eem.market24:id/ProductDetailsFragment

                    if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        closeDrawer(view);
                        return true;
                    }

                 if (currentFragment.substring(currentFragment.indexOf("/")+1 , currentFragment.length()).equals("HomeFragment")  ) {
                     return true;
                 }

                    return false;
                }
                return false;
            }
        } );

        initViews(view);

        return view;
    }


    private void initViews(View view) {
        preferences = getContext().getSharedPreferences(Constants.SHARED, Context.MODE_PRIVATE);
        searchView = (SearchView) (getActivity().findViewById(R.id.search_view));
       searchView.setVisibility(View.VISIBLE);
        filterImage = ((ImageView) getActivity().findViewById(R.id.filter_icon));
        filterImage.setVisibility(View.VISIBLE);

       navController = Navigation.findNavController(getActivity(),R.id.fragment_container);

        filterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.MapRadiusFragment);
            }
        });
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit:"+query);
                if (query != null || query.isEmpty()) {
                    Bundle arguments = new Bundle();
                    arguments.putString("search_name", query);

                    navController.navigate(R.id.SearchFragment, arguments);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange:"+newText);
                if (newText == null || newText.isEmpty()) {
                    Log.d(TAG, "onQueryText:"+newText);
                    navController.navigate(R.id.HomeFragment);
                }
                return true;
            }
        });

        paymentAdsRecyclerView = view.findViewById(R.id.paid_ads_recycler_view);
        recommendedRecyclerView = view.findViewById(R.id.recommended_recycler_view);
        recommendedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        all_product = view.findViewById(R.id.all_product);
        cat_one = view.findViewById(R.id.cat_one);
        cat_two = view.findViewById(R.id.cat_two);
        cat_three = view.findViewById(R.id.cat_three);
        cat_four = view.findViewById(R.id.cat_four);
        cat_one_img = view.findViewById(R.id.cat_one_img);
        cat_one_text = view.findViewById(R.id.cat_one_text);
        cat_two_img = view.findViewById(R.id.cat_two_img);
        cat_two_text = view.findViewById(R.id.cat_two_text);
        cat_three_img = view.findViewById(R.id.cat_three_img);
        cat_three_text = view.findViewById(R.id.cat_three_text);
        cat_four_img = view.findViewById(R.id.cat_four_img);
        cat_four_text = view.findViewById(R.id.cat_four_text);

        allCatText = view.findViewById(R.id.all_cat);
      //  sliderView = view.findViewById(R.id.imageSlider);
        pluto_flipper = view.findViewById(R.id.pluto_flipper);
        productViewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);
        categoryViewModel = ViewModelProviders.of(getActivity()).get(CategoryViewModel.class);
        //------------
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        //---------
        productViewModel.getHome();
        categoryViewModel.getAllCategories();

        categoryViewModel.mutableCategoryList.observe(getActivity(), new Observer<List<CategoryModel>>() {
            @Override
            public void onChanged(List<CategoryModel> categoryModels) {
                if(categoryModels != null){
                    list_category = categoryModels;
                    setCategoryData(categoryModels);
                }
            }
        });

        productViewModel.mutableProductList.observe(getActivity(), new Observer<List<ProductModel>>() {
            @Override
            public void onChanged(List<ProductModel> productModels) {

                if (getActivity() != null) {
                    if (productModels != null) {
                        productList = new ArrayList<>(productModels);
                    } else {
                        if (getActivity() != null && getContext() != null)
                            Toast.makeText(getContext(), R.string.no_result, Toast.LENGTH_SHORT).show();
                    }
                    adsAdapter = new AdsAdapter();
                    adsAdapter.setList(productModels,navController);
                    recommendedRecyclerView.setAdapter(adsAdapter);
                }
            }
        });
        productViewModel.paymentAdsList.observe(getActivity(), new Observer<List<PaymentAdsModel>>() {
            @Override
            public void onChanged(List<PaymentAdsModel> productModels) {
                if (productModels != null) {
                    progressDialog.dismiss();
                    paymentAdsList = new ArrayList<>(productModels);
                    paymentAdsAdapter = new PaymentAdsAdapter();
                    paymentAdsAdapter.setList(paymentAdsList,navController);
                    paymentAdsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    paymentAdsRecyclerView.setAdapter(paymentAdsAdapter);
                }
            }
        });
        //AutoImageSliderAdapter adapter = new AutoImageSliderAdapter(getContext(),getActivity());


        productViewModel.pannerImages.observe(getActivity(), new Observer<List<PannerModel>>() {
            @Override
            public void onChanged(List<PannerModel> pannerModels) {
                ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(pannerModels ,getContext());

                imageSliderAdapter.renewItems(pannerModels);
                //pass the lifecycle to make the slider aware of lifecycle to avoid memory leak and handling the pause/destroy/resume
                pluto_flipper.create(imageSliderAdapter, getLifecycle());

            }
        });

        allCatText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null && getContext() != null) {
                  navController.navigate(R.id.AllCategoriesFragment);
                }
            }
        });

        textUploadAdsFree = view.findViewById(R.id.upload_ads_free);
        textUploadAdsFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkRegister())
                    startRegisterActivity();
                else if (!checkLoggedIn())
                    startLoginActivity();
                else {
                    if(checkPermission())
                  navController.navigate(R.id.MapFragment);
                }
            }
        });

        cat_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                arguments.putString("cat_id",   list_category.get(0).getCategoryId()+"");
                if (lang.equals("AR"))
                    arguments.putString("cat_name",list_category.get(0).getCategoryName());
                else
                    arguments.putString("cat_name",list_category.get(0).getCatNameEn());
              navController.navigate(R.id.AllSubCategoriesFragment,arguments);
            }
        });
        cat_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                arguments.putString("cat_id",   list_category.get(1).getCategoryId()+"");
                if (lang.equals("AR"))
                    arguments.putString("cat_name",list_category.get(1).getCategoryName());
                else
                    arguments.putString("cat_name",list_category.get(1).getCatNameEn());
                navController.navigate(R.id.AllSubCategoriesFragment,arguments);
            }
        });
        cat_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                arguments.putString("cat_id",   list_category.get(2).getCategoryId()+"");
                if (lang.equals("AR"))
                    arguments.putString("cat_name",list_category.get(2).getCategoryName());
                else
                    arguments.putString("cat_name",list_category.get(2).getCatNameEn());
                navController.navigate(R.id.AllSubCategoriesFragment,arguments);           }
        });
        cat_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Bundle arguments = new Bundle();
                arguments.putString("cat_id",   list_category.get(3).getCategoryId()+"");
                if (lang.equals("AR"))
                    arguments.putString("cat_name",list_category.get(3).getCategoryName());
                else
                    arguments.putString("cat_name",list_category.get(3).getCatNameEn());

                navController.navigate(R.id.AllSubCategoriesFragment,arguments);
            }
        });
        all_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Bundle arguments = new Bundle();
                arguments.putString("search_name", "");
                navController.navigate(R.id.SearchFragment,arguments);
            }
        });

    }

    public void closeDrawer(View view) {
        String lang = Constants.getLocal(getContext());
        if (lang.equals("AR"))
            drawerLayout.closeDrawer(Gravity.RIGHT);
        else
            drawerLayout.closeDrawer(Gravity.LEFT);
    }

    public void openDrawer(View view) {
        String lang = Constants.getLocal(getContext());
        if (lang.equals("AR"))
            drawerLayout.openDrawer(Gravity.RIGHT);
        else
            drawerLayout.openDrawer(Gravity.LEFT);
    }
    private void setCategoryData(List<CategoryModel> categoryModels) {

        Glide.with(getContext()).load(image_domain+categoryModels.get(0).getUrlImage()).into(cat_one_img);
        Glide.with(getContext()).load(image_domain+categoryModels.get(1).getUrlImage()).into(cat_two_img);
        Glide.with(getContext()).load(image_domain+categoryModels.get(2).getUrlImage()).into(cat_three_img);
        Glide.with(getContext()).load(image_domain+categoryModels.get(3).getUrlImage()).into(cat_four_img);
        String lang = Constants.getLocal(getContext());
        if (lang.equals("AR")) {

            cat_one_text.setText(categoryModels.get(0).getCategoryName());
            cat_two_text.setText(categoryModels.get(1).getCategoryName());
            cat_three_text.setText(categoryModels.get(2).getCategoryName());
            cat_four_text.setText(categoryModels.get(3).getCategoryName());

        }
        else {
            cat_one_text.setText(categoryModels.get(0).getCatNameEn());
            cat_two_text.setText(categoryModels.get(1).getCatNameEn());
            cat_three_text.setText(categoryModels.get(2).getCatNameEn());
            cat_four_text.setText(categoryModels.get(3).getCatNameEn());
        }
    }

    public Boolean checkPermission(){
        if(ContextCompat.checkSelfPermission(
                getContext().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED){
            service = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!enabled) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
                enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }
            return true;

        }else{
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST
            );
            //    check_location = false;
            return false;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST) {
            if (  grantResults[0] !=
                    PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(),
                        "Unable to show location - permission required",
                        Toast.LENGTH_LONG).show();
            }
            service =(LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!enabled) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
                enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }

        }

    }


    private void startRegisterActivity() {
        Intent intent = new Intent(getContext(), RegisterActivity.class);
        startActivity(intent);
    }

    private void startLoginActivity() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
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

    @Override
    public void onDetach() {
        super.onDetach();
    }
}