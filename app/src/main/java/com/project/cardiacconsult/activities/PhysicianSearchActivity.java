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
import com.project.cardiacconsult.models.User;

public class PhysicianSearchActivity extends AppCompatActivity {

    private static final String TAG = PhysicianSearchActivity.class.getName();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    User user;
    FirebaseRecyclerAdapter adapter;
    RecyclerView recyclerView;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physician_search);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Physician Search");

        Log.e(TAG, "onCreate: 1111");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        recyclerView=findViewById(R.id.recyclerview);

        Query query= databaseReference.limitToFirst(20);

        FirebaseRecyclerOptions<User> options= new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(query,User.class).build();

        Log.e(TAG, "onCreate: 2222");

        adapter= new FirebaseRecyclerAdapter<User, PhysicianViewHolder>(options) {

            @NonNull
            @Override
            public PhysicianViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
            {
                Log.e(TAG, "onCreateViewHolder");

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.physician_search_list_item, parent, false);

                return new PhysicianViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(PhysicianViewHolder holder, int position, User model)
            {
                Log.e(TAG, "onBindViewHolder");
                holder.username.setText(model.getuserName());
                holder.clinicname.setText(model.getRole().toString());
                holder.clinicaddress.setText(model.getAddress());
            }
        };

        Log.e(TAG, "Attaching Adapter to RecyclerView");
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

            username = (TextView) itemView.findViewById(R.id.username);
            clinicname = (TextView) itemView.findViewById(R.id.clinicname);
            clinicaddress = (TextView) itemView.findViewById(R.id.clinicaddress);
            callimage = (ImageView) itemView.findViewById(R.id.callimage);


        }
    }





}
