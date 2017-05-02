package com.gnirt69.firebaseregistrationloginexam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;

import com.google.firebase.auth.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class UserInfo extends AppCompatActivity {

    private TextView tvNick, tvEmail;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();

        tvEmail = (TextView) findViewById(R.id.currentEmail);
        tvEmail.setText("Your current email:"+ getIntent().getExtras().getString("Email"));

        //tvNick = (TextView) findViewById(R.id.currentNickname);
      //  tvNick.setText("Your current nickname:"+ getIntent().getExtras().getString("Nickname");

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    public void editUser_Click(View v) {
        Intent i = new Intent(UserInfo.this, EditUser.class);
        startActivity(i);
    }
}

