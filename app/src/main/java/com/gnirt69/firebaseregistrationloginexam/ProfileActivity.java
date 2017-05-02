package com.gnirt69.firebaseregistrationloginexam;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import android.util.Log;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    private Button LogOutBtn, editUserBtn, enterBtn;

    private ImageView unLocked, locked, addAlbum;

    private TextView tvEmail,tvNick, textSqParent;
    private EditText answerParent;

    private String answer;

    public ArrayList<String> Imagelist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tvEmail = (TextView) findViewById(R.id.tvEmailProfile);
        tvEmail.setText(getIntent().getExtras().getString("Email"));

        firebaseAuth = FirebaseAuth.getInstance();

        unLocked = (ImageView) findViewById(R.id.unlock);
        locked = (ImageView) findViewById(R.id.lock);
        addAlbum = (ImageView) findViewById(R.id.album4);
        editUserBtn = (Button) findViewById(R.id.UserClick);
        enterBtn = (Button) findViewById(R.id.enterBtn);
        textSqParent = (TextView)findViewById(R.id.textSqParent);
        answerParent = (EditText) findViewById(R.id.answerParent);

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        //FirebaseUser user = firebaseAuth.getCurrentUser();

        unLocked.setVisibility(View.VISIBLE);
        tvEmail.setVisibility(View.GONE);
        locked.setVisibility(View.GONE);
        //LogOutBtn.setVisibility(View.VISIBLE);
        addAlbum.setVisibility(View.VISIBLE);
        editUserBtn.setVisibility(View.VISIBLE);
        enterBtn.setVisibility(View.GONE);
        textSqParent.setVisibility(View.GONE);
        answerParent.setVisibility(View.GONE);


        LogOutBtn = (Button) findViewById(R.id.LogOutBtn);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Cards");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        // Result will be holded Here
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            Imagelist.add(String.valueOf(dsp.getValue()));
                        }
                        Log.d("hej" + Imagelist, "hej");
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }

    });
        Intent intent = new Intent(ProfileActivity.this,GalleryMain.class);
        intent.putExtra("MyImages",Imagelist);

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

    public void addCard_Click(View v) {
        Intent i = new Intent(ProfileActivity.this, AddPhoto.class);
        startActivity(i);
    }
    public void toGallery_Click(View v) {
        Intent i = new Intent(ProfileActivity.this, GalleryMain.class);
        startActivity(i);
    }
   public void toChild_Click(View v) {
       unLocked.setVisibility(View.GONE);
       locked.setVisibility(View.VISIBLE);
       LogOutBtn.setVisibility(View.GONE);
       addAlbum.setVisibility(View.GONE);
       editUserBtn.setVisibility(View.GONE);
       tvEmail.setVisibility(View.GONE);

    }
    public void toParent_Click(View v) {
        enterBtn.setVisibility(View.VISIBLE);
        textSqParent.setVisibility(View.VISIBLE);
        answerParent.setVisibility(View.VISIBLE);
    }

    public void testParent(View v) {
        answer = answerParent.getText().toString();

        if(answer.equals("parent")){
            unLocked.setVisibility(View.VISIBLE);
            locked.setVisibility(View.GONE);
            LogOutBtn.setVisibility(View.VISIBLE);
            addAlbum.setVisibility(View.VISIBLE);
            editUserBtn.setVisibility(View.VISIBLE);
            tvEmail.setVisibility(View.GONE);
            enterBtn.setVisibility(View.GONE);
            textSqParent.setVisibility(View.GONE);
            answerParent.setVisibility(View.GONE);
        }
        else {

            Toast.makeText(ProfileActivity.this, "Wrong password", Toast.LENGTH_LONG).show();
        }
    }

    public void seeUser_Click(View v) {
        Intent i = new Intent(ProfileActivity.this, UserInfo.class);
        i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
        //i.putExtra("Nickname", firebaseAuth.getCurrentUser().getNickname());
        startActivity(i);
    }

}
