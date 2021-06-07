package com.ka8eem.market24.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.ka8eem.market24.R;
import com.ka8eem.market24.models.ImageModel;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.loader.ImageLoader;

import java.util.List;

import static com.ka8eem.market24.util.Keys.image_domain;

public class ProductImageSliderAdapter extends SliderViewAdapter<ProductImageSliderAdapter.SliderAdapterVH> {

    Bitmap bitmap;
    private Context context;
    List<ImageModel> productItems ;

    boolean show = false;
    public ProductImageSliderAdapter(Context context) {
        this.context = context;

    }



    public void newItems(List<ImageModel> productItems) {
        this.productItems = productItems;
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
                    .load(image_domain + productItems.get(position).getImgUrl())
                    .resize(600, 200)
                    .placeholder(R.drawable.ic_camera)
                    .into(viewHolder.imageView);

            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new StfalconImageViewer.Builder <>(context, productItems, new ImageLoader<ImageModel>() {
                        @Override
                        public void loadImage(ImageView imageView, ImageModel image) {
                            Picasso.get().load(image_domain + image.getImgUrl()).into(imageView);
                        }
                    }).show();
                }
            });
    }

    @Override
    public int getCount() {
        if (productItems == null)
            return 0;
        return productItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        ImageView imageView;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.payment_ads_img_item);
        }
    }

}