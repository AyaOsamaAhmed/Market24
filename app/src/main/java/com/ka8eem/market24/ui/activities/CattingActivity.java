package com.ka8eem.market24.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ka8eem.market24.Notification.Client;
import com.ka8eem.market24.Notification.Data;
import com.ka8eem.market24.Notification.MyResponse;
import com.ka8eem.market24.Notification.Sender;
import com.ka8eem.market24.Notification.Token;
import com.ka8eem.market24.R;
import com.ka8eem.market24.adapters.MessageAdapter;
import com.ka8eem.market24.interfaces.DataInterface;
import com.ka8eem.market24.models.ChatModel;
import com.ka8eem.market24.models.UserFirebaseModel;
import com.ka8eem.market24.ui.fragments.ProfileFragment;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CattingActivity extends AppCompatActivity {
Bitmap bitmap;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    Intent intent;
    ValueEventListener valueEventListener ;
    MessageAdapter messageAdapter;
    List<ChatModel> chatModelList;

    RecyclerView recyclerView;
    ImageView img_profile;
    TextView userTxt;
    ImageButton sending;
    EditText message_text;
    DataInterface dataInterface;
    String user_id , ID_ADS , one_img , name_pro;
    boolean notify = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        // Toolbar toolbar = findViewById(R.id.toolbar_chat);
        //setSupportActionBar(toolbar);
        dataInterface = Client.getClient("https://fcm.googleapis.com/").create(DataInterface.class);

        recyclerView = (RecyclerView) findViewById(R.id.message_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getApplicationContext());
        linearLayout.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayout);
        img_profile = (ImageView) findViewById(R.id.profile_image);

        userTxt = (TextView) findViewById(R.id.username);
        sending = (ImageButton) findViewById(R.id.btn_send);
        message_text = (EditText) findViewById(R.id.message_text);

        intent = getIntent();
        user_id = intent.getStringExtra("id_user");
        one_img = intent.getStringExtra("IMG_ADS");
        ID_ADS = intent.getStringExtra("ID_ADS");
        name_pro = intent.getStringExtra("name_ADS");
        new GetImageFromUrl(img_profile).execute(one_img);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        sending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                String message = message_text.getText().toString();
                if (message != null && !message.equals("")) {
                    sendmessage(firebaseUser.getUid(), user_id, message);
                } else {
                    Toast.makeText(CattingActivity.this, "You Can't Send empty Message", Toast.LENGTH_SHORT).show();
                }
                message_text.setText("");
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user_id);
        // Query query = databaseReference.orderByChild("id").equalTo(user_id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserFirebaseModel user = snapshot.getValue(UserFirebaseModel.class);
                userTxt.setText( name_pro + " (" + snapshot.child("name").getValue(String.class)+")" );
                readMessage(firebaseUser.getUid(), user_id, one_img , ID_ADS);
                //if()
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendmessage(String sender, String receiver, String Message) {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm:ss  dd MM yyyy");
        String currentDateandTime = sdf.format(new Date());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("ID_ADS" , ID_ADS);
        hashMap.put("name_ADS" , name_pro);
        hashMap.put("img_ad" , one_img);
        hashMap.put("sender" , sender);
        hashMap.put("receiver" , receiver);
        hashMap.put("Message" , Message);
        hashMap.put("date" , currentDateandTime);
        hashMap.put("isSeen" , false);
        databaseReference.child("Chats").push().setValue(hashMap);

/////
        final  String msg = Message;
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserFirebaseModel userFirebaseModel = new UserFirebaseModel();
                String username = snapshot.child("name").getValue(String.class);
                // Toast.makeText(getContext(), "user_idssssss : "+user_id, Toast.LENGTH_SHORT).show();
                //String name = dataSnapshot.child("name").getValue(String.class);
                //String profile_img = dataSnapshot.child("profile_img").getValue(String.class);*/
                if(notify) {
                    sendNotification(receiver, username, msg , ID_ADS , one_img , name_pro);

                } notify = false;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
// add user to chat fragment
       /* final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(firebaseUser.getUid())
                .child(user_id);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                    chatRef.child("id").setValue(user_id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }
private void seen_message(String userid)
{
    databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
            valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {

                        String sender = ds.child("sender").getValue(String.class);
                        String receiver = ds.child("receiver").getValue(String.class);
                        String id_ad = ds.child("ID_ADS").getValue(String.class);
                        if (receiver.equals(firebaseUser.getUid()) && sender.equals(userid) && id_ad.equals(ID_ADS) || receiver.equals(userid) && sender.equals(firebaseUser.getUid()) && id_ad.equals(ID_ADS)) {

                            if(!firebaseUser.getUid().equals(userid)) {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("isSeen", true);
                                ds.getRef().updateChildren(hashMap);
                            }
                        }
                        // Toast.makeText(CattingActivity.this, "message1" + Message, Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
}
    private  void sendNotification(String receiver, String username, String msg ,String ID_ADS ,String img_ad ,String name_ADS)
    {
        Toast.makeText(CattingActivity.this, "sendNotification", Toast.LENGTH_SHORT).show();
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Token token = ds.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid() , R.mipmap.ic_logo , username+" : "+msg ,"New Message"  , firebaseUser.getUid(),ID_ADS ,img_ad ,name_ADS);
                    Sender sender = new Sender(data , token.getToken());
                    dataInterface.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() == 200)
                                    {
                                        if(response.body().success != 1)
                                        {
                                            Toast.makeText(CattingActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }//else{ //Toast.makeText(CattingActivity.this, "success", Toast.LENGTH_SHORT).show();}
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void readMessage(String myid, String userid, String imgurl , String ID_ADS) {

        chatModelList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatModelList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    ChatModel chatModel = new ChatModel();
                    String sender = ds.child("sender").getValue(String.class);
                    String receiver = ds.child("receiver").getValue(String.class);
                    String Message = ds.child("Message").getValue(String.class);
                    String id_ad = ds.child("ID_ADS").getValue(String.class);
                    String str_date = ds.child("date").getValue(String.class);
                    if (receiver.equals(myid) && sender.equals(userid) && id_ad.equals(ID_ADS) || receiver.equals(userid) && sender.equals(myid) && id_ad.equals(ID_ADS)) {

                        chatModel.setMessage(Message);
                        chatModel.setReceiver(receiver);
                        chatModel.setSender(sender);
                        chatModel.setDate(str_date);
                        chatModelList.add(chatModel);
                    }
                    // Toast.makeText(CattingActivity.this, "message1" + Message, Toast.LENGTH_SHORT).show();

                }
                seen_message(userid);
                messageAdapter = new MessageAdapter(CattingActivity.this, chatModelList, imgurl);
                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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