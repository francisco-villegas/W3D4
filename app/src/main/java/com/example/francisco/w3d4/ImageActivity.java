package com.example.francisco.w3d4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {

    ImageView ivFullImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        getSupportActionBar().hide();

        ivFullImage = (ImageView) findViewById(R.id.ivFullImage);
        Intent intent = getIntent();
        String img =  intent.getStringExtra("image");
        Picasso.with(this).load(img).into(ivFullImage);
    }
}
