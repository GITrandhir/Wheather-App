package com.example.randhir.weatherapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editText ;

    TextView resultTextView ;

    Button history;

    SharedPreferences sharedPreferences;

    ArrayList<String> searchHistory = new ArrayList<String>();

    ArrayList<String> newSearches = new ArrayList<>();

    public void Activity2(){
        Intent intent = new Intent(this,Main2Activity.class);
        intent.putExtra("newsearches",newSearches);
        startActivity(intent);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);

        resultTextView = (TextView) findViewById(R.id.resultTextView);

        history = (Button) findViewById(R.id.button1);

        resultTextView.setVisibility(View.VISIBLE);



        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity2();
            }
        });

        sharedPreferences = this.getSharedPreferences(" com.example.randhir.weatherapp", Context.MODE_PRIVATE);







    }


    public void getWeather(View view){

        try {
            DownloadTask task = new DownloadTask();

            String encodedCityName = URLEncoder.encode(editText.getText().toString(), "UTF-8");

            task.execute("https://openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&appid=439d4b804bc8187953eb36d2a8c26a02");

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Could Not Find Weather :(",Toast.LENGTH_SHORT).show();
        }
        if(editText.getText().toString().length() != 0){
            searchHistory.add(editText.getText().toString());
        }

        try {

            sharedPreferences.edit().putString("SEARCH HISTORY", ObjectSerializer.serialize(searchHistory)).apply();

            Log.i("SEARCHED FOR",ObjectSerializer.serialize(searchHistory));

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            newSearches = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("NEW SEARCHES",ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (Exception e) {
            e.printStackTrace();
        }
         Log.i("HISTORY",newSearches.toString());
    }

    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();

                Toast.makeText(getApplicationContext(),"Could Not Find Weather :(",Toast.LENGTH_SHORT).show();

                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);

                String weatherInfo = jsonObject.getString("weather");



                JSONArray arr = new JSONArray(weatherInfo);

                String message = "";

                for (int i=0; i < arr.length(); i++) {
                    JSONObject jsonPart = arr.getJSONObject(i);

                    String main = jsonPart.getString("main") ;
                    String description = jsonPart.getString("description");

                    if(!main.equals("") && !description.equals("")){
                        message += main + " : " + description + "\r\n";
                    }
                }

                if(!message.equals("")){
                    resultTextView.setText(message);
                }else {
                    Toast.makeText(getApplicationContext(),"Could Not Find Weather :(",Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {

                Toast.makeText(getApplicationContext(),"Could Not Find Weather :(",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
    }






}
