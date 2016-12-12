package com.izv.dam.newquip.vistas.ubicacion;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.izv.dam.newquip.R;
import com.izv.dam.newquip.basedatos.AyudanteORM;
import com.izv.dam.newquip.pojo.MapaORM;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.field.types.DoubleObjectType;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mostrarMapa();

        // Add a marker in Sydney and move the camera
//        LatLng madrid = new LatLng(40.35911159201925, -3.5864961836914455);
//        mMap.addMarker(new MarkerOptions().position(madrid).title("Marker in Madrid"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(madrid));
    }

    public void mostrarMapa() {
        AyudanteORM ayudanteORM = new AyudanteORM(this);
        RuntimeExceptionDao<MapaORM, Integer> simpleDao = ayudanteORM.getDataDao();
        List<MapaORM> mapaORMs = null;
        try {
            QueryBuilder<MapaORM, Integer> queryBuilder = simpleDao.queryBuilder();
            mapaORMs = simpleDao.query(queryBuilder.prepare());

            for (MapaORM orm : mapaORMs) {
                System.out.println(orm.toString());
                marcar(Double.valueOf(orm.getLatitud()), Double.valueOf(orm.getLongitud()), String.valueOf(orm.getHistoria()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void marcar(double latitud, double longitud, String titulo) {
        LatLng madrid = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions().position(madrid).title(titulo));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(madrid));
    }
}
