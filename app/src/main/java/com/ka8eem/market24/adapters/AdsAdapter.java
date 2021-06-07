package com.ka8eem.market24.adapters;

import android.content.Context;
import android.content.Intent;
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

import com.ka8eem.market24.R;
import com.ka8eem.market24.models.ProductModel;
import com.ka8eem.market24.util.Constants;
import com.ka8eem.market24.util.Keys;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.MyViewHolder> {

    Context context;
    List<ProductModel> list;
    NavController navController ;

    public AdsAdapter() {

    }

    public void setList(List<ProductModel> list , NavController navController) {
        this.list = list;
        this.navController = navController ;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.ads_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        ProductModel model = list.get(position);
        String curLang = Constants.getLocal(context);
        String price = model.getPrice();
        String url = null ;
        if( model.getAdsImage() != null) {
              url = Keys.image_domain + model.getAdsImage().getImgUrl();
        }
        String productName = model.getProduct_name();
        if (curLang.equals("AR")) {
            price = price + " ل.س" ;
        } else {
            price = price + " L.S";
        }
     //   Glide.with(context).load(model.getProductImages().get(0).getImgUrl()).fitCenter().into(holder.imageView);

        holder.price.setText(price);
        holder.ProductName.setText(model.getProduct_name());

       /* Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.WHITE)
                .borderWidthDp(3)
                .cornerRadiusDp(20)
                .oval(false)
                .build();


        */
        Picasso.get()
                .load(url).resize(600, 200)
                .placeholder(R.mipmap.ic_logo_round)
//                .transform(transformation)
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                arguments.putString("product_id", list.get(position).getAdsID()+"");

                navController.navigate(R.id.ProductDetailsFragment,arguments);
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
        TextView price, ProductName;

        public MyViewHolder(@NonNull View item) {
            super(item);
            imageView = item.findViewById(R.id.ads_img_item);
            ProductName = item.findViewById(R.id.product_name);
            price = item.findViewById(R.id.product_price);

        }
    }
}