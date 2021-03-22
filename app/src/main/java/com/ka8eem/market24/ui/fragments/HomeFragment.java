package com.ka8eem.market24.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;
import com.ka8eem.market24.R;
import com.ka8eem.market24.adapters.AdsAdapter;
import com.ka8eem.market24.adapters.AutoImageSliderAdapter;
import com.ka8eem.market24.adapters.CategoryAdapter;
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
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    public HomeFragment() {
        // Required empty public constructor
    }

    RecyclerView recommendedRecyclerView, paymentAdsRecyclerView;
    SearchAdapter searchAdapter;
    LinearLayout furnishedText, carText, animalText, allCatText, dressText ,buildingText  ;
    static ProductViewModel productViewModel;
    static CategoryViewModel categoryViewModel ;
    SharedPreferences preferences;
    TextView textUploadAdsFree;
    NavigationView navigationView ;
    ArrayList<PaymentAdsModel> paymentAdsList;
    ArrayList<ProductModel> productList;
    ArrayList<CategoryModel> categoryModel ;
    SliderView sliderView;
    PaymentAdsAdapter paymentAdsAdapter;
    AdsAdapter adsAdapter ;
    CategoryAdapter categoryAdapter ;
    SearchView searchView;
    ImageView filterImage;
    LinearLayout toolbar ;
     NavController navController ;
    ArrayList<PaymentAdsModel> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        FragmentManager fm = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        initViews(view);

        toolbar = getActivity().findViewById(R.id.relative1);
        toolbar.setVisibility(View.VISIBLE);
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
            //    Intent intent = new Intent(getContext(), FilterActivity.class);
             //   startActivity(intent);
            }
        });
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null)
                    newText = "";
             //   getProducts("0", "0", "0", "0", newText);
                return true;
            }
        });

        paymentAdsRecyclerView = view.findViewById(R.id.paid_ads_recycler_view);
        recommendedRecyclerView = view.findViewById(R.id.recommended_recycler_view);
        recommendedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        carText = view.findViewById(R.id.car_cat);
        dressText = view.findViewById(R.id.fashion_cat);
        animalText = view.findViewById(R.id.animal_cat);
        buildingText = view.findViewById(R.id.building_cat);
        furnishedText = view.findViewById(R.id.furnished_cat);
        allCatText = view.findViewById(R.id.all_cat);
        sliderView = view.findViewById(R.id.imageSlider);
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
        AutoImageSliderAdapter adapter = new AutoImageSliderAdapter(getContext());

        productViewModel.pannerImages.observe(getActivity(), new Observer<List<PannerModel>>() {
            @Override
            public void onChanged(List<PannerModel> pannerModels) {

                adapter.renewItems(pannerModels);
                sliderView.setSliderAdapter(adapter);
                sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                sliderView.setSliderTransformAnimation(SliderAnimations.ANTICLOCKSPINTRANSFORMATION);
                sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                sliderView.setIndicatorSelectedColor(Color.WHITE);
                sliderView.setIndicatorUnselectedColor(Color.GRAY);
                sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
                sliderView.startAutoCycle();
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
                  navController.navigate(R.id.AddProductFragment);
                }
            }
        });

        furnishedText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                arguments.putString("cat_id", "4");
                arguments.putString("cat_name",getString(R.string.furnished));
              navController.navigate(R.id.AllSubCategoriesFragment,arguments);
            }
        });
        buildingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                arguments.putString("cat_id", "4");
                arguments.putString("cat_name",getString(R.string._home));
                navController.navigate(R.id.AllSubCategoriesFragment,arguments);
            }
        });
        dressText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                arguments.putString("cat_id", "2");
                arguments.putString("cat_name",getString(R.string.fashion));

                navController.navigate(R.id.AllSubCategoriesFragment,arguments);           }
        });
        animalText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Bundle arguments = new Bundle();
                arguments.putString("cat_id", "11");
                arguments.putString("cat_name",getString(R.string.animal));

                navController.navigate(R.id.AllSubCategoriesFragment,arguments);
            }
        });
        carText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Bundle arguments = new Bundle();
                arguments.putString("cat_id", "1");
                arguments.putString("cat_name",getString(R.string.vehicles));

                navController.navigate(R.id.AllSubCategoriesFragment,arguments);
            }
        });

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