package com.example.sidra.picasso_trial;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by sidra on 27-08-2016.
 */
public class PhotoView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_view);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        int position = getIntent().getIntExtra("position",-1);
        if(position != -1){
            Picasso.with(PhotoView.this)
                    .load(MainActivity.result[position])
                    .into(imageView);
        }
        else{
            Picasso.with(PhotoView.this)
                    .load(R.drawable.err)
                    .into(imageView);
        }

    }
}
