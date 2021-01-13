package com.ka8eem.market24.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ka8eem.market24.Notification.Token;
import com.ka8eem.market24.R;
import com.ka8eem.market24.adapters.AdsAdapter;
import com.ka8eem.market24.adapters.FavouriteAdapter;
import com.ka8eem.market24.adapters.UserFirebaseAdapter;
import com.ka8eem.market24.models.ChatModel;
import com.ka8eem.market24.models.ChatlistModel;
import com.ka8eem.market24.models.ProductModel;
import com.ka8eem.market24.models.SpecialInfoModel;
import com.ka8eem.market24.models.UserFirebaseModel;
import com.ka8eem.market24.models.UserModel;
import com.ka8eem.market24.ui.activities.ProductDetails;
import com.ka8eem.market24.util.Constants;
import com.ka8eem.market24.viewmodel.ProductViewModel;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static android.content.Context.MODE_PRIVATE;

public class AllChatsFragment extends Fragment {
    String date_st="";
    public static final String DATE_PARSING_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
Date date;
    public AllChatsFragment() {
        // Required empty public constructor
    }
    ProductViewModel productVM;
    SharedPreferences preferences;
    UserModel userModel;
    Gson gson;
    DateFormat currentDateTimeString = null;
    SearchView searchView;
    ImageView filterImage;

    RecyclerView recyclerView;
    private UserFirebaseAdapter userfirebaseAdapter;
    private List<UserFirebaseModel> Muser ;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    //ArrayList<String> arr_st= new ArrayList<>();
    final List<String> userslist= new ArrayList<>();
    final List<String> _id_ad= new ArrayList<>();
    final List<String> _img= new ArrayList<>();
    final List<String> _name_pro= new ArrayList<>();
    final List<String> _str_date= new ArrayList<>();
    final List<Integer> _num_seen= new ArrayList<>();
    final List<Boolean> _isSeen= new ArrayList<>();

    //final List<List<String>> _ads_id= new ArrayList<>();
   // final List<String> _ads_img= new ArrayList<>();
    UserFirebaseModel userFirebaseModel;
    private List<ChatlistModel> My_Chats ;
    private List<SpecialInfoModel> specialInfoModels = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allchats, container, false);
        searchView = (SearchView) getActivity().findViewById(R.id.search_view);
        searchView.setVisibility(View.GONE);
        filterImage = (ImageView) getActivity().findViewById(R.id.filter_icon);
        filterImage.setVisibility(View.GONE);
        recyclerView = view.findViewById(R.id.chats_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ////get ads
     /*   preferences = getContext().getSharedPreferences(Constants.SHARED, Context.MODE_PRIVATE);
        gson = new Gson();
        Type type = new TypeToken<UserModel>() {
        }.getType();
        String json = preferences.getString("USER_MODEL", null);
        userModel = gson.fromJson(json, type);

        // 1
        productVM.getMyAds_Message(userModel.getUserId());
        productVM.MyAds_Message.observe((getActivity()), new Observer<ArrayList<SpecialInfoModel>>() {
            @Override
            public void onChanged(ArrayList<SpecialInfoModel> specialInfoModels) {
                if (getActivity() != null) {
                    if (specialInfoModels != null) {
                        specialInfoModels.addAll(specialInfoModels);
                    } else {
                        if (getActivity() != null && getContext() != null)
                            Toast.makeText(getContext(), getString(R.string.no_ads), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });*/
        
        //get message
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userslist.clear();
                _id_ad.clear();
                _img.clear();
                _name_pro.clear();
                _str_date.clear();
                _isSeen.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);

                    if(chatModel.getSender().equals(firebaseUser.getUid()))
                    {
                        userslist.add(chatModel.getReceiver());
                        _id_ad.add( chatModel.getID_ADS() );
                        _img.add( chatModel.getImg_ad());
                        _name_pro.add( chatModel.getName_ADS());
                        _str_date.add(chatModel.getDate());
                        boolean seen = dataSnapshot.child("isSeen").getValue(boolean.class);

                            _isSeen.add(seen);

                    }
                    if(chatModel.getReceiver().equals(firebaseUser.getUid()))
                    {
                        userslist.add(chatModel.getSender());
                        _id_ad.add( chatModel.getID_ADS() );
                        _img.add( chatModel.getImg_ad());
                        _name_pro.add( chatModel.getName_ADS());
                        _str_date.add(chatModel.getDate());
                        boolean seen = dataSnapshot.child("isSeen").getValue(boolean.class);

                            _isSeen.add(seen);

                    }


                }
               // Toast.makeText(getContext(), "1 :// "+userslist.size()+"", Toast.LENGTH_SHORT).show();
               /* Collections.sort(_str_date, Collections.reverseOrder());
                Collections.sort(userslist, Collections.reverseOrder());
                Collections.sort(_id_ad, Collections.reverseOrder());
                Collections.sort(_img, Collections.reverseOrder());
                Collections.sort(_name_pro, Collections.reverseOrder());
                //Collections.sort(_str_date, Collections.reverseOrder());*/
                read_chat();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        updateToken(FirebaseInstanceId.getInstance().getToken());
        return view;
    }
    private void updateToken(String token)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(firebaseUser.getUid()).setValue(token1);
    }

    public void read_chat(){
        My_Chats = new ArrayList<>();
        Muser = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Muser.clear();
                My_Chats.clear();
                boolean b_user = false;
                boolean b_ad = false;
                boolean b_img = false;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserFirebaseModel user = new UserFirebaseModel();
                    String user_id = dataSnapshot.child("id").getValue(String.class);
                    String name = dataSnapshot.child("name").getValue(String.class);
                    //  String profile_img = dataSnapshot.child("profile_img").getValue(String.class);

                    user.setUser_id(user_id);
                    //  user.setProfile_image(profile_img);
                    user.setUsername(name);

                    Muser.add(user);
                }
                //Muser : all user
                //userlist : all user who message with me
                //_ads_id : ads chat

                for ( int o = userslist.size()-1  ; o>=0 ; o--) {

                     //   for (int id_ad = 0 ; id_ad <_id_ad.size(); id_ad++) {

                                for (int i = Muser.size() -1 ; i >=0  ; i--) {
                                    if (Muser.get(i).getUser_id().equals(userslist.get(o))) {
                                        ChatlistModel chatlistModel = new ChatlistModel();
                                        //_num_seen.clear();

                                        if (My_Chats.size() != 0) {

                                            for (int j = 0; j <My_Chats.size() ; j++) {

                                                if (My_Chats.get(j).getId_user().equals(userslist.get(o)) && My_Chats.get(j).getId_ads().equals(_id_ad.get(o))) {

                                                    b_user = true;
                                                    break;
                                                } else {
                                                    b_user = false;

                                                }
                                            }
                                        } else {



                                            chatlistModel.setImg_ad(_img.get(o));
                                            chatlistModel.setName(Muser.get(i).getUsername());
                                            chatlistModel.setName_product(_name_pro.get(o));
                                            chatlistModel.setId_user(Muser.get(i).getUser_id());
                                            chatlistModel.setId_ads(_id_ad.get(o));
                                            date_st = _str_date.get(o);
                                            SimpleDateFormat formatter = new SimpleDateFormat("h:mm:ss  dd MM yyyy");
                                            try {
                                                date=formatter.parse(_str_date.get(o));
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            chatlistModel.setStr_date(date);
                                            b_ad = true;

                                            My_Chats.add(chatlistModel);
                                            break;
                                        }

                                        if (b_user == false) {

                                            b_user = true;
                                          //  chatlistModel.setNum_seen(count);
                                            chatlistModel.setImg_ad(_img.get(o));
                                            chatlistModel.setName(Muser.get(i).getUsername());
                                            chatlistModel.setName_product(_name_pro.get(o));
                                            chatlistModel.setId_user(Muser.get(i).getUser_id());
                                            chatlistModel.setId_ads(_id_ad.get(o));
                                             date_st = _str_date.get(o);
                                            SimpleDateFormat formatter = new SimpleDateFormat("h:mm:ss  dd MM yyyy");
                                            try {
                                                date=formatter.parse(_str_date.get(o));
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            chatlistModel.setStr_date(date);

                                            My_Chats.add(chatlistModel);
                                            b_ad = true;
                                        }
                                    }
                                }


                }
                int count = 0;

                    for (int j = 0; j < My_Chats.size(); j++) {

                        for ( int o = 0  ; o< userslist.size() ; o++) {
                        if (My_Chats.get(j).getId_user().equals(userslist.get(o)) && My_Chats.get(j).getId_ads().equals(_id_ad.get(o))) {


                                if (_isSeen.get(o) == false) {
                                //    Toast.makeText(getContext(), "in", Toast.LENGTH_SHORT).show();
                                    count++;
                                }

                                if(o ==  userslist.size()-1)
                                {
                                    My_Chats.get(j).setNum_seen(count);
                                }
                        }
                    }
                    count=0;
                }

               // My_Chats.sort(Comparator.comparing(ChatlistModel::getStr_date).reversed());
                //Collections.sort(My_Chats, Collections.reverseOrder());
                userfirebaseAdapter = new UserFirebaseAdapter(getContext(), My_Chats);
                recyclerView.setAdapter(userfirebaseAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}