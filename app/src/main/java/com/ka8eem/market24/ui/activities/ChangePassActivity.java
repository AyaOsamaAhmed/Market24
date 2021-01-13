package com.ka8eem.market24.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.ka8eem.market24.R;
import com.ka8eem.market24.models.UserModel;
import com.ka8eem.market24.viewmodel.UserViewModel;

public class ChangePassActivity extends AppCompatActivity {
    EditText pass , confirm , email;
    Button save;
    String email_txt , pass_txt , confirm_txt;
    boolean valid;
    UserModel retUserModel = new UserModel();
    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);
        email = (EditText)findViewById(R.id.user_text);
        pass = (EditText)findViewById(R.id.pass_text);
        confirm = (EditText)findViewById(R.id.confirm_pass_text);
        save = (Button) findViewById(R.id.btn_save);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email_txt = email.getText().toString().trim();
                pass_txt = pass.getText().toString();
                confirm_txt = confirm.getText().toString();
                valid = true;
                validData();
                if (!valid)
                    return;
                userViewModel.updatePass(email_txt, pass_txt);
                userViewModel.userUpdate.observe(ChangePassActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String response) {
                        String res = getString(R.string.check_your_mail);
                        if(response.equals("1")) {
                            firebase_resetPass(email_txt);

                        }else{Toast.makeText(ChangePassActivity.this, "no", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ChangePassActivity.this, LoginActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void validData() {
        if (email_txt.isEmpty()) {
            email.setError("");
            email.requestFocus();
            valid = false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email_txt).matches()) {
            email.setError("");
            email.requestFocus();
            valid = false;
        }
        if (pass_txt.isEmpty()) {
            pass.setError("");
            pass.requestFocus();
            valid = false;
        }
        if (pass_txt.length() < 6) {
            pass.setError("");
            pass.requestFocus();
            valid = false;
        }
        if (confirm == null || confirm_txt.isEmpty()) {
            confirm.setError("");
            confirm.requestFocus();
            valid = false;
        }
    }
    private void firebase_resetPass(String email)
    {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                           // Toast.makeText(ChangePassActivity.this, res, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ChangePassActivity.this, LoginActivity.class);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}
