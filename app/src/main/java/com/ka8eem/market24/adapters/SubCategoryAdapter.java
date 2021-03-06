package com.ka8eem.market24.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ka8eem.market24.R;
import com.ka8eem.market24.models.CategoryModel;
import com.ka8eem.market24.models.SubCategoryModel;
import com.ka8eem.market24.ui.activities.AdsByCategoryActivity;
import com.ka8eem.market24.util.Constants;

import java.util.ArrayList;

import static com.ka8eem.market24.util.Keys.image_domain;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {

    ArrayList<SubCategoryModel> list;
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String catName ;
    NavController navController ;

    public SubCategoryAdapter( NavController navController) {
        this.navController = navController ;
    }

    public void setList(ArrayList<SubCategoryModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.subcategory_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle arguments = new Bundle();
                arguments.putString("search_sub_cat_id", list.get(position).getSub_id() +"");
                arguments.putString("search_cat_id", list.get(position).getSubCatId() +"");

                navController.navigate(R.id.SearchFragment,arguments);
            }
        });
        String lang = Constants.getLocal(context);

        if (lang.equals("AR"))
            catName = list.get(position).getCubCatName();
        else
            catName = list.get(position).getSubCatNameEn();
        holder.textView.setText(catName);
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
             textView = itemView.findViewById(R.id.sub_category);
        }
    }
}