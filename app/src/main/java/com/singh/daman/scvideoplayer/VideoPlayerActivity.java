package com.singh.daman.scvideoplayer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class VideoPlayerActivity extends AppCompatActivity implements OnPreparedListener {
    EMVideoView emVideoView;
    int size;
    String storagePath = Environment.getExternalStorageDirectory().toString() + "/FILE.mp4";
    File file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        if(isNetworkAvailable()) {
            ProgressBack progressBack = new ProgressBack();
            progressBack.execute("");

            file = new File(storagePath);

            emVideoView = (EMVideoView) findViewById(R.id.video_view);
            startVideo();
        }
        else{
            Toast.makeText(this, "No Internet Connection!!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void startVideo(){
        emVideoView.setOnPreparedListener(this);
        emVideoView.setVideoURI(Uri.fromFile(file));
        emVideoView.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion() {
                emVideoView.restart();
            }
        });
    }

    @Override
    public void onPrepared() {
        emVideoView.start();
    }

    private class ProgressBack extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... arg0) {
            downloadFile("https://socialcops.com/images/spec/home/header-img-background_video-1920-480.mp4");
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    private void downloadFile(String videoURL) {
        URL u = null;
        InputStream is = null;
        try {
            u = new URL(videoURL);
            is = u.openStream();
            HttpURLConnection huc = (HttpURLConnection) u.openConnection(); //to know the size of video
            size = huc.getContentLength();

            if (huc != null) {
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len1 = 0;
                if (is != null) {
                    while ((len1 = is.read(buffer)) > 0) {
                        fos.write(buffer, 0, len1);
                    }
                }
                if (fos != null) {
                    fos.close();
                }
            }
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ioe) {

            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}