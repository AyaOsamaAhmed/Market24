package com.ka8eem.market24.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ka8eem.market24.Notification.Client;
import com.ka8eem.market24.Notification.Data;
import com.ka8eem.market24.Notification.MyResponse;
import com.ka8eem.market24.Notification.Sender;
import com.ka8eem.market24.Notification.Token;
import com.ka8eem.market24.R;
import com.ka8eem.market24.interfaces.DataInterface;
import com.ka8eem.market24.models.ProductModel;
import com.ka8eem.market24.models.RequestModel;
import com.ka8eem.market24.models.UserFirebaseModel;
import com.ka8eem.market24.models.UserModel;
import com.ka8eem.market24.ui.activities.CattingActivity;
import com.ka8eem.market24.ui.activities.ProductDetails;
import com.ka8eem.market24.viewmodel.ProductViewModel;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageDialog extends AppCompatDialogFragment {


    private ProductModel productModel;
    private String userId;
    boolean notify= false;;
    public MessageDialog(ProductModel productModel, String userId) {
        this.productModel = productModel;
        this.userId = userId;
    }

    EditText textMessage;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    String owner_id ,ID_ADS , one_img_ad , name_ADS;
    //dialog_Message
    Button send , cancel;
    EditText first_message ;
    AlertDialog alertDialog;
    DataInterface dataInterface;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        dataInterface = Client.getClient("https://fcm.googleapis.com/").create(DataInterface.class);
        View view = inflater.inflate(R.layout.message_dialog, null);
        send = view.findViewById(R.id.btn_send);
        cancel = view.findViewById(R.id.btn_cancel);
        first_message = view.findViewById(R.id.message_text);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        owner_id = getArguments().getString("owner_ad_id");
        ID_ADS = getArguments().getString("ID_ADS");
        one_img_ad = getArguments().getString("IMG_ADS");
        name_ADS = getArguments().getString("name_ADS");
        //Toast.makeText(getContext(), "owner"+owner_id, Toast.LENGTH_SHORT).show();
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setView(view).setTitle(R.string.first);
        alertDialog = dialog.show();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                String message = first_message.getText().toString();
                if (message != null && !message.equals("")) {
                    makeRequest();
                } else {
                    Toast.makeText(getContext(), "You Can't Send empty Message", Toast.LENGTH_SHORT).show();
                }


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        return alertDialog;

        /////
       /* AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setTitle(getString(R.string.sent_message))
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        makeRequest();
                    }
                });
        return builder.create();*/
    }

    private void makeRequest() {
        ProductViewModel productViewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);
        RequestModel requestModel = new RequestModel(productModel.getProductID() + "", userId, productModel.getUserID() + "", productModel.getPrice());

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        productViewModel.requestProduct(requestModel);
        productViewModel.mutableRequestProduct.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (getActivity() != null) {
                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    String message = first_message.getText().toString();
                    // TODO send message with firebase and open chat activity
                    sendmessage(firebaseUser.getUid() , owner_id , message , ID_ADS , one_img_ad);
                }
            }
        });
    }

    private void sendmessage(String sender , String receiver , String Message , String ID_ADS ,String img_ad ){
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm:ss  dd MM yyyy");
        String currentDateandTime = sdf.format(new Date());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("ID_ADS" , ID_ADS);
        hashMap.put("name_ADS" , name_ADS);
        hashMap.put("img_ad" , img_ad);
        hashMap.put("sender" , sender);
        hashMap.put("receiver" , receiver);
        hashMap.put("Message" , Message);
        hashMap.put("date" , currentDateandTime);
        hashMap.put("isSeen" , false);
        databaseReference.child("Chats").push().setValue(hashMap);
        alertDialog.dismiss();

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
                    sendNotification(receiver, username, msg , ID_ADS , img_ad , name_ADS);

                } notify = false;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Intent i = new Intent(getContext() , CattingActivity.class);
        i.putExtra("id_user" , receiver);
        i.putExtra("ID_ADS",ID_ADS);
        i.putExtra("IMG_ADS", one_img_ad);
        //i.putExtra("name_owner", user.getName());
        i.putExtra("name_ADS",name_ADS );

        startActivity(i);

    }

    private  void sendNotification(String receiver, String username, String msg ,String ID_ADS ,String img_ad ,String name_ADS)
    {
        //Toast.makeText(getContext(), "sendNotification", Toast.LENGTH_SHORT).show();
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Token token = ds.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid() , R.mipmap.ic_logo , username+" : "+msg ,"New Message"  , firebaseUser.getUid() ,ID_ADS ,img_ad ,name_ADS );
                    Sender sender = new Sender(data , token.getToken());
                    dataInterface.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() == 200)
                                    {
                                        if(response.body().success != 1)
                                        {
                                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
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
}

