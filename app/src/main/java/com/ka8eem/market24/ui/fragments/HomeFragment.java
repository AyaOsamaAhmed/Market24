package com.ka8eem.market24.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;
import com.ka8eem.market24.R;
import com.ka8eem.market24.adapters.AutoImageSliderAdapter;
import com.ka8eem.market24.adapters.PaymentAdsAdapter;
import com.ka8eem.market24.adapters.SearchAdapter;
import com.ka8eem.market24.models.CategoryModel;
import com.ka8eem.market24.models.CityModel;
import com.ka8eem.market24.models.PannerModel;
import com.ka8eem.market24.models.PaymentAdsModel;
import com.ka8eem.market24.models.ProductModel;
import com.ka8eem.market24.models.SubCategoryModel;
import com.ka8eem.market24.ui.activities.AdsByCategoryActivity;
import com.ka8eem.market24.ui.activities.AllCategories;
import com.ka8eem.market24.ui.activities.FilterActivity;
import com.ka8eem.market24.ui.activities.LoginActivity;
import com.ka8eem.market24.ui.activities.MainActivity;
import com.ka8eem.market24.ui.activities.RegisterActivity;
import com.ka8eem.market24.util.Constants;
import com.ka8eem.market24.viewmodel.CategoryViewModel;
import com.ka8eem.market24.viewmodel.ProductViewModel;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    public HomeFragment() {
        // Required empty public constructor
    }

    RecyclerView recommendedRecyclerView, paymentAdsRecyclerView;
    SearchAdapter searchAdapter;
    LinearLayout homeText, carText, propertyText, allCatText, dressText;
    static ProductViewModel productViewModel;
    SharedPreferences preferences;
    TextView textUploadAdsFree;
    NavigationView navigationView;
    ArrayList<PaymentAdsModel> paymentAdsList;
    ArrayList<ProductModel> productList;
    SliderView sliderView;
    PaymentAdsAdapter paymentAdsAdapter;
    SearchView searchView;
    ImageView filterImage;

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

        return view;
    }


    private void initViews(View view) {
//        list = new ArrayList<>(MainActivity.paymentList);
        preferences = getContext().getSharedPreferences(Constants.SHARED, Context.MODE_PRIVATE);
        searchView = (SearchView) (getActivity().findViewById(R.id.search_view));
        searchView.setVisibility(View.VISIBLE);
        filterImage = ((ImageView) getActivity().findViewById(R.id.filter_icon));
        filterImage.setVisibility(View.VISIBLE);
        filterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FilterActivity.class);
                startActivity(intent);
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
                getProducts("0", "0", "0", "0", newText);
                return true;
            }
        });
        paymentAdsRecyclerView = view.findViewById(R.id.paid_ads_recycler_view);
        recommendedRecyclerView = view.findViewById(R.id.recommended_recycler_view);
        recommendedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        homeText = view.findViewById(R.id.home_cat);
        carText = view.findViewById(R.id.car_cat);
        propertyText = view.findViewById(R.id.property_cat);
        allCatText = view.findViewById(R.id.all_cat);
        dressText = view.findViewById(R.id.dress_cat);
        sliderView = view.findViewById(R.id.imageSlider);
        productViewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);

        productViewModel.getPaymentAds();
        productViewModel.getPannerImages();
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        productViewModel.paymentAdsList.observe(getActivity(), new Observer<ArrayList<PaymentAdsModel>>() {
            @Override
            public void onChanged(ArrayList<PaymentAdsModel> productModels) {
                if (productModels != null) {
                    progressDialog.dismiss();
                    paymentAdsList = new ArrayList<>(productModels);
                    paymentAdsAdapter = new PaymentAdsAdapter();
                    paymentAdsAdapter.setList(paymentAdsList);
                    paymentAdsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    paymentAdsRecyclerView.setAdapter(paymentAdsAdapter);
                }
            }
        });
        AutoImageSliderAdapter adapter = new AutoImageSliderAdapter(getContext());
        ArrayList<PannerModel> pannerModels1 = new ArrayList<>();
        productViewModel.pannerImages.observe(getActivity(), new Observer<ArrayList<PannerModel>>() {
            @Override
            public void onChanged(ArrayList<PannerModel> pannerModels) {


                    for (int u = 0; u < pannerModels.size(); u++) {
                        if (u < 4) {
                            pannerModels1.add(pannerModels.get(u));
                        } else {
                            break;
                        }


                    }
                    adapter.renewItems(pannerModels1);


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
        if (paymentAdsList == null) {
            sliderView = view.findViewById(R.id.imageSlider);
            AutoImageSliderAdapter sliderAdapter = new AutoImageSliderAdapter(getContext());
            sliderView.setSliderAdapter(sliderAdapter);

        }
        initRecyclerView();
        textUploadAdsFree = view.findViewById(R.id.upload_ads_free);
        textUploadAdsFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkRegister())
                    startRegisterActivity();
                else if (!checkLoggedIn())
                    startLoginActivity();
                else {
                    navigationView = getActivity().findViewById(R.id.nav_view);
                    navigationView.setCheckedItem(R.id.nav_add);
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new AddProductFragment()).addToBackStack(null).commit();
                }
            }
        });

        homeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), getString(R.string.home), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), AdsByCategoryActivity.class);
                intent.putExtra("cat_id", "3");
                startActivity(intent);
            }
        });
        dressText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), getString(R.string.fashion), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), AdsByCategoryActivity.class);
                intent.putExtra("cat_id", "2");
                startActivity(intent);
            }
        });
        propertyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), getString(R.string.property), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), AdsByCategoryActivity.class);
                intent.putExtra("cat_id", "4");
                startActivity(intent);
            }
        });
        carText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), getString(R.string.vehicles), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), AdsByCategoryActivity.class);
                intent.putExtra("cat_id", "1");
                startActivity(intent);
            }
        });
        allCatText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), R.string.all_categories, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), AllCategories.class);
                startActivity(intent);
            }
        });
    }

    public static void getProducts(String catID, String cityID, String subCatId, String subAreaId, String searchText) {
        productViewModel.getProducts(catID, cityID, subCatId, subAreaId, searchText);
    }

    private void initRecyclerView() {
        getProducts("0", "0", "0", "0", "");
        productViewModel.mutableProductList.observe(getActivity(), new Observer<ArrayList<ProductModel>>() {
            @Override
            public void onChanged(ArrayList<ProductModel> productModels) {

                if (getActivity() != null) {
                    if (productModels != null) {
                        productList = new ArrayList<>(productModels);
                    } else {
                        if (getActivity() != null && getContext() != null)
                            Toast.makeText(getContext(), R.string.no_result, Toast.LENGTH_SHORT).show();
                    }
                    searchAdapter = new SearchAdapter();
                    searchAdapter.setList(productModels);
                    recommendedRecyclerView.setAdapter(searchAdapter);
                }
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