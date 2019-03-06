package com.project.cardiacconsult.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.cardiacconsult.R;
import com.project.cardiacconsult.models.Feedback;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    Feedback feedback;
    RatingBar ratingBar1, ratingBar2, ratingBar3, ratingBar4;
    Button btfeedback;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Feedback");


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

        firebaseUser = auth.getCurrentUser();
        DatabaseReference feedbackRef = database.getReference("feedback");
        feedbackRef.child(firebaseUser.getUid()).setValue(feedback);

    }
}
