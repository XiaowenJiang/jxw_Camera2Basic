package com.example.android.camera2basic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class DisplayActivity extends Activity {
    private static final String TAG = "DisplayActivity";
    private String locationAddress;
    private TextView addressText;
    Intent intent;
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            addressText.setText(locationAddress);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        intent = getIntent();
        String path = intent.getStringExtra("path");
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ImageView imageView = (ImageView)findViewById(R.id.phototaken);
        addressText = (TextView)findViewById(R.id.address);
        Button takemore = (Button)findViewById(R.id.takemorephoto);
        takemore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CameraActivity.class);
                startActivity(intent);
                finish();
            }
        });
        imageView.setImageBitmap(bitmap);
        try {
            ExifInterface exif = new ExifInterface(path);
            geoDegree geoDegree = new geoDegree(exif);
            double latitude = geoDegree.getLatitude();
            double longitude = geoDegree.getLongitude();
            ToAddress.getAddressFromLocation(latitude,longitude,getApplicationContext(),new GeocoderHandler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
