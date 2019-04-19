package com.project.cardiacconsult.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.cardiacconsult.R;
import com.project.cardiacconsult.models.Clinic;
//import com.project.cardiacconsult.models.User;

public class PhysicianSearchActivity extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;
    private static final String TAG = PhysicianSearchActivity.class.getName();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference clinicRef, userRef;
    FirebaseRecyclerAdapter adapter;
    RecyclerView recyclerView;
    Toolbar toolbar;
    User clinicUser;

    ImageView callimage;
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physician_search);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Physician Search");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //callimage=findViewById(R.id.callimage);
        //callimage.setOnClickListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        clinicRef = firebaseDatabase.getReference("clinics");
        userRef = firebaseDatabase.getReference("users");
        recyclerView = findViewById(R.id.recyclerview);

        Query query = clinicRef.limitToFirst(20);

        FirebaseRecyclerOptions<Clinic> options = new FirebaseRecyclerOptions.Builder<Clinic>()
                .setQuery(query, Clinic.class).build();

        adapter = new FirebaseRecyclerAdapter<Clinic, PhysicianViewHolder>(options) {

            @NonNull
            @Override
            public PhysicianViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.physician_search_list_item, parent, false);

                return new PhysicianViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(final PhysicianViewHolder holder, int position, final Clinic clinic) {

                DatabaseReference singleClinicRef = getRef(position);
                Log.d(TAG, "onBindViewHolder: " + singleClinicRef.getKey());
                String key = singleClinicRef.getKey();


                userRef.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                        Log.d(TAG, "onDataChange: "+ dataSnapshot.toString());
                        clinicUser = dataSnapshot.getValue(User.class);
                        if (clinicUser != null) {
                            Log.d(TAG, "onDataChange: " + clinicUser.toString());
                            holder.username.setText(clinicUser.getuserName());
                           /* holder.callimage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    makePhoneCall(clinicUser.getPhoneNO());
                                }
                            });*/
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: No User Available");
                    }
                });


                holder.clinicname.setText(clinic.getName());
                holder.clinicaddress.setText(clinic.getAddress());
                holder.callimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        makePhoneCall(clinic.getContactNo());
                    }
                });
            }
        };

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

   /* @Override
    public void onClick(View v)
    {
        makePhoneCall();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       // super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(String number);
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }*/
    private void makePhoneCall(String number)

    {
//        String number = "7211153030";
        if (number.trim().length() > 0) {

            if (ContextCompat.checkSelfPermission(PhysicianSearchActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(PhysicianSearchActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        } else {
            Toast.makeText(PhysicianSearchActivity.this, "Number not in use", Toast.LENGTH_SHORT).show();
        }

    }

    public class PhysicianViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView clinicname;
        public TextView clinicaddress;
        public ImageView callimage;

        public PhysicianViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            clinicname = itemView.findViewById(R.id.clinicname);
            clinicaddress = itemView.findViewById(R.id.clinicaddress);
            callimage = itemView.findViewById(R.id.callimage);

            //callimage.setOnClickListener(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
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

                return true;
        }
    }
}
