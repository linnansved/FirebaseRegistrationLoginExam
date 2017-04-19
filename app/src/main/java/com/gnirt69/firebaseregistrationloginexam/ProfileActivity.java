package com.gnirt69.firebaseregistrationloginexam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private Button LogOutBtn;

    private TextView tvEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tvEmail = (TextView) findViewById(R.id.tvEmailProfile);
        tvEmail.setText(getIntent().getExtras().getString("Email"));

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        //FirebaseUser user = firebaseAuth.getCurrentUser();

        LogOutBtn = (Button) findViewById(R.id.LogOutBtn);

        LogOutBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){

        if(view == LogOutBtn){
            firebaseAuth.signOut();;
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
    public void editUser_Click(View v) {
        Intent i = new Intent(ProfileActivity.this, EditUser.class);
        startActivity(i);
    }

    public void addPhoto_Click(View v) {
        Intent i = new Intent(ProfileActivity.this, AddPhoto.class);
        startActivity(i);
    }

}
