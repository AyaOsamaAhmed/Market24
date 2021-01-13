package com.ka8eem.market24.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ka8eem.market24.R;
import com.ka8eem.market24.models.PannerModel;
import com.ka8eem.market24.models.PaymentAdsModel;
import com.ka8eem.market24.ui.activities.WebViewActivity;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AutoImageSliderAdapter extends SliderViewAdapter<AutoImageSliderAdapter.SliderAdapterVH> {
Bitmap bitmap;
    private Context context;
    private ArrayList<PannerModel> list;

    boolean show = false;
    public AutoImageSliderAdapter(Context context) {
        this.context = context;
    }

    public void renewItems(ArrayList<PannerModel> sliderItems) {
        this.list = sliderItems;
        notifyDataSetChanged();
    }

    public void addItem(PannerModel model) {
        list.add(model);
        notifyDataSetChanged();
    }





    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.auto_slider_item, parent, false);

        return new SliderAdapterVH(view);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {



        Picasso.get()
                .load(list.get(position).getImgUrl()).resize(600, 200)
                //  .transform(RoundedCornersTransformation(30, 1))
                .placeholder(R.drawable.ic_camera)
                .into(viewHolder.imageView);
             //new AutoImageSliderAdapter.GetImageFromUrl(viewHolder.imageView).execute(list.get(position).getImgUrl());

        //viewHolder.paidIcon.setImageResource(R.drawable.paid);


           // Glide.with(viewHolder.imageView).load(list.get(position).getImgUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).dontTransform().into(viewHolder.imageView);

    }

    @Override
    public int getCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        ImageView imageView, paidIcon;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.payment_ads_img_item);
            //paidIcon = itemView.findViewById(R.id.paid_icon);
        }
    }

}