package com.gnirt69.firebaseregistrationloginexam;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

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
    private DatabaseReference profilePicReference;
    private StorageReference storageReference;
    public String picName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        picView = (CircleImageView) findViewById(R.id.profile_image);



        tvEmail = (TextView) findViewById(R.id.currentEmail);
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

        FirebaseUser user = firebaseAuth.getCurrentUser();
        final String userID = user.getUid();

        //Get profilepic URL from database
        profilePicReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("pic").child("URL");
        profilePicReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot4) {
                picName = dataSnapshot4.getValue(String.class);

                if (picName == null){
                    ImageView img= (ImageView) findViewById(R.id.profile_image);
                    img.setImageResource(R.drawable.alva);
                }
                else {

                    try {
                        localFile = File.createTempFile("images", "jpg");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(picName);
                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            CircleImageView img = (CircleImageView) findViewById(R.id.profile_image);
                            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath(), bmOptions);
                            img.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }
        });

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

