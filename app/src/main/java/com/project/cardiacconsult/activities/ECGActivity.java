package com.project.cardiacconsult.activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
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

    // BluetoothAdapter mybluetooth = null;
    //BluetoothSocket bSocket = null;
    //Set<BluetoothDevice> pairedDevices;
    GraphView graph;
    LineGraphSeries<DataPoint> series;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference ecgReadingRef;
    TextView ecgValue;

    //Abnormality parameters
    public static int noItrn = 3, stdMean = 516, upperBound = stdMean + 250, lowerBound = stdMean - 250, preEpoch = 0;
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

        series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, 0)
        });
        series.setAnimated(true);
        //int xValue;

        ecgReadingRef = firebaseDatabase.getReference("ecgReading").child(firebaseUser.getUid()).child("ecg");
        ecgReadingRef.addValueEventListener(new ValueEventListener() {
            int xValue = 0;
            int epochCounter = 0;
            int epochSum = 0;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: " + dataSnapshot.getValue().toString());

                int i = 0;
                ecgValue.setText("ECG: " + dataSnapshot.getValue().toString());
                String ecgValueString = dataSnapshot.getValue().toString();
                StringBuilder displayString = new StringBuilder();
                displayString.append("ECG: " + ecgValueString);

                series.appendData(
                        new DataPoint(xValue++, Integer.parseInt(ecgValueString)),
                        true,
                        50000
                );
                Log.d(TAG, "X: " + xValue);
                Log.d(TAG, "EpochCounter: " + epochCounter);
                Log.d(TAG, "Epoch Value: " + epochSum);
                if (epochCounter < 20) {
                    epochSum = epochSum + Integer.parseInt(ecgValueString);
                    epochCounter++;
                } else {
                    checkAbnormality(epochSum);
                    epochCounter = 0;
                    epochSum = 0;
                }
                displayString.append("\nEPOCH SUM: " + epochSum);
                displayString.append("\nEPOCH COUNTER: " + epochCounter);
                ecgValue.setText(displayString.toString());
                displayString.replace(0, displayString.toString().length(), "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onDatabaseError: " + databaseError);
            }
        });

        graph.addSeries(series);
        graph.getViewport().setScalableY(true);

    }

    private void checkAbnormality(int epochSum) {

        //int noOfAbn = Integer.parseInt(Common.currentUser.getNoOfAbn());

        // Log.w("Checking abnormality","Yes checking it" + noOfAbn);

        if (epochSum > upperBound && preEpoch == 1) {
            tempItern--;
            preEpoch = 1;
        } else if (epochSum > upperBound && preEpoch != 1) {
            tempItern = noItrn - 1;
            preEpoch = 1;
        } else if (epochSum < lowerBound && preEpoch == -1) {
            tempItern--;
            preEpoch = -1;
        } else if (epochSum < lowerBound && preEpoch != -1) {
            tempItern = noItrn - 1;
            preEpoch = -1;
        } else {
            tempItern = noItrn;
            preEpoch = 0;
        }

        Log.w("Abnormality test", preEpoch + "-------" + tempItern + "----");
        Log.d(TAG, "Abnormality Test : \nPreEpoch" + preEpoch + " TempItern: " + tempItern);
        Toast.makeText(getApplicationContext(), "Abnormality Test : \n PreEpoch" + preEpoch + "\n TempItern: " + tempItern, Toast.LENGTH_SHORT).show();
        if (preEpoch == -1 && tempItern <= 1) {
            Toast.makeText(this, "Abnormal Low Condition Detected!!!( Low )", Toast.LENGTH_SHORT).show();
            generateNotificaiton("Abnormal Low Condition Detected!!!( Low )");
//            noOfAbn++;

        } else if (preEpoch == 1 && tempItern <= 1) {
            Toast.makeText(this, "Abnormal Hart Condition Detected!!!( Heigh )", Toast.LENGTH_SHORT).show();
            generateNotificaiton("Abnormal Hart Condition Detected!!!( Heigh )");
//            noOfAbn++;
        }
        Toast.makeText(getApplicationContext(), "No Abnormalities", Toast.LENGTH_LONG).show();
        generateNotificaiton("No Abnormalities");

        preEpoch = 0;
        tempItern = noItrn;

    }

    public void generateNotificaiton(String notificationText) {
        NotificationManager notificationManager;
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "default_channel_id";
        String channelDescription = "Default Channel";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelId);
            if (notificationChannel == null) {
                int importance = NotificationManager.IMPORTANCE_HIGH; //Set the importance level
                notificationChannel = new NotificationChannel(channelId, channelDescription, importance);
                notificationChannel.setLightColor(Color.GREEN); //Set if it is necesssary
                notificationChannel.enableVibration(true); //Set if it is necesssary
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ECGActivity.this)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Cardiac Consult")
                .setContentText(notificationText)
                .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                .setChannelId(channelId)
                .setAutoCancel(true);

        notificationManager.notify(0, notificationBuilder.build());

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
