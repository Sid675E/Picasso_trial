package com.example.sidra.picasso_trial;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
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

    String[] mThumbIds = {"images/first.jpg","images/frustration.jpg","images/g.jpg"};
    //,"images/05es.jpg","images/15es","images/2 ee.jpg","images/25es.jpg","images/again??.jpg"

   // String[] mThumbIds = {"https://s3-ap-northeast-1.amazonaws.com/pixliapp01/images/05es.jpg",
    //"https://s3-ap-northeast-1.amazonaws.com/pixliapp01/images/15es.jpg"};

    //Declaring our ImageView
    //private ImageView imageView;

    CognitoCachingCredentialsProvider credentialsProvider;
    AmazonS3 s3;
    TransferUtility transferUtility;

    Uri uri;

    int position;
    public static String[] result = new String[2];
    GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_main);

        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),    /* get the context for the application */
                "ap-northeast-1:2c9313f6-ef22-44e7-bdb3-2a41f5b155a3",    /* Identity Pool ID */
                Regions.AP_NORTHEAST_1          /* Region for your identity pool--US_EAST_1 or EU_WEST_1*/
        );

        s3 = new AmazonS3Client(credentialsProvider);
        s3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));

        transferUtility = new TransferUtility(s3, getApplicationContext());

        //setImage(getApplicationContext(),imageView,1);

        //Initializing the GridView
        gridview = (GridView) findViewById(R.id.gridView);

        setImage(this,gridview,1);
        //gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id){
                Intent intent= new Intent(MainActivity.this,PhotoView.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }

        });
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    private void setImage(final Context context,final ViewGroup parent, final int id) {

        new AsyncTask<Void, Void, String[]>() {

            @Override
            protected String[] doInBackground(Void... params) {

                try {

                    for(int i=0;i< mThumbIds.length;i++) {
                        //Extending the expiry time for photto remission
                        java.util.Date expiration = new java.util.Date();
                        long msec = expiration.getTime();
                        msec += 1000 * 60 * 60; // 1 hour.
                        expiration.setTime(msec);

                        //generating uri for image in S3 bucket
                        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                                new GeneratePresignedUrlRequest("pixliapp01", mThumbIds[i]);
                               // new GeneratePresignedUrlRequest("pixliapp01", "images/05es.jpg");
                        generatePresignedUrlRequest.setMethod(HttpMethod.GET); // Default.
                        generatePresignedUrlRequest.setExpiration(expiration);

                        URL ss = s3.generatePresignedUrl(generatePresignedUrlRequest);
                        System.out.println("" + ss);
                        result[i]=ss.toString();
                        //uri = Uri.parse(ss.toString());
                        System.out.println("jnkjnn" + result[i]);
                        //result[i]= uri;
                    }
                }catch(Exception e){System.out.println(""+e);}
                return result;
            }

            @Override
            protected void onPostExecute(String[] result) {
                super.onPostExecute(result);

                for(int j=0;j<result.length;j++) {
                    System.out.println("In Post Execute :" + result[j]);
                }

                gridview.setAdapter(new ImageAdapter(context));
            }
        }.execute();
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        // Constructor
        public ImageAdapter(Context context) {
            mContext = context;
        }

        public int getCount() {
            return result.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;

            if (convertView == null) {
                System.out.println("yoyoyoyooooooo");
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(300, 300)); //size of photos
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(1, 1, 1, 1);
            }
            else
            {
                System.out.println("rrrrr");
                imageView = (ImageView) convertView;
            }
            //imageView.setImageResource(mThumbIds[position]);
            System.out.println("iiiiiiiiii");
            Picasso.with(mContext)
                    .load(result[position])
                    .placeholder(R.drawable.err)
                    .error(R.drawable.err)
                    //.resize(60, 60)
                    //.centerInside()
                    .into(imageView);
            return imageView;
        }
    }

}
