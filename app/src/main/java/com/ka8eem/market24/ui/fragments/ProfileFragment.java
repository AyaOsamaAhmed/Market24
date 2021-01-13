package com.ka8eem.market24.ui.fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.appcompat.widget.SearchView;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ka8eem.market24.R;
import com.ka8eem.market24.models.UserModel;
import com.ka8eem.market24.ui.activities.LoginActivity;
import com.ka8eem.market24.util.Constants;
import com.ka8eem.market24.viewmodel.UserViewModel;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    DatabaseReference reference;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    private  static  final  int IMAGE_REQUEST = 1;
    private  Uri imageUri ;
    private StorageTask storageTask;
    // widgets
    Button btnSaveEdit;
    CircleImageView circleImageView;
    EditText textName, textAddress, textEmail, textPhone;
    UserModel retUserModel;
    SearchView searchView;
    ImageView filterImage;


    // vars
    private boolean edit;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private UserModel userModel;
    private String name, address, email, phone, encodedImg  , pass;
    private  int user_id;
    Bitmap bitmap;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);
      //  storageReference = FirebaseStorage.getInstance().getReference("Uploads");
        circleImageView.setClickable(false);
        if (savedInstanceState != null) {
            edit = savedInstanceState.getBoolean("edit");
        }
        if (!edit)
            btnSaveEdit.setText(getString(R.string.edit_info));
        else
            btnSaveEdit.setText(getString(R.string.save_changes));
        btnSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit) {
                    btnSaveEdit.setText(getString(R.string.edit_info));
                    updateProfile();
                    disableViews();
                } else {
                    btnSaveEdit.setText(getString(R.string.save_changes));
                    enableViews();
                }
                edit = !edit;
            }
        });
        return view;
    }

    private void updateProfile() {
        searchView = (SearchView) getActivity().findViewById(R.id.search_view);
        searchView.setVisibility(View.GONE);
        filterImage = (ImageView) getActivity().findViewById(R.id.filter_icon);
        filterImage.setVisibility(View.GONE);
        UserViewModel userViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        name = textName.getText().toString();
        address = textAddress.getText().toString();
        phone = textPhone.getText().toString();
        email = textEmail.getText().toString();



        if (encodedImg == null)
            encodedImg = "";
        UserModel model = new UserModel(pass  ,user_id , name, phone, address, encodedImg);
        userViewModel.updateProfile(model);
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        userViewModel.userUpdate.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (getActivity() != null) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), getString(R.string.changes_saved), Toast.LENGTH_SHORT).show();
                    userViewModel.login(email, pass);
                    userViewModel.userModel.observe(ProfileFragment.this, new Observer<UserModel>() {
                        @Override
                        public void onChanged(UserModel userModel) {
                            retUserModel = userModel;
                            if (retUserModel.getExist().equals("yes")) {
                                Gson gson = new Gson();
                                String json = gson.toJson(retUserModel);
                                editor.putString("USER_MODEL", json);
                                editor.putBoolean("LOGGED_IN", true);
                                editor.putBoolean("REGISTERED", true);
                                editor.commit();
                                editor.apply();

                                /*if(storageTask != null &&storageTask.isInProgress())
                                {
                                    Toast.makeText(getContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
                                }else{
                                    uploadImage();
                                }*/
                            } else {
                             //   Toast.makeText(getContext(), getString(R.string.user_not_exist), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
                }

            }
        });

    }


    private void enableViews() {
        textAddress.setEnabled(true);
        textEmail.setVisibility(View.INVISIBLE);
        textName.setEnabled(true);
        textPhone.setEnabled(true);
        circleImageView.setClickable(true);
    }

    private void disableViews() {
        textAddress.setEnabled(false);
        textEmail.setVisibility(View.VISIBLE);
        textEmail.setEnabled(false);
        textName.setEnabled(false);
        textPhone.setEnabled(false);
        circleImageView.setClickable(false);
    }

    private void initViews(View view) {
        preferences = getContext().getSharedPreferences(Constants.SHARED, Context.MODE_PRIVATE);
        editor = preferences.edit();
        getUserFromShared();
        btnSaveEdit = view.findViewById(R.id.btn_save_changes);
        textPhone = view.findViewById(R.id.phone_profile);
        textName = view.findViewById(R.id.user_name_profile);
        textEmail = view.findViewById(R.id.email_profile);
        textAddress = view.findViewById(R.id.address_profile);
        circleImageView = view.findViewById(R.id.image_profile);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!v.isClickable())
                    return;
                CropImage.activity().start(getContext(), ProfileFragment.this);

            }
        });
        pass = userModel.getPassword();
        user_id = userModel.getUserId();
        textAddress.setText(userModel.getAddress());
        textEmail.setText(userModel.getEmail());
        textName.setText(userModel.getUserName());
        textPhone.setText(userModel.getPhone());
        if (userModel.getImage() == null || userModel.getImage().equals("")){
            circleImageView.setImageResource(R.drawable.ic_person);}
        else{
         //   Toast.makeText(getContext(), userModel.getImage(), Toast.LENGTH_SHORT).show();
            new GetImageFromUrl(circleImageView).execute(userModel.getImage());
            //Glide.with(ProfileFragment.this).load(userModel.getImage()).into(circleImageView);
            }


    }

    private void getUserFromShared() {
        Gson gson = new Gson();
        String json = preferences.getString("USER_MODEL", null);
        Type type = new TypeToken<UserModel>() {
        }.getType();
        userModel = gson.fromJson(json, type);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("edit", edit);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode ==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK)
            {
                Uri imageUrl = result.getUri();
                try {
                     bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUrl);
                    ByteArrayOutputStream byteArrayOutputStreamObject ;
                    byteArrayOutputStreamObject = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStreamObject);
                    byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
                    encodedImg = Base64.encodeToString(byteArrayVar,Base64.DEFAULT);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                circleImageView.setImageURI(imageUrl);
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception e = result.getError();

            }
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

/*private  String getFileExtension(Uri uri){
    ContentResolver contentResolver = getContext().getContentResolver();
    MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
    return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
}*/

   /* private  void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading");
        pd.show();

        if(imageUri != null)
        {
            final StorageReference file = storageReference.child(System.currentTimeMillis()
            +"."+getFileExtension(imageUri));

            storageTask = file.putFile(imageUri);
            storageTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot , Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                   if(!task.isSuccessful())
                   {
                       throw task.getException();
                   }
                   return file.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri downloaduri = task.getResult();
                        String mUri = downloaduri.toString();
                        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                        HashMap<String , Object> map = new HashMap<>();
                        map.put("imageURL" , mUri);
                        reference.updateChildren(map);
                        pd.dismiss();
                    }else{
                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }else{
            Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
        }
}*/

}
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode ==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK)
            {
                imageUrl = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                actual_photo.setImageURI(imageUrl);
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception e = result.getError();

            }
        }


    }  */
