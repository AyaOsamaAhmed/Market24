package com.ka8eem.market24.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ka8eem.market24.R;
import com.ka8eem.market24.models.PannerModel;
import com.ka8eem.market24.util.Keys;
import com.opensooq.pluto.base.PlutoAdapter;
import com.opensooq.pluto.base.PlutoViewHolder;
import com.opensooq.pluto.listeners.OnItemClickListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public class ImageSliderAdapter extends PlutoAdapter<PannerModel, ImageSliderAdapter.autoPlutoViewHolder> {

    private List<PannerModel> list;
    Context context ;

    public ImageSliderAdapter(@NotNull List<PannerModel> items , Context context) {
        super(items );
        this.context = context ;
    }

    public void renewItems(List<PannerModel> sliderItems) {
        this.list = sliderItems;
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public autoPlutoViewHolder getViewHolder(@NotNull ViewGroup parent, int i) {
        return new autoPlutoViewHolder(parent , R.layout.auto_slider_item);
    }



    class autoPlutoViewHolder extends PlutoViewHolder<PannerModel> {

        ImageView imageView;

        public autoPlutoViewHolder(@NotNull ViewGroup parent, int itemLayoutId) {
            super(parent, itemLayoutId);
            imageView = itemView.findViewById(R.id.payment_ads_img_item);
        }

        @Override
        public void set(PannerModel pannerModel, int position) {

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(list.get(position).getLink()));
                    context.startActivity(i);
                }
            });
            Uri uri = Uri.parse(Keys.image_domain+list.get(position).getImgUrl());

            Picasso.get()
                    .load(uri)
                    .resize(800, 200)
                    .placeholder(R.drawable.ic_camera)
                    .into(imageView);
        }
    }

}