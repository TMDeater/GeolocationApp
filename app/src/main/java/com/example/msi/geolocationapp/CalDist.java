package com.example.msi.geolocationapp;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import io.skygear.skygear.Container;
import io.skygear.skygear.Database;
import io.skygear.skygear.Error;
import io.skygear.skygear.Query;
import io.skygear.skygear.Record;
import io.skygear.skygear.RecordQueryResponseHandler;

public class CalDist extends AppCompatActivity {
    private Location location;
    private String name;
    private TextView backButton;
    private Container skygear;
    private Database publicDB;
    private ArrayList<String> locNameAndDistList;
    private ArrayAdapter<String> arrayAdapter;
    private ListView lv;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal_dist);
        context = this;

        skygear = Container.defaultContainer(this);
        publicDB = skygear.getPublicDatabase();
        lv = (ListView) findViewById(R.id.loclist) ;

        Intent i= getIntent();
        location = new Location("gps");
        location.setLatitude(i.getDoubleExtra("latitude",0.0));
        location.setLongitude(i.getDoubleExtra("longitude",0.0));
        name = new String(i.getStringExtra("title"));

        setTitle("Calculate Distance from "+ name);

        locNameAndDistList = new ArrayList<>();

        backButton = (TextView) findViewById(R.id.backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });

        calculate();
    }

    private void backPage(){
        Intent i = new Intent();
        i.setClass(this,MapPage.class);
        startActivity(i);
    }

    private void calculate(){
        Query locQuery = new Query("LocationList")
                .equalTo("Location", "Location item")
                .transientIncludeDistance("LocationInfo", "distanceFromLocation", location);

        publicDB.query(locQuery, new RecordQueryResponseHandler() {
            @Override
            public void onQuerySuccess(Record[] records) {
                for (int i = 0; i < records.length; i++) {
                    Double distance = (Double) records[i].getTransient().get("distanceFromLocation");
                    locNameAndDistList.add(records[i].get("LocationName")+": "+distance.toString()+" metre");
                }
                if (locNameAndDistList.size()<=0){
                    locNameAndDistList.add("No Distance Calculated");}

                arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, locNameAndDistList);

                lv.setAdapter(arrayAdapter);
            }
            @Override
            public void onQueryError(Error error) {}
        });
    }
}
