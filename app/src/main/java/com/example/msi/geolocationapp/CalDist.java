package com.example.msi.geolocationapp;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import io.skygear.skygear.Container;
import io.skygear.skygear.Database;

public class CalDist extends AppCompatActivity {
    private Location location;
    private String name;
    private TextView backButton;
    private Container skygear;
    private Database publicDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal_dist);

        skygear = Container.defaultContainer(this);
        publicDB = skygear.getPublicDatabase();

        Intent i= getIntent();
        location = new Location("gps");
        location.setLatitude(i.getDoubleExtra("latitude",0.0));
        location.setLongitude(i.getDoubleExtra("longitude",0.0));
        name = new String(i.getStringExtra("title"));

        setTitle("Calculate Distance from "+ name);

        backButton = (TextView) findViewById(R.id.backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });
    }

    private void backPage(){
        Intent i = new Intent();
        i.setClass(this,Map.class);
        startActivity(i);
    }
}
