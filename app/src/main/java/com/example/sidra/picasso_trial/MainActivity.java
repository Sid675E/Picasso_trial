package com.example.sidra.picasso_trial;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;

public class MainActivity extends AppCompatActivity {

    //Declaring our ImageView
    private ImageView imageView;

    CognitoCachingCredentialsProvider credentialsProvider;
    AmazonS3 s3;
    TransferUtility transferUtility;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing the ImageView
        imageView = (ImageView) findViewById(R.id.imageView);





    }

    @Override
    public void onStart(){
        super.onStart();

        DownloadFilesTask downloadTask = new DownloadFilesTask();
        downloadTask.execute();
    }


    private class DownloadFilesTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),    /* get the context for the application */
                    "ap-northeast-1:2c9313f6-ef22-44e7-bdb3-2a41f5b155a3",    /* Identity Pool ID */
                    Regions.AP_NORTHEAST_1          /* Region for your identity pool--US_EAST_1 or EU_WEST_1*/
            );

            s3 = new AmazonS3Client(credentialsProvider);
            s3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));

            transferUtility = new TransferUtility(s3, getApplicationContext());
            //.load("https://upload.wikimedia.org/wikipedia/commons/d/d5/Mona_Lisa_(copy,_Hermitage).jpg")
            String S3_END_POINT = "https://s3-ap-northeast-1.amazonaws.com/"; // public files
            S3Object s3Object = s3.getObject("pixliapp01","/images/05es.jpg" );
            String url = S3_END_POINT + s3Object.getBucketName() + "/" + s3Object.getKey();

            System.out.println(" this   "+url);




//        uri = Uri.parse(ss.toString());
//        System.out.println(""+uri);


//            Picasso.with(getApplicationContext())
//                    .load("https://upload.wikimedia.org/wikipedia/commons/d/d5/Mona_Lisa_(copy,_Hermitage).jpg")
//                    //.placeholder(R.drawable.image_name_default) //have to put images inside studio for use
//                    .error(R.drawable.err)
//                    .resize(50,50)
//                    .centerInside()
//                    .into(imageView);
            return null;
        }
    }



    /*private class DownloadFilesTask extends AsyncTask<url , Integer, Long>
    {
        @Override
        protected Long doInBackground(URL... urls)
        {
            int count = urls.length;
            long totalSize = 0;
            for (int i = 0; i < count; i++)
            {
                Uri uri=null;
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
            }
            return totalSize;
        }

        @Override
        protected void onProgressUpdate(Integer... progress)
        {
            setProgressPercent(progress[0]);
        }

        @Override
        protected void onPostExecute(Long result) {
            showDialog("Downloaded " + result + " bytes");
        }
    }*/

}
