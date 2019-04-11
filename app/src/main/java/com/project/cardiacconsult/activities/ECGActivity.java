package com.project.cardiacconsult.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.project.cardiacconsult.R;
import com.project.cardiacconsult.models.Abnormality;

import java.util.Set;
import java.util.UUID;

public class ECGActivity extends AppCompatActivity {

    private static final String TAG = ECGActivity.class.getName();
    Toolbar toolbar;
    Button bshowecg;

    String name = null, address = null;

    BluetoothAdapter mybluetooth = null;
    BluetoothSocket bSocket = null;
    Set<BluetoothDevice> pairedDevices;
    GraphView graph;
    LineGraphSeries<DataPoint> series;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference ecgReadingRef;
    TextView ecgValue;

    //Abnormality parameters
    public static int noItrn = 3, stdMean = 516, upperBound =stdMean + 250, lowerBound = stdMean - 250, preEpoch = 0;
    public int tempItern = noItrn;
    public static int xValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_activity_ecg);

        graph = (GraphView) findViewById(R.id.graph);
        Viewport viewport = graph.getViewport();
        viewport.setXAxisBoundsManual(true);
        viewport.setMinX(0);
        viewport.setMaxX(200);
        viewport.setYAxisBoundsManual(true);
        viewport.setMaxY(1200);
        viewport.setMinY(0);

        viewport.setScalableY(false);
        viewport.setScalable(false);
        viewport.setScrollableY(true);
        viewport.setScrollable(true);

        ecgValue = findViewById(R.id.ecgValue);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        Log.d(TAG, "onCreate: " + firebaseUser.getUid());

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("ECG");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 0)
        });
        series.setAnimated(true);
        //int xValue;

        ecgReadingRef = firebaseDatabase.getReference("ecgReading").child(firebaseUser.getUid()).child("ecg");
        ecgReadingRef.addValueEventListener(new ValueEventListener() {
            int xValue = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: " + dataSnapshot.getValue().toString());
                int epochSum=0;
                int i=0;
                ecgValue.setText("ECG: " + dataSnapshot.getValue().toString());
                String ecgValue = dataSnapshot.getValue().toString();

                series.appendData(
                        new DataPoint(xValue++, Integer.parseInt(ecgValue)),
                        true,
                        50000
                );
                Log.d(TAG, "X: "+ xValue);

                for(i=0;i<=100;i++)
                {
                    epochSum = epochSum + Integer.parseInt(ecgValue);

                }
                //checkAbnormality(epochSum);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onDatabaseError: " + databaseError);
            }
        });

        graph.addSeries(series);
        graph.getViewport().setScalableY(true);

    }

    /*private void checkAbnormality(int epochSum ) {

        //int noOfAbn = Integer.parseInt(Common.currentUser.getNoOfAbn());

       // Log.w("Checking abnormality","Yes checking it" + noOfAbn);

        if (epochSum > upperBound && preEpoch == 1 ){
            tempItern--;
            preEpoch = 1;
        }else if(epochSum > upperBound && preEpoch != 1){
            tempItern = noItrn-1;
            preEpoch = 1;
        }else if (epochSum < lowerBound && preEpoch == -1){
            tempItern--;
            preEpoch = -1;
        }else if(epochSum < lowerBound && preEpoch != -1){
            tempItern = noItrn-1;
            preEpoch = -1;
        }else{
            tempItern = noItrn;
            preEpoch = 0;
        }

        Log.w("Abnormality test", preEpoch+"-------"+tempItern+"----");

        if (preEpoch == -1 && tempItern<= 1){
            Toast.makeText(this, "Abnormal Low Condition Detected!!!( Low )", Toast.LENGTH_SHORT).show();
           // noOfAbn++;
           // Common.currentUser.setNoOfAbn(noOfAbn+"");
            Abnormality abnormality = new Abnormality(firebaseUser,"Low Heart Rate", "Heart rate decreased to lowest level", "false", noOfAbn+"");
            abnormalityReference.child((noOfAbn)+"").setValue(abnormality);
            userReference.child(Common.currentUser.getUserName()).setValue(Common.currentUser);

        }else if(preEpoch == 1 && tempItern <= 1){
            Toast.makeText(this, "Abnormal Hart Condition Detected!!!( Heigh )", Toast.LENGTH_SHORT).show();
            noOfAbn++;
            Common.currentUser.setNoOfAbn(noOfAbn+"");
            Abnormality abnormality = new Abnormality(Common.currentUser.getUserName(),"High Heart Rate", "Heart rate increased to highest level", "false", noOfAbn+"");
            abnormalityReference.child((noOfAbn)+"").setValue(abnormality);
            userReference.child(Common.currentUser.getUserName()).setValue(Common.currentUser);
        }
        preEpoch = 0;
        tempItern = noItrn;

    }*/

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
