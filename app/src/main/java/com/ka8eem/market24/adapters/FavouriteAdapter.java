package com.ka8eem.market24.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ka8eem.market24.R;
import com.ka8eem.market24.models.ProductModel;
import com.ka8eem.market24.ui.activities.ProductDetails;
import com.ka8eem.market24.util.Constants;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.MyViewHolder> {

    ArrayList<ProductModel> listInFav;
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Gson gson;
String price , cityName , catName ;
    ItemTouchHelper itemTouchHelper;

    public void setList(ArrayList<ProductModel> list) {
        this.listInFav = list;
    }

    public FavouriteAdapter(Context context, ItemTouchHelper itemTouchHelper) {
        this.itemTouchHelper = itemTouchHelper;
        this.context = context;
        preferences = context.getSharedPreferences(Constants.SHARED, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public FavouriteAdapter() {

    }

    public void removeItem(int pos) {
        if (pos < 0 || pos >= listInFav.size()) {
            return;
        }
        Gson gson = new Gson();
        String json = preferences.getString("listFav", null);
        Type type = new TypeToken<ArrayList<ProductModel>>() {
        }.getType();
        listInFav = gson.fromJson(json, type);
        listInFav.remove(pos);
        json = gson.toJson(listInFav);
        editor.putString("listFav", json);
        editor.commit();
        editor.apply();
        notifyItemRemoved(pos);
        notifyDataSetChanged();
    }

    public void clearAdapter() {
        Gson gson = new Gson();
        String json = preferences.getString("listFav", null);
        Type type = new TypeToken<ArrayList<ProductModel>>() {
        }.getType();
        listInFav = gson.fromJson(json, type);
        listInFav.clear();
        json = gson.toJson(listInFav);
        editor.putString("listFav", json);
        editor.commit();
        editor.apply();
        listInFav.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favourite_item_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        String curLang = Constants.getLocal(context);
        price = listInFav.get(position).getPrice();
        if (curLang.equals("AR")) {
            price = price + " ل.س";
            cityName = listInFav.get(position).getCityName();
            catName = listInFav.get(position).getCategoryName();
        } else {
            price = price + " L.S";
            cityName = listInFav.get(position).getCityNameEn();
            catName = listInFav.get(position).getCategoryNameEn();
        }
        holder.sub_area.setText(listInFav.get(position).getSubCityName());
        Glide.with(context).load(listInFav.get(position).getProductImages().get(0).getImgUrl()).fitCenter().into(holder.imageView);
        holder.textName.setText(listInFav.get(position).getProductName());
        holder.user_name.setText(listInFav.get(position).getUserName());
        holder.city_name.setText(cityName);
        holder.textPrice.setText(price);
        holder.cat.setText(catName);
        String time = listInFav.get(position).getDateTime();
        time = time.substring(0, time.indexOf(' '));
        holder.time_.setText(time);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductModel model = listInFav.get(position);

                Intent intent = new Intent(context, ProductDetails.class);
                intent.putExtra("product_id", model.getProductID() + "");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listInFav.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textName, textPrice , user_name , city_name , cat , time_ , sub_area;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.fav_img_item);
            textName = itemView.findViewById(R.id.fav_prod_name);
            textPrice = itemView.findViewById(R.id.fav_salary_item);
            user_name = itemView.findViewById(R.id.user_name);
            cat = itemView.findViewById(R.id.category);
            city_name = itemView.findViewById(R.id.city_name);
            sub_area = itemView.findViewById(R.id.sub_area);
            time_ = itemView.findViewById(R.id.time_date);
        }
    }
}
