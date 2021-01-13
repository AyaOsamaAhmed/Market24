package com.ka8eem.market24.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ka8eem.market24.R;
import com.ka8eem.market24.models.PaymentAdsModel;
import com.ka8eem.market24.ui.activities.WebViewActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class PaymentAdsAdapter extends RecyclerView.Adapter<PaymentAdsAdapter.PaymentVM> {


    ArrayList<PaymentAdsModel> list;
    Context context;

    public void setList(ArrayList<PaymentAdsModel> list) {
        this.list = list;
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

        String url = list.get(position).getAdsImage();
        Glide.with(context).load(url).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                String url = list.get(position).getAdsLink();
                if (url == null || url.equals("")) {
                    Toast.makeText(context, "No such web site!", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra("url", url);
                context.startActivity(intent);
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
        public PaymentVM(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.payment_ads_img_item);
        }
    }

}
