package com.ka8eem.market24.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ka8eem.market24.R;
import com.ka8eem.market24.models.ChatlistModel;
import com.ka8eem.market24.ui.activities.ChattingActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class UserFirebaseAdapter extends RecyclerView.Adapter<UserFirebaseAdapter.MyViewHolder> {

    List<ChatlistModel> list;
    Context context;
    Bitmap bitmap;

    public UserFirebaseAdapter(Context context , List<ChatlistModel> list)
    {
        this.context = context;
        this.list = list;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_users_chat, parent, false);
        return new UserFirebaseAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserFirebaseAdapter.MyViewHolder holder, int position) {

        holder.textView.setText(list.get(position).getName_product()+ " ("+list.get(position).getName() + ")");

        holder.date.setText(list.get(position).getStr_date()+"");
        if(list.get(position).getNum_seen() == 0)
        {holder.textView2.setVisibility(View.GONE);
        }else{
        holder.textView2.setText(list.get(position).getNum_seen()+"");}
        //if(user.getImage().equals(""))
        // {
        //holder.profile.setImageResource(R.drawable.ic_person);
        //Glide.with(context).load(user.getImg_ad()).fitCenter().into(holder.profile);
        //new ProfileFragment.GetImageFromUrl(holder.profile).execute(userModel.getImage());
        //Toast.makeText(context, user.getImg_ad(), Toast.LENGTH_SHORT).show();
        new GetImageFromUrl(holder.profile).execute(list.get(position).getImg_ad());
        // }
        // else{
        /////////////////////
        //  }

       holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context , ChattingActivity.class);
                //args1.putString("owner_ad_id", owner_ad);
                 i.putExtra("ID_ADS", list.get(position).getId_ads());
                 i.putExtra("IMG_ADS", list.get(position).getImg_ad());
                //i.putExtra("name_owner", user.getName());
                 i.putExtra("name_ADS",list.get(position).getName_product() );
                i.putExtra("id_user" , list.get(position).getId_user());
                context.startActivity(i);
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

        ImageView profile ;
        TextView textView , textView2 , date;

        public MyViewHolder(@NonNull View item) {
            super(item);

            textView = item.findViewById(R.id.text_name);
            textView2 = item.findViewById(R.id.num_seen);
            date = item.findViewById(R.id.text_date);
            profile = item.findViewById(R.id.profile_image);

        }
    }

    public class GetImageFromUrl extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        public GetImageFromUrl(ImageView img){
            this.imageView = img;
        }
        @Override
        protected Bitmap doInBackground(String... url) {
            String stringUrl = url[0];
            bitmap = null;
            InputStream inputStream;
            try {
                inputStream = new java.net.URL(stringUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap){
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }
}