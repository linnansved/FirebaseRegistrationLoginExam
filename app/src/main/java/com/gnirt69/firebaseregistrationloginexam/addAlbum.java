package com.gnirt69.firebaseregistrationloginexam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class addAlbum extends AppCompatActivity {

    public ArrayList<String> arrayDeckID = new ArrayList<String>();

    private DatabaseReference mDatabase;
    private DatabaseReference deckRef;
    private DatabaseReference userRef;

    public String albumName, len;
    public String deckID;

    public int length;
    public int j = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_album);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        len = "";
        len=Integer.toString(length);


        deckID = UUID.randomUUID().toString();

        length = getIntent().getExtras().getInt("Length");


    }

    public void onClickCreateAlbum(View view) {
        if(length == 8) {
            Toast.makeText(addAlbum.this, "You must delete an album in order to add a new one", Toast.LENGTH_LONG).show();
            Intent i = new Intent(addAlbum.this, ProfileActivity.class);
            startActivity(i);

        }
        else{
            createAlbumToDb(view);
            createDeckIdUnderUserToDb();
            arrayDeckID.add(j, deckID);
            j++;

            Intent intent = new Intent(addAlbum.this, ProfileActivity.class);
            startActivity(intent);
        }

    }
    

    public void createAlbumToDb(View view) {
            EditText editText = (EditText) findViewById(R.id.albumName);
            String message = editText.getText().toString();
            albumName = message.toString();
            deckRef = mDatabase.child("Decks").child(deckID);
            Map<String, String> string = new HashMap<>();
            string.put("albumName", message);
            deckRef.setValue(string);
    }

    public void createDeckIdUnderUserToDb() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Boolean> decks = new HashMap<>();
        userRef = mDatabase.child("Users").child(userID).child("Decks").child(deckID);
        decks.put(deckID, true);
        userRef.setValue(decks);
        
    }
    
}

