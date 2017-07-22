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
import io.skygear.skygear.RecordSaveResponseHandler;

public class SaveLocation extends AppCompatActivity {
    private Location location;
    private EditText inputName;
    private TextView saveButton;
    private Container skygear;
    private Database publicDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_location);
        setTitle("Save Location");

        skygear = Container.defaultContainer(this);
        publicDB = skygear.getPublicDatabase();

        Intent i= getIntent();
        location = new Location("gps");
        location.setLatitude(i.getDoubleExtra("latitude",0.0));
        location.setLongitude(i.getDoubleExtra("longitude",0.0));

        inputName = (EditText) findViewById(R.id.inputboxname);

        saveButton = (TextView) findViewById(R.id.savebutton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecord();
            }
        });
    }

    private void saveRecord() {
        Record aRecord = new Record("LocationList");
        aRecord.set("Location","Location item");
        aRecord.set("LocationName",inputName.getText().toString());
        aRecord.set("LocationInfo",location);
        publicDB.save(aRecord, new RecordSaveResponseHandler() {
            @Override
            public void onSaveSuccess(Record[] records) {
                backPage();
            }
            @Override
            public void onPartiallySaveSuccess(Map<String, Record> successRecords, Map<String, Error> errors) {}
            @Override
            public void onSaveFail(Error error) {}
        });
    }

    private void backPage(){
        Intent i = new Intent();
        i.setClass(this,Map.class);
        startActivity(i);
    }
}
