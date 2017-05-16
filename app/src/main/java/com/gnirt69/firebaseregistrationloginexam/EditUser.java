package com.gnirt69.firebaseregistrationloginexam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditUser extends AppCompatActivity {

    private Button btnChangeEmail, btnChangePassword, btnChangeNick, changeEmail, changePassword, changeNickname, addProfilePic, uploadPic;

    private EditText oldEmail, newEmail, password, newPassword, newNickname;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private DatabaseReference nick;
    public String userID;
    public String nickname, newNick;
    private static final int PICK_IMAGE_REQUES = 234;
    private Uri imageFilePath;
    public ArrayList<String> arrayPicID = new ArrayList<String>();
    public ArrayList<Uri> picUrlList = new ArrayList<>();
    public int i = 0;
    public String picID;
    private DatabaseReference picRef;
    private String picName;
    private StorageReference storageReference;
    public Uri downloadPicUrl;
    private CircleImageView picView;
    private static final String LOG_TAG = "EditUser";
    public ImageView bubble;
    public boolean visible;
    public File localFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        picView = (CircleImageView) findViewById(R.id.profile_image);
        localFile = (File) getIntent().getExtras().get("localFile");

        if (localFile == null){
            picView= (CircleImageView) findViewById(R.id.profile_image);
            picView.setImageResource(R.drawable.alva);
        }
        else{
            picView= (CircleImageView) findViewById(R.id.profile_image);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath(),bmOptions);
            picView.setImageBitmap(bitmap);
        }

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        bubble = (ImageView) findViewById(R.id.prat);
        bubble.setVisibility(View.GONE);
        visible = false;
        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(EditUser.this, LoginActivity.class));
                    finish();
                }
            }
        };

        btnChangeEmail = (Button) findViewById(R.id.change_email_button);
        btnChangePassword = (Button) findViewById(R.id.change_password_button);
        btnChangeNick = (Button) findViewById(R.id.change_nickname_button);
        changeEmail = (Button) findViewById(R.id.changeEmail);
        changePassword = (Button) findViewById(R.id.changePass);
        changeNickname= (Button) findViewById(R.id.changeNick);
        addProfilePic = (Button) findViewById(R.id.addProfilePic);
        uploadPic = (Button) findViewById(R.id.uploadPic);
        //picView = (ImageView) findViewById(R.id.picView);

        oldEmail = (EditText) findViewById(R.id.old_email);
        newEmail = (EditText) findViewById(R.id.new_email);
        password = (EditText) findViewById(R.id.password);
        newPassword = (EditText) findViewById(R.id.newPassword);
        newNickname = (EditText) findViewById(R.id.newNickname);

        //KNAPPARNA FÖR ATT VÄLJA VAD MAN VILL ÄNDRA
        oldEmail.setVisibility(View.GONE);
        newEmail.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        newPassword.setVisibility(View.GONE);
        changeEmail.setVisibility(View.GONE);
        changePassword.setVisibility(View.GONE);
        newNickname.setVisibility(View.GONE);
        changeNickname.setVisibility(View.GONE);
        uploadPic.setVisibility(View.GONE);

        //addProfilePic.setVisibility(View.GONE);

        btnChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldEmail.setVisibility(View.VISIBLE);
                newEmail.setVisibility(View.VISIBLE);
                password.setVisibility(View.GONE);
                newPassword.setVisibility(View.GONE);
                changeEmail.setVisibility(View.VISIBLE);
                changePassword.setVisibility(View.GONE);
                newNickname.setVisibility(View.GONE);
                changeNickname.setVisibility(View.GONE);
                uploadPic.setVisibility(View.GONE);
                btnChangeNick.setVisibility(View.VISIBLE);
                btnChangeEmail.setVisibility(View.GONE);
                btnChangePassword.setVisibility(View.VISIBLE);
                addProfilePic.setVisibility(View.VISIBLE);
                uploadPic.setVisibility(View.GONE);
            }
        });


        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null && !newEmail.getText().toString().trim().equals("")) {
                    user.updateEmail(newEmail.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(EditUser.this, "Email address is updated. Please sign in with new email id!", Toast.LENGTH_LONG).show();
                                        signOut();
                                    } else {
                                        Toast.makeText(EditUser.this, "Failed to update email!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                } else if (newEmail.getText().toString().trim().equals("")) {
                    newEmail.setError("Enter email");
                }
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldEmail.setVisibility(View.GONE);
                newEmail.setVisibility(View.GONE);
                password.setVisibility(View.VISIBLE);
                newPassword.setVisibility(View.VISIBLE);
                changeEmail.setVisibility(View.GONE);
                changePassword.setVisibility(View.VISIBLE);
                newNickname.setVisibility(View.GONE);
                changeNickname.setVisibility(View.GONE);
                uploadPic.setVisibility(View.GONE);
                btnChangeNick.setVisibility(View.VISIBLE);
                btnChangeEmail.setVisibility(View.VISIBLE);
                btnChangePassword.setVisibility(View.GONE);
                addProfilePic.setVisibility(View.VISIBLE);
                uploadPic.setVisibility(View.GONE);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user != null && !newPassword.getText().toString().trim().equals("")) {
                    if (newPassword.getText().toString().trim().length() < 6) {
                        newPassword.setError("Password too short, enter minimum 6 characters");

                    } else {
                        user.updatePassword(newPassword.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(EditUser.this, "Password is updated, sign in with new password!", Toast.LENGTH_SHORT).show();
                                            signOut();

                                        } else {
                                            Toast.makeText(EditUser.this, "Failed to update password!", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                    }
                } else if (newPassword.getText().toString().trim().equals("")) {
                    newPassword.setError("Enter password");
                }
            }
        });

        btnChangeNick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldEmail.setVisibility(View.GONE);
                newEmail.setVisibility(View.GONE);
                password.setVisibility(View.GONE);
                newPassword.setVisibility(View.GONE);
                changeEmail.setVisibility(View.GONE);
                changePassword.setVisibility(View.GONE);
                newNickname.setVisibility(View.VISIBLE);
                changeNickname.setVisibility(View.VISIBLE);
                uploadPic.setVisibility(View.GONE);
                btnChangeNick.setVisibility(View.GONE);
                btnChangeEmail.setVisibility(View.VISIBLE);
                btnChangePassword.setVisibility(View.VISIBLE);
                addProfilePic.setVisibility(View.VISIBLE);
                uploadPic.setVisibility(View.GONE);

            }
        });



        changeNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null && !newNickname.getText().toString().trim().equals("")) {
                    if (newNickname.getText().toString().trim().length() < 0) {
                        newNickname.setError("Enter Nickname");

                    } else {
                        newNick = newNickname.getText().toString();
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        nickname = getIntent().getExtras().getString("Nickname");
                        nick=mDatabase.child("Users").child(userID).child("nickname");
                        nick.setValue(newNick)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(EditUser.this, "Nickname is updated!", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(EditUser.this, ProfileActivity.class);
                                            i.putExtra("Nickname", newNick);
                                            startActivity(i);

                                        } else {
                                            Toast.makeText(EditUser.this, "Failed to update nickname!", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                    }
                } else if (newPassword.getText().toString().trim().equals("")) {
                    newPassword.setError("Enter password");
                }
            }
        });

    }

    public void signOut() {
        auth.signOut();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    public void addProfilePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUES);
        intent.setType("images/jpg");
        /*intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), PICK_IMAGE_REQUES);*/

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUES && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageFilePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);
                picView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        oldEmail.setVisibility(View.GONE);
        newEmail.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        newPassword.setVisibility(View.GONE);
        newNickname.setVisibility(View.GONE);
        changeEmail.setVisibility(View.GONE);
        changePassword.setVisibility(View.GONE);
        changeNickname.setVisibility(View.GONE);
        btnChangeNick.setVisibility(View.VISIBLE);
        btnChangeEmail.setVisibility(View.VISIBLE);
        btnChangePassword.setVisibility(View.VISIBLE);
        addProfilePic.setVisibility(View.GONE);
        uploadPic.setVisibility(View.VISIBLE);
    }
    public void onClickProfileImage(View view) {
        if (view == addProfilePic) {
            addProfilePhoto();
        } else {
            Toast.makeText(getApplicationContext(), "hej", Toast.LENGTH_LONG).show();
        }
    }

    private String generateRandom() {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        return timeStamp;
    }

    public void onClickUploadProfilePic(View view) {
        if (imageFilePath !=null) {
            picID = UUID.randomUUID().toString();
            uploadProfilePic();
        } else {
            Toast.makeText(getApplicationContext(), "du måste lägga till bild och ljud", Toast.LENGTH_LONG).show();
        }

    }

    private void uploadProfilePic() {
        picName = "pic_"+generateRandom().toString()+".jpeg";
        StorageReference riversRef = storageReference.child(userID).child("profilepic/").child(picName);
        riversRef.putFile(imageFilePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        int x = 0;
                        Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_LONG).show();
                        downloadPicUrl = taskSnapshot.getDownloadUrl();
                        picUrlList.add(x, downloadPicUrl);
                        uploadPicToDatabase();
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

    private void uploadPicToDatabase() {
        mDatabase.child("Users").child(userID).child("pic").child("picID").setValue(picID);
        mDatabase.child("Users").child(userID).child("pic").child("URL").setValue(downloadPicUrl.toString());

        /*Intent i = new Intent(EditUser.this, EditUser.class);
        i.putExtra("picID", picID);
        startActivity(i);*/
    }


    public ArrayList getUri(){
        return picUrlList;
    }


    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    public void hideShowBubble(View v) {
        if (visible){
            bubble.setVisibility(View.GONE);
            visible = false;
        }
        else{
            bubble.setVisibility(View.VISIBLE);
            visible = true;
        }
    }


}


