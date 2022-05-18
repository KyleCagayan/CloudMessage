package com.example.cloudmessageex;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class MediaMessage1 extends AppCompatActivity {

    ImageView imageView = new ImageView(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media1);

    }

    public void setImageView(Intent intent) {
        Glide.with(this).load(intent.getStringExtra("imgUrl")).into(imageView);
    }
}
