package com.gnirt69.firebaseregistrationloginexam;

        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.media.MediaPlayer;
        import android.media.MediaRecorder;
        import android.net.Uri;
        import android.provider.ContactsContract;
        import android.provider.MediaStore;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.Toast;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.UploadTask;
        import com.google.firebase.storage.StorageReference;
        import java.io.File;
        import java.io.IOException;
        import java.net.URI;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.HashMap;
        import java.util.Map;
        import java.util.UUID;


public class AddPhoto extends AppCompatActivity {
    private static final String LOG_TAG = "AudioRecordTest";
    private MediaRecorder mRecorder = null;
    private String mAudioName = null;
    private boolean booleanIsRecordAudioStarted = true;
    private MediaPlayer   mPlayer = null;
    private boolean booleanIsAudioPlayed = true;
    private Uri imageFilePath;
    private ImageView imageView;
    private static final int PICK_IMAGE_REQUES = 234;
    public Button buttonImageChoose;
    private String imageName;
    private StorageReference storageReference;
    public Uri downloadImageUrl;
    public Uri downloadAudioUrl;
    private DatabaseReference mDatabase;
    private DatabaseReference imageRef;
    private DatabaseReference audioRef;
    private DatabaseReference deckRef;
    private DatabaseReference userRef;
    private DatabaseReference stringRef;
    public ArrayList<Uri> imageUrlList = new ArrayList<>();
    public ArrayList<Uri> audioUrlList = new ArrayList<>();
    public String cardID;
    public ArrayList<String> arrayCardID = new ArrayList<String>();
    public ArrayList<String> arrayUserID = new ArrayList<String>();
    //public ArrayList<String> deckID = new ArrayList<String>();
    public int i = 0;
    public String deckID;
    public String stringUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);
        mAudioName = getExternalCacheDir().getAbsolutePath();
        mAudioName += "/"+"audio_"+generateRandom().toString()+".3gp";
        imageView = (ImageView) findViewById(R.id.imageView);
        buttonImageChoose = (Button) findViewById(R.id.chooseImage);
        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDisplayDatabase = FirebaseDatabase.getInstance().getReference().child("Decks").child(deckID);
        deckID = UUID.randomUUID().toString();

    }
    public void startRecording(View view) {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mAudioName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        mRecorder.start();
    }
    public void stopRecording(View view) {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }
    public void onClickRecord(View view) {
        if (booleanIsRecordAudioStarted) {
            startRecording(view);
            booleanIsRecordAudioStarted = false;
        } else {
            stopRecording(view);
            booleanIsRecordAudioStarted = true;
        }
    }
    public void startPlaying(View view) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mAudioName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }
    public void stopPlaying(View view) {
        mPlayer.release();
        mPlayer = null;
    }
    public void onClickPlay(View view) {
        if (booleanIsAudioPlayed) {
            startPlaying(view);
            booleanIsAudioPlayed = false;
        } else {
            stopPlaying(view);
            booleanIsAudioPlayed = true;
        }
    }
    private String generateRandom() {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        return timeStamp;
    }
    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/jpg");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), PICK_IMAGE_REQUES);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUES && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageFilePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void onClickImage(View view) {
        if (view == buttonImageChoose) {
            chooseImage();
        } else {
            Toast.makeText(getApplicationContext(), "hej", Toast.LENGTH_LONG).show();
        }
    }
    public void onClickUpload(View view) {
        if (mAudioName!=null && imageFilePath !=null) {
            cardID = UUID.randomUUID().toString();
            uploadImage();
            uploadAudio();
            uploadStringToDatabase(view);
            arrayCardID.add(i, cardID);
            i++;

        } else {
            Toast.makeText(getApplicationContext(), "du måste lägga till bild och ljud", Toast.LENGTH_LONG).show();
        }
    }


    public void onClickDone(View view) {
        //createDecksToDB();
        //createUserInDecksToDB();
        //displayFiles();
    }

    /*
    private void createDecksToDB(){
        Map<String, String> cards = new HashMap<>();
        for(int k = 0; k < arrayCardID.size(); k++){
            deckRef = mDatabase.child("Decks").child(deckID);
            cards.put("CardID"+" "+k, arrayCardID.get(k));
            deckRef.setValue(cards);
        }
    }
*/

    /*
    private void createUserInDecksToDB(){
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, String> ownerUserID = new HashMap<>();
        for(int k = 0; k < arrayUserID.size(); k++){
            deckRef = mDatabase.child("Decks").child(deckID);
            ownerUserID.put("ownerUser", userID);
            deckRef.setValue(ownerUserID);
        }
    }*/


    private void uploadImage() {
        imageName = "image_"+generateRandom().toString()+".jpeg";
        StorageReference riversRef = storageReference.child("images/").child(imageName);
        riversRef.putFile(imageFilePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        int x = 0;
                        Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_LONG).show();
                        //downloadImageUrl = taskSnapshot.getDownloadUrl();
                        imageUrlList.add(x, downloadImageUrl);
                        uploadImageToDatabase();
                        x++;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
        ;
    }

    public ArrayList getUri(){
        return imageUrlList;
    }

    private void uploadAudio(){
        Uri uri = Uri.fromFile(new File(mAudioName));
        String uriString = generateRandom();
        StorageReference riversRef1 = storageReference.child("audio/").child(uriString);
        riversRef1.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        int y=0;
                        Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_LONG).show();
                        //downloadAudioUrl = taskSnapshot.getDownloadUrl();
                        audioUrlList.add(y, downloadAudioUrl);
                        uploadAudioToDatabase();
                        y++;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void uploadImageToDatabase() {
        imageRef = mDatabase.child("Cards").child(cardID);
        Map<String, String> images = new HashMap<>();
        images.put("URL", downloadImageUrl.toString());
        imageRef = mDatabase.child("Cards").child(cardID).child("Images");
        imageRef.setValue(images);
    }
    private void uploadAudioToDatabase(){
        audioRef = mDatabase.child("Cards").child(cardID);
        Map<String, String> audio = new HashMap<>();
        audio.put("URL", downloadAudioUrl.toString());
        audioRef = mDatabase.child("Cards").child(cardID).child("Audio");
        audioRef.setValue(audio);
    }


    private void uploadStringToDatabase(View view){
        EditText editText = (EditText) findViewById(R.id.picName);
        String message = editText.getText().toString();
        Log.v(LOG_TAG, message);
        DatabaseReference stringRef = mDatabase.child("Cards").child(cardID);
        Map<String, String> string = new HashMap<>();
        string.put("picName", message);
        stringRef.setValue(string);

    }

    public void tryDeckDatabase(View view) {
        EditText editText2 = (EditText) findViewById(R.id.albumName);
        String message2 = editText2.getText().toString();
        Log.v(LOG_TAG, message2);
        DatabaseReference deckRef = mDatabase.child("Decks").child(deckID);
        Map<String, String> string2 = new HashMap<>();
        string2.put("albumName", message2);
        deckRef.setValue(string2);
    }

    /*
    private void addDeckIdToUserDB(){
        String stringUser = getIntent().getExtras().getString("UserID");
        System.out.print(stringUser);
        Map<String, String> decks = new HashMap<>();
        for(int k = 0; k < arrayDeckID.size(); k++){
            userRef = mDatabase.child("Users").child(stringUser);
            decks.put("deckID"+" "+k, arrayDeckID.get(k));
            userRef.setValue(decks);
        }
    }*/


    }