package com.ka8eem.market24.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ka8eem.market24.R;
import com.ka8eem.market24.models.PaymentAdsModel;
import com.ka8eem.market24.models.ProductModel;
import com.ka8eem.market24.ui.activities.WebViewActivity;
import com.ka8eem.market24.util.Keys;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class PaymentAdsAdapter extends RecyclerView.Adapter<PaymentAdsAdapter.PaymentVM> {


    ArrayList<PaymentAdsModel> list;
    Context context;
    NavController navController ;

    public void setList(ArrayList<PaymentAdsModel> list ,NavController navController) {
        this.list = list;
        this.navController = navController ;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PaymentVM onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.payment_ads_list_item, parent, false);
        return new PaymentVM(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentVM holder, int position) {
        PaymentAdsModel model = list.get(position);
        String url = null ;
        if( model.getAdsImage() != null) {
              url = Keys.image_domain + list.get(position).getAdsImage().getImgUrl();
        }
        holder.price.setText(model.getPrice());
        holder.product_name.setText(model.getProduct_name());

        Picasso.get()
                .load(url).resize(600, 200)
                .placeholder(R.mipmap.ic_logo_round)
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

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    class PaymentVM extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView  product_name , price ;
        public PaymentVM(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.payment_ads_img);
            product_name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);

        }
    }

}
