package com.ka8eem.market24.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.ka8eem.market24.R;
import com.ka8eem.market24.models.PannerModel;
import com.ka8eem.market24.util.Keys;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class AutoImageSliderAdapter extends SliderViewAdapter<AutoImageSliderAdapter.SliderAdapterVH> {

    private Context context;
    private List<PannerModel> list;

    public AutoImageSliderAdapter(Context context) {
        this.context = context;
    }

    public void renewItems(List<PannerModel> sliderItems) {
        this.list = sliderItems;
        notifyDataSetChanged();
    }





    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.auto_slider_item, parent, false);

        return new SliderAdapterVH(view);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.WHITE)
                .borderWidthDp(3)
                .cornerRadiusDp(20)
                .oval(false)
                .build();

            Picasso.get()
                    .load(Keys.image_domain+list.get(position).getImgUrl()).resize(600, 200)
                    .placeholder(R.drawable.ic_camera)
        //            .transform(transformation)
                    .into(viewHolder.imageView);

            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(list.get(position).getLink()));
                    context.startActivity(i);
                }
            });

    }

    @Override
    public int getCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        ImageView imageView;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.payment_ads_img_item);
        }
    }

}