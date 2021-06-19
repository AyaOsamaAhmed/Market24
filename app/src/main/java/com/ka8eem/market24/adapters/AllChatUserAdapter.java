package com.ka8eem.market24.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.ka8eem.market24.Notification.Data;
import com.ka8eem.market24.R;
import com.ka8eem.market24.models.ConversationModel;
import com.ka8eem.market24.models.ProductModel;
import com.ka8eem.market24.ui.activities.ChattingActivity;
import com.ka8eem.market24.util.Keys;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AllChatUserAdapter extends RecyclerView.Adapter<AllChatUserAdapter.MyViewHolder> {

    List<ConversationModel> list;
    Context context;
    Bitmap bitmap;
    int user_id ;

    String type_user ;

    public AllChatUserAdapter(Context context  , int user_id)
    {
        this.context = context;
        this.user_id = user_id;
    }

    public void setList(List<ConversationModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_users_chat, parent, false);
        return new AllChatUserAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllChatUserAdapter.MyViewHolder holder, int position) {

        holder.last_message.setText(list.get(position).getLast_message().getMessage());
        holder.tx_name.setText(list.get(position).getAds_model().getProduct_name());

         SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        // String st_date=formatter.format(list.get(position).getLast_message().getUpdated_at());
             holder.date.setText(list.get(position).getLast_message().getUpdated_at() );




        if( list.get(position).getAds_model().getAdsImage() != null ) {
            String url = Keys.image_domain + list.get(position).getAds_model().getAdsImage().getImgUrl();

            Picasso.get()
                    .load(url).resize(600, 200)
                    .placeholder(R.mipmap.ic_logo_round)
                    .into(holder.img_profile);
        }

       holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_id == list.get(position).getBuyer_id())
                {
                    type_user  = Keys.buyer;
                }else
                    type_user = Keys.seller;
                Intent i = new Intent(context , ChattingActivity.class);
                 i.putExtra("ID_ADS", list.get(position).getAds_id());
                 i.putExtra("IMG_ADS", list.get(position).getAds_model().getAdsImage().getImgUrl());
                 i.putExtra("name_ADS",list.get(position).getAds_model().getProduct_name() );
                 i.putExtra("id_user" , user_id);
                 i.putExtra("type_user" , type_user);
                i.putExtra("conversation_id" , list.get(position).getId());
                i.putExtra("buyer_id" , list.get(position).getBuyer_id());
                i.putExtra("seller_id" , list.get(position).getSeller_id());
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

        ImageView img_profile;
        TextView tx_name, tx_num_seen, date , last_message;

        public MyViewHolder(@NonNull View item) {
            super(item);

            tx_name = item.findViewById(R.id.text_name);
            tx_num_seen = item.findViewById(R.id.num_seen);
            last_message = item.findViewById(R.id.text_message);
            date = item.findViewById(R.id.text_date);
            img_profile = item.findViewById(R.id.profile_image);



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