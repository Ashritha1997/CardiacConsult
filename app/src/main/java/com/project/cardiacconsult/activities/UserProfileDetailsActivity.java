package com.project.cardiacconsult.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.project.cardiacconsult.R;
import com.project.cardiacconsult.models.Clinic;
import com.project.cardiacconsult.models.User;
import com.project.cardiacconsult.utils.RoleEnum;
import com.project.cardiacconsult.utils.SharedPrefHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.lang3.StringUtils;

public class UserProfileDetailsActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String CHANNEL_ID = "main";
    private static final String TAG = UserProfileDetailsActivity.class.getName();
    EditText edtFullName, edtAddress, edtPhoneNo, edtEmail;
    EditText edtCName, edtCPhoneNo, edtCAddress, edtCLicNo;
    Button btnSubmit;
    RadioGroup roleGroup;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference userReference;
    User user;
    ProgressBar progressBar;
    LinearLayout clinicDetailsLayout;
    boolean isPhysician = false;
//    SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_details);
        setTitle("User Profile Details");
        findViewById();

//        sharedPrefHelper = new SharedPrefHelper(getApplicationContext());

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            firebaseUser = auth.getCurrentUser();
            edtEmail.setText(firebaseUser.getEmail());
            edtEmail.setEnabled(false);
            edtEmail.setFocusable(false);
        }

        database = FirebaseDatabase.getInstance();
        userReference = database.getReference("users");

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void findViewById() {
        edtFullName = findViewById(R.id.edt_fullname);
        //edtPassword = findViewById(R.id.edt_password);
        edtAddress = findViewById(R.id.edt_address);
        edtEmail = findViewById(R.id.edt_email);
        edtPhoneNo = findViewById(R.id.edt_phone_no);
        roleGroup = findViewById(R.id.radioGroup);
        roleGroup.setOnCheckedChangeListener(this);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        progressBar = findViewById(R.id.progressbar);
        clinicDetailsLayout = findViewById(R.id.clinicDetailsLayout);
    }

    @Override
    public void onClick(View view) {

        RadioButton selectedRole = findViewById(roleGroup.getCheckedRadioButtonId());
        String fullName = edtFullName.getText().toString();
        String address = edtAddress.getText().toString();
        String phoneNo = edtPhoneNo.getText().toString();
        String email = edtEmail.getText().toString();
        RoleEnum userRole = RoleEnum.valueOf(selectedRole.getText().toString().toUpperCase());

        if (StringUtils.isBlank(fullName) || StringUtils.length(phoneNo) < 10 || !email.matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$")) {
            Toast.makeText(getApplicationContext(), "User Details are not valid. Please update and try again.", Toast.LENGTH_SHORT).show();
            edtFullName.requestFocus();
        } else {
            user = new User(fullName, address, phoneNo, email, userRole);
            switch (view.getId()) {
                case R.id.btnSubmit:

                    if (isPhysician) {
                        GetValidateThenStoreUserPlusClinicData(user);
                    }else{
                        validateAndStoreUserData(user);
                    }
//                    sharedPrefHelper.setUserRegistrationDone(true);
                    Intent intent = new Intent(UserProfileDetailsActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
            }
        }
    }

    private void GetValidateThenStoreUserPlusClinicData(User user) {

            findViewByIdForClinic();
            String cName = edtCName.getText().toString().trim();
            String cAddress = edtCAddress.getText().toString().trim();
            String cPhone = edtCPhoneNo.getText().toString().trim();
            String cLicNo = edtCLicNo.getText().toString().trim();
        if (StringUtils.isBlank(cName) || StringUtils.isBlank(cAddress) || StringUtils.length(cPhone) < 10 || StringUtils.isBlank(cLicNo)) {
            Toast.makeText(getApplicationContext(), "Clinic Details are not valid. Please update and try again.", Toast.LENGTH_SHORT).show();
            edtCName.requestFocus();
        }else{
            validateAndStoreUserData(user);
            //method call to store clinic data
            Clinic clinic = new Clinic(cName, cAddress, cPhone, cLicNo);
            storeClinicData(clinic);
        }

    }

    private void storeClinicData(Clinic clinic) {

        DatabaseReference clinicRef = database.getReference("clinics");
        clinicRef.child(firebaseUser.getUid()).setValue(clinic);
//        sharedPrefHelper.saveClinicToPref(clinic);
        Toast.makeText(getApplicationContext(), "Clinic Data Registered.", Toast.LENGTH_SHORT).show();
        edtCName.setText("");
        edtCName.requestFocus();
        edtCAddress.setText("");
        edtCPhoneNo.setText("");
        edtCLicNo.setText("");

    }

    private void findViewByIdForClinic() {
        edtCName = findViewById(R.id.edtClinicname);
        edtCAddress = findViewById(R.id.edtClinic_address);
        edtCPhoneNo = findViewById(R.id.edt_clinic_phone_no);
        edtCLicNo = findViewById(R.id.edt_clininc_lic_no);
    }

    private void validateAndStoreUserData(User user) {

        userReference.child(firebaseUser.getUid()).setValue(user);
//        sharedPrefHelper.saveUserToPref(user);
        Toast.makeText(getApplicationContext(), "User Data Registered.", Toast.LENGTH_SHORT).show();
        edtFullName.setText("");
        edtFullName.requestFocus();
        edtPhoneNo.setText("");
        edtAddress.setText("");
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.sign_in:
                startActivity(new Intent(UserProfileDetailsActivity.this, SigninActivity.class));
                return true;
            case R.id.notification:
                startActivity(new Intent(UserProfileDetailsActivity.this, NotificationActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        switch (i) {
            case R.id.rbPatient:
                if (btnSubmit.getVisibility() == View.GONE) {
                    btnSubmit.setVisibility(View.VISIBLE);
                }
                if (clinicDetailsLayout.getVisibility() == View.VISIBLE) {
                    clinicDetailsLayout.setVisibility(View.GONE);
                }
                isPhysician = false;
                break;

            case R.id.rbPhysician:
                if (btnSubmit.getVisibility() == View.GONE) {
                    btnSubmit.setVisibility(View.VISIBLE);
                }
                if (clinicDetailsLayout.getVisibility() == View.GONE) {
                    clinicDetailsLayout.setVisibility(View.VISIBLE);
                }
                isPhysician = true;
                break;
        }

    }
}
