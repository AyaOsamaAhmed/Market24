package com.ka8eem.market24.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.ka8eem.market24.R;
import com.ka8eem.market24.models.ImageModel;
import com.ka8eem.market24.ui.activities.GalleryActivity;

import java.util.ArrayList;

public class PhotoAdapter extends PagerAdapter {

    private ArrayList<ImageModel> images;
    private LayoutInflater inflater;
    private Context context;

    public PhotoAdapter(Context context, ArrayList<ImageModel> images){
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((View)object);
    }


    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position){
        View myImageLayout  = inflater.inflate(R.layout.photo_item_list, view, false);
        ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.image);
        TextView textCounter = (TextView) myImageLayout.findViewById(R.id.text_counter);
        String imgUrl = images.get(position).getImgUrl();
        Glide.with(context).load(imgUrl).fitCenter().into(myImage);
        textCounter.setText((position + 1) +"/"+images.size());
        view.addView(myImageLayout, 0);
        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Bundle args1 = new Bundle();
                //args1.pu();
                ArrayList<ImageModel> array_img = images;
                ArrayList<String> array_img_string = new ArrayList<>();
                for(int i = 0 ; i <array_img.size() ; i++)
                {
                    array_img_string.add(array_img.get(i).getImgUrl());
                }
                Intent intent = new Intent(context , GalleryActivity.class);
                intent.putStringArrayListExtra("array_img_string",array_img_string );
                context.startActivity(intent);
            }
        });
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}