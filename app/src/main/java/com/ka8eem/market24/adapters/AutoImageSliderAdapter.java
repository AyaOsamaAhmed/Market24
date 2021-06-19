package com.ka8eem.market24.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ka8eem.market24.R;
import com.ka8eem.market24.models.PannerModel;
import com.ka8eem.market24.util.Keys;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.util.HashMap;
import java.util.List;

public class AutoImageSliderAdapter extends SliderViewAdapter<AutoImageSliderAdapter.SliderAdapterVH> {

    private Context context;
    private List<PannerModel> list;
    private HashMap<Integer,Bitmap>  list_img ;
    private  Activity activity ;

    public AutoImageSliderAdapter(Context context , Activity activity
    ) {
        this.context = context;
        this.activity = activity;
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(activity));
    }

    public void renewItems(List<PannerModel> sliderItems) {
        this.list = sliderItems;
        list_img = new HashMap<Integer,Bitmap>();
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.auto_slider_item, parent, false);

        return new SliderAdapterVH(view);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {

        Uri uri = Uri.parse(Keys.image_domain+list.get(position).getImgUrl());

   //  if(list_img.get(position) == null  ) {
         Picasso.get()
                 .load(uri)
                 .resize(800, 200)
                 .placeholder(R.drawable.ic_camera)
                 //          .transform(transformation)
                 .into(new Target() {
                     @Override
                     public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                         viewHolder.imageView.setImageBitmap(bitmap);
                         list_img.put(position, bitmap);
                         Log.i("Image Adapter", bitmap + " : " + position + ": ");
                     }

                     @Override
                     public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                     }

                     @Override
                     public void onPrepareLoad(Drawable placeHolderDrawable) {
                     }
                 });

     //}else viewHolder.imageView.setImageBitmap(list_img.get(position));

/*
        // Simple Request
            ImageLoader imageLoader = ImageLoader.getInstance();
            ImageSize targetSize = new ImageSize(800, 200); // result Bitmap will be fit to this size

         //   imageLoader.displayImage(String.valueOf(uri), viewHolder.imageView, targetSize);

            // Load image, decode it to Bitmap and return Bitmap to callback
            imageLoader.loadImage(String.valueOf(uri), targetSize, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    // Load image, decode it to Bitmap and return Bitmap synchronously
                  //  Bitmap bmp = imageLoader.loadImageSync(imageUri);
                 //   list_img.add(position, loadedImage);
             //       list_img.set(position, loadedImage);
           //         viewHolder.imageView.setImageBitmap(loadedImage);
                }
            });
 */

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