package com.ka8eem.market24.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.ka8eem.market24.R;
import com.ka8eem.market24.models.ForgetPasswordResponse;
import com.ka8eem.market24.models.UploadImageModel;
import com.ka8eem.market24.models.UserModel;
import com.ka8eem.market24.repository.DataClient;
import com.ka8eem.market24.util.Constants;
import com.ka8eem.market24.viewmodel.UserViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    // widget
    EditText userName, password;
    Button btnLogin;
    TextView textRegister  , forgetPass;

    // vars
    String email, pass;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    UserViewModel userViewModel;
    UserModel retUserModel;
    boolean valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        valid = true;
        preferences = getSharedPreferences(Constants.SHARED, MODE_PRIVATE);
        editor = preferences.edit();
        userName = findViewById(R.id.user_text);
        password = findViewById(R.id.login_pass_text);
        forgetPass = findViewById(R.id.forget_pass);
        btnLogin = findViewById(R.id.btn_login);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        textRegister = findViewById(R.id.txt_register);
        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = userName.getText().toString().trim();
                pass = password.getText().toString();
                valid = true;
                validData();
                if (!valid)
                    return;
                userViewModel.login(email, pass);
                userViewModel.userModel.observe(LoginActivity.this, new Observer<UserModel>() {
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
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.user_not_exist), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
            }
        });
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               final Dialog dialog = new Dialog(LoginActivity.this);
                dialog.setContentView(R.layout.forget_password_dialog);
                
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                EditText email = dialog.findViewById(R.id.email);
                Button send = dialog.findViewById(R.id.send);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ForgetPassword(email.getText().toString());

                        dialog.dismiss();
                    }
                });
                dialog.show();



             //   Intent intent = new Intent(LoginActivity.this , ChangePassActivity.class);
              //  startActivity(intent);
            }
        });
    }


    public void ForgetPassword(String email) {
        DataClient.getINSTANCE().forgetPassword(email).enqueue(new Callback<ForgetPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgetPasswordResponse> call, Response<ForgetPasswordResponse> response) {
               if(response.body() != null) {
                   if (response.body().getStatus()) {
                       Toast.makeText(LoginActivity.this, response.message()+"", Toast.LENGTH_SHORT).show();
                   }
               }else{
                   Toast.makeText(LoginActivity.this, response.message()+" This email", Toast.LENGTH_SHORT).show();
               }

            }

            @Override
            public void onFailure(Call<ForgetPasswordResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, getString(R.string.user_not_exist), Toast.LENGTH_SHORT).show();
                Log.e("Forget Password error", t.getMessage());
            }
        });
    }

    public void login_firebase(String Email, String pass) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(Email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                }
            }
        });
    }

    private void validData() {
        if (email.isEmpty()) {
            userName.setError("");
            userName.requestFocus();
            valid = false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userName.setError("");
            userName.requestFocus();
            valid = false;
        }
        if (pass.isEmpty()) {
            password.setError("");
            password.requestFocus();
            valid = false;
        }
        if (pass.length() < 6) {
            password.setError("");
            password.requestFocus();
            valid = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean logged = getSharedPreferences(Constants.SHARED, MODE_PRIVATE).getBoolean("LOGGED_IN", false);
        if (logged) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}