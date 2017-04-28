package com.gnirt69.firebaseregistrationloginexam;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    private EditText txtEmailLogin;
    private EditText txtPwd;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth firebaseFindUid;
    private DatabaseReference userRef;
    private DatabaseReference mDatabase;
    public String userID;
    public ArrayList<String> arrayDeckID = new ArrayList<>();
    public ArrayList<String> arrayUserID = new ArrayList<>();
    private static final String LOG_TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtEmailLogin = (EditText) findViewById(R.id.txtEmailLogin);
        txtPwd = (EditText) findViewById(R.id.txtPasswordLogin);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void btnUserLogin_Click(View v) {
        final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "Please wait...", "Proccessing...", true);

        (firebaseAuth.signInWithEmailAndPassword(txtEmailLogin.getText().toString(), txtPwd.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(LoginActivity.this, ProfileActivity.class);
                            i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
                            startActivity(i);

                        } else {
                            Log.e("ERROR", task.getException().toString());
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    public void btnRegistration_Click(View v) {
        Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(i);
    }

    public void btnForgotPwd_Click(View v) {
        Intent i = new Intent(LoginActivity.this, ForgotPassword.class);
        startActivity(i);
    }

    public void uploadUserToDatabase(){
        try{
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Log.d(LOG_TAG, userID);

            EditText editText = (EditText) findViewById(R.id.nickname);
            String message = editText.getText().toString();
            Log.v(LOG_TAG, message);

            EditText editText1 = (EditText) findViewById(R.id.deckname);
            String deckID = editText1.getText().toString();

            userRef = mDatabase.child("Users").child(userID);
            Map<String, String> info = new HashMap<>();


            info.put("nickname", message);
            info.put("decks", deckID);
            //info.put("decks", arrayUserID);
            userRef.setValue(info);


            /*arrayUserID.add(i, deckID);
            i++;*/

        }
        catch (Exception e){
            Log.e(LOG_TAG, e.getMessage());

        }

    }
}
