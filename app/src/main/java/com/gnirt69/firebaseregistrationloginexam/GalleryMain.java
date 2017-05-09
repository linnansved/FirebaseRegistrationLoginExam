package com.gnirt69.firebaseregistrationloginexam;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class GalleryMain extends AppCompatActivity {
    GalleryAdapter mAdapter;
    RecyclerView mRecyclerView;
    public String imageName;
    public String audioName;
    private DatabaseReference mDatabase;


    ArrayList<GalleryImageModel> data = new ArrayList<>();
    ArrayList<String> cardKey = new ArrayList<>();
    private StorageReference storageReference;

    private DatabaseReference imageDatabaseNameRef;
    private DatabaseReference audioDatabaseNameRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        storageReference = FirebaseStorage.getInstance().getReference();
        ArrayList<String> SendDeckID = (ArrayList<String>) getIntent().getSerializableExtra("DeckID");

        HashMap<String, String> Imagelist = (HashMap<String, String>) getIntent().getSerializableExtra("MyImages");
        HashMap<String, String> AudioList = (HashMap<String, String>) getIntent().getSerializableExtra("MyAudio");
        HashMap<String, String> TextList = (HashMap<String, String>) getIntent().getSerializableExtra("MyText");


        for (HashMap.Entry<String, String> entry : Imagelist.entrySet()) {
            GalleryImageModel imageModel = new GalleryImageModel();
            String key = entry.getKey();
            cardKey.add(key);
            imageModel.setUrl(entry.getValue());
            imageModel.setName(TextList.get(key));
            imageModel.setAudio(AudioList.get(key));
            data.add(imageModel);

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setHasFixedSize(true);


        mAdapter = new GalleryAdapter(GalleryMain.this, data);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {

                        Intent intent = new Intent(GalleryMain.this, GalleryDetailActivity.class);
                        intent.putParcelableArrayListExtra("data", data);
                        intent.putExtra("pos", position);

                        String cardID = cardKey.get(position);
                        getNameRemoveImgStorage(cardID);
                        getNameRemoveAudStorage(cardID);
                        ////removeDB(cardID);

                        startActivity(intent);

                    }
                }));

    }

    private void removeStorageImage(String imageName) {

                StorageReference imageRef = storageReference.child("images/" + imageName);
                imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("yes", "!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("no", "!");

                    }
                });
        }

    private void removeStorageAudio(String audioName) {

        StorageReference audioRef = storageReference.child("audio/" + audioName);
        audioRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("yes", "!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("no", "!");

            }
        });
    }

    /*
    private void removeDB(String cardID){
        mDatabase.child("Decks").child(deckID).child(cardID).removeValue();
        mDatabase.child("Cards").child(cardID).removeValue();
    }*/

    private void getNameRemoveImgStorage(String cardID){
        imageDatabaseNameRef = mDatabase.child("Cards").child(cardID).child("Images").child("Name");
        imageDatabaseNameRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imageName = dataSnapshot.getValue(String.class);
                removeStorageImage(imageName);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }
        });
    }
    private void getNameRemoveAudStorage(String cardID){
        audioDatabaseNameRef = mDatabase.child("Cards").child(cardID).child("Audio").child("Name");
        audioDatabaseNameRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                audioName = dataSnapshot.getValue(String.class);
                removeStorageAudio(audioName);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }
        });
    }
}
