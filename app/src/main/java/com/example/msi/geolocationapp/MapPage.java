package com.example.msi.geolocationapp;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import io.skygear.skygear.Container;
import io.skygear.skygear.Database;
import io.skygear.skygear.Error;
import io.skygear.skygear.Query;
import io.skygear.skygear.Record;
import io.skygear.skygear.RecordQueryResponseHandler;

public class MapPage extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Container skygear;
    private Database publicDB;
    private ArrayList<Location> locationList;
    private ArrayList<String> nameList;
    private TextView saveLoc;
    private TextView delLoc;
    private TextView calDist;
    private Marker current;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        skygear = Container.defaultContainer(this);
        publicDB = skygear.getPublicDatabase();

        locationList = new ArrayList<>();
        nameList = new ArrayList<>();

        Query findQuery = new Query("LocationList").equalTo("Location", "Location item");
        publicDB.query(findQuery, new RecordQueryResponseHandler() {
            @Override
            public void onQuerySuccess(Record[] records) {
                if (records.length<=0){}
                else{
                    loadLocation(records);
                }
            }
            @Override
            public void onQueryError(Error error) {}
        });

        saveLoc = (TextView) findViewById(R.id.savebutton);
        saveLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage(1);
            }
        });
        delLoc = (TextView) findViewById(R.id.deletebutton);
        delLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage(2);
            }
        });
        calDist = (TextView) findViewById(R.id.calculatebutton);
        calDist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage(3);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Location office = new Location("gps");
        office.setLatitude(22.336265);
        office.setLongitude(114.147932);

        current = mMap.addMarker(new MarkerOptions().position(LocationConvertLatLng(office)).title("current"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LocationConvertLatLng(office),15));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                current.setPosition(latLng);
            }
        });
    }

    private void loadLocation(Record[] records){
        for (int i=0;i<records.length;i++){
            locationList.add((Location) records[i].get("LocationInfo"));
            nameList.add((String) records[i].get("LocationName"));
        }
        loadMarker();
    }

    private void loadMarker(){
        for (int i=0;i<locationList.size();i++){
            mMap.addMarker(new MarkerOptions()
                    .position(LocationConvertLatLng(locationList.get(i)))
                    .title(nameList.get(i))
                    .icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                current.setPosition(marker.getPosition());
                current.setTitle(marker.getTitle());
                return false;
            }
        });
    }

    public LatLng LocationConvertLatLng(Location loc){
        LatLng ltlg = new LatLng(loc.getLatitude(),loc.getLongitude());
        return ltlg;
    }

    public void nextPage(int num){
        Intent i = new Intent();
        i.putExtra("latitude",current.getPosition().latitude);
        i.putExtra("longitude",current.getPosition().longitude);
        switch (num){
            case 1:i.setClass(this,SaveLocation.class);
                break;
            case 2:i.setClass(this,DelLocation.class);
                i.putExtra("title",current.getTitle());
                break;
            case 3:i.setClass(this,CalDist.class);
                i.putExtra("title",current.getTitle());
                break;
        }
        startActivity(i);
    }
}
