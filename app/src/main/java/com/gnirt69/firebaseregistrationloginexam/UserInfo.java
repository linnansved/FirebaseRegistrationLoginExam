package com.gnirt69.firebaseregistrationloginexam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import static com.gnirt69.firebaseregistrationloginexam.R.id.LogOutBtn;

public class UserInfo extends AppCompatActivity {

        private FirebaseAuth firebaseAuth;

        private Button LogOutBtn;

        private TextView tvEmail;

        //private TextView tvNickname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        tvEmail = (TextView) findViewById(R.id.tvEmailProfile);
        //tvNickname = (TextView) findViewById(R.id.tvNickname);

        tvEmail.setText(getIntent().getExtras().getString("Email"));
        //tvNickname.setText(getIntent().getExtras().getString("nickname"));

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

}