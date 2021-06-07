package com.ka8eem.market24.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ka8eem.market24.Notification.Data;
import com.ka8eem.market24.Notification.MyResponse;
import com.ka8eem.market24.Notification.Sender;
import com.ka8eem.market24.Notification.Token;
import com.ka8eem.market24.R;
import com.ka8eem.market24.adapters.MessageAdapter;
import com.ka8eem.market24.interfaces.DataInterface;
import com.ka8eem.market24.models.ChatModel;
import com.ka8eem.market24.models.ConversationModel;
import com.ka8eem.market24.models.GetMessagesModel;
import com.ka8eem.market24.models.MessageModel;
import com.ka8eem.market24.models.MessagesModel;
import com.ka8eem.market24.models.NewMessageModel;
import com.ka8eem.market24.models.UserFirebaseModel;
import com.ka8eem.market24.models.UserModel;
import com.ka8eem.market24.viewmodel.ProductViewModel;
import com.ka8eem.market24.viewmodel.UserViewModel;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.ChannelEventListener;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ka8eem.market24.util.Keys.image_domain;

//https://pusher.com/tutorials/chat-kotlin-android
public class ChattingActivity extends AppCompatActivity {

    Bitmap bitmap;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    Intent intent;
    ProductViewModel productViewModel ;
    UserViewModel userViewModel;
    ValueEventListener valueEventListener ;
    MessageAdapter messageAdapter;
    List<MessageModel> chatModelList;
    RecyclerView recyclerView;
    LinearLayoutManager manager ;
    CircleImageView img_ads;
    TextView userTxt;
    ImageButton sending;
    EditText message_text;
    DataInterface dataInterface;
    String user_id  , imgAds, name_pro , type_user;
    ProgressDialog progressDialog ;
    Integer seller_id, buyer_id ,conversation_id , ID_ADS , currentItems , totalItems,scrollOutItems;
    boolean notify = false , isScroll = false;
    String currentDate , buyer_img ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);


        recyclerView = (RecyclerView) findViewById(R.id.message_recycler_view);
        recyclerView.setHasFixedSize(true);
        manager = new LinearLayoutManager(getApplicationContext());
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        img_ads = (CircleImageView) findViewById(R.id.ads_image);
        userTxt = (TextView) findViewById(R.id.username);
        sending = (ImageButton) findViewById(R.id.btn_send);
        message_text = (EditText) findViewById(R.id.message_text);

        loading();
        startChat();
        chatModelList = new ArrayList<>();
        messageAdapter = new MessageAdapter(ChattingActivity.this );
        recyclerView.setAdapter(messageAdapter);


        intent = getIntent();
        user_id = intent.getStringExtra("id_user");
        imgAds = intent.getStringExtra("IMG_ADS");
        ID_ADS = intent.getIntExtra("ID_ADS",0);
        name_pro = intent.getStringExtra("name_ADS");
        type_user = intent.getStringExtra("type_user");
        conversation_id = intent.getIntExtra("conversation_id",0);
        buyer_id = intent.getIntExtra("buyer_id",0);
        seller_id = intent.getIntExtra("seller_id",0);


        userViewModel.userData.observe(this, new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                buyer_img = userModel.getImage();

            }
        });

        userTxt.setText(name_pro);
        Picasso.get()
                .load(image_domain + imgAds)
                .placeholder(R.drawable.user)
                .into(img_ads);


        if(conversation_id !=0 ){
            userViewModel.userData(buyer_id);
            productViewModel.getMessages(conversation_id);
        }else
            progressDialog.dismiss();
         userViewModel.userData(buyer_id);

        sending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                String message = message_text.getText().toString();
                if (message != null && !message.equals("")) {
                    //sendmessage(firebaseUser.getUid(), user_id, message);
                   // NewMessageModel new_message =new NewMessageModel(type_user,message,imgAds,seller_id,buyer_id );
                    Log.d("ChattingActivity", "onClick: "+buyer_id+"-"+seller_id+"-"+ID_ADS+"-"+type_user);
                    productViewModel.sendMessage(buyer_id, seller_id, ID_ADS,message,type_user);

                } else {
                    Toast.makeText(ChattingActivity.this, "You Can't Send empty Message", Toast.LENGTH_SHORT).show();
                }
                message_text.setText("");
            }
        });

        productViewModel.mutableSendMessageModel.observe(this, new Observer< MessageModel>() {
            @Override
            public void onChanged( MessageModel messageModels) {
                DateTimeFormatter dtf = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    currentDate = dtf.format(now);
                }
/*
                MessageModel message = new MessageModel(messageModels.getId(),messageModels.getUser_id(),messageModels.getConversation_id(),
                        messageModels.getMessage(),messageModels.getType(),messageModels.getCreated_at(),messageModels.getUpdated_at(),
                        messageModels.getConversation() );
                chatModelList.add(message);
                messageAdapter.addMessage(chatModelList);
                recyclerView.scrollToPosition(chatModelList.size());

 */
           //       resetInput();
            }
        });

        productViewModel.mutableGetMessagesModel.observe(this, new Observer<GetMessagesModel>() {
            @Override
            public void onChanged(GetMessagesModel getMessagesModel) {
                setMessage(getMessagesModel.getData());

                if(getMessagesModel.getCurrent_page() != getMessagesModel.getLast_page()) {
                    productViewModel.getMessagesByPage(conversation_id,getMessagesModel.getCurrent_page()+1+"");
                }

                progressDialog.dismiss();
            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("cahttingActivity", "onScrollStateChanged: ");
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScroll = true;;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                    Log.d("cahttingActivity", "onScrolled: ");

                    currentItems = manager.getChildCount();
                    totalItems = manager.getItemCount();
                    scrollOutItems = manager.findFirstVisibleItemPosition();
                    
                    if(isScroll &&(currentItems+scrollOutItems == totalItems)){
                        getAnotherData();
                    }
            }
        });

        //  dataInterface = Client.getClient("https://fcm.googleapis.com/").create(DataInterface.class);
        // firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
       // databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user_id);
        // Query query = databaseReference.orderByChild("id").equalTo(user_id);
  /*      databaseReference.addValueEventListener(new ValueEventListener() {
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
        */
    }

    private void getAnotherData() {

        
    }

    private void resetInput() {
            // Clean text box
            userTxt.setText("");

            // Hide keyboard
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(
                    userTxt.findFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

    void setMessage(List<MessageModel> messageModels){
        for (int i=0 ; i<messageModels.size() ; i++){
            chatModelList.add(messageModels.get(i));
        }
        Log.d("chattingActivity", "setMessage: " + chatModelList.size());
        show_new_message();
    }

    void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
    }
    private void startChat() {

        PusherOptions options = new PusherOptions();
        options.setCluster("eu");

        Pusher pusher = new Pusher("1fe4a4817b317803df0e", options);

        Channel  channel = pusher.subscribe("chat");//market-24

        channel.bind("new-message", new SubscriptionEventListener() {
            @Override
            public void onEvent(PusherEvent event) {
                int len_data = event.getData().length();
                String data = event.getData().substring(11,len_data-1);
                Log.i("Pusher", "Received event with data: " + event.toString()+"--"+event.getData());
                Log.i("Successss","Pusher run on Background  "+event.getData().substring(11,len_data-1));
                Gson gson = new GsonBuilder().setLenient().create();

                MessageModel new_message = gson.fromJson(data, MessageModel.class);
                MessageModel message = new MessageModel(new_message.getId(),new_message.getUser_id(),new_message.getConversation_id(),
                        new_message.getMessage(),new_message.getType(),new_message.getCreated_at(),new_message.getUpdated_at(),
                        new_message.getConversation() );
                chatModelList.add(message);
                show_new_message();
            }
        });
        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.i("Pusher", "State changed from " + change.getPreviousState() +
                        " to " + change.getCurrentState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                Log.i("Pusher", "There was a problem connecting! " +
                        "\ncode: " + code +
                        "\nmessage: " + message +
                        "\nException: " + e
                );
            }
        }, ConnectionState.ALL);


    }

    private void show_new_message() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageAdapter.addMessage(chatModelList,buyer_img);
                recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
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
        hashMap.put("img_ad" , imgAds);
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
                 //   sendNotification(receiver, username, msg , ID_ADS , imgAds, name_pro);

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
        Toast.makeText(ChattingActivity.this, "sendNotification", Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(ChattingActivity.this, "Failed", Toast.LENGTH_SHORT).show();
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
                        //chatModelList.add(chatModel);
                    }
                    // Toast.makeText(CattingActivity.this, "message1" + Message, Toast.LENGTH_SHORT).show();
                }
                seen_message(userid);
               // messageAdapter = new MessageAdapter(ChattingActivity.this, chatModelList, imgurl,user_id);
               // recyclerView.setAdapter(messageAdapter);
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