package com.ka8eem.market24.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.ka8eem.market24.R;
import com.ka8eem.market24.models.CategoryModel;
import com.ka8eem.market24.ui.activities.AdsByCategoryActivity;
import com.ka8eem.market24.ui.fragments.AllSubCategoriesFragment;
import com.ka8eem.market24.util.Constants;

import java.util.ArrayList;

import static com.ka8eem.market24.util.Keys.image_domain;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    ArrayList<CategoryModel> list;
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    NavController navController ;
    String lang ,catName;

    public CategoryAdapter() {

    }

    public void setList(ArrayList<CategoryModel> list ,NavController navController ) {
        this.list = list;
        this.navController = navController;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.category_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        lang = Constants.getLocal(context);

        Glide.with(context).load(image_domain+list.get(position).getUrlImage()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Intent intent = new Intent(context, a.class);
               // intent.putExtra("cat_id", list.get(position).getCategoryId() + "");
              //  context.startActivity(intent);

                Bundle arguments = new Bundle();
                arguments.putString("cat_id", list.get(position).getCategoryId()+"");
                if (lang.equals("AR")){
                    arguments.putString("cat_name",list.get(position).getCategoryName());
                }else
                arguments.putString("cat_name",list.get(position).getCatNameEn());
                navController.navigate(R.id.AllSubCategoriesFragment,arguments);


            }
        });

        if (lang.equals("AR"))
            catName = list.get(position).getCategoryName();
        else
            catName = list.get(position).getCatNameEn();
        holder.textView.setText(catName);
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cat_img_list_item);
            textView = itemView.findViewById(R.id.text_category_list_item);
        }
    }
}