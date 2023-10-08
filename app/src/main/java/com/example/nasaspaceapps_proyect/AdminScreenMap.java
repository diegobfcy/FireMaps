package com.example.nasaspaceapps_proyect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

public class  AdminScreenMap extends AppCompatActivity implements OnMapReadyCallback {
    private ClusterManager<FireClusterItem> mClusterManager;
    private GoogleMap mMap;
    private FloatingActionButton fabDanger;
    private LatLng lastSelectedPoint; // Usamos esto para recordar el último punto seleccionado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_screen_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);
        new FetchDataTask().execute();


        fabDanger = findViewById(R.id.fabDanger);
        fabDanger.setVisibility(View.INVISIBLE); // Inicialmente lo hacemos invisible

        fabDanger.setOnClickListener(v -> {
            Intent intent = new Intent(AdminScreenMap.this, AdminScreenMessageSend.class);
            intent.putExtra("latitude", lastSelectedPoint.latitude);
            intent.putExtra("longitude", lastSelectedPoint.longitude);
            startActivity(intent);

        });
    }
    private class FetchDataTask extends AsyncTask<Void, Void, List<FireRecord>> {

        @Override
        protected List<FireRecord> doInBackground(Void... voids) {
            FirmsDataFetcher fetcher = new FirmsDataFetcher();
            String result = fetcher.fetchData("2828ce32622161f15b85f7c26c09483a", "VIIRS_SNPP_NRT", "-85,-57,-32,14", "1");
            return parseData(result.split("\n")); // Suponiendo que los registros están separados por saltos de línea
        }

        @Override
        protected void onPostExecute(List<FireRecord> records) {
            BitmapDescriptor redDot = getBitmapDescriptor(R.drawable.red_dot);

            for (FireRecord record : records) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(record.latitude, record.longitude))
                        .icon(redDot));
            }
        }
    }
    private BitmapDescriptor getBitmapDescriptor(int id) {
        Drawable vectorDrawable = ContextCompat.getDrawable(this, id);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    private List<FireRecord> parseData(String[] records) {
        List<FireRecord> fireRecords = new ArrayList<>();

        // Saltamos la primera línea porque es el header
        for (int i = 1; i < records.length; i++) {
            String[] parts = records[i].split(",");
            double latitude = Double.parseDouble(parts[0]);
            double longitude = Double.parseDouble(parts[1]);
            fireRecords.add(new FireRecord(latitude, longitude));
        }

        return fireRecords;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mMap.setOnMarkerClickListener(marker -> {
            lastSelectedPoint = marker.getPosition();
            fabDanger.setVisibility(View.VISIBLE);
            return false; // Esto permite que el evento se propague y que el marcador muestre su ventana de información (si tiene alguna).
        });

        }
}