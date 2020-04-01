package com.example.onlineshop;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SensorActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer, mGyroscope, mTemperature, mProximity;
    private SensorEventListener accelerometerEventListener, gyroscopeEventListener, proximityEventListener, temperatureEventListener;
    private TextView accelerometerSensor, proximitySensor, temperatureSensor, gyroscopeSensor, gpsSensor;
    private Spinner sensorsSpinner;
    private ArrayList<String> sensorNameList;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        // back arrow
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // initialise sensor manager and sensors
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);


        // initialise textviews of sensors
        accelerometerSensor = findViewById(R.id.valueAccelerometer);
        gyroscopeSensor = findViewById(R.id.valueGyroscope);
        temperatureSensor = findViewById(R.id.valueTemperature);
        proximitySensor = findViewById(R.id.valueProximity);
        gpsSensor = findViewById(R.id.valueGPS);

        // get available sensors and fill the spinner
        sensorsSpinner = findViewById(R.id.sensorSpinner);
        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        sensorNameList = new ArrayList<>();

        for (Sensor s : sensorList) {
            sensorNameList.add(s.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sensorNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sensorsSpinner.setAdapter(adapter);

        // accelerometer
        accelerometerEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                accelerometerSensor.setText("X: " + event.values[0] + " Y: " + event.values[1] + " Z: " + event.values[2]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        // gyroscope
        gyroscopeEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                gyroscopeSensor.setText(String.valueOf(event.values[2]));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        // temperature
        temperatureEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                temperatureSensor.setText(event.values[0] + " °C");
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        // proximity
        proximityEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                proximitySensor.setText(event.values[0] + " cm");
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        // GPS
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                gpsSensor.setText("Lat: " + location.getLatitude() + "\nLong: " + location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
            }, 10);
            return;
        }

        setGPS();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 10:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    setGPS();
                }
                return;
        }

    }

    private void setGPS() {
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(accelerometerEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(gyroscopeEventListener, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(proximityEventListener, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(temperatureEventListener, mTemperature, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(accelerometerEventListener);
        mSensorManager.unregisterListener(gyroscopeEventListener);
        mSensorManager.unregisterListener(proximityEventListener);
        mSensorManager.unregisterListener(temperatureEventListener);
    }
}
