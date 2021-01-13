package com.ka8eem.market24.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import static com.ka8eem.market24.util.Constants.SHARED;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    Context context;
    ArrayList<ProductModel> list, listInFav;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Gson gson;


    public SearchAdapter() {

    }

    private int isFav(int _id) {
        for (int i = 0; i < listInFav.size(); i++)
            if (listInFav.get(i).getProductID() == _id)
                return i;
        return -1;
    }

    private void getFavList() {
        listInFav = new ArrayList<>();
        preferences = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();
        String json = preferences.getString("listFav", null);
        Type type = new TypeToken<ArrayList<ProductModel>>() {
        }.getType();
        listInFav = gson.fromJson(json, type);
        if (listInFav == null)
            listInFav = new ArrayList<>();
    }

    public void setList(ArrayList<ProductModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        getFavList();
        View view = LayoutInflater.from(context).inflate(R.layout.search_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        ProductModel model = list.get(position);
        String curLang = Constants.getLocal(context);
        String price = model.getPrice();
        String cityName = model.getCityName();
        String catName = model.getCategoryName();
        String time = model.getDateTime();
        time = time.substring(0, time.indexOf(' '));
        if (curLang.equals("AR")) {
            price = price + " ل.س";
        } else {
            price = price + " L.S";
            cityName = model.getCityNameEn();
            catName = model.getCategoryNameEn();
        }
        Glide.with(context).load(model.getProductImages().get(0).getImgUrl()).fitCenter().into(holder.imageView);
        holder.textCity.setText(cityName);
        holder.textSalary.setText(price);
        holder.textProductName.setText(model.getProductName());
        holder.textUserName.setText(model.getUserName());
        holder.textCatType.setText(catName);
        holder.textDataTime.setText(time);
        holder.imageStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFavourite(holder, list.get(position));
            }
        });

        int ok = isFav(model.getProductID());
        Drawable drw = context.getResources().getDrawable(R.drawable.ic_favorite_border);
        if (ok > -1) {
            drw = context.getResources().getDrawable(R.drawable.ic_favorite);
        }
        holder.imageStar.setImageDrawable(drw);
        holder.txtSubArea.setText(list.get(position).getSubCityName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetails.class);
                intent.putExtra("product_id", model.getProductID() + "");
                context.startActivity(intent);
            }
        });
    }

    private void addToFavourite(MyViewHolder holder, ProductModel productModel) {
        String toastMess = "";
        int _id = productModel.getProductID();
        Gson gson = new Gson();
        String json = preferences.getString("listFav", null);
        Type type = new TypeToken<ArrayList<ProductModel>>() {
        }.getType();
        listInFav = gson.fromJson(json, type);
        if (listInFav == null || listInFav.size() == 0) {
            listInFav = new ArrayList<>();
            listInFav.add(productModel);
            toastMess = context.getString(R.string.add_to_fav);
            holder.imageStar.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
        } else {
            int pos = isFav(_id);
            if (pos > -1) {
                toastMess = context.getString(R.string.remove_from_fav);
                removeFromFavourite(holder, pos);
            } else {
                listInFav.add(productModel);
                toastMess = context.getString(R.string.add_to_fav);
                holder.imageStar.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
            }
        }
        json = gson.toJson(listInFav);
        editor.putString("listFav", json);
        editor.commit();
        editor.apply();
        notifyDataSetChanged();
        Toast.makeText(context, toastMess, Toast.LENGTH_SHORT).show();
    }

    private void removeFromFavourite(MyViewHolder holder, int pos) {
        String json = preferences.getString("listFav", null);
        Type type = new TypeToken<ArrayList<ProductModel>>() {
        }.getType();
        listInFav = gson.fromJson(json, type);
        listInFav.remove(pos);
        json = gson.toJson(listInFav);
        editor.putString("listFav", json);
        editor.commit();
        editor.apply();
        holder.imageStar.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border));
        notifyItemRemoved(pos);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, imageStar;
        TextView textSalary, textCatType, textCity, textProductName, textUserName, textDataTime, txtSubArea;

        public MyViewHolder(@NonNull View item) {
            super(item);
            imageStar = item.findViewById(R.id.search_star_);
            imageView = item.findViewById(R.id.img_search_list_item);
            textProductName = item.findViewById(R.id.product_name);
            textUserName = item.findViewById(R.id.user_name);
            textSalary = item.findViewById(R.id.price_search_item);
            textCity = item.findViewById(R.id.city_name);
            textCatType = item.findViewById(R.id.category);
            textDataTime = item.findViewById(R.id.time_date);
            txtSubArea = item.findViewById(R.id.sub_area);
        }
    }
}