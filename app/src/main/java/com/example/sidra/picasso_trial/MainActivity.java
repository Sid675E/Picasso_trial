package com.example.sidra.picasso_trial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    //Declaring our ImageView
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing the ImageView
        imageView = (ImageView) findViewById(R.id.imageView);


       Picasso.with(this)
                .load("https://upload.wikimedia.org/wikipedia/commons/d/d5/Mona_Lisa_(copy,_Hermitage).jpg")
                .into(imageView);

    }
}
