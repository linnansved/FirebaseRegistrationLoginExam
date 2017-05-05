package com.gnirt69.firebaseregistrationloginexam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GalleryMain extends AppCompatActivity {
    GalleryAdapter mAdapter;
    RecyclerView mRecyclerView;

    ArrayList<GalleryImageModel> data = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_main);

        HashMap<String, String> Imagelist = (HashMap<String, String>) getIntent().getSerializableExtra("MyImages");
        HashMap<String, String> AudioList = (HashMap<String, String>) getIntent().getSerializableExtra("MyAudio");
        HashMap<String, String> TextList = (HashMap<String, String>) getIntent().getSerializableExtra("MyText");


        for (HashMap.Entry<String, String> entry: Imagelist.entrySet()){
            GalleryImageModel imageModel = new GalleryImageModel();
            String key = entry.getKey();
            imageModel.setUrl(entry.getValue());
            imageModel.setName(TextList.get(key));
            imageModel.setAudio(AudioList.get(key));
            data.add(imageModel);

        }

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
                    public void onItemClick(View view, int position) {

                        Intent intent = new Intent(GalleryMain.this, GalleryDetailActivity.class);
                        intent.putParcelableArrayListExtra("data", data);
                        intent.putExtra("pos", position);
                        startActivity(intent);

                    }
                }));

    }

}
