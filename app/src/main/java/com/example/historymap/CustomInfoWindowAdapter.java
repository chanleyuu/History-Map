package com.example.historymap;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter extends AppCompatActivity implements GoogleMap.InfoWindowAdapter  {

    private Activity context;
    ImageView img;
    Bitmap bit;
    final private int REQUEST_INTERNET = 123;
    String url;

    private InputStream OpenHttpConnection(String urlString) throws IOException{
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (! (conn instanceof HttpURLConnection) )
            throw new IOException("Not an HTTP connection");
        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK){
                in = httpConn.getInputStream();
            }
        }
        catch (Exception ex){
            Log.d("Networking", ex.getLocalizedMessage());
            throw new IOException("Error connecting");
        }
        return in;
    }

    public CustomInfoWindowAdapter(Activity context, String image){
        this.context = context;
        url = image;
    }


    @Override
    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.customwindow, null);

        if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.context, new String[]{
                    Manifest.permission.INTERNET}, REQUEST_INTERNET
            );
        }
        else {
            new DownloadImageTask().execute(url);
        }

        img = (ImageView) view.findViewById(R.id.tv_title);
        TextView tvSubTitle = (TextView) view.findViewById(R.id.tv_subtitle);

        //tvTitle.setText(marker.getTitle());

        img.setImageBitmap(bit);

        tvSubTitle.setText(marker.getSnippet());

        return view;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case REQUEST_INTERNET:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    new DownloadImageTask().execute(url);
                }
                else {
                    Toast.makeText(this.context, "Permission Denied", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... urls){
            return DownloadImage(urls[0]);
        }

        protected void onPostExecute(Bitmap result) {
           // ImageView img = (ImageView) context.findViewById(R.id.tv_title);
           // img.setImageBitmap(result);
            bit = result;
        }
    }

    private Bitmap DownloadImage(String URL){
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        }
        catch (IOException e1){
            Log.d("NetworkingActivity", e1.getLocalizedMessage());
        }
        return bitmap;
    }

    public void SetImage(String img) throws ExecutionException, InterruptedException {
        url = img;
        bit = new DownloadImageTask().execute(url).get();
    }
}


