package com.ka8eem.market24.ui.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ka8eem.market24.R;
import com.ka8eem.market24.adapters.AdsAdapter;
import com.ka8eem.market24.adapters.MyAdsAdapter;
import com.ka8eem.market24.models.MyProductModel;
import com.ka8eem.market24.models.MySearchProductModel;
import com.ka8eem.market24.models.ProductModel;
import com.ka8eem.market24.models.UserModel;
import com.ka8eem.market24.util.Constants;
import com.ka8eem.market24.viewmodel.ProductViewModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MyAdsFragment extends Fragment {


    public MyAdsFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    MyAdsAdapter my_adsAdapter;
    ProductViewModel productVM;
    SharedPreferences preferences;
    UserModel userModel;
    Gson gson;
    LinearLayout toolbar ;
    NavController navController ;
    ImageView  no_img_product;
    TextView  no_product ;
    MyProductModel myProductModel;
    List<ProductModel>  listProductModels ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_ads, container, false);
        toolbar = getActivity().findViewById(R.id.relative1);
        toolbar.setVisibility(View.GONE);

        productVM = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);
        recyclerView = view.findViewById(R.id.my_ads_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        navController = Navigation.findNavController(getActivity(),R.id.fragment_container);
        listProductModels = new ArrayList<>();
        no_img_product = view.findViewById(R.id.no_img_product);
        no_product = view.findViewById(R.id.no_product);

        view.setOnKeyListener( new View.OnKeyListener()
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
        //loading

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        // get user data
        preferences = getContext().getSharedPreferences(Constants.SHARED, Context.MODE_PRIVATE);
        gson = new Gson();
        Type type = new TypeToken<UserModel>() {
        }.getType();
        String json = preferences.getString("USER_MODEL", null);
        userModel = gson.fromJson(json, type);
        //get product data
        my_adsAdapter = new MyAdsAdapter();
        productVM.getMyAds(userModel.getUserId());
        productVM.mutableMyAdsList.observe((getActivity()), new Observer<MyProductModel>() {
            @Override
            public void onChanged(MyProductModel productModels) {

                progressDialog.dismiss();
                if (productModels.getData().size() == 0) {
                    no_product.setVisibility(View.VISIBLE);
                    no_img_product.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    no_product.setVisibility(View.GONE);
                    no_img_product.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    myProductModel = productModels ;
                   setAds(productModels.getData());
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, "onScrollStateChanged: "+myProductModel.getCurrent_page());
                if(myProductModel.getCurrent_page() != myProductModel.getLast_page()) {
                    productVM.getMyAdsByPage(userModel.getUserId(),myProductModel.getCurrent_page()+1);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "onScrolled: "+myProductModel.getCurrent_page());
            }
        });


        return view;
    }

    void setAds(List<ProductModel> productModels){
        for (int i=0 ; i<productModels.size() ; i++){
            listProductModels.add(productModels.get(i));
        }
        Log.d("My Ads Fragment", "setads: " + listProductModels.size());

        my_adsAdapter.setList(listProductModels,navController);
        my_adsAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(my_adsAdapter);


    }
    private void deleteProduct(String productID) {
        ProductViewModel viewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);
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
                        viewModel.mutableDeleteProduct.observe(getActivity(), new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                if (getActivity() != null && getContext() != null) {
                                    AdsAdapter adapter = (AdsAdapter) recyclerView.getAdapter();
                                    adapter.clearAdapter();
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), getString(R.string.delete_done), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
        );
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }
}