package com.ka8eem.market24.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ka8eem.market24.R;
import com.ka8eem.market24.adapters.SearchAdapter;
import com.ka8eem.market24.models.CategoryModel;
import com.ka8eem.market24.models.MessageModel;
import com.ka8eem.market24.models.MySearchProductModel;
import com.ka8eem.market24.models.ProductModel;
import com.ka8eem.market24.models.SubCategoryModel;
import com.ka8eem.market24.viewmodel.CategoryViewModel;
import com.ka8eem.market24.viewmodel.ProductViewModel;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class SearchFragment extends Fragment {


    public SearchFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    SearchAdapter searchAdapter;
    ProductViewModel productVM;
    NavController navController ;
    String search_name , search_lat , search_long , search_radius , search_cat_id , search_sub_cat_id;
    ImageView no_img_product;
    TextView no_product;
    LinearLayout toolbar ;
    MySearchProductModel  mySearchProductModel;
    List<ProductModel>  listProductModels ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        search_name = bundle.getString("search_name","");

        search_lat = bundle.getString("search_lat", "");
        search_long = bundle.getString("search_long", "");
        search_radius = bundle.getString("search_radius", "");
        search_cat_id = bundle.getString("search_cat_id", "");
        search_sub_cat_id = bundle.getString("search_sub_cat_id", "");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        recyclerView = view.findViewById(R.id.search_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        no_img_product = view.findViewById(R.id.no_img_product);
        no_product = view.findViewById(R.id.no_product);

        toolbar = getActivity().findViewById(R.id.relative1);
        toolbar.setVisibility(View.VISIBLE);

        listProductModels = new ArrayList<>() ;

        searchAdapter = new SearchAdapter();
        productVM = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);
        navController = Navigation.findNavController(getActivity(),R.id.fragment_container);

        //loading
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        //
        if(listProductModels.size() ==0 )
        productVM.getSearch(search_name,search_lat,search_long,search_cat_id,search_sub_cat_id,search_radius);


        productVM.mutableAdsList.observe((getActivity()), new Observer<MySearchProductModel>() {
            @Override
            public void onChanged(MySearchProductModel productModels) {
                progressDialog.dismiss();
                    if (productModels.getData().size() != 0 ) {
                        recyclerView.setVisibility(View.VISIBLE);
                        no_img_product.setVisibility(View.GONE);
                        no_product.setVisibility(View.GONE);
                        mySearchProductModel = productModels ;
                        setAds(productModels.getData());
                    } else {
                    //    Toast.makeText(getContext(), getString(R.string.no_ads), Toast.LENGTH_SHORT).show();
                        recyclerView.setVisibility(View.GONE);
                        no_img_product.setVisibility(View.VISIBLE);
                        no_product.setVisibility(View.VISIBLE);
                    }

            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
             @Override
             public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                 super.onScrollStateChanged(recyclerView, newState);
                 Log.d(TAG, "onScrollStateChanged: "+mySearchProductModel.getCurrent_page());
                 if(mySearchProductModel.getCurrent_page() != mySearchProductModel.getLast_page()) {
                     productVM.getSearchByPage(search_name,search_lat,search_long,search_cat_id,search_sub_cat_id,search_radius , mySearchProductModel.getCurrent_page()+1);

                 }
             }

             @Override
             public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                 super.onScrolled(recyclerView, dx, dy);
                 Log.d(TAG, "onScrolled: "+mySearchProductModel.getCurrent_page());
             }
         });
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
                   // navController.navigate(R.id.HomeFragment);
                    getActivity().onBackPressed();
                    return true;
                }
                return false;
            }
        } );

        return view;
    }

    void setAds(List<ProductModel> productModels){
     
        for (int i=0 ; i<productModels.size() ; i++){
            listProductModels.add(productModels.get(i));
        }
        Log.d("Search Fragment", "setads: " + listProductModels.size());
        searchAdapter.setList(listProductModels,navController);
        searchAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(searchAdapter);


    }
}