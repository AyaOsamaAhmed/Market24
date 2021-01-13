package com.ka8eem.market24.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ka8eem.market24.R;
import com.ka8eem.market24.models.ProductModel;
import com.ka8eem.market24.ui.activities.ProductDetails;
import com.ka8eem.market24.util.Constants;

import java.io.Serializable;
import java.util.ArrayList;

public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.MyViewHolder> {

    Context context;
    ArrayList<ProductModel> list;


    public AdsAdapter() {

    }

    public void setList(ArrayList<ProductModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.ads_list_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        ProductModel model = list.get(position);

        String curLang = Constants.getLocal(context);
        String price = model.getPrice();
        String cityName = model.getCityName();
        if (curLang.equals("AR")) {
            price = price + " ل.س" ;
        } else {
            price = price + " L.S";
            cityName = model.getCityNameEn();
        }
        Glide.with(context).load(model.getProductImages().get(0).getImgUrl()).fitCenter().into(holder.imageView);
        holder.textCity.setText(cityName);
        holder.textSalary.setText(price);
        holder.textProductName.setText(model.getProductName());
        holder.textUserName.setText(model.getUserName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductModel model = list.get(position);
                Intent intent = new Intent(context, ProductDetails.class);
                intent.putExtra("product_id", model.getProductID() + "");
                context.startActivity(intent);
            }
        });
    }

    public void clearAdapter() {
        list.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }

//    public void deleteItemAtPos(String id) {
//        int idx = 0;
//        for (idx = 0; idx < list.size(); idx++) {
//            if (id.equals(list.get(idx).getProductID() + "")) {
//                list.remove(idx);
//                notifyItemRemoved(idx);
//            }
//            idx++;
//        }
//    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textSalary, textCatType, textCity, textProductName, textUserName , txtSubArea;

        public MyViewHolder(@NonNull View item) {
            super(item);
            imageView = item.findViewById(R.id.img_ads_list_item);
            textProductName = item.findViewById(R.id.ads_product_name);
            textUserName = item.findViewById(R.id.ads_user_name);
            textSalary = item.findViewById(R.id.price_ads_item);
            textCity = item.findViewById(R.id.ads_city_name);
            textCatType = item.findViewById(R.id.ads_category);

        }
    }
}