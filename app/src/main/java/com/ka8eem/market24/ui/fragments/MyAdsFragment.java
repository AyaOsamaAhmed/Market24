package com.ka8eem.market24.ui.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ka8eem.market24.R;
import com.ka8eem.market24.adapters.AdsAdapter;
import com.ka8eem.market24.models.ProductModel;
import com.ka8eem.market24.models.UserModel;
import com.ka8eem.market24.ui.activities.ProductDetails;
import com.ka8eem.market24.util.Constants;
import com.ka8eem.market24.viewmodel.ProductViewModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MyAdsFragment extends Fragment {


    public MyAdsFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    public static AdsAdapter adsAdapter;
    ProductViewModel productVM;
    SharedPreferences preferences;
    UserModel userModel;
    Gson gson;
    FloatingActionButton fabDeleteAllAds;
    SearchView searchView;
    ImageView filterImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_ads, container, false);
        searchView = (SearchView) getActivity().findViewById(R.id.search_view);
        searchView.setVisibility(View.GONE);
        filterImage = (ImageView) getActivity().findViewById(R.id.filter_icon);
        filterImage.setVisibility(View.GONE);

        productVM = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);
        recyclerView = view.findViewById(R.id.my_ads_recycler_view);
        fabDeleteAllAds = view.findViewById(R.id.delete_all_my_ads);
        fabDeleteAllAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct("0");
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        preferences = getContext().getSharedPreferences(Constants.SHARED, Context.MODE_PRIVATE);
        gson = new Gson();
        Type type = new TypeToken<UserModel>() {
        }.getType();
        String json = preferences.getString("USER_MODEL", null);
        userModel = gson.fromJson(json, type);
        adsAdapter = new AdsAdapter();
        // 1
        productVM.getMyAds(userModel.getUserId());
        productVM.mutableAdsList.observe((getActivity()), new Observer<ArrayList<ProductModel>>() {
            @Override
            public void onChanged(ArrayList<ProductModel> productModels) {
                if (getActivity() != null) {
                    if (productModels != null) {
                        adsAdapter.setList(productModels);
                        adsAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adsAdapter);
                    } else {
                        if (getActivity() != null && getContext() != null)
                            Toast.makeText(getContext(), getString(R.string.no_ads), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return view;
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