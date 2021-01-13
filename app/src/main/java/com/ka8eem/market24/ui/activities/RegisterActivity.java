package com.ka8eem.market24.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.ka8eem.market24.R;
import com.ka8eem.market24.models.RegisterResponse;
import com.ka8eem.market24.models.UserModel;
import com.ka8eem.market24.util.Constants;
import com.ka8eem.market24.viewmodel.ProductViewModel;
import com.ka8eem.market24.viewmodel.UserViewModel;

import java.util.HashMap;

import okhttp3.ResponseBody;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    // widgets
    EditText editTextName, editTextPassword, editTextConfirmPassword, editTextPhone, editTextAddress, editTextEmail;
    Button btnRegister;
    TextView textView;

    // vars
    String name, password, confirmPassword, address, phone, email;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    UserViewModel userViewModel;
    UserModel userModel;
    ProductViewModel productViewModel;
    boolean valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FirebaseApp.initializeApp(this.getApplicationContext());
        initViews();
    }

    private void initViews() {
        valid = true;
        preferences = getSharedPreferences(Constants.SHARED, MODE_PRIVATE);
        editor = preferences.edit();
        productViewModel =  ViewModelProviders.of(this).get(ProductViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        editTextName = findViewById(R.id.register_name_edit_text);
        editTextPassword = findViewById(R.id.register_password_edit_text);
        editTextAddress = findViewById(R.id.register_address_edit_text);
        editTextPhone = findViewById(R.id.register_phone_edit_text);
        editTextConfirmPassword = findViewById(R.id.register_confirm_password_edit_text);
        editTextEmail = findViewById(R.id.register_email_edit_text);
        btnRegister = findViewById(R.id.register_btn);
        textView = findViewById(R.id.login_text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = editTextName.getText().toString();
                password = editTextPassword.getText().toString();
                address = editTextAddress.getText().toString();
                phone = editTextPhone.getText().toString().trim();
                confirmPassword = editTextConfirmPassword.getText().toString();
                email = editTextEmail.getText().toString().trim();
                valid = true;
                validData();
                if (!valid)
                    return;
                userModel = new UserModel(name, phone, address, email, password);
                userViewModel.register(userModel);
                userViewModel.userModelRegister.observe(RegisterActivity.this, new Observer<RegisterResponse>() {
                    @Override
                    public void onChanged(RegisterResponse registerResponse) {
                        String res = getString(R.string.check_your_mail);
                        String s = registerResponse.getRes();
                        if (s.equals("1")) {
                            Gson gson = new Gson();
                            String json = gson.toJson(userModel);
                            editor.putString("USER_MODEL", json);
                            editor.commit();
                            editor.apply();

                            Toast.makeText(RegisterActivity.this, res, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else if (s.equals("0")) {
                            res = getString(R.string.user_exist);
                        } else if (s.equals("2")) {
                            res = getString(R.string.check_your_mail);
                        } else
                            res = getString(R.string.something_wrong);
                        Toast.makeText(RegisterActivity.this, res, Toast.LENGTH_LONG).show();
                    }
                });
                // TODO loading with progress par while creating the user
            }
        });
    }



    private void validData() {
        if (name == null || name.isEmpty()) {
            editTextName.setError("");
            editTextName.requestFocus();
            valid = false;
        }
        if (phone == null || phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()) {
            editTextPhone.setError("");
            editTextPhone.requestFocus();
            valid = false;
        }
        if (address == null || address.isEmpty()) {
            editTextAddress.setError("");
            editTextAddress.requestFocus();
            valid = false;
        }
        if (email == null || email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("");
            editTextEmail.requestFocus();
            valid = false;
        }
        if (password == null || password.isEmpty()) {
            editTextPassword.setError("");
            editTextPassword.requestFocus();
            valid = false;
        }
        if (password == null || password.length() < 6) {
            editTextPassword.setError(getString(R.string.check_pass_length));
            editTextPassword.requestFocus();
            valid = false;
        }
        if (confirmPassword == null || confirmPassword.isEmpty()) {
            editTextConfirmPassword.setError("");
            editTextConfirmPassword.requestFocus();
            valid = false;
        }
        if (confirmPassword == null || !confirmPassword.equals(password)) {
            editTextConfirmPassword.requestFocus();
            editTextConfirmPassword.setError(getString(R.string.pass_not_matchs));
            valid = false;
        }
    }

    private void createUser() {
     /*   LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
        TextView tv = (TextView) layout.findViewById(R.id.txtvw);
        tv.setText(response);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();*/
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}