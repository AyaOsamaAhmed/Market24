package com.ka8eem.market24.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.lang.UProperty;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ka8eem.market24.R;
import com.ka8eem.market24.models.ChatModel;
import com.ka8eem.market24.models.MessageModel;
import com.ka8eem.market24.models.UserModel;
import com.ka8eem.market24.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final String buyer = "buyer";
    public static final String seller = "seller";

    boolean show  = false;
    FirebaseUser firebaseUser;

    private List<MessageModel> list;
    private Context context;
    private String buy_img;

    public MessageAdapter(Context context ) {
     //   this.list = list;
        this.context = context;
    }

    public void setBuyerImg(String buyer_img){
        this.buy_img = buy_img;
    }

    public void addMessage(List<MessageModel> chatModelList,String buyer_img) {
       list = chatModelList;
        this.buy_img = buyer_img;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == RIGHT) {

            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_right, parent, false);
            return new MessageAdapter.MyViewHolder(view);
        } else {

            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_left, parent, false);
            return new MessageAdapter.MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, int position) {

        //  Glide.with(context).load(list.get(position).getUrlImage()).into(holder.imageView);
        //ChatModel chatModel = list.get(position);
      //  Toast.makeText(context, "message" + list.get(position).getMessage(), Toast.LENGTH_SHORT).show();
         if(buy_img !=null)
            Picasso.get()
                    .load(Uri.parse(buy_img))
                    .placeholder(R.drawable.user_bk_profile)
                    .into(holder.profile_image);

        holder.show_message.setText(list.get(position).getMessage());
        holder.str_date.setText(list.get(position).getUpdated_at());
        holder.str_date.setTextColor(Color.parseColor("#000000"));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(show == false) {
                    show = true;
                    holder.str_date.setVisibility(View.VISIBLE);
                }else{
                    show = false;
                    holder.str_date.setVisibility(View.GONE);
                }

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

        ImageView profile_image;
        TextView show_message , str_date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.profile_image);
            show_message = itemView.findViewById(R.id.message);
            str_date = itemView.findViewById(R.id.message_date);
        }
    }

    @Override
    public int getItemViewType(int position) {

       // firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (list.get(position).getType().equals(buyer)) {
            return LEFT;
        } else {
            return RIGHT;
        }
    }
}
