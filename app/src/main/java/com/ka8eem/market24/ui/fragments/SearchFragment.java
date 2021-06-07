package com.ka8eem.market24.ui.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.ka8eem.market24.models.ProductModel;
import com.ka8eem.market24.models.SubCategoryModel;
import com.ka8eem.market24.viewmodel.CategoryViewModel;
import com.ka8eem.market24.viewmodel.ProductViewModel;
import java.util.ArrayList;
import java.util.List;


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

        searchAdapter = new SearchAdapter();
        productVM = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);
        navController = Navigation.findNavController(getActivity(),R.id.fragment_container);


        productVM.getSearch(search_name,search_lat,search_long,search_cat_id,search_sub_cat_id,search_radius);

        productVM.mutableAdsList.observe((getActivity()), new Observer<List<ProductModel>>() {
            @Override
            public void onChanged(List<ProductModel> productModels) {

                    if (productModels.size() != 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        no_img_product.setVisibility(View.GONE);
                        no_product.setVisibility(View.GONE);
                        searchAdapter.setList(productModels,navController);
                        searchAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(searchAdapter);

                    } else {
                        Toast.makeText(getContext(), getString(R.string.no_ads), Toast.LENGTH_SHORT).show();

                        recyclerView.setVisibility(View.GONE);
                        no_img_product.setVisibility(View.VISIBLE);
                        no_product.setVisibility(View.VISIBLE);
                    }

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
                    navController.navigate(R.id.HomeFragment);
                    return true;
                }
                return false;
            }
        } );

        return view;
    }

    private void initView() {


    }
}