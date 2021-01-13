package com.ka8eem.market24.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.ka8eem.market24.R;
import com.ka8eem.market24.adapters.GalleryImageAdapter;
import com.ka8eem.market24.adapters.MessageAdapter;
import com.ka8eem.market24.interfaces.DataInterface;
import com.ka8eem.market24.models.ChatModel;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {


    // widgets
    ViewPager viewPager;
    TextView textClose;

    // vars
    ArrayList<String> imgArray;
    GalleryImageAdapter galleryImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallary_layout);
        Intent intent = getIntent();
        imgArray = intent.getStringArrayListExtra("array_img_string");
        viewPager = findViewById(R.id.gallery_pager);
        textClose = findViewById(R.id.close_gallery);
        textClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        galleryImageAdapter = new GalleryImageAdapter();
        galleryImageAdapter.setList(imgArray);
        viewPager.setAdapter(galleryImageAdapter);
    }
}