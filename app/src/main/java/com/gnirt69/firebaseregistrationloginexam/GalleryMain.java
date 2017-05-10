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
import com.google.firebase.database.ChildEventListener;
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
    public String url_name;
    public String audio_name;
    public String text_name;
    public String this_turn, deckID;
    public Toolbar toolbar1;

    public ArrayList<String> CardID = new ArrayList<>();
    public ArrayList<GalleryImageModel> data = new ArrayList<>();
    public ArrayList<String> cardKey = new ArrayList<>();
    private StorageReference storageReference;

    private DatabaseReference imageDatabaseNameRef;
    private DatabaseReference audioDatabaseNameRef;
    private DatabaseReference mDatabase;

    public HashMap<String, String> Imagelist = new HashMap<>();
    public HashMap<String, String> AudioList = new HashMap<>();
    public HashMap<String, String> TextList = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        toolbar1 = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar1);

        //Get DeckID
        deckID = getIntent().getExtras().getString("DeckId");

        //Get CardID and add it to ArrayList CardID
        DatabaseReference ref = mDatabase.child("Decks").child(deckID).child("Cards");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CardID.add(dataSnapshot.getKey().toString());

                for (int i = 0; i < CardID.size(); i++) {
                    this_turn = (String) CardID.get(i);
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Cards").child(this_turn).child("Images").child("URL");
                    reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot2) {
                            url_name = dataSnapshot2.getValue(String.class);
                            Imagelist.put(this_turn, url_name);
                            Log.d("ImageList", String.valueOf(Imagelist));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //handle databaseError
                        }
                    });

                    //Get audio URL from database
                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("Cards").child(this_turn).child("Audio").child("URL");
                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot3) {
                            audio_name = dataSnapshot3.getValue(String.class);
                            AudioList.put(this_turn, audio_name);
                            Log.d("AudioList", String.valueOf(AudioList));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //handle databaseError
                        }

                    });

                    //Get text from database
                    DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference().child("Cards").child(this_turn).child("picName");
                    reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot3) {
                            text_name = dataSnapshot3.getValue(String.class);
                            TextList.put(this_turn, text_name);
                            Log.d("TextList", String.valueOf(TextList));

                            for (HashMap.Entry<String, String> entry : Imagelist.entrySet()) {
                                Log.d("imagelist", String.valueOf(Imagelist));
                                GalleryImageModel imageModel = new GalleryImageModel();
                                String key = entry.getKey();
                                cardKey.add(key);
                                imageModel.setUrl(entry.getValue());
                                imageModel.setName(TextList.get(key));
                                imageModel.setAudio(AudioList.get(key));
                                data.add(imageModel);
                                Log.d("data", String.valueOf(data));
                            }

                            createGallery();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //handle databaseError
                        }

                    });
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
    }


        public void createGallery() {

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
                        intent.putExtra("pos", position);

                        String cardID = cardKey.get(position);
                        getNameRemoveImgStorage(cardID);
                        getNameRemoveAudStorage(cardID);
                        ////removeDB(cardID);

                        startActivity(intent);

                    }
                }));

        }



    /*public void getData(ArrayList CardID){
        for (int i = 0; i < CardID.size(); i++) {
            this_turn = (String) CardID.get(i);
            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Cards").child(this_turn).child("Images").child("URL");
            reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot2) {
                    url_name = dataSnapshot2.getValue(String.class);
                    Imagelist.put(this_turn, url_name);
                    Log.d("ImageList", String.valueOf(Imagelist));
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //handle databaseError
                }
            });

            //Get audio URL from database
            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("Cards").child(this_turn).child("Audio").child("URL");
            reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot3) {
                    audio_name = dataSnapshot3.getValue(String.class);
                    AudioList.put(this_turn, audio_name);
                    Log.d("AudioList", String.valueOf(AudioList));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //handle databaseError
                }

            });

            //Get text from database
            DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference().child("Cards").child(this_turn).child("picName");
            reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot3) {
                    text_name = dataSnapshot3.getValue(String.class);
                    TextList.put(this_turn, text_name);
                    Log.d("TextList", String.valueOf(TextList));

                    for (HashMap.Entry<String, String> entry : Imagelist.entrySet()) {
                        GalleryImageModel imageModel = new GalleryImageModel();
                        String key = entry.getKey();
                        cardKey.add(key);
                        imageModel.setUrl(entry.getValue());
                        imageModel.setName(TextList.get(key));
                        imageModel.setAudio(AudioList.get(key));
                        data.add(imageModel);
                    }
                    Log.d("data", String.valueOf(data));


                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //handle databaseError
                }

            });
            Log.d("data2", String.valueOf(data));
        }
    }*/

    /*
    public void displayData(){
        for (HashMap.Entry<String, String> entry : Imagelist.entrySet()) {
            Log.d("hej", "hejsan");
            GalleryImageModel imageModel = new GalleryImageModel();
            String key = entry.getKey();
            cardKey.add(key);
            imageModel.setUrl(entry.getValue());
            imageModel.setName(TextList.get(key));
            imageModel.setAudio(AudioList.get(key));
            data.add(imageModel);
        }
    }*/

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


    public void goToAddPhoto(){
        Intent intent = new Intent(GalleryMain.this, AddPhoto.class);
        intent.putExtra("deckID", deckID);
    }
}
