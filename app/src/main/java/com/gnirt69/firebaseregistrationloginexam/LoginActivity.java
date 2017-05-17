package com.gnirt69.firebaseregistrationloginexam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
    public String userID;
    public ArrayList<String> arrayDeckID = new ArrayList<>();
    public ArrayList<String> arrayUserID = new ArrayList<>();
    private static final String LOG_TAG = "LoginActivity";
    public ImageView bubble;
    public boolean visible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtEmailLogin = (EditText) findViewById(R.id.txtEmailLogin);
        txtPwd = (EditText) findViewById(R.id.txtPasswordLogin);
        firebaseAuth = FirebaseAuth.getInstance();
        bubble = (ImageView) findViewById(R.id.prat);
        bubble.setVisibility(View.VISIBLE);
        visible = true;
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
                            finish();

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
