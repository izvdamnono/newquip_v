package com.izv.dam.newquip.vistas;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.izv.dam.newquip.R;

public class MapsLocation extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    Button btnGetLastLocation;
    TextView textLastLocation;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_location);
        textLastLocation = (TextView) findViewById(R.id.lastlocation);
        btnGetLastLocation = (Button) findViewById(R.id.getlastlocation);
        btnGetLastLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mGoogleApiClient != null)
                    if (mGoogleApiClient.isConnected()) getMyLocation();
                    else
                        Toast.makeText(MapsLocation.this, "!mGoogleApiClient.isConnected()", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(MapsLocation.this, "mGoogleApiClient == null", Toast.LENGTH_LONG).show();
            }
        });
        if (mGoogleApiClient == null)
            mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }

    private void getMyLocation() {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                textLastLocation.setText(String.valueOf(mLastLocation.getLatitude()) + "\n" + String.valueOf(mLastLocation.getLongitude()));
                Toast.makeText(this, String.valueOf(mLastLocation.getLatitude()) + "\n" + String.valueOf(mLastLocation.getLongitude()), Toast.LENGTH_LONG).show();
            } else Toast.makeText(this, "Señal no encontrada", Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Toast.makeText(this, "SecurityException:\n" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getMyLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "onConnectionSuspended: " + String.valueOf(i), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "onConnectionFailed: \n" + connectionResult.toString(), Toast.LENGTH_LONG).show();
    }
}