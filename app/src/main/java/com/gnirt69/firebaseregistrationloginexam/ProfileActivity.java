package com.gnirt69.firebaseregistrationloginexam;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.*;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import android.util.Log;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.data;
import static android.R.attr.tag;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private DatabaseReference profilePicReference;
    private StorageReference storageReference;

    public ArrayList<String> DeckList = new ArrayList<>();
    public ArrayList<String> AlbumNames = new ArrayList<>();
    public ArrayList<String> Arrays = new ArrayList<>();
    public ArrayList<ImageView> AlbumList = new ArrayList<>();

    private Button LogOutBtn, editUserBtn, enterBtn;

    private ImageView unLocked, locked, imageView, addAlbum, instructions;
    private ImageView alb1, alb2, alb3, alb4, alb5, alb6, alb7, alb8;

    private TextView tvEmail,tvNick, textSqParent, infoText, plusText;
    private EditText answerParent;

    public String nickname, picName, setName;
    public String getDeck_string;
    public ArrayList<TextView> AlbumName = new ArrayList<>();
    private TextView alb1T, alb2T, alb3T, alb4T, alb5T, alb6T, alb7T, alb8T, AlbNameView;
    private String answer, DeckId, DeckId1, albumNameValue;
    private DatabaseReference hej, getDeck, albumNameDatabaseRef;
    private DatabaseReference mDatabase;
    public int length;
    public int counter;


    public File localFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        unLocked = (ImageView) findViewById(R.id.unlock);
        instructions = (ImageView) findViewById(R.id.album1);
        locked = (ImageView) findViewById(R.id.lock);
        addAlbum = (ImageView) findViewById(R.id.album2);
        editUserBtn = (Button) findViewById(R.id.UserClick);
        enterBtn = (Button) findViewById(R.id.enterBtn);
        textSqParent = (TextView)findViewById(R.id.textSqParent);
        answerParent = (EditText) findViewById(R.id.answerParent);
        tvNick = (TextView) findViewById(R.id.tvNickname);
        infoText = (TextView) findViewById(R.id.infoText);
        plusText = (TextView) findViewById(R.id.plusText);

        CircleImageView i = (CircleImageView)findViewById(R.id.profile_image);

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

        //Tar in alla albumtexter

        alb1T = (TextView) findViewById(R.id.alb1Text);
        alb2T = (TextView) findViewById(R.id.alb2Text);
        alb3T = (TextView) findViewById(R.id.alb3Text);
        alb4T = (TextView) findViewById(R.id.alb4Text);
        alb5T = (TextView) findViewById(R.id.alb5Text);
        alb6T = (TextView) findViewById(R.id.alb6Text);
        alb7T = (TextView) findViewById(R.id.alb7Text);
        alb8T = (TextView) findViewById(R.id.alb8Text);

        AlbumName.add(alb1T);
        AlbumName.add(alb2T);
        AlbumName.add(alb3T);
        AlbumName.add(alb4T);
        AlbumName.add(alb5T);
        AlbumName.add(alb6T);
        AlbumName.add(alb7T);
        AlbumName.add(alb8T);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        unLocked.setVisibility(View.VISIBLE);
        instructions.setVisibility(View.VISIBLE);
        locked.setVisibility(View.GONE);
        //LogOutBtn.setVisibility(View.VISIBLE);
        addAlbum.setVisibility(View.VISIBLE);
        editUserBtn.setVisibility(View.VISIBLE);
        enterBtn.setVisibility(View.GONE);
        textSqParent.setVisibility(View.GONE);
        answerParent.setVisibility(View.GONE);


        //Gör alla album & albumnamn osynliga tills vidare
        alb1.setVisibility(View.GONE);
        alb2.setVisibility(View.GONE);
        alb3.setVisibility(View.GONE);
        alb4.setVisibility(View.GONE);
        alb5.setVisibility(View.GONE);
        alb6.setVisibility(View.GONE);
        alb7.setVisibility(View.GONE);
        alb8.setVisibility(View.GONE);

        alb1T.setVisibility(View.GONE);
        alb2T.setVisibility(View.GONE);
        alb3T.setVisibility(View.GONE);
        alb4T.setVisibility(View.GONE);
        alb5T.setVisibility(View.GONE);
        alb6T.setVisibility(View.GONE);
        alb7T.setVisibility(View.GONE);
        alb8T.setVisibility(View.GONE);

        LogOutBtn = (Button) findViewById(R.id.LogOutBtn);
        final String userID = user.getUid();

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
                Log.d("deckList", String.valueOf(DeckList));

                for ( int i = 0; i < DeckList.size(); i++) {
                    imageView = AlbumList.get(i);
                    imageView.setId(i);
                    counter = 0;
                    imageView.setVisibility(View.VISIBLE);
                    DeckId1 = DeckList.get(i);
                    albumNameDatabaseRef = mDatabase.child("Decks").child(DeckId1).child("albumName");
                    albumNameDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            albumNameValue = dataSnapshot.getValue(String.class);
                            if (AlbumNames.contains(albumNameValue)) {
                               // Log.d("finns redan", String.valueOf(AlbumNames));
                            }
                            else {
                                AlbumNames.add(albumNameValue);
                                AlbNameView=AlbumName.get(counter);
                                AlbNameView.setText(albumNameValue);
                                AlbNameView.setVisibility(View.VISIBLE);
                                counter++;
                            }


                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }

                    });

                    //AlbNameView.setText("hej");

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

        LogOutBtn.setOnClickListener(this);


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

                            ImageView img = (ImageView) findViewById(R.id.profile_image);
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


    public void toGallery_Click(View v) {
        int id = v.getId();
        DeckId = DeckList.get(id);
        Intent i = new Intent(ProfileActivity.this, GalleryMain.class);
        i.putExtra("DeckId", DeckId);
        i.putExtra("albumName", AlbumNames.get(id));
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
        length =DeckList.size();
        String len = Integer.toString(length);
        Log.d("langden ar faktsikt", len);
        i.putExtra("Length", (int)length);
        startActivity(i);
    }

    public void toChild_Click(View v) {
        Log.d("går till kids", "toChild_Click: ");
        unLocked.setVisibility(View.GONE);
        locked.setVisibility(View.VISIBLE);
        instructions.setVisibility(View.GONE);
        LogOutBtn.setVisibility(View.GONE);
        addAlbum.setVisibility(View.GONE);
        editUserBtn.setVisibility(View.GONE);
        //tvEmail.setVisibility(View.GONE);
        tvNick.setText(nickname);
        tvNick.setVisibility(View.VISIBLE);
        infoText.setVisibility(View.GONE);
        plusText.setVisibility(View.GONE);


    }
    public void toParent_Click(View v) {
        textSqParent.setVisibility(View.VISIBLE);
        answerParent.setVisibility(View.VISIBLE);
        enterBtn.setVisibility(View.VISIBLE);
        infoText.setVisibility(View.VISIBLE);
        plusText.setVisibility(View.VISIBLE);
    }

    public void testParent(View v) {
        answer = answerParent.getText().toString();

        answerParent.setText("");

        if(answer.equals("parent")){
            unLocked.setVisibility(View.VISIBLE);
            locked.setVisibility(View.GONE);
            // LogOutBtn.setVisibility(View.VISIBLE);
            addAlbum.setVisibility(View.VISIBLE);
            editUserBtn.setVisibility(View.VISIBLE);
            // tvEmail.setVisibility(View.GONE);
            textSqParent.setVisibility(View.GONE);
            answerParent.setVisibility(View.GONE);
            enterBtn.setVisibility(View.GONE);
            tvNick.setText(nickname);
            tvNick.setVisibility(View.VISIBLE);
            LogOutBtn.setVisibility(View.VISIBLE);
            instructions.setVisibility(View.VISIBLE);
        }
        else {

            Toast.makeText(ProfileActivity.this, "You typed the wrong word, retype parent to enter the admin pages", Toast.LENGTH_LONG).show();
        }
    }

    public void seeUser_Click(View v) {
        Intent i = new Intent(ProfileActivity.this, EditUser.class);
        i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
        i.putExtra("Nickname", nickname);
        i.putExtra("localFile", localFile);
        //i.putExtra("localFile", localFile);
        startActivity(i);
    }

    public void toInstructions(View v){
        Intent i = new Intent(ProfileActivity.this, Instructions.class);
        startActivity(i);
        Log.d("Här går det eh", "toInstructions: ");
    }


}

