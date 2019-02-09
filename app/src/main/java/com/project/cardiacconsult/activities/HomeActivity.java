package com.project.cardiacconsult.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.project.cardiacconsult.R;
import com.project.cardiacconsult.adapters.MainMenuAdapter;


public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    ProgressBar progressBar;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar tool;

    GridView gridView;

    String[] textview = {"ecg", "physician", "heartbeat"};
    int[] Imageid = {R.drawable.logo, R.drawable.logo, R.drawable.logo};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Home");


        tool = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tool);

        drawer=(DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView=(NavigationView)findViewById(R.id.nav_view);

        // TOGGEL ACTIONBAR EVENT WITH ICON
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, tool, R.string.openDrawer, R.string.closeDrawer);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        drawer.openDrawer(GravityCompat.START);

        setTitle("Home");

        findViewById();

        auth = FirebaseAuth.getInstance();

        gridView = findViewById(R.id.grid_view);
        MainMenuAdapter adapter = new MainMenuAdapter(HomeActivity.this, textview, Imageid);

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(HomeActivity.this, "You Clicked at " +textview[+ position], Toast.LENGTH_SHORT).show();

            }


        });




    }

    private void findViewById() {
        progressBar = findViewById(R.id.progressbar);
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
