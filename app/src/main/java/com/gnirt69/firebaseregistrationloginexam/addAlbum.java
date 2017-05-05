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
    public String deckID;

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

        Intent i = new Intent(addAlbum.this, AddPhoto.class);
        i.putExtra("deckID", deckID);
        startActivity(i);


    }


    public void createDeckIdUnderUserToDb() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.v(LOG_TAG, "SE HIT" + userID); //hit funkar det
        Map<String, String> decks = new HashMap<>(); //Hit funkar det också, men inte innanför for-loopen
        // Här vill vi Lägga till if-sats som gör att bara ett deck-id med samma id kan lagras under user
        //if deckID
        Log.v(LOG_TAG, "SE HIT1" + userID);
        for(int m = 0; m < arrayDeckID.size(); m++){
            Log.v(LOG_TAG, "SE HIT2 " + userID);
            userRef = mDatabase.child("Users").child(userID).child("Decks");
            Log.v(LOG_TAG, "SE HIT3" + userID);
            decks.put("DeckID"+" " + m, arrayDeckID.get(m));
            Log.v(LOG_TAG, "SE HIT4" + userID);
            userRef.setValue(decks);
            Log.v(LOG_TAG, "SE HIT5" + userID);
        }
    }

    public void createAlbumToDb(View view) {
        EditText editText = (EditText) findViewById(R.id.albumName);
        String message = editText.getText().toString();
        Log.v(LOG_TAG, message);
        deckRef = mDatabase.child("Decks").child(deckID);
        Map<String, String> string = new HashMap<>();
        string.put("albumName", message);
        deckRef.setValue(string);
    }

}
