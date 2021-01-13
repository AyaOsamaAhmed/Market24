package com.ka8eem.market24.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ka8eem.market24.R;
import com.ka8eem.market24.adapters.FavouriteAdapter;
import com.ka8eem.market24.models.ProductModel;
import com.ka8eem.market24.util.Constants;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FavouriteFragment extends Fragment {



    public FavouriteFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    FavouriteAdapter favouriteAdapter;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    FloatingActionButton fab;
    Gson gson;
    ItemTouchHelper itemTouchHelper;
    SearchView searchView;
    ImageView filterImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        searchView = (SearchView) getActivity().findViewById(R.id.search_view);
        searchView.setVisibility(View.GONE);
        filterImage = (ImageView) getActivity().findViewById(R.id.filter_icon);
        filterImage.setVisibility(View.GONE);        recyclerView = view.findViewById(R.id.favourite_recycler_view);
        fab = view.findViewById(R.id.fab);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        favouriteAdapter = new FavouriteAdapter(getContext(), itemTouchHelper);
        favouriteAdapter.setList(getFavouriteList());
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(favouriteAdapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFavList();
                Toast.makeText(getContext(), R.string.fav_list_empty, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private ArrayList<ProductModel> getFavouriteList() {
        ArrayList<ProductModel> listInFav = new ArrayList<>();
        preferences = getContext().getSharedPreferences(Constants.SHARED, Context.MODE_PRIVATE);
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
}