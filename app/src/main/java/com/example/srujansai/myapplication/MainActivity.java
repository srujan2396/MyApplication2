package com.example.srujansai.myapplication;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "Something";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    GoogleSignInOptions gso;
    GoogleApiClient mGoogleApiClient;
    int RC_SIGN_IN = 9001;
    SignInButton gsign;
    String phno,displayname,email,photourl,uid;
    public static final int REQUEST_LOCATION_CODE = 99;
    public static final int REQUEST_READ_CONTACTS = 79;
    public static final int REQUEST_PHONE_STATE= 89;
    int l=0,c=0,p=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);
        Button b1;
        Toast r1;
        TextView t1, t2, un, pd;
        sharedPreferences = getSharedPreferences("mypreference", Context.MODE_PRIVATE);
        // Configure Google Sign In
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        // Initiliasing Google sign in button
        gsign = (SignInButton) findViewById(R.id.gsign);
        //
        gsign.setOnClickListener(this);



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    displayname=user.getDisplayName();
                    email=user.getEmail();
                    uid=user.getUid();
                    photourl=user.getPhotoUrl().toString();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(MainActivity.this, "Already Signed in", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        checkLocationPermission();
                    }


                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        b1 = (Button) findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  createAccount();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);


            }
        });
    }

    public boolean checkLocationPermission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)){

                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }
            else{
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},REQUEST_READ_CONTACTS);
            }
            else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},REQUEST_READ_CONTACTS);
            }
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)){

                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},REQUEST_PHONE_STATE);

            }
            else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},REQUEST_PHONE_STATE);

            }
            return false;
        } else{
            try {
                TelephonyManager tMgr = (TelephonyManager) MainActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
                String mPhoneNumber = tMgr.getLine1Number();
                String n = mPhoneNumber.replaceAll("[()\\s-]+", "").trim();
                if (n.length() == 10) {

                    phno = n;
                    // FirebaseMessaging.getInstance().subscribeToTopic(n);
                    //jhjvjhv,jh

                } else if (n.length() > 10) {
                    if (n.length() == 11) {
                        String num = n.substring(1);
                        phno = num;

                    } else if (n.length() == 12) {
                        String num = n.substring(2);

                        phno = num;

                    } else if (n.length() == 13) {
                        String num = n.substring(3);

                        phno = num;

                    }
                }

                Intent i = new Intent(MainActivity.this, UserDashboard.class);

                i.putExtra("name", displayname);
                i.putExtra("email", email);
                i.putExtra("photourl", photourl);

                i.putExtra("phno", phno);
                i.putExtra("uid", uid);
                startActivity(i);
                finish();
            }catch(NullPointerException ne){ ne.printStackTrace();}
            return true;

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                       l=1;
                        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},REQUEST_READ_CONTACTS);

                    } else {
                        Toast.makeText(this, "her please give Permission", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
                    }
                }
                break;
            case REQUEST_READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                        c=1;
                        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},REQUEST_PHONE_STATE);
                    } else {
                        Toast.makeText(this, "her please give Permission", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},REQUEST_READ_CONTACTS);
                    }
                }
                break;
            case REQUEST_PHONE_STATE:

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
                       p=1;
                    if(p==1&&c==1&&l==1){
                        try {
                            TelephonyManager tMgr = (TelephonyManager) MainActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
                            String mPhoneNumber = tMgr.getLine1Number();
                            String n = mPhoneNumber.replaceAll("[()\\s-]+", "").trim();
                            if (n.length() == 10) {

                                phno = n;
                                // FirebaseMessaging.getInstance().subscribeToTopic(n);
                                //jhjvjhv,jh

                            } else if (n.length() > 10) {
                                if (n.length() == 11) {
                                    String num = n.substring(1);
                                    phno = num;

                                } else if (n.length() == 12) {
                                    String num = n.substring(2);

                                    phno = num;

                                } else if (n.length() == 13) {
                                    String num = n.substring(3);

                                    phno = num;

                                }
                            }

                            Intent i = new Intent(MainActivity.this, UserDashboard.class);

                            i.putExtra("name", displayname);
                            i.putExtra("email", email);
                            i.putExtra("photourl", photourl);

                            i.putExtra("phno", phno);
                            i.putExtra("uid", uid);
                            startActivity(i);
                            finish();
                        }catch(NullPointerException ne){ ne.printStackTrace();}
                    }
                } else {
                    Toast.makeText(this, "her please give Permission", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},REQUEST_PHONE_STATE);

                }
            }
            break;

        }
     // if(requestCode==REQUEST_LOCATION_CODE){}

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(MainActivity.this, "Sign completed", Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("name",user.getDisplayName());
                            editor.putString("email",user.getEmail());
                            editor.putString("uid",user.getUid());
                            editor.putString("photourl",user.getPhotoUrl().toString());

                            editor.commit();
                            FirebaseDatabase  database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference();
                            String user_name= user.getDisplayName();
                            String user_email= user.getEmail();
                            String photourl= user.getPhotoUrl().toString();
                          //  String user_phno=getIntent().getStringExtra("phno");


                            HashMap<String,String> store_userpro=new HashMap<String,String>();
                            store_userpro.put("name",user_name);
                            store_userpro.put("email",user_email);
                            store_userpro.put("photourl",photourl);
                            store_userpro.put("phno",phno);
                            DatabaseReference childref= myRef.child("Users").child(user.getUid());
                            childref.setValue(store_userpro);
                            Toast.makeText(MainActivity.this, "Authentication sucess",
                                    Toast.LENGTH_SHORT).show();
                            displayname=user.getDisplayName();
                            email=user.getEmail();
                            uid=user.getUid();
                            photourl=user.getPhotoUrl().toString();


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                checkLocationPermission();

                            }

                     /*       Intent i = new Intent(MainActivity.this, UserDashboard.class);

                            i.putExtra("name",user.getDisplayName());
                            i.putExtra("email",user.getEmail());
                           // i.putExtra("photourl",user.getPhotoUrl());
                            i.putExtra("uid",user.getUid());


                            startActivity(i);*/

                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                          //  updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
        public void onStart()
        {
            super.onStart();
            mAuth.addAuthStateListener(mAuthListener);
        }
        @Override
        public void onStop() {
            super.onStop();
            if (mAuthListener != null) {
                mAuth.removeAuthStateListener(mAuthListener);
            }
        }

        public void createAccount() {


    /*    mAuth.createUserWithEmailAndPassword("email", "password")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        Toast.makeText(MainActivity.this, "Sign completed", Toast.LENGTH_SHORT).show();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        Intent i = new Intent(MainActivity.this, UserDashboard.class);
                        startActivity(i);


                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Login Failed",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });*/
    }
  /*      public void signIn() {
        mAuth.signInWithEmailAndPassword("email", "password")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this, "AUTH FAILED",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }*/
        public void getCurrentUser()
        {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
        }
    }

    @Override
    public void onClick(View v) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
