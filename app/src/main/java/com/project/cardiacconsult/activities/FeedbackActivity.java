package com.project.cardiacconsult.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.cardiacconsult.R;
import com.project.cardiacconsult.models.Feedback;


public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = FeedbackActivity.class.getName();
    android.support.v7.widget.Toolbar toolbar;
    Feedback feedback;
    RatingBar ratingBar1, ratingBar2, ratingBar3, ratingBar4;
    Button btfeedback;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference feedbackRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.app_bar_activity_feedback);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Feedback");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        firebaseUser = auth.getCurrentUser();
        feedbackRef = database.getReference("feedback");


        ratingBar1=findViewById(R.id.ratingBar1);
        ratingBar2=findViewById(R.id.ratingBar2);
        ratingBar3=findViewById(R.id.ratingBar3);
        ratingBar4=findViewById(R.id.ratingBar4);

        btfeedback=findViewById(R.id.btfeedback);
        btfeedback.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


        float overallrating=ratingBar1.getRating();
        float accuracy=ratingBar2.getRating();
        float diagnosis=ratingBar3.getRating();
        float phyrating=ratingBar4.getRating();

        feedback=new Feedback(overallrating, accuracy, diagnosis, phyrating);
        storefeedbackdetails(feedback);

    }

    public void storefeedbackdetails(Feedback feedback)
    {
        Log.d(TAG, "storefeedbackdetails: " + feedbackRef.toString());
        Log.d(TAG, "storefeedbackdetails: " + feedback.toString());

        feedbackRef.child(firebaseUser.getUid()).setValue(feedback);

        Toast.makeText(getApplicationContext(), "Feedback Submitted Successfully.", Toast.LENGTH_SHORT).show();
        finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                onBackPressed();

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
