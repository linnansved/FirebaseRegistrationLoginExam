package com.gnirt69.firebaseregistrationloginexam;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;

import com.google.firebase.auth.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserInfo extends AppCompatActivity {

    private TextView tvNick, tvEmail;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    public String nickname1;
    public ImageView bubble;
    public boolean visible;
    public File localFile = null;
    private CircleImageView picView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        picView = (CircleImageView) findViewById(R.id.profile_image);
        localFile = (File) getIntent().getExtras().get("localFile");

        picView= (CircleImageView) findViewById(R.id.profile_image);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath(),bmOptions);
        picView.setImageBitmap(bitmap);

        tvEmail = (TextView) findViewById(R.id.currentEmail);
        //tvEmail.setText("Your current email:"+ getIntent().getExtras().getString("Email"));
        tvEmail.setText(getIntent().getExtras().getString("Email"));

        tvNick = (TextView) findViewById(R.id.currentNickname);
        String nickname = (getIntent().getExtras().getString("Nickname"));

        if (nickname ==null) {
            tvNick.setText("No nickname added");
        }
        else{
            tvNick.setText(nickname);

        }

        nickname1 = getIntent().getExtras().getString("Nickname");

        bubble = (ImageView) findViewById(R.id.prat);
        bubble.setVisibility(View.GONE);
        visible = false;

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

    }


    public void editUser_Click(View v) {
        Intent i = new Intent(UserInfo.this, EditUser.class);
        i.putExtra("Nickname", nickname1);
        i.putExtra("localFile", localFile);
        startActivity(i);
    }
    public void hideShowBubble(View v) {
        if (visible){
            bubble.setVisibility(View.GONE);
            visible = false;
        }
        else{
        bubble.setVisibility(View.VISIBLE);
            visible = true;
        }
    }
}

