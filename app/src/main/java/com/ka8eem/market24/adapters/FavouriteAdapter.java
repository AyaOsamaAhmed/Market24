package com.ka8eem.market24.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ka8eem.market24.R;
import com.ka8eem.market24.models.FavouriteModel;
import com.ka8eem.market24.models.ProductModel;
import com.ka8eem.market24.util.Constants;
import com.ka8eem.market24.util.Keys;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.MyViewHolder> {

    List<FavouriteModel> listInFav;
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    NavController navController ;
    String price  ;

    public void setList(List<FavouriteModel> list) {

        this.listInFav = list;
    }

    public FavouriteAdapter(Context context , NavController navController) {
        this.context = context;
        this.navController = navController ;
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
        View view = LayoutInflater.from(context).inflate(R.layout.favourite_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        String curLang = Constants.getLocal(context);

        price = listInFav.get(position).getAds().getPrice();
        if (curLang.equals("AR")) {
            price = price + " ل.س";
        } else {
            price = price + " L.S";
        }

        holder.product_Price.setText(price);
        holder.product_name.setText(listInFav.get(position).getAds().getProduct_name());

        if( listInFav.get(position).getAds().getListAdsImage().size() != 0) {
           String url = Keys.image_domain + listInFav.get(position).getAds().getListAdsImage().get(0).getImgUrl();

            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(Color.WHITE)
                    .borderWidthDp(3)
                    .cornerRadiusDp(20)
                    .oval(false)
                    .build();

            Picasso.get()
                    .load(url).resize(600, 200)
                    .placeholder(R.mipmap.ic_logo_round)
                    .into(holder.img_view);
        }


        holder.fav_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                arguments.putString("product_id", listInFav.get(position).getAds().getAdsID()+"");

                navController.navigate(R.id.ProductDetailsFragment,arguments);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listInFav.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_view ,  fav_item ;
        TextView  product_name , product_Price ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_view = itemView.findViewById(R.id.img_item);
            fav_item = itemView.findViewById(R.id.fav_item);
            product_name = itemView.findViewById(R.id.product_name);
            product_Price= itemView.findViewById(R.id.product_price);


        }
    }
}
