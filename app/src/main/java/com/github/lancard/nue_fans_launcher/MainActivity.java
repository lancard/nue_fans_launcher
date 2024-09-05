package com.github.lancard.nue_fans_launcher;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebMessage;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


/** @noinspection FieldCanBeLocal*/
public class MainActivity extends AppCompatActivity implements LocationListener, SensorEventListener {
    private WebView webView;
    private WebAppInterface webAppInterface;
    private SensorManager sensorManager;
    private LocationManager locationManager;
    private float lastBearing = 0.0f;
    private final static String startUrl = "file:///android_asset/www/index.html";

    // sensor start -----------------------------------------------
    public void initializeSensor() {
        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getSensorList(Sensor.TYPE_PRESSURE).get(0), SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        sendMessageToWebView("PRESSURE|" + event.values[0]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    // sensor end -----------------------------------------------


    // gps start -----------------------------------------------
    private void checkLocationPermission() {
        boolean coarseLocationGranted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        boolean fineLocationGranted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!coarseLocationGranted || !fineLocationGranted) {
            ActivityResultLauncher<String[]> locationPermissionRequest =
                    registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                                Boolean fineLocationGrantedResult = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);

                                if (fineLocationGrantedResult == null || !fineLocationGrantedResult) {
                                    this.finishAffinity();
                                    return;
                                }

                                initializeWebView();
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                            }
                    );

            locationPermissionRequest.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        } else {
            initializeWebView();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    public void initializeLocation() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        checkLocationPermission();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            float bearing = location.getBearing();
            if (location.getSpeed() < 0.001f) {
                bearing = lastBearing;
            } else {
                lastBearing = bearing;
            }
            if (bearing > 360.0f) {
                bearing -= 360.0f;
            }

            sendMessageToWebView("GPS|" +
                    location.getLatitude() + "|" +
                    location.getLongitude() + "|" +
                    location.getAltitude() + "|" +
                    location.getAccuracy() + "|" +
                    bearing + "|" +
                    location.getSpeed() + "|" +
                    location.getTime()
            );
        } catch (Exception e) {
            sendMessageToWebView(e.toString());
        }
    }
    // gps end -----------------------------------------------


    // web view start -----------------------------------------
    @SuppressLint("SetJavaScriptEnabled")
    public void initializeWebView() {
        webView = findViewById(R.id.webview);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        webView.setWebViewClient(new WebViewClient(this));
        webView.setWebChromeClient(new WebChromeClient());

        // link Javascript interface
        webAppInterface = new WebAppInterface(this);
        webView.addJavascriptInterface(webAppInterface, "NueFANS");
        webView.loadUrl(startUrl);
    }

    private void sendMessageToWebView(String message) {
        try {
            webView.postWebMessage(new WebMessage(message), Uri.parse("*"));
        }
        catch(Exception e) {
            Log.e("main", e.toString());
        }
    }
    // web view end -----------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeSensor();
        initializeLocation();
    }
}