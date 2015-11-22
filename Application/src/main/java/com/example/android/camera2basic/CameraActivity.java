/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.camera2basic;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class CameraActivity extends Activity implements SensorEventListener{
    private static final String TAG ="CameraActivity";
    private SensorManager mSensorManager;
    private Camera2VideoFragment videofragment;
    private Camera2BasicFragment camerafragment;
    private Sensor mSensor;
    private double threshold = 4.0;
    private boolean Take = false;
    private boolean onListenAcc = true;
    private String accdata ="";
    private WriteLog mWriteLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        checkPlayServices(this);
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        Intent intent = new Intent(this.getApplicationContext(),LocationService.class);
        startService(intent);
        camerafragment = Camera2BasicFragment.newInstance();
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, camerafragment)
                    .commit();
        }

    }

    public Camera2BasicFragment getCamerafragment() {
        return camerafragment;
    }


    public void setTake(boolean take) {
        Take = take;
    }

    public boolean getTake()
    {
        return Take;
    }

    private boolean checkPlayServices(Activity activity) {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity, 9000).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        accdata = "x: "+Math.abs(x)+", y: "+Math.abs(y)+", z: "+Math.abs(z)+"\n";
        if(mWriteLog==null)
        {
            try {
                mWriteLog = new WriteLog();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(mWriteLog!=null)
        {
            try {
                mWriteLog.writefile(Math.abs(x)+","+Math.abs(y)+","+Math.abs(z)+"\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(camerafragment!=null)
        {
            camerafragment.setTextViewText(accdata);
        }
        if(videofragment!=null)
        {
            //Log.d(TAG,"get text");
            videofragment.setTextViewText(accdata);
        }
        if(onListenAcc) {
            if (Math.abs(x) > threshold || Math.abs(y) > threshold || Math.abs(z) > threshold) {
                videofragment = Camera2VideoFragment.newInstance();
                onListenAcc = false;
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, videofragment)
                        .commit();
            }
        }

    }



    public void setOnListenAcc(boolean onListenAcc) {
        this.onListenAcc = onListenAcc;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
