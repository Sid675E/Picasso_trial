package com.example.sidra.picasso_trial;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    //Declaring our ImageView
    private ImageView imageView;

    CognitoCachingCredentialsProvider credentialsProvider;
    AmazonS3 s3;
    TransferUtility transferUtility;

    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing the ImageView
        imageView = (ImageView) findViewById(R.id.imageView);

        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),    /* get the context for the application */
                "ap-northeast-1:2c9313f6-ef22-44e7-bdb3-2a41f5b155a3",    /* Identity Pool ID */
                Regions.AP_NORTHEAST_1          /* Region for your identity pool--US_EAST_1 or EU_WEST_1*/
        );

        s3 = new AmazonS3Client(credentialsProvider);
        s3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));

        transferUtility = new TransferUtility(s3, getApplicationContext());

        //DownloadFilesTask downloadTask = new DownloadFilesTask();

        setImage(getApplicationContext(),imageView,1);



    }

    @Override
    public void onStart(){
        super.onStart();


    }


    private void setImage(final Context context, final ImageView iv, final int id) {

        new AsyncTask<Void, Void, Uri>() {

            @Override
            protected Uri doInBackground(Void... params) {
                try {
                    java.util.Date expiration = new java.util.Date();
                    long msec = expiration.getTime();
                    msec += 1000 * 60 * 60; // 1 hour.
                    expiration.setTime(msec);

                    GeneratePresignedUrlRequest generatePresignedUrlRequest =
                            new GeneratePresignedUrlRequest("pixliapp01", "images/05es.jpg");
                    generatePresignedUrlRequest.setMethod(HttpMethod.GET); // Default.
                    generatePresignedUrlRequest.setExpiration(expiration);

                    URL ss = s3.generatePresignedUrl(generatePresignedUrlRequest);
                    System.out.println(""+ss);
                    uri = Uri.parse(ss.toString());
                    System.out.println(""+uri);
                }catch(Exception e){System.out.println(""+e);}

                return uri;
            }

            @Override
            protected void onPostExecute(Uri result) {
                super.onPostExecute(result);
                System.out.println("In Post Execute :"+result);
                Picasso.with(context)
                        .load(result)
                        //.placeholder(R.drawable.err)
                        .error(R.drawable.err)
                        .resize(500,500)
                        .centerInside()
                        .into(imageView);
            }
        }.execute();
    }

}
