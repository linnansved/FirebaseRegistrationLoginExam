package com.gnirt69.firebaseregistrationloginexam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class addAlbum extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private DatabaseReference deckRef;
    private static final String LOG_TAG = "AddAlbum";
    public int j = 0;
    private DatabaseReference userRef;
    public ArrayList<String> arrayDeckID = new ArrayList<String>();
    public String deckID, albumName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_album);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        deckID = UUID.randomUUID().toString();

    }

    public void onClickCreateAlbum(View view) {
        createAlbumToDb(view);
        createDeckIdUnderUserToDb();
        arrayDeckID.add(j, deckID);
        j++;
        //Skickar deckID till AddPhoto
        Intent i = new Intent(addAlbum.this, AddPhoto.class);
        i.putExtra("deckID", deckID);
        startActivity(i);
    }



    public void createAlbumToDb(View view) {
        EditText editText = (EditText) findViewById(R.id.albumName);
        String message = editText.getText().toString();
        Log.v(LOG_TAG, message);
        albumName = message.toString();
        deckRef = mDatabase.child("Decks").child(deckID);
        Map<String, String> string = new HashMap<>();
        string.put("albumName", message);
        deckRef.setValue(string);


        //        Intent i = new Intent(addAlbum.this, AddPhoto.class);
       // i.putExtra("deckID", deckID);
     //   startActivity(i);
    }

    public void createDeckIdUnderUserToDb() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.v(LOG_TAG, "SE HIT" + userID);
        userRef = mDatabase.child("Users").child(userID).child("Decks");
        Map<String, Boolean> decks = new HashMap<>();
        decks.put(deckID, true);
        userRef.setValue(decks);

    }

}

