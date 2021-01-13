package com.ka8eem.market24.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.ka8eem.market24.R;
import com.ka8eem.market24.ui.activities.EditProductActivity;
import com.ka8eem.market24.ui.fragments.AddProductFragment;
import com.ka8eem.market24.viewmodel.ProductViewModel;

import java.util.ArrayList;

public class ImageProductAdapter extends RecyclerView.Adapter<ImageProductAdapter.MyViewHolder> {

    Context context;
    ArrayList<Uri> list;
    boolean fromEdit;

    public ImageProductAdapter(boolean fromEdit) {
        this.fromEdit = fromEdit;
    }


    public void clearAdapter() {
        list.clear();
        notifyDataSetChanged();
    }


    public void setList(ArrayList<Uri> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.image_product_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        try {
            if (list.get(position) != null) {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), list.get(position));
                holder.imageView.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            //handle exception
        }
        holder.delete_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                if (fromEdit)
                    EditProductActivity.encodedImages.remove(position);
                else
                    AddProductFragment.encodedImages.remove(position);
                notifyDataSetChanged();
            }
        });
        //holder.textCnt.setText(" " + ((position + 1) % (list.size() + 1)) + "/" + list.size());
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView, delete_image;
        //TextView textCnt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.photo_list_item);
            delete_image = itemView.findViewById(R.id.delete_list_item);
        }
    }
}
