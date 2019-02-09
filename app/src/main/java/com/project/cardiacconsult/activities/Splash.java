package com.project.cardiacconsult.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.project.cardiacconsult.R;
import com.project.cardiacconsult.utils.SharedPrefHelper;
import com.google.firebase.auth.FirebaseAuth;


public class Splash extends AppCompatActivity {

    ProgressBar progressBar;
    FirebaseAuth auth;
    SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        auth = FirebaseAuth.getInstance();

        sharedPrefHelper = new SharedPrefHelper(Splash.this);

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {

                        if (auth.getCurrentUser() == null) {
                            startActivity(new Intent(Splash.this, SigninActivity.class));
                        }else{
                            if (sharedPrefHelper.checkUserRegistration()) {
                                startActivity(new Intent(Splash.this, HomeActivity.class));
                            }else{
                                startActivity(new Intent(Splash.this, UserProfileDetailsActivity.class));
                            }
                        }
                        finish();
                    }
                }, 3000);
    }
}
