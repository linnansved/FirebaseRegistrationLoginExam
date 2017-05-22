package com.gnirt69.firebaseregistrationloginexam;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class GalleryMain extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener{

    private StorageReference storageReference;

    private DatabaseReference imageDatabaseNameRef;
    private DatabaseReference audioDatabaseNameRef;
    private DatabaseReference mDatabase;
    public DatabaseReference DecksCardsRef;

    public FirebaseAuth firebaseAuth;

    public String imageName;
    public String audioName;
    public String url_name;
    public String audio_name;
    public String text_name;
    public String this_turn, deckID;
    public Toolbar toolbar1;
    public TextView title;

    private String cardID;
    public String userID, albumNameValue;
    public String TAG = GalleryMain.class.getSimpleName();
    public String invitation_title = "Download this epic app :)";
    public String invitation_message;
    public String invitation_deep_link = "http://4vector.com/i/free-vector-ramiras-earth-small-icon-clip-art_104864_Ramiras_Earth_Small_Icon_clip_art_medium.png";

    private GoogleApiClient mGoogleApiClient;

    public int REQUEST_INVITE = 1;
    public Boolean delete = false;

    public ArrayList<String> CardID = new ArrayList<>();
    public ArrayList<GalleryImageModel> data = new ArrayList<>();
    public ArrayList<String> cardKey = new ArrayList<>();

    public HashMap<String, String> Imagelist = new HashMap<>();
    public HashMap<String, String> AudioList = new HashMap<>();
    public HashMap<String, String> TextList = new HashMap<>();

    GalleryAdapter mAdapter;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        albumNameValue = getIntent().getExtras().getString("albumName");

        toolbar1 = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) findViewById(R.id.toolbar_title);
        toolbar1.setTitle(albumNameValue);
        setSupportActionBar(toolbar1);
        title.setText(toolbar1.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Get DeckID
        deckID = getIntent().getExtras().getString("DeckId");

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user =  firebaseAuth.getCurrentUser();
        userID = user.getUid();

        //Get CardID and add it to ArrayList CardID
        DatabaseReference ref = mDatabase.child("Decks").child(deckID).child("Cards");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CardID.add(dataSnapshot.getKey().toString());
                getData(CardID);

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

        // Build GoogleApiClient with AppInvite API for receiving deep links
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(AppInvite.API)
                .build();


        boolean autoLaunchDeepLink = false;
        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, this, autoLaunchDeepLink)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @Override
                            public void onResult(@NonNull AppInviteInvitationResult result) {
                                if (result.getStatus().isSuccess()) {
                                    // Extract deep link from Intent
                                    Intent intent = result.getInvitationIntent();
                                    String deepLink = AppInviteReferral.getDeepLink(intent);

                                    // Handle the deep link. For example, open the linked
                                    // content, or apply promotional credit to the user's
                                    // account.

                                    // Byt plats. ProfileActivity
                                } else {
                                    Log.d(TAG, "getInvitation: no deep link found.");
                                }
                            }
                        });
    }

    public void getData(ArrayList CardID){
        for (int i = 0; i < CardID.size(); i++) {
            this_turn = (String) CardID.get(i);
            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Cards").child(this_turn).child("Images").child("URL");
            reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot2) {
                    url_name = dataSnapshot2.getValue(String.class);

                    if (Imagelist.values().contains(url_name)){
                    }

                    else{
                        Imagelist.put(this_turn, url_name);
                    }
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

                    if (AudioList.values().contains(audio_name)){
                    }
                    else{
                        AudioList.put(this_turn, audio_name);
                    }
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

                    if (TextList.values().contains(text_name)){
                    }
                    else{
                        TextList.put(this_turn, text_name);
                        syncData();
                        createGallery();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //handle databaseError
                }

            });
        }

    }

    public void syncData(){
        for (HashMap.Entry<String, String> entry : Imagelist.entrySet()) {
            GalleryImageModel imageModel = new GalleryImageModel();
            String key = entry.getKey();
            cardKey.add(key);
            imageModel.setUrl(entry.getValue());
            imageModel.setName(TextList.get(key));
            imageModel.setAudio(AudioList.get(key));
            data.add(imageModel);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement

        if (id == R.id.home_button){
            goToProfileActivity();

        }

        if (id == R.id.action_delete){
            deletePhotos();
        }

        if (id == R.id.action_add) {
            goToAddPhoto();
        }


        if (id == R.id.action_deletealbum) {

            deleteAlbum();
        }

        if (id == R.id.action_share) {

            shareAlbum();
        }

        return super.onOptionsItemSelected(item);
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
                        public void onItemClick(View view, final int position) {
                            if (delete == false){
                                Intent intent = new Intent(GalleryMain.this, GalleryDetailActivity.class);
                                intent.putExtra("pos", position);
                                intent.putParcelableArrayListExtra("data", data);
                                startActivity(intent);

                            }else {
                                DialogInterface.OnClickListener dialogCklickListener = new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which){
                                    switch (which){
                                        case DialogInterface.BUTTON_POSITIVE:
                                            String cardID = CardID.get(position);
                                            getNameRemoveImgStorage(cardID);
                                            getNameRemoveAudStorage(cardID);
                                            removeDB(cardID);

                                            mRecyclerView.setAlpha(1);
                                            delete = false;
                                            finish();
                                            startActivity(getIntent());
                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            Toast.makeText(getApplicationContext(), "Ok", Toast.LENGTH_LONG).show();
                                            mRecyclerView.setAlpha(1);
                                            delete = false;
                                    }
                                }
                            };
                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            builder.setMessage("Är du säker att du vill ta bort bilden? Ändringarna är permanenta")
                                    .setPositiveButton("Ja", dialogCklickListener)
                                    .setNegativeButton("Nej", dialogCklickListener).show();
                        }
                    }
                }));
        }

    private void removeStorageImage(String imageName) {

        StorageReference imageRef = storageReference.child(userID).child("images/" + imageName);
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

        StorageReference audioRef = storageReference.child(userID).child("audio/" + audioName);
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

    private void removeDB(String cardID){
        mDatabase.child("Decks").child(deckID).child("Cards").child(cardID).removeValue();
        mDatabase.child("Cards").child(cardID).removeValue();
    }

    private void getNameRemoveImgStorage(String cardID){
        imageDatabaseNameRef = mDatabase.child("Cards").child(cardID).child("Images").child("Name");
        imageDatabaseNameRef.addValueEventListener(new ValueEventListener() {

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
        audioDatabaseNameRef.addValueEventListener(new ValueEventListener() {

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
        intent.putExtra("sendDeckID", deckID);
        startActivity(intent);
    }

    public void goToProfileActivity(){
        Intent intent = new Intent(GalleryMain.this, ProfileActivity.class);
        startActivity(intent);
    }

    public void deletePhotos() {
        mRecyclerView.setAlpha(0.5f);
        delete = true;

    }


    public void deleteAlbum() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener(){
           @Override
            public void onClick(DialogInterface dialog, int which){
               switch (which){
                   case DialogInterface.BUTTON_POSITIVE:
                       deleteDeckFromUser();
                       deleteDeckFromDeck();

                       Intent intent = new Intent(GalleryMain.this, ProfileActivity.class);
                       startActivity(intent);

                       Toast.makeText(getApplicationContext(), "Albumet är borttaget", Toast.LENGTH_LONG).show();


                   case DialogInterface.BUTTON_NEGATIVE:
                       Toast.makeText(getApplicationContext(), "Ok", Toast.LENGTH_LONG).show();

               }
           }

        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Är du säker att du vill ta bort albumet? Ändringarna är permanenta")
                .setPositiveButton("Ja", dialogClickListener)
                .setNegativeButton("Nej", dialogClickListener).show();

    }

    public void shareAlbum() {
        Intent intent = new AppInviteInvitation.IntentBuilder(invitation_title)
                .setDeepLink(Uri.parse(invitation_deep_link))
                .setMessage(invitation_message)
                .setEmailHtmlContent("<html><body>"
                        + "<p>Ladda ner den senaste appen för ditt barn</p>"
                        + "%%APPINVITE_LINK_PLACEHOLDER%%"
                        + "<p>Va med och skapa en bättre värld för dig, din familj och ditt barn</p>"
                        + "</body></html>")
                .setEmailSubject("XYZ Offer")
                .build();
        startActivityForResult(intent, REQUEST_INVITE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
            }
        }
    }


    private void deleteDeckFromUser(){
        mDatabase.child("Users").child(userID).child("Decks").child(deckID).removeValue();

    }

    private void deleteDeckFromDeck(){

        DecksCardsRef = mDatabase.child("Decks").child(deckID).child("Cards");
        DecksCardsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                cardID = (dataSnapshot.getKey().toString());
                deleteCardsInDeck(cardID);
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
        mDatabase.child("Decks").child(deckID).removeValue();

    }

    private void deleteCardsInDeck(String cardID) {
            mDatabase.child("Cards").child(cardID).removeValue();
        }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}


