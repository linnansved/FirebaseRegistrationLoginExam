package com.gnirt69.firebaseregistrationloginexam;

        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.media.Image;
        import android.media.MediaPlayer;
        import android.media.MediaRecorder;
        import android.net.Uri;
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
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
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
    private String mAudioFilePath;
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
    private DatabaseReference stringRef;
    public FirebaseAuth firebaseAuth;
    public ArrayList<Uri> imageUrlList = new ArrayList<>();
    public ArrayList<Uri> audioUrlList = new ArrayList<>();
    public String cardID;
    public ArrayList<String> arrayCardID = new ArrayList<String>();
    public int i = 0;
    public String deckID;
    private ImageView recordRedButton;
    private Button recordButton;

    public String userID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);
        mAudioFilePath = getExternalCacheDir().getAbsolutePath();
        imageView = (ImageView) findViewById(R.id.imageView);
        buttonImageChoose = (Button) findViewById(R.id.chooseImage);
        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDisplayDatabase = FirebaseDatabase.getInstance().getReference().child("Decks").child(deckID);
        Intent intent = getIntent();
        //Tar emot deckID från addAlbum
        deckID = intent.getExtras().getString("deckID");
        Log.v(LOG_TAG, "hejhej");

        recordRedButton = (ImageView) findViewById(R.id.RecordNotice);
        recordRedButton.setVisibility(View.GONE);

        recordButton = (Button) findViewById(R.id.RecordButton);
        recordButton.setVisibility(View.VISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user =  firebaseAuth.getCurrentUser();
        userID = user.getUid();


    }
    public void startRecording(View view) {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mAudioFilePath + "/" + mAudioName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recordRedButton.setVisibility(View.VISIBLE);
        //recordButton.setVisibility(View.GONE);

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
        recordRedButton.setVisibility(View.GONE);
        //recordButton.setVisibility(View.VISIBLE);
    }
    public void onClickRecord(View view) {
        if (booleanIsRecordAudioStarted) {
            mAudioName = "audio_" + generateRandom().toString() + ".3gp";
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
            mPlayer.setDataSource(mAudioFilePath + "/" + mAudioName);
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
            createCardIdInDecksToDB();
            Intent i = new Intent(AddPhoto.this, ProfileActivity.class);
            i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
            startActivity(i);
        } else {
            Toast.makeText(getApplicationContext(), "du måste lägga till bild och ljud", Toast.LENGTH_LONG).show();
        }
    }



    private void createCardIdInDecksToDB() {
        Map<String, Boolean> cards = new HashMap<>();
        for (int k = 0; k < arrayCardID.size(); k++) {
            deckRef = mDatabase.child("Decks").child(deckID).child("Cards");
            cards.put(arrayCardID.get(k), true);
            deckRef.setValue(cards);
        }
    }



    private void uploadImage() {
        imageName = "image_"+generateRandom().toString()+".jpeg";
        StorageReference storageImageRef = storageReference.child(userID).child("images/").child(imageName);
        storageImageRef.putFile(imageFilePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        int x = 0;
                        Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_LONG).show();
                        downloadImageUrl = taskSnapshot.getDownloadUrl();
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
        Uri uri = Uri.fromFile(new File(mAudioFilePath + "/" + mAudioName));
        StorageReference storageAudioRef = storageReference.child(userID).child("audio/").child(mAudioName);
        storageAudioRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        int y=0;
                        Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_LONG).show();
                        downloadAudioUrl = taskSnapshot.getDownloadUrl();
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
        images.put("Name", imageName);
        images.put("URL", downloadImageUrl.toString());
        imageRef = mDatabase.child("Cards").child(cardID).child("Images");
        imageRef.setValue(images);
    }
    private void uploadAudioToDatabase(){
        audioRef = mDatabase.child("Cards").child(cardID);
        Map<String, String> audio = new HashMap<>();
        audio.put("Name", mAudioName);
        audio.put("URL", downloadAudioUrl.toString());
        audioRef = mDatabase.child("Cards").child(cardID).child("Audio");
        audioRef.setValue(audio);
    }


    private void uploadStringToDatabase(View view){
        EditText editText = (EditText) findViewById(R.id.picName);
        String message = editText.getText().toString();
        Log.v(LOG_TAG, message);
        stringRef = mDatabase.child("Cards").child(cardID);
        Map<String, String> string = new HashMap<>();
        string.put("picName", message);
        stringRef.setValue(string);

    }




    }