package com.example.srujansai.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.concurrent.atomic.AtomicInteger;

public class UserDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_dashboard, menu);
        return true;
    } */
    Button addfrnd;
    RecyclerView frndlist;
    TextView selcon;
    SharedPreferences sharedPreferences;
    private static final int CONTACT_PICKER_RESULT = 1001;
    private static final String DEBUG_TAG = "Contact List";
    private static final int RESULT_OK = -1;
    String name,email,photo_url,uid,phno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences("mypreference", Context.MODE_PRIVATE);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserDashboard.this, MapsActivity.class);
                startActivity(i);
            }
        });

        addfrnd = (Button) findViewById(R.id.addfrnd);
        selcon = (TextView) findViewById(R.id.selectedcontact);

        TelephonyManager tMgr = (TelephonyManager) UserDashboard.this.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        String n = mPhoneNumber.replaceAll("[()\\s-]+", "").trim();
        if (n.length() == 10) {
            selcon.setText("name :" + name + "\nPhone: " + n + "\nEmail:" + email);
            phno=n;
            FirebaseMessaging.getInstance().subscribeToTopic(n);
            //jhjvjhv,jh

        } else{

            String num = n.substring(3);

            if (num.length() == 10) {

                selcon.setText("name :" + name + "\nPhone: " + num + "\nEmail:" + email);
                phno=num;
                FirebaseMessaging.getInstance().subscribeToTopic(num);
            } else {
                selcon.setText("name :" + name + "\nPhone: " + num + "\nEmail:" + email);
                Toast.makeText(this, "Choose the 10 digit phone number you will not get notifications  "+num, Toast.LENGTH_SHORT).show();
            }

        }




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_dashboard, menu);
        return true;
    }

  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    public void chooseContact(View v) {
        Intent it = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(it, CONTACT_PICKER_RESULT);

    }

    // handle after selecting a contact from the list
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Toast.makeText(UserDashboard.this,"OnResult fragment called",Toast.LENGTH_LONG).show();
            Uri contactData=ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            if(data!=null) {
                contactData = data.getData();
            }
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    // handle contact results

                    Uri contact = data.getData();
                   // Cursor c = getContentResolver().query(contact, null, null, null, null);


                    Cursor cursor = getContentResolver().query(contact, null,
                            null, null, null);
                    Cursor c = (UserDashboard.this.getContentResolver().query(contactData, null, null, null, null));
                    String name="",email="",number="";
                    if (c.moveToFirst()) {


                        String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        //String hasEmail =c.getString(c.getColumnIndex(ContactsContract.Contacts.Data.DATA1.));


                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = (UserDashboard.this.getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null));
                            phones.moveToFirst();
                            number = phones.getString(phones.getColumnIndex("data1"));
                            phones.close();
                        }

                        Cursor emailCur = (UserDashboard.this.getContentResolver().query(
                                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                new String[]{id}, null));
                        while (emailCur.moveToNext()) {
                            // This would allow you get several email addresses
                            // if the email addresses were stored in an array
                            if(emailCur.getString(
                                    emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))!="")
                                email = emailCur.getString(
                                        emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                            String emailType = emailCur.getString(
                                    emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                        }
                        emailCur.close();
                        name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        c.close();
                    }

                    setData(name,number,email);
                    // Do something with the phone number...
            }

                    }
            }

    private void setData(String name, String number, String email) {
      String n=number.replaceAll("[()\\s-]+", "").trim();
        if(n.length()==10){
        selcon.setText("name :"+name+"\nPhone: "+n+"\nEmail:"+email);
            FirebaseMessaging fm = FirebaseMessaging.getInstance();
            AtomicInteger msgId=new AtomicInteger();;
            fm.send(new RemoteMessage.Builder(number)
                    .setMessageId(Integer.toString(msgId.incrementAndGet()))
                    .addData("my_message", "Hello World")
                    .addData("my_action","SAY_HELLO")
                    .build());
            //asdf
        } else{

           String num= n.substring(3);
            if(num.length() == 10){  selcon.setText("name :"+name+"\nPhone: "+num+"\nEmail:"+email); }else{
                selcon.setText("name :"+name+"\nPhone: "+num+"\nEmail:"+email);
                Toast.makeText(this, "Choose the 10 digit phone number", Toast.LENGTH_SHORT).show();
            }

        }
        ///kljglh
    }
}



