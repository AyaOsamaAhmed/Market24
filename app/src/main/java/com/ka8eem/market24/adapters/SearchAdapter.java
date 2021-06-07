package com.ka8eem.market24.adapters;

import android.content.Context;
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
import com.squareup.picasso.Picasso;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    Context context;
    List<ProductModel> list;
    NavController navController ;

    public SearchAdapter() {

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
        View view = LayoutInflater.from(context).inflate(R.layout.my_ads_list_item, parent, false);
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
            Picasso.get()
                    .load(url).resize(600, 200)
                    .placeholder(R.mipmap.ic_logo_round)
                    .into(holder.imageView);
        }

        if (curLang.equals("AR")) {
            price = price + " ل.س" ;
        } else {
            price = price + " L.S";
        }

        holder.price.setText(price);
        holder.ProductName.setText(model.getProduct_name());
        holder.product_desc.setText(model.getDescription());



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


    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView price, ProductName , product_desc;

        public MyViewHolder(@NonNull View item) {
            super(item);
            imageView = item.findViewById(R.id.my_ads_img_item);
            ProductName = item.findViewById(R.id.my_product_name);
            product_desc = item.findViewById(R.id.my_product_desc);
            price = item.findViewById(R.id.my_product_price);

        }
    }
}