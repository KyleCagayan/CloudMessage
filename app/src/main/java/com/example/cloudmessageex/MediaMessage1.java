package com.example.cloudmessageex;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class MediaMessage1 extends AppCompatActivity {

    public static final String TAG = "CloudMessageEx";

    ImageView imageView;
    TextView mediaTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media1);

        imageView = findViewById(R.id.imageView);
        mediaTitle = findViewById(R.id.tv_mediaTitle);
        Bundle extras = getIntent().getExtras();
        String imgUrl = extras.getString("imgUrl");
        setImageView(imgUrl);

        mediaTitle.setText(extras.getString("title"));

    }

    public void setImageView(String imgUrl) {
        Log.d(TAG, imgUrl);
        Glide.with(this).load(imgUrl).into(imageView);

    }
}
