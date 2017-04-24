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
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
    private Uri filePath;
    private ImageView imageView;
    private static final int PICK_IMAGE_REQUES = 234;
    public Button buttonImageChoose;
    private String imageName;
    private String audioName;
    private StorageReference storageReference;
    public Uri downloadUrl;
    private DatabaseReference mDatabase;
    private DatabaseReference imageRef;
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
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
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
        if (mAudioName!=null && filePath!=null) {
            uploadImage();
            uploadAudio();
        } else {
            Toast.makeText(getApplicationContext(), "du måste lägga till bild och ljud", Toast.LENGTH_LONG).show();
        }
    }
    private void uploadImage() {
        imageName = "image_"+generateRandom().toString()+".jpeg";
        StorageReference riversRef = storageReference.child("images/").child(imageName);
        riversRef.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_LONG).show();
                        //downloadUrl = taskSnapshot.getDownloadUrl();
                        //ImagesDB();
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
    private void uploadAudio(){
        Uri uri = Uri.fromFile(new File(mAudioName));
        String uriString = uri.getLastPathSegment();
        StorageReference riversRef1 = storageReference.child("audio/").child(uriString);
        riversRef1.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}