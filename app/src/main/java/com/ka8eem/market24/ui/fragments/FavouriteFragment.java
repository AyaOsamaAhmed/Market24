package com.ka8eem.market24.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ka8eem.market24.R;
import com.ka8eem.market24.adapters.FavouriteAdapter;
import com.ka8eem.market24.interfaces.CheckFavourite;
import com.ka8eem.market24.models.FavouriteModel;
import com.ka8eem.market24.models.MainModel;
import com.ka8eem.market24.models.MyFavouriteModel;
import com.ka8eem.market24.models.MyProductModel;
import com.ka8eem.market24.models.ProductModel;
import com.ka8eem.market24.models.UserModel;
import com.ka8eem.market24.util.Constants;
import com.ka8eem.market24.viewmodel.ProductViewModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class FavouriteFragment extends Fragment implements CheckFavourite {



    public FavouriteFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    FavouriteAdapter favouriteAdapter;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    FloatingActionButton fab;
    Gson gson;
   // ItemTouchHelper itemTouchHelper;
    LinearLayout toolbar ;
    NavController navController ;
    ProductViewModel productViewModel ;
    UserModel userModel ;
    MyFavouriteModel myFavouriteModel;
    List<FavouriteModel>  listfavoriteModel ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        productViewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);
        preferences = getContext().getSharedPreferences(Constants.SHARED, Context.MODE_PRIVATE);
        gson = new Gson();
        Type type = new TypeToken<UserModel>() {
        }.getType();
        String json = preferences.getString("USER_MODEL", null);
        userModel = gson.fromJson(json, type);
        listfavoriteModel = new ArrayList<>();
        toolbar = getActivity().findViewById(R.id.relative1);
        toolbar.setVisibility(View.GONE);
        navController = Navigation.findNavController(getActivity(),R.id.fragment_container);
        //loading
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        //
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
        recyclerView = view.findViewById(R.id.favourite_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

            productViewModel.getAllFavourite(userModel.getUserId());

        favouriteAdapter = new FavouriteAdapter(getContext(), navController, this);

        productViewModel.mutableFavouriteProduct.observe(getActivity(), new Observer<MyFavouriteModel>() {
            @Override
            public void onChanged(MyFavouriteModel favouriteModels) {
                progressDialog.dismiss();

                myFavouriteModel = favouriteModels;

               setAds(favouriteModels.getData());
            }
        });

        productViewModel.mutableUpdateFavourite.observe(getActivity(), new Observer<MainModel>() {
            @Override
            public void onChanged(MainModel mainModel) {
                if(mainModel.getStatus()) {
                    myFavouriteModel = new MyFavouriteModel();
                    listfavoriteModel.clear();
                    favouriteAdapter.setList(listfavoriteModel);
                    favouriteAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(favouriteAdapter);
                    productViewModel.getAllFavourite(userModel.getUserId());

                }
            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, "onScrollStateChanged: "+myFavouriteModel.getCurrent_page());
                if(myFavouriteModel.getCurrent_page() != myFavouriteModel.getLast_page()) {
                    productViewModel.getAllFavouriteByPage(userModel.getUserId(),myFavouriteModel.getCurrent_page()+1);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "onScrolled: "+myFavouriteModel.getCurrent_page());
            }
        });

        return view;
    }


    void setAds(List<FavouriteModel> favouriteModel){
        for (int i=0 ; i<favouriteModel.size() ; i++){
            listfavoriteModel.add(favouriteModel.get(i));
        }
        Log.d("Favourite Fragment", "setads: " + listfavoriteModel.size());

        favouriteAdapter.setList(listfavoriteModel);
        favouriteAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(favouriteAdapter);


    }
    private boolean checkLoggedIn() {
        boolean ret = preferences.getBoolean("LOGGED_IN", false);
        return ret;
    }

    private ArrayList<ProductModel> getFavouriteList() {
        ArrayList<ProductModel> listInFav = new ArrayList<>();
        editor = preferences.edit();
        gson = new Gson();
        String json = preferences.getString("listFav", null);
        Type type = new TypeToken<ArrayList<ProductModel>>() {
        }.getType();
        listInFav = gson.fromJson(json, type);
        if (listInFav == null)
            listInFav = new ArrayList<>();
        return listInFav;
    }

    private ArrayList<ProductModel> getItems() {
        ArrayList<ProductModel> list;
        preferences = getContext().getSharedPreferences(Constants.SHARED, Context.MODE_PRIVATE);
        editor = preferences.edit();
        Gson gson = new Gson();
        String json = preferences.getString("listFav", null);
        Type type = new TypeToken<ArrayList<ProductModel>>() {
        }.getType();
        list = gson.fromJson(json, type);
        if (list == null)
            list = new ArrayList<>();
        return list;
    }

    private void clearFavList() {
        ArrayList<ProductModel> list = getItems();
        list.clear();
        preferences = getContext().getSharedPreferences(Constants.SHARED, Context.MODE_PRIVATE);
        editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("listFav", json);
        editor.commit();
        editor.apply();
        FavouriteAdapter favAdapter = (FavouriteAdapter) recyclerView.getAdapter();
        favAdapter.clearAdapter();
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int pos = viewHolder.getAdapterPosition();
            FavouriteAdapter favAdapter = (FavouriteAdapter) recyclerView.getAdapter();
            switch (direction) {
                case ItemTouchHelper.LEFT:
                case ItemTouchHelper.RIGHT:
                    favAdapter.removeItem(pos);
                    break;
            }
        }
    };


    @Override
    public void onCheckFavourite(int fav , String product_id) {

        productViewModel.updateItemFavourite(userModel.getUserId(), product_id);
        Toast.makeText(getContext(), "Success favourite", Toast.LENGTH_LONG).show();


    }
}