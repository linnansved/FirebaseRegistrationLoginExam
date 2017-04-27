package com.gnirt69.firebaseregistrationloginexam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.auth.FirebaseAuth;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegistrationActivity extends AppCompatActivity {

    private EditText txtEmailAddress;
    private EditText txtPassword;
    private FirebaseAuth firebaseAuth;
    private EditText txtPassword2;
    public EditText txtNickname;
    private StorageReference storageNick;
    private static final String LOG_TAG = "AudioRecordTest";
    /*private DatabaseReference userRef;
    private DatabaseReference mDatabase;
    public String userID;
    public ArrayList<String> arrayDeckID = new ArrayList<>();
    public ArrayList<String> arrayUserID = new ArrayList<>();*/





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        txtEmailAddress = (EditText) findViewById(R.id.txtEmailRegistration);
        txtPassword = (EditText) findViewById(R.id.txtPasswordRegistration);
        txtPassword2 = (EditText) findViewById(R.id.txtPasswordRegistration2);
        txtNickname = (EditText) findViewById(R.id.txtNicknameRegistration);
        firebaseAuth = FirebaseAuth.getInstance();
        storageNick = FirebaseStorage.getInstance().getReference();
    }

    public void btnRegistrationUser_Click(View v) {

       //if(checkPass()){

        final ProgressDialog progressDialog = ProgressDialog.show(RegistrationActivity.this, "Please wait...", "Processing...", true);
        (firebaseAuth.createUserWithEmailAndPassword(txtEmailAddress.getText().toString(), txtPassword.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        Log.d(LOG_TAG, "createUserWithEmail:onComplete" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            Log.d(LOG_TAG, "onComplete: uid =" + user.getUid());
                            Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_LONG).show();
                            //registerNick();

                            //uploadUserToDatabase();

                            Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                            startActivity(i);






                        } else {
                            Log.e("ERROR", task.getException().toString());
                            Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });



    }

   /* public void uploadUserToDatabase(){
        userRef = mDatabase.child("Users").child(userID);
        Map<String, String> decks = new HashMap<>();
        for(int k = 0; k < arrayUserID.size(); ++k){
            decks.put("DeckID"+k, arrayDeckID.get(k));
            userRef.setValue(decks);
        }
    }*/





    }


   /* public void registerNick() {
        txtNickname.getText().toString();
    }*/

  //  }
  /*  private boolean checkPass() {
        if (txtPassword == txtPassword2){
            Toast.makeText(RegistrationActivity.this, "Matching password", Toast.LENGTH_LONG).show();
            return true;
        }else {
            return false;
        }
    }*/
