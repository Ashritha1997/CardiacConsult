package com.project.cardiacconsult.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.cardiacconsult.R;
import com.project.cardiacconsult.models.Feedback;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {

    RatingBar ratingBar,ratingBar2,ratingBar3,ratingBar4;
    Button btfeedback;

    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;

   Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Feedback");

        findViewById();

        //auth = FirebaseAuth.getInstance();
        //firebaseDatabase = FirebaseDatabase.getInstance();
        //userReference = firebaseDatabase.getReference("feedback");

        //String userUID = auth.getCurrentUser().getUid();
        //firebaseUser = auth.getCurrentUser();

    }
   private void findViewById() {

        ratingBar = findViewById(R.id.ratingBar);
        ratingBar2 = findViewById(R.id.ratingBar2);
        ratingBar3 = findViewById(R.id.ratingBar3);
        ratingBar4 = findViewById(R.id.ratingBar4);

        btfeedback = findViewById(R.id.btfeedback);
        btfeedback.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        findViewById();
        float overallrating = ratingBar.getRating();
        float accuracy = ratingBar2.getRating();
        float diagnosis = ratingBar3.getRating();
        float phyrating = ratingBar4.getRating();

        Feedback feedback=new Feedback(overallrating,accuracy,diagnosis,phyrating);
        storefeedbackdata(feedback);

    }

    public void storefeedbackdata(Feedback feedback)
    {

       // String userUID = auth.getCurrentUser().getUid();
        auth = FirebaseAuth.getInstance();

        firebaseUser = auth.getCurrentUser();
        DatabaseReference feedbackRef = database.getReference("feedback");
        feedbackRef.child(firebaseUser.getUid()).setValue(feedback);

    }
}
