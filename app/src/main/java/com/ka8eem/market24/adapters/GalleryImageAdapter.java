package com.ka8eem.market24.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ka8eem.market24.R;
import com.ka8eem.market24.models.ChatModel;
import com.ka8eem.market24.models.ProductModel;
import com.ka8eem.market24.ui.activities.ProductDetails;
import com.ka8eem.market24.util.Constants;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GalleryImageAdapter extends PagerAdapter {

    private ArrayList<String> list;
    private Context context;

    public GalleryImageAdapter() {

    }

    public void setList(ArrayList<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        context = container.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_view_galary, container, false);
        ImageView imageView = view.findViewById(R.id.image);
        TextView textView = view.findViewById(R.id.text_counter);
        Glide.with(context).load(list.get(position)).fitCenter().into(imageView);
        textView.setText((position + 1) + "/" + list.size());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}
