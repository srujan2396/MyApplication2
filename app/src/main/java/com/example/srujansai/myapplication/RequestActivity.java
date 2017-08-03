package com.example.srujansai.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RequestActivity extends AppCompatActivity implements View.OnClickListener {
    TextView body;
    Button acept,reject;
    String phno,name,uid,selfphno,fromname,fromphno;
    DatabaseReference myRef,childref,frnref;
    FirebaseDatabase database;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        phno=getIntent().getStringExtra("phno");
        name=getIntent().getStringExtra("name");
        body=(TextView)findViewById(R.id.req);
        acept=(Button)findViewById(R.id.act);
        reject=(Button)findViewById(R.id.rej);
        acept.setOnClickListener(this);
        reject.setOnClickListener(this);

        // Firebase database initialisation
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        //  store_userpro.put("phno",user_phno);
        sharedPreferences = getSharedPreferences("mypreference", Context.MODE_PRIVATE);
/// Getting firebase authentication user id from previous activity
        uid=sharedPreferences.getString("uid",null);
        selfphno=getIntent().getStringExtra("phno");
        fromname=getIntent().getStringExtra("fromname");
        fromphno=getIntent().getStringExtra("fromphno");
        body.setText("TrackO Request From "+fromname +"\n Phno: "+fromphno);

        System.out.println("USER ID "+uid+"  Phone:"+phno);

        childref=myRef.child("Users").child(uid).child("friendslist").child(fromphno);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.act:
                String msg="accept";
                updateinfriendlist(fromname,fromphno,phno,msg);
                break;
            case R.id.rej:
                String msg1="reject";
                updateinfriendlist(fromname,fromphno,phno,msg1);
                break;
        }
    }

    private void updateinfriendlist(String fromname,String fromphno,String phno, String msg) {
        HashMap<String,String> friendslist=new HashMap<String,String>();
        friendslist.put("fname",fromname);
        friendslist.put("fphno",fromphno);
        friendslist.put("selfname",name);
        friendslist.put("selfphno",selfphno);
        friendslist.put("status",msg);
        childref.setValue(friendslist);
        System.out.println("fromname:"+fromname+" fromphno:"+fromphno);
        Toast.makeText(this, "updated in friends list", Toast.LENGTH_SHORT).show();
        friendslist.put("message","Location Request From TRACKO "+fromphno);
        DatabaseReference nr=myRef.child("acceptrequests").push();
        nr.setValue(friendslist);
        finish();

    }
}
