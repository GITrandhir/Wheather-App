package com.example.randhir.weatherapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {


    ArrayList<String> newSearches ;
    ListView history ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        history = (ListView) findViewById(R.id.listview);

        Intent intent = getIntent();

        newSearches = intent.getStringArrayListExtra("newsearches");

        Log.i("DATA", newSearches.toString());


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,newSearches);

        history.setAdapter(arrayAdapter);









    }
}
