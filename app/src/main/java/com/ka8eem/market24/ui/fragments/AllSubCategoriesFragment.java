package com.ka8eem.market24.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ka8eem.market24.R;
import com.ka8eem.market24.adapters.CategoryAdapter;
import com.ka8eem.market24.adapters.SubCategoryAdapter;
import com.ka8eem.market24.models.CategoryModel;
import com.ka8eem.market24.models.SubCategoryModel;
import com.ka8eem.market24.viewmodel.CategoryViewModel;
import java.util.ArrayList;
import java.util.List;

public class AllSubCategoriesFragment extends Fragment {


    RecyclerView recyclerView;
    SubCategoryAdapter subCategoryAdapter;
    CategoryViewModel categoryVM;
    LinearLayout toolbar ;
    String sub_catid , sub_catName;
    TextView title ;
    ImageView back ;
    NavController navController ;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        sub_catid = bundle.getString("cat_id","");
        sub_catName = bundle.getString("cat_name","");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_sub_categories_fragment, container, false);


        FragmentManager fm = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        initView(view);

        toolbar = getActivity().findViewById(R.id.relative1);
        toolbar.setVisibility(View.GONE);
        return view;
    }



    private void initView(View view) {
        navController = Navigation.findNavController(getActivity(),R.id.fragment_container);

        recyclerView = view.findViewById(R.id.all_sub_category_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        title = view.findViewById(R.id.title);
        title.setText( getString(R.string.all_sub_of)+"  "+ sub_catName);
        back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.AllCategoriesFragment);
            }
        });

        subCategoryAdapter = new SubCategoryAdapter();
        categoryVM = ViewModelProviders.of(this).get(CategoryViewModel.class);
        categoryVM.getSubCategory(sub_catid);
        categoryVM.subCategoryList.observe(getActivity(), new Observer<List<SubCategoryModel>>() {
            @Override
            public void onChanged(List<SubCategoryModel> subCategoryModels) {
                ArrayList<SubCategoryModel> list = new ArrayList<>(subCategoryModels);
                subCategoryAdapter.setList(list);
                recyclerView.setAdapter(subCategoryAdapter);
            }
        });
    }



}