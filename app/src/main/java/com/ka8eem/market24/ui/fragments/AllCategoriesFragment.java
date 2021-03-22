package com.ka8eem.market24.ui.fragments;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.ka8eem.market24.R;
import com.ka8eem.market24.adapters.CategoryAdapter;
import com.ka8eem.market24.models.CategoryModel;
import com.ka8eem.market24.util.Constants;
import com.ka8eem.market24.viewmodel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class AllCategoriesFragment extends Fragment {


    RecyclerView recyclerView;
    CategoryAdapter categoryAdapter;
    CategoryViewModel categoryVM;
    private DrawerLayout drawerLayout;
    LinearLayout toolbar ;
    NavController navController ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_all_categories, container, false);


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
        recyclerView = view.findViewById(R.id.all_category_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        navController = Navigation.findNavController(getActivity(),R.id.fragment_container);

        categoryAdapter = new CategoryAdapter();
        categoryVM = ViewModelProviders.of(this).get(CategoryViewModel.class);
        categoryVM.getAllCategories();
        categoryVM.mutableCategoryList.observe(getActivity(), new Observer<List<CategoryModel>>() {
            @Override
            public void onChanged(List<CategoryModel> categoryModels) {
                ArrayList<CategoryModel> list = new ArrayList<>(categoryModels);
                categoryAdapter.setList(list,navController);
                recyclerView.setAdapter(categoryAdapter);
            }
        });
    }

    public void openDrawer(View view) {
        String lang = Constants.getLocal(getContext());
        if (lang.equals("AR"))
            drawerLayout.openDrawer(Gravity.RIGHT);
        else
            drawerLayout.openDrawer(Gravity.LEFT);
    }

}