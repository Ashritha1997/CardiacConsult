package com.project.cardiacconsult.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.cardiacconsult.R;
import com.project.cardiacconsult.models.User;
import com.project.cardiacconsult.utils.CryptWithMD5;
import com.project.cardiacconsult.utils.SharedPrefHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.apache.commons.lang3.StringUtils;


public class SigninActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 100;
    private static final String TAG = SigninActivity.class.getName();
    /*GoogleSignInOptions gso;
    GoogleSignInClient signInClient;
    SignInButton signInButton;*/

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;

    ProgressBar progressBar;
    TextView tvRegister;
    EditText edtEmail, edtPassword;
    Button btnSignin;
    String email, password;
    SharedPrefHelper sharedPrefHelper;
    DatabaseReference userReference;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigin);

        setTitle("Signing In");

        findViewById();

        sharedPrefHelper = new SharedPrefHelper(SigninActivity.this);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference("users");


        /*signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);


        firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            updateUi(firebaseUser);
        }else{
            signInButton.setVisibility(View.VISIBLE);
        }

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(this,gso);*/
    }

    private void findViewById() {
        progressBar = findViewById(R.id.progressbar);
        tvRegister = findViewById(R.id.tvLabel_register);
        tvRegister.setOnClickListener(this);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        btnSignin = findViewById(R.id.btnSignIn);
        btnSignin.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            /*case R.id.sign_in_button:
                signIn();
                break;*/
            case R.id.tvLabel_register:
                Intent intent = new Intent(SigninActivity.this, RegistrationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

            case R.id.btnSignIn:
                signInWithEmail();
                break;
        }

    }

    private void signInWithEmail() {

        progressBar.setVisibility(View.VISIBLE);

        if (validateUserData()) {
            String passwordHash = CryptWithMD5.cryptWithMD5(password);
            auth.signInWithEmailAndPassword(email, passwordHash)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (progressBar.getVisibility() == View.VISIBLE) {
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                            if (task.isSuccessful())
                            {
                                Log.d(TAG, "signInWithEmail:success");
                                String userUID = auth.getCurrentUser().getUid();
                                firebaseUser = auth.getCurrentUser();

                                sharedPrefHelper.saveUidToPref(auth.getCurrentUser().getUid());

                                userReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        boolean profileRegistered = false;
                                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                                            User user = userSnapshot.getValue(User.class);
                                            if (user.getEmail().equalsIgnoreCase(firebaseUser.getEmail())) {
                                                profileRegistered = true;
                                                sharedPrefHelper.saveUserToPref(user);
                                            }
                                        }

                                        if (!profileRegistered) {
                                            Toast.makeText(getApplicationContext(), "No Profile Registered", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SigninActivity.this, UserProfileDetailsActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }else{
                                            Toast.makeText(getApplicationContext(), " Profile Registered", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SigninActivity.this, HomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                /*DatabaseReference uidReference = firebaseDatabase.getReference("users").child(userUID);
                                if (uidReference == null) {
                                    Toast.makeText(getApplicationContext(), "No Profile Registered", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SigninActivity.this, UserProfileDetailsActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(getApplicationContext(), " Profile Registered", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SigninActivity.this, HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }*/

                               /* Query uidQuery = userReference.orderByChild(email).equalTo(firebaseUser.getEmail());

                                uidQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        User user = null;
                                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                                            user = singleSnapshot.getValue(User.class);
                                        }

                                        Log.d(TAG, "onDataChange: " + user.toString());
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e(TAG, "onCancelled", databaseError.toException());
                                    }
                                });
*/
                                /*if (sharedPrefHelper.checkUserRegistration()) {
                                    Intent intent = new Intent(SigninActivity.this, HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }else{
                                    Intent intent = new Intent(SigninActivity.this, UserProfileDetailsActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }*/


                            }
                            else
                                {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(SigninActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                        }
                    });
        }
        else {
            Toast.makeText(getApplicationContext(), "Enter Proper Data", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateUserData() {

        email = edtEmail.getText().toString();
        password = edtPassword.getText().toString();

        if (email.matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$") && !StringUtils.isBlank(password) && StringUtils.length(password) >= 6) {
            return true;
        } else {
            return false;
        }
    }

    /* private void signIn() {

         progressBar.setVisibility(View.VISIBLE);
         startActivityForResult(signInClient.getSignInIntent(), RC_SIGN_IN);
     }
 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }*/
    }

    /*private void handleSignInResult(Task<GoogleSignInAccount> task) {

        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Can Not Log In Right Now", Toast.LENGTH_LONG).show();
        }
    }*/

    /*private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success");
                    FirebaseUser user = auth.getCurrentUser();
                    signInButton.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    updateUi(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }*/

    /*private void updateUi(FirebaseUser user) {
        tvEmail.setText(user.getEmail());
        tvId.setText(user.getUid());
        tvIdToken.setText(user.getProviderId());
    }*/

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_signout, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                progressBar.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (signInButton.getVisibility() == View.INVISIBLE) {
                            signInButton.setVisibility(View.VISIBLE);
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                        tvEmail.setText("");
                        tvIdToken.setText("");
                        tvId.setText("");
                    }
                }, 3000);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
}
