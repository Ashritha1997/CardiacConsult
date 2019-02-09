package com.project.cardiacconsult.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.project.cardiacconsult.R;
import com.project.cardiacconsult.utils.CryptWithMD5;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.apache.commons.lang3.StringUtils;


public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = RegistrationActivity.class.getName();
    TextView tvLabelSigin;
    FirebaseAuth auth;
    EditText edtEmail, edtPassword;
    Button btnRegister;
    String email, password;
    ProgressBar pBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance();

        findViewById();

        setTitle("Registration");


    }

    private void findViewById() {
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        tvLabelSigin = findViewById(R.id.tvLabel_signin);
        tvLabelSigin.setOnClickListener(this);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        pBar = findViewById(R.id.progressbar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvLabel_signin:
                Intent intent = new Intent(RegistrationActivity.this, SigninActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

            case R.id.btnRegister:
                registerUserWithEmail();
                break;
        }
    }

    private void registerUserWithEmail() {
        
        pBar.setVisibility(View.VISIBLE);

        if (validateUserData()) {
            String passwordHash = CryptWithMD5.cryptWithMD5(password);
            auth.createUserWithEmailAndPassword(email, passwordHash)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (pBar.getVisibility() == View.VISIBLE) {
                                pBar.setVisibility(View.INVISIBLE);
                            }
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "User Registered. Sign In Please.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegistrationActivity.this, SigninActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else{
                                Log.w("Authentication", "onComplete: " + task);
                                Toast.makeText(getApplicationContext(), "Registration Failed, Try Again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(), "Enter Proper Data",Toast.LENGTH_SHORT).show();
        }



    }

    private boolean validateUserData() {

        email = edtEmail.getText().toString();
        password = edtPassword.getText().toString();

        if (email.matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$") && !StringUtils.isBlank(password) && StringUtils.length(password) >= 6) {
            return true;
        }else{
            return false;
        }


    }
}
