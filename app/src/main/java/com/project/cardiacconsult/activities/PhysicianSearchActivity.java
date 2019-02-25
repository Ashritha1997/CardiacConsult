package com.project.cardiacconsult.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.project.cardiacconsult.R;
import com.project.cardiacconsult.models.Clinic;
import com.project.cardiacconsult.models.User;

public class PhysicianSearchActivity extends AppCompatActivity {

    private static final String TAG = PhysicianSearchActivity.class.getName();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseRecyclerAdapter adapter;
    RecyclerView recyclerView;
    Toolbar toolbar;

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

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("clinics");
        recyclerView=findViewById(R.id.recyclerview);

        Query query= databaseReference.limitToFirst(20);

        FirebaseRecyclerOptions<Clinic> options= new FirebaseRecyclerOptions.Builder<Clinic>()
                .setQuery(query,Clinic.class).build();

        adapter= new FirebaseRecyclerAdapter<Clinic, PhysicianViewHolder>(options) {

            @NonNull
            @Override
            public PhysicianViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
            {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.physician_search_list_item, parent, false);

                return new PhysicianViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(PhysicianViewHolder holder, int position, Clinic clinic)
            {

                //holder.username.setText(clinic.getName());
                holder.clinicname.setText(clinic.getName());
                holder.clinicaddress.setText(clinic.getAddress());
            }
        };

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    public class PhysicianViewHolder extends RecyclerView.ViewHolder{
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


        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
