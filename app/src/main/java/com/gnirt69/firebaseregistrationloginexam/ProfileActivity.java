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

import static android.R.attr.data;
import static android.R.attr.tag;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference1;
    private DatabaseReference databaseReference2;
    private DatabaseReference databaseReference3;

    private Button LogOutBtn, editUserBtn, enterBtn;

    private ImageView unLocked, locked, addAlbum;

    private TextView tvEmail,tvNick, textSqParent;
    private EditText answerParent;

    private String answer;

    public String nickname;
    public String url_name;
    public String audio_name;
    public String text_name;

    private DatabaseReference mDatabase;
    private DatabaseReference hej;

    public ArrayList<String> Imagelist = new ArrayList<>();
    public ArrayList<String> AudioList = new ArrayList<>();
    public ArrayList<String> TextList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        firebaseAuth = FirebaseAuth.getInstance();

        unLocked = (ImageView) findViewById(R.id.unlock);
        locked = (ImageView) findViewById(R.id.lock);
        addAlbum = (ImageView) findViewById(R.id.album4);
        editUserBtn = (Button) findViewById(R.id.UserClick);
        enterBtn = (Button) findViewById(R.id.enterBtn);
        textSqParent = (TextView)findViewById(R.id.textSqParent);
        answerParent = (EditText) findViewById(R.id.answerParent);

        tvNick = (TextView) findViewById(R.id.tvNickname);
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        //FirebaseUser user = firebaseAuth.getCurrentUser();

        unLocked.setVisibility(View.VISIBLE);
        locked.setVisibility(View.GONE);
        //LogOutBtn.setVisibility(View.VISIBLE);
        addAlbum.setVisibility(View.VISIBLE);
        editUserBtn.setVisibility(View.VISIBLE);
        enterBtn.setVisibility(View.GONE);
        textSqParent.setVisibility(View.GONE);
        answerParent.setVisibility(View.GONE);


        LogOutBtn = (Button) findViewById(R.id.LogOutBtn);
        String userID = user.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        hej = mDatabase.child("Users").child(userID).child("nickname");
        Log.d("hej", hej.toString());
        hej.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("we enter on datachange", hej.toString());
                nickname = dataSnapshot.getValue(String.class);
                //Log.d("nickname is fine", nickname.toString());
                //tvEmail = (TextView) findViewById(R.id.tvEmailProfile);
                tvNick.setText(nickname);
                tvNick.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Get image URL from database
        String cardID = "fb7186d5-afd3-40c3-8edb-3937672acdd0";
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Cards").child(cardID).child("Images").child("URL");
        Log.d("tjena", databaseReference1.toString());
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot2) {
                url_name = dataSnapshot2.getValue(String.class);
                Imagelist.add(url_name);

                Log.d("string", Imagelist.toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }

        });

        //Get audio URL from database
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Cards").child(cardID).child("Audio").child("URL");
        Log.d("tjenis", databaseReference2.toString());
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot3) {
                audio_name = dataSnapshot3.getValue(String.class);
                AudioList.add(audio_name);

                Log.d("audio", AudioList.toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }

        });

        //Get text from database
        databaseReference3 = FirebaseDatabase.getInstance().getReference().child("Cards").child(cardID).child("picName");
        Log.d("tjaaa", databaseReference3.toString());
        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot3) {
                text_name = dataSnapshot3.getValue(String.class);
                TextList.add(text_name);

                Log.d("text", TextList.toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }

        });

        LogOutBtn.setOnClickListener(this);


    }

    public void toGallery_Click(View v) {
        Intent i = new Intent(ProfileActivity.this, GalleryMain.class);
        i.putExtra("MyImages", Imagelist);
        i.putExtra("MyAudio", AudioList);
        i.putExtra("MyText", TextList);
        startActivity(i);
    }


    @Override
    public void onClick(View view){

        if(view == LogOutBtn){
            firebaseAuth.signOut();;
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
   /* public void editUser_Click(View v) {
        Intent i = new Intent(ProfileActivity.this, UserInfo.class);
        i.putExtra("Nickname", nickname);
        startActivity(i);
    }*/

    public void addCard_Click(View v) {
        Intent i = new Intent(ProfileActivity.this, AddPhoto.class);
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
        i.putExtra("Nickname", nickname);
        startActivity(i);
    }
//MARKUS AVSNITT
    public void dynamicLink_Click(View v) {
        //MARKUS KOD
    }


}
