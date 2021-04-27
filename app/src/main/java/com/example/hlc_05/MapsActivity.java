package com.example.hlc_05;

import androidx.appcompat.app.AppCompatActivity;


import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback
{
    private GoogleMap mMap;
    private MarkerOptions inicio;
    private PolygonOptions polygonOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        LatLng spain = new LatLng(36.719, -4.453);
        mMap.addMarker(new MarkerOptions().position(spain).title("Marcador en España"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(spain));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng point)
            {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(point));
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener()
        {
            @Override
            public void onMapLongClick(LatLng latLng)
            {
                mMap.clear();

                PolylineOptions polylineOptions = new PolylineOptions().color(Color.RED);
                polygonOptions = new PolygonOptions().strokeColor(Color.RED);


                polygonOptions.add(latLng);
                polylineOptions.add(latLng);

                inicio = new MarkerOptions().position(latLng);

                Toast toast = Toast.makeText(getApplicationContext(),"Coordenadas( " + inicio.getPosition().latitude + "  ,  " + inicio.getPosition().longitude + " ) agregadas correctamente",Toast.LENGTH_SHORT);
                toast.show();

                mMap.addMarker(inicio);
                mMap.addPolygon(polygonOptions);
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
                {
                    @Override
                    public void onMapClick(LatLng point)
                    {
                        polylineOptions.add(point);
                        polygonOptions.add(point);
                        Polyline polyline = mMap.addPolyline(polylineOptions);
                    }
                });
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        if(menuItem.getItemId() == R.id.mCuadrado)
        {
            mMap.clear();
            Polygon polygon = mMap.addPolygon(polygonOptions);

            List<LatLng> latLngs = new ArrayList<>();
            latLngs = polygon.getPoints();

            Toast toast = Toast.makeText(getApplicationContext(),"Area de la zona marcada: " + SphericalUtil.computeArea(latLngs) + "m2",Toast.LENGTH_SHORT);
            toast.show();
        }
        if(menuItem.getItemId() == R.id.perimetro)
        {
            mMap.clear();
            Polygon polygon = mMap.addPolygon(polygonOptions);

            List<LatLng> latLngs = new ArrayList<>();
            latLngs = polygon.getPoints();

            Toast toast = Toast.makeText(getApplicationContext(),"Perímetro de la zona marcada: " + SphericalUtil.computeLength(latLngs) + "m",Toast.LENGTH_SHORT);
            toast.show();
        }
        if(menuItem.getItemId() == R.id.borrar)
        {
            mMap.clear();
        }
        return true;
    }

}