package com.example.inclass05;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String items;
    Button goButton;
    Spinner sl;
    int index=0;
    String[] itemsarray;
    ProgressBar progressBar;
    String[] urlarray=null;
    ImageView imageView1,nextImage,prevImage;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        items=null;
        goButton= findViewById(R.id.gobutton);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        sl=findViewById(R.id.itemspinner);
        nextImage=findViewById(R.id.nxtImage);
        prevImage=findViewById(R.id.prevImage);
        sl.setOnItemSelectedListener(this);
        imageView1=findViewById(R.id.imageView);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetDataAsync().execute(" https://dev.theappsdr.com/apis/photos/keywords.php");

            }
        });
        nextImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index==urlarray.length-1){
                    index = 0;
                    new GetImageAsync(imageView1).execute(urlarray[index]);
                }
                else {
                    index=index +1;
                    new GetImageAsync(imageView1).execute(urlarray[index]);
                }
            }
        });
        prevImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index==0){
                    index=urlarray.length-1;
                    new GetImageAsync(imageView1).execute(urlarray[index]);
                }
                else {
                    index=index-1;
                    new GetImageAsync(imageView1).execute(urlarray[index]);
                }
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        RequestParams requestParams=new RequestParams();
        requestParams.addParameter("keyword",item);
        new GetUrlAsync(requestParams).execute("http://dev.theappsdr.com/apis/photos/index.php");

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }

    private class GetUrlAsync extends AsyncTask<String,Void,String>{
        RequestParams params;

        public GetUrlAsync(RequestParams parameters){
            params=parameters;
        }


        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder =new StringBuilder();
            HttpURLConnection connection=null;
            BufferedReader reader=null;
            String result=null;
            try {
                URL url=new URL(params.getEncodedURL(strings[0]));
                connection = (HttpURLConnection)url.openConnection();
                connection.connect();
                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    result= IOUtils.toString(connection.getInputStream(),"UTF-8");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection!=null)
                    connection.disconnect();
            }
            if (reader!=null){

                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return result;

        }
        @Override
        protected void onPostExecute(String s) {
            if(s!="") {

                urlarray = s.split("\n");
                new GetImageAsync(imageView1).execute(urlarray[0]);
            }
            else
                Toast.makeText(MainActivity.this,"No images found",Toast.LENGTH_SHORT).show();
                imageView1.setImageResource(R.mipmap.ic_launcher);

        }

    }
    private class GetImageAsync extends AsyncTask<String, Void, Void> {
        ImageView imageView;
        Bitmap bitmap = null;

        public GetImageAsync(ImageView iv) {
            imageView = iv;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... params) {
            HttpURLConnection connection = null;
            bitmap = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    bitmap = BitmapFactory.decodeStream(connection.getInputStream());

                }
                else
                    bitmap=null;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //Close open connection
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (bitmap != null && imageView != null) {
                imageView.setImageBitmap(bitmap);
                progressBar.setVisibility(View.INVISIBLE);
            }
            else {
                imageView1.setImageResource(R.mipmap.ic_launcher);
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this,"Can't Load URL",Toast.LENGTH_SHORT).show();
            }

        }
    }


    private class GetDataAsync extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder =new StringBuilder();
            HttpURLConnection connection=null;
            BufferedReader reader=null;
            String result=null;
            try {
                URL url=new URL(strings[0]);
                connection = (HttpURLConnection)url.openConnection();
                connection.connect();
                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    result= IOUtils.toString(connection.getInputStream(),"UTF-8");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection!=null)
                    connection.disconnect();
            }
            if (reader!=null){

                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            items=s;
            itemsarray=items.split(";");
            final List<String> categories= new ArrayList<String>();
            for (int i=0;i<itemsarray.length;i++)
                categories.add(itemsarray[i]);
            ArrayAdapter<String> dataAdapter= new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item,categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sl.setAdapter(dataAdapter);

        }
    }
}
