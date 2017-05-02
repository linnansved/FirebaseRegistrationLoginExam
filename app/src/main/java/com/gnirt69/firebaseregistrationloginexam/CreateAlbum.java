package com.gnirt69.firebaseregistrationloginexam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by linnansved on 2017-04-26.
 */

public class CreateAlbum extends AppCompatActivity {

    private ImageView imageView;
    private DatabaseReference mDatabase;
    public String deckID;
    public ArrayList<String> arrayDeckID = new ArrayList<String>();
    private DatabaseReference deckRef;
    private static final String LOG_TAG = "AlbumUploadTest";
    public int i = 0;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_album);
        imageView = (ImageView) findViewById(R.id.imageView);
        //StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void onClickUpload(View view) {

        deckID = UUID.randomUUID().toString();
        uploadAlbumNameToDatabase(view);

        arrayDeckID.add(i, deckID);
        i++;
        Intent i = new Intent(CreateAlbum.this, AddPhoto.class);
        startActivity(i);

        Toast.makeText(getApplicationContext(), "Album skapat", Toast.LENGTH_LONG).show();
    }


   /* private void uploadAlbumNameToDatabase(View view){
        try{
            EditText editText = (EditText) findViewById(R.id.albumName);
            String message = editText.getText().toString();
            Log.v(LOG_TAG, message);

            deckRef = mDatabase.child("Albums").child(deckID);
            Map<String, String> album = new HashMap<>();
            album.put("decks", message);
        }
        catch (Exception e){
            Log.e(LOG_TAG, e.getMessage());

        }

    }*/

    private void uploadAlbumNameToDatabase(View view) {
        EditText editText = (EditText) findViewById(R.id.deckName);
        String deckName = editText.getText().toString();

        deckRef = mDatabase.child("Decks").child(deckID);
        Map<String, String> decks = new HashMap<>();
        decks.put("Name", deckName);
        deckRef.setValue(decks);
    }


   /*public void uploadUserToDatabase(){
        try{
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Log.d(LOG_TAG, userID);

            EditText editText1 = (EditText) findViewById(R.id.deckname);
            String deckID = editText1.getText().toString();

            userRef = mDatabase.child("Users").child(userID);
            Map<String, String> info = new HashMap<>();

            info.put("decks", deckID);
            //info.put("decks", arrayUserID);
            userRef.setValue(info);


            //arrayUserID.add(i, deckID);
            //i++;

        }
        catch (Exception e){
            Log.e(LOG_TAG, e.getMessage());

        }

    } */
}