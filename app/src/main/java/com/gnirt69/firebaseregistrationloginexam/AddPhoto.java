package com.gnirt69.firebaseregistrationloginexam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog; import android.content.Intent; import android.graphics.Bitmap; import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull; import android.support.v7.app.AppCompatActivity; import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener; import com.google.android.gms.tasks.OnSuccessListener; import com.google.firebase.storage.FirebaseStorage; import com.google.firebase.storage.OnProgressListener; import com.google.firebase.storage.StorageMetadata; import com.google.firebase.storage.StorageReference; import com.google.firebase.storage.UploadTask;
import java.io.IOException; import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPhoto extends AppCompatActivity implements
        View.OnClickListener{

    private StorageReference storageReference;
    private static final int PICK_IMAGE_REQUES = 234;
    private ImageView imageView, logo;
    private Button buttonChoose, buttonUpload;
    private Uri filePath;
    private Button addPhoto, addAudio, addString, saveString;
    private EditText photoString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        logo = (ImageView) findViewById(R.id.tapetaplogo);
        storageReference = FirebaseStorage.getInstance().getReference();
        imageView = (ImageView) findViewById(R.id.showUploadedPic);
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        addPhoto = (Button) findViewById(R.id.addPhoto);
        addAudio = (Button) findViewById(R.id.addAudio);
        addString = (Button) findViewById(R.id.addString);
        photoString = (EditText) findViewById(R.id.photoString);
        saveString = (Button) findViewById(R.id.saveString);
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

        buttonChoose.setVisibility(View.GONE);
        buttonUpload.setVisibility(View.GONE);
        photoString.setVisibility(View.GONE);
        saveString.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonChoose.setVisibility(View.VISIBLE);
                buttonUpload.setVisibility(View.VISIBLE);
                photoString.setVisibility(View.GONE);
                saveString.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                logo.setVisibility(View.GONE);
            }
        });

        addString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonChoose.setVisibility(View.GONE);
                buttonUpload.setVisibility(View.GONE);
                photoString.setVisibility(View.VISIBLE);
                saveString.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
            }
        });

        addAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonChoose.setVisibility(View.GONE);
                buttonUpload.setVisibility(View.GONE);
                photoString.setVisibility(View.GONE);
                saveString.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);

            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/jpg"); intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), PICK_IMAGE_REQUES);
    }

    private void uploadFile() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading....");
            progressDialog.show();

            StorageReference riversRef = storageReference.child("images/").child(generateRandom() + ".jpeg");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage(((int) progress) + "Uploaded...");

                        }
                    })
            ;
        } else {
            Toast.makeText(getApplicationContext(), "Choose a picture!", Toast.LENGTH_LONG).show();

        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUES && resultCode == RESULT_OK && data != null && data.getData() != null)
            filePath = data.getData();

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateRandom() {

        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

        return timeStamp;
    }
            @Override
            public void onClick (View view){
                if (view == buttonChoose) {
                    showFileChooser();
                } else if (view == buttonUpload) {
                    uploadFile();
                }
        }
}
