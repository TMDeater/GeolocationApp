package com.example.msi.geolocationapp;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.*;
import java.util.Map;

import io.skygear.skygear.Container;
import io.skygear.skygear.Database;
import io.skygear.skygear.Error;
import io.skygear.skygear.Record;
import io.skygear.skygear.RecordDeleteResponseHandler;

public class DelLocation extends AppCompatActivity {
    private Location location;
    private String name;
    private TextView delMsg;
    private TextView backButton;
    private Container skygear;
    private Database publicDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_location);

        skygear = Container.defaultContainer(this);
        publicDB = skygear.getPublicDatabase();

        Intent i= getIntent();
        location = new Location("gps");
        location.setLatitude(i.getDoubleExtra("latitude",0.0));
        location.setLongitude(i.getDoubleExtra("longitude",0.0));
        name = new String(i.getStringExtra("title"));

        delMsg = (TextView) findViewById(R.id.deleteMsg);
        backButton = (TextView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });

        Record aRecord = new Record("LocationList");
        aRecord.set("Location","Location item");
        aRecord.set("LocationName",name);
        aRecord.set("LocationInfo",location);

        publicDB.delete(aRecord, new RecordDeleteResponseHandler() {
            @Override
            public void onDeleteSuccess(String[] ids) {
                delMsg.setText("Location deleted successfully");
            }
            @Override
            public void onDeletePartialSuccess(String[] ids, Map<String, Error> errors) {}
            @Override
            public void onDeleteFail(Error error) {
                delMsg.setText("Fail to delete location");
            }
        });
    }

    private void backPage(){
        Intent i = new Intent();
        i.setClass(this,Map.class);
        startActivity(i);
    }
}
