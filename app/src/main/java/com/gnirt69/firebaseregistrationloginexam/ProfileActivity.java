package com.gnirt69.firebaseregistrationloginexam;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.*;
import com.google.firebase.database.ChildEventListener;
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
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.data;
import static android.R.attr.tag;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference1;
    private DatabaseReference databaseReference2;
    private DatabaseReference databaseReference3;

    public ArrayList<String> DeckList = new ArrayList<>();

    public ArrayList<ImageView> AlbumList = new ArrayList<>();

    private Button LogOutBtn, editUserBtn, enterBtn;

    private ImageView unLocked, locked, addAlbum;

    private ImageView alb1, alb2, alb3, alb4, alb5, alb6, alb7, alb8;

    private TextView tvEmail,tvNick, textSqParent;
    private EditText answerParent;

    private String answer, DeckId;

    public String nickname;
    public String url_name;
    public String audio_name;
    public String text_name;
    public String getDeck_string;

    public ImageView imageView;

    private DatabaseReference mDatabase;
    private DatabaseReference hej, getDeck;

    public HashMap<String, String> Imagelist = new HashMap<>();
    public HashMap<String, String> AudioList = new HashMap<>();
    public HashMap<String, String> TextList = new HashMap<>();

    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        unLocked = (ImageView) findViewById(R.id.unlock);
        locked = (ImageView) findViewById(R.id.lock);
        addAlbum = (ImageView) findViewById(R.id.addAlbumButton);
        editUserBtn = (Button) findViewById(R.id.UserClick);
        enterBtn = (Button) findViewById(R.id.enterBtn);
        textSqParent = (TextView)findViewById(R.id.textSqParent);
        answerParent = (EditText) findViewById(R.id.answerParent);
        tvNick = (TextView) findViewById(R.id.tvNickname);


        //Tar in alla möjliga album
        alb1 = (ImageView) findViewById(R.id.AddAlbum1);
        alb2 = (ImageView) findViewById(R.id.AddAlbum2);
        alb3 = (ImageView) findViewById(R.id.AddAlbum3);
        alb4 = (ImageView) findViewById(R.id.AddAlbum4);
        alb5 = (ImageView) findViewById(R.id.AddAlbum5);
        alb6 = (ImageView) findViewById(R.id.AddAlbum6);
        alb7 = (ImageView) findViewById(R.id.AddAlbum7);
        alb8 = (ImageView) findViewById(R.id.AddAlbum8);

        AlbumList.add(alb1);
        AlbumList.add(alb2);
        AlbumList.add(alb3);
        AlbumList.add(alb4);
        AlbumList.add(alb5);
        AlbumList.add(alb6);
        AlbumList.add(alb7);
        AlbumList.add(alb8);
        
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


        //Gör alla album osyndliga tillsvidare
        alb1.setVisibility(View.GONE);
        alb2.setVisibility(View.GONE);
        alb3.setVisibility(View.GONE);
        alb4.setVisibility(View.GONE);
        alb5.setVisibility(View.GONE);
        alb6.setVisibility(View.GONE);
        alb7.setVisibility(View.GONE);
        alb8.setVisibility(View.GONE);


        LogOutBtn = (Button) findViewById(R.id.LogOutBtn);
        String userID = user.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        hej = mDatabase.child("Users").child(userID).child("nickname");
        hej.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nickname = dataSnapshot.getValue(String.class);
                tvNick.setText(nickname);
                tvNick.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        getDeck = mDatabase.child("Users").child(userID).child("Decks");
        getDeck.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getDeck_string = dataSnapshot.getKey().toString();
                DeckList.add(getDeck_string);
                Log.d("deck", String.valueOf(DeckList));

                for (int i = 0; i < DeckList.size(); i++) {
                    imageView = AlbumList.get(i);
                    imageView.setId(i);
                    imageView.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });


        // Get image URL from database
        ArrayList<String> cardID = new ArrayList<>();

        //Här ska vi hämta in och addera CardID - här lägger vi in manuellt
        cardID.add("10575602-d5b0-487e-9050-05fbb429d911"); //Sailormoon
        cardID.add("2f9ca101-ae77-4bb1-bc79-1a6deffe4f38"); //gadda
        cardID.add("e0c1af48-ee82-4dde-b548-ed17246f1112"); // hund


        for (int i = 0; i < cardID.size(); i++) {
            final String this_turn = cardID.get(i);
            databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Cards").child(this_turn).child("Images").child("URL");
            databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot2) {
                    url_name = dataSnapshot2.getValue(String.class);
                    Imagelist.put(this_turn, url_name);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //handle databaseError
                }

            });


            //Get audio URL from database
            databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Cards").child(this_turn).child("Audio").child("URL");
            databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot3) {
                    audio_name = dataSnapshot3.getValue(String.class);
                    AudioList.put(this_turn, audio_name);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //handle databaseError
                }

            });

            //Get text from database
            databaseReference3 = FirebaseDatabase.getInstance().getReference().child("Cards").child(this_turn).child("picName");
            databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot3) {
                    text_name = dataSnapshot3.getValue(String.class);
                    TextList.put(this_turn, text_name);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //handle databaseError
                }

            });

        }

        LogOutBtn.setOnClickListener(this);
    }

    public void toGallery_Click(View v) {
        int id = v.getId();
        DeckId = DeckList.get(id);
        Intent i = new Intent(ProfileActivity.this, GalleryMain.class);
        i.putExtra("DeckId", DeckId);

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
        Intent i = new Intent(ProfileActivity.this, addAlbum.class);
        //Log.d("Här är DeckID", ID);
      //  Button btn = new Button(this);
       // btn.setText("hej");
        startActivity(i);
    }

   public void toChild_Click(View v) {

       Log.d("går till kids", "toChild_Click: ");
       unLocked.setVisibility(View.GONE);
       locked.setVisibility(View.VISIBLE);
       //LogOutBtn.setVisibility(View.GONE);
       addAlbum.setVisibility(View.GONE);
       editUserBtn.setVisibility(View.GONE);
       //tvEmail.setVisibility(View.GONE);
       tvNick.setText(nickname);
       tvNick.setVisibility(View.VISIBLE);


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
           // LogOutBtn.setVisibility(View.VISIBLE);
            addAlbum.setVisibility(View.VISIBLE);
            editUserBtn.setVisibility(View.VISIBLE);
           // tvEmail.setVisibility(View.GONE);
            enterBtn.setVisibility(View.GONE);
            textSqParent.setVisibility(View.GONE);
            answerParent.setVisibility(View.GONE);
            tvNick.setText(nickname);
            tvNick.setVisibility(View.VISIBLE);
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
    

}
