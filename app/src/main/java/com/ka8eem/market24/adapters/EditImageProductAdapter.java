package com.ka8eem.market24.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ka8eem.market24.R;
import com.ka8eem.market24.models.ImageModel;
import com.ka8eem.market24.ui.fragments.AddProductFragment;
import com.ka8eem.market24.viewmodel.ProductViewModel;

import java.util.ArrayList;

public class EditImageProductAdapter extends RecyclerView.Adapter<EditImageProductAdapter.MyViewHolder> {


    Context context;
    ArrayList<ImageModel> list;

    public EditImageProductAdapter() {

    }


    public void clearAdapter() {
        list.clear();
        notifyDataSetChanged();
    }


    public void setList(ArrayList<ImageModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EditImageProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.image_product_item, parent, false);
        return new EditImageProductAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditImageProductAdapter.MyViewHolder holder, final int position) {

        Glide.with(context).load(list.get(position).getImgUrl()).into(holder.imageView);
        holder.delete_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                ProductViewModel viewModel = ViewModelProviders.of((FragmentActivity) context).get(ProductViewModel.class);
                viewModel.deleteImage(list.get(position).getImgId());
                final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                progressDialog.setCancelable(false);
                viewModel._deleteImage.observe((LifecycleOwner) context, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s != null) {
                            if (s.equals("0") || s.contains("error")) {
                                Toast.makeText(context, context.getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, context.getString(R.string.image_deleted), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                notifyDataSetChanged();
            }
        });
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
