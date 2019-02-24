package com.project.cardiacconsult.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.cardiacconsult.R;
import com.project.cardiacconsult.adapters.MainMenuAdapter;
import com.project.cardiacconsult.models.Clinic;
import com.project.cardiacconsult.models.User;
import com.project.cardiacconsult.utils.RoleEnum;
import com.project.cardiacconsult.utils.SharedPrefHelper;


public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    ProgressBar progressBar;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference clinicDatabaseReference;
    SharedPrefHelper sharedPrefHelper;

    User currentUser;
    Clinic clinicInfo;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar tool;
    MainMenuAdapter mainMenuAdapter;

    GridView gridView;

    TextView tvUserEmail, tvUserContact, tvUserAddress;
    TextView tvClinicName, tvClinicAddress, tvClinicContact, tvClinicLicNo, tvClinicInfoTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        progressBar = findViewById(R.id.progressbar);

        tool = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tool);
        tool.setTitle("HOME");

        drawer=(DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView=(NavigationView)findViewById(R.id.nav_view);

        // TOGGEL ACTIONBAR EVENT WITH ICON
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, tool, R.string.openDrawer, R.string.closeDrawer);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        drawer.openDrawer(GravityCompat.START);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        sharedPrefHelper = new SharedPrefHelper(HomeActivity.this);

        if (auth.getCurrentUser() != null) {
            displayUserInfoInNavBar();
        }

        gridView = findViewById(R.id.grid_view);
        mainMenuAdapter = new MainMenuAdapter(HomeActivity.this);

        gridView.setAdapter(mainMenuAdapter);

    }

    private void displayUserInfoInNavBar() {

        findViewByIdNavBar();

        currentUser = sharedPrefHelper.getUserFromPref();
        tvUserEmail.setText(currentUser.getEmail());
        tvUserAddress.setText(currentUser.getAddress());
        tvUserContact.setText(currentUser.getPhoneNO());

        if (currentUser.getRole() == RoleEnum.PHYSICIAN) {
            Toast.makeText(getApplicationContext(), "Is Physician", Toast.LENGTH_LONG).show();
            getAndDisplayClinicInfoInNavBar(sharedPrefHelper.getUidFromPref());

        } else {
            Toast.makeText(getApplicationContext(),"Is Not Physician", Toast.LENGTH_LONG).show();
            tvClinicInfoTitle.setVisibility(View.GONE);
            tvClinicName.setVisibility(View.GONE);
            tvClinicAddress.setVisibility(View.GONE);
            tvClinicContact.setVisibility(View.GONE);
            tvClinicLicNo.setVisibility(View.GONE);
        }
    }

    private void getAndDisplayClinicInfoInNavBar(String uidFromPref) {

        clinicDatabaseReference = firebaseDatabase.getReference("clinics");
        clinicDatabaseReference.child(uidFromPref).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("DataSnapshot", "onDataChange: " + dataSnapshot.toString());

                clinicInfo = dataSnapshot.getValue(Clinic.class);
                sharedPrefHelper.saveClinicToPref(clinicInfo);
                tvClinicName.setText(clinicInfo.getName());
                tvClinicAddress.setText(clinicInfo.getAddress());
                tvClinicContact.setText(clinicInfo.getContactNo());
                tvClinicLicNo.setText(clinicInfo.getLicenceNo());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "No Clinic Data Availabe", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void findViewByIdNavBar() {

        tvUserEmail = findViewById(R.id.tvEmail);
        tvUserAddress = findViewById(R.id.tvAddress);
        tvUserContact = findViewById(R.id.tvContact);

        tvClinicInfoTitle = findViewById(R.id.tvTitleClinicData);
        tvClinicName = findViewById(R.id.tvClinicName);
        tvClinicAddress = findViewById(R.id.tvClinicAddress);
        tvClinicContact = findViewById(R.id.tvClinicContact);
        tvClinicLicNo = findViewById(R.id.tvClinicLicNo);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_signout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sign_out:
                auth.signOut();
                sharedPrefHelper.setUserRegistrationDone(false);
                sharedPrefHelper.saveUserToPref(null);
                sharedPrefHelper.saveClinicToPref(null);
                sharedPrefHelper.saveUidToPref("");
                progressBar.setVisibility(View.VISIBLE);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(HomeActivity.this, SigninActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }, 3000);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
