package com.example.weathergo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

public class WebsiteSwitcher extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_website_switcher );

        new Handler().postDelayed(new Runnable() {

            public void run() {

                String url = "https://openweathermap.org";
                Intent intent = new Intent( Intent.ACTION_VIEW );
                intent.setData( Uri.parse(url));

                startActivity(intent);
            }
        }, 1500);


    }
}
