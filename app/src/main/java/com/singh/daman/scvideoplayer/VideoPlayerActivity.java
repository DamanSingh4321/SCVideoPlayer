package com.singh.daman.scvideoplayer;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class VideoPlayerActivity extends AppCompatActivity {
    ProgressDialog pDialog;
    VideoView videoview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the layout from video_main.xml
        setContentView(R.layout.activity_video_player);
        // Find your VideoView in your video_main.xml layout

        ProgressBack PB = new ProgressBack();
        PB.execute("");

        videoview = (VideoView) findViewById(R.id.VideoView);

        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(
                    VideoPlayerActivity.this);
            mediacontroller.setAnchorView(videoview);
            // Get the URL from String VideoURL
            String storagePath = Environment.getExternalStorageDirectory().toString();
            Uri video = Uri.parse(storagePath+"/FILE.mp4");
            videoview.setMediaController(mediacontroller);
            videoview.setVideoURI(video);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoview.requestFocus();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                videoview.start();
            }
        });

    }

    private class ProgressBack extends AsyncTask<String, String, String> {
        ProgressDialog PD;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... arg0) {
            downloadFile("https://socialcops.com/images/spec/home/header-img-background_video-1920-480.mp4", "Sample.mp4");
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            System.out.println(values);
        }
    }

    private void downloadFile(String videoURL, String name) {
        URL u = null;
        InputStream is = null;
        try {
            u = new URL(videoURL);
            is = u.openStream();
            HttpURLConnection huc = (HttpURLConnection) u.openConnection(); //to know the size of video
            int size = huc.getContentLength();

            System.out.println(size);

            if (huc != null) {
                String fileName = "FILE.mp4";
                String storagePath = Environment.getExternalStorageDirectory().toString();
                File f = new File(storagePath, fileName);
                FileOutputStream fos = new FileOutputStream(f);
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
                // just going to ignore this one
            }


        }
    }
}