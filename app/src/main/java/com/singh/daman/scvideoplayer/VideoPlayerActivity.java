package com.singh.daman.scvideoplayer;

import android.content.Context;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class VideoPlayerActivity extends AppCompatActivity {
    private VideoView player;
    int size;
    String storagePath = Environment.getExternalStorageDirectory().toString() + "/FILE.mp4";
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_video_player);
        ProgressBack PB = new ProgressBack();
        PB.execute("");

        file = new File(storagePath);

        player = (VideoView) findViewById(R.id.VideoView);
        player.setVideoURI(Uri.fromFile(file));
        player.setMediaController(new MediaController(this));
        if (file.length() > 0) {
            player.start();
        }
        if (file.length() != size) {
            player.getCurrentPosition();
        }
    }



    private class ProgressBack extends AsyncTask<String, Integer, String> {
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
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    private void downloadFile(String videoURL, String name) {
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
                // just going to ignore this one
            }
        }
    }
}