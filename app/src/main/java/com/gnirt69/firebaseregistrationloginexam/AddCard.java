package com.gnirt69.firebaseregistrationloginexam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddCard extends AppCompatActivity {

    private Button addPhoto, addAudio, addString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        /*addPhoto = (Button) findViewById(R.id.addPhoto);
        addAudio = (Button) findViewById(R.id.addAudio);
        addString = (Button) findViewById(R.id.addString);*/

    }

    public void goToAddPhoto_Click(View v) {
        Intent i = new Intent(AddCard.this, AddPhoto.class);
        startActivity(i);
    }

    public void goToAddSound_Click(View v) {
        Intent i = new Intent(AddCard.this, RecordSound.class);
        startActivity(i);
    }

    public void goToAddString_Click(View v) {
        Intent i = new Intent(AddCard.this, AddString.class);
        startActivity(i);
    }
}
