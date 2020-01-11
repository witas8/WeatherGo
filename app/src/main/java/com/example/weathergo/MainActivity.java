package com.example.weathergo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView cityName;
    Button searchBtn;
    Button alertBtn;
    TextView result;
    NotificationManagerCompat notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        notificationManager = NotificationManagerCompat.from(this);

        searchBtn = findViewById( R.id.searchBTN );
        alertBtn = findViewById( R.id.alertBtn );
        result = findViewById( R.id.result );
        cityName = findViewById( R.id.cityName );
        cityName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        alertBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String providedCity = cityName.getText().toString();
                if (providedCity.isEmpty()){
                    emptyCity();
                }
                else {
                    Intent intent = new Intent( MainActivity.this, AlertSender.class );

                    intent.putExtra( "city", providedCity );
                    startActivity( intent );
                }
            }
        } );

        searchBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String providedCity = cityName.getText().toString();
                if (providedCity.isEmpty()){
                    emptyCity();
                }
                else {
                    search();
                }
            }
        } );
    }

    public void search(){

        String content;
        Weather weather = new Weather();
        String providedCity = cityName.getText().toString().replace(" ", "");

        try {
            content = weather.execute( "https://api.openweathermap.org/data/2.5/weather?q=" + providedCity
                    + "&APPID=b1eac1fdb94316c7190687274b93d937" ).get();
            Log.i("content", content);

            //JSON
            JSONObject jsonObject = new JSONObject( content );
            String weatherData = jsonObject.getString( "weather" );
            //this main is not part of weather array, it is separate variable like weather
            String mainTemp = jsonObject.getString( "main" );

            Log.i("WeatherData", weatherData);
            //weatherData is an array
            JSONArray jarray = new JSONArray( weatherData );

            //initialization
            String main = "";
            String description = "";
            String temperature = "";

            for(int i=0; i<jarray.length(); i++){
                JSONObject weatherPart = jarray.getJSONObject( i );
                main = (weatherPart.getString( "main" )).toLowerCase();
                description = weatherPart.getString( "description" );
            }

            JSONObject mainPart = new JSONObject( mainTemp );
            temperature = mainPart.getString( "temp" );
            double celsius = Double.parseDouble(temperature) - 273.15;
            int temp = (int) Math.round( celsius );
            String tempC = String.valueOf( temp );

            //Log.i("main", main);

            //reaction
            ImageView weatherImage = (ImageView) findViewById( R.id.weather_image );
            switch(main){
                case "rain":
                    weatherImage.setImageResource( R.drawable.raining);
                    sendNotification();
                    break;
                case "wind":
                    weatherImage.setImageResource( R.drawable.windy);
                    sendNotification();
                    break;
                case "clear":
                    weatherImage.setImageResource( R.drawable.sunny);
                    break;
                case "snow":
                    weatherImage.setImageResource( R.drawable.snowy);
                    if(temp < 0) { this.sendNotification();}
                    break;
                case "clouds":
                    weatherImage.setImageResource( R.drawable.cloudly);
                    if(temp < 5) { this.sendNotification();}
                    break;
                case "mist":
                    weatherImage.setImageResource( R.drawable.misty);
                    if(temp < 4) { this.sendNotification(); }
                    break;
                case "drizzle":
                    sendNotification();
                    weatherImage.setImageResource( R.drawable.drizzle);
                    if(temp < 3) { sendNotification(); }
                    break;
                case "fog":
                    weatherImage.setImageResource( R.drawable.foggy);
                    if(temp < 2) { this.sendNotification(); }
                    break;
                case "haze":
                    weatherImage.setImageResource( R.drawable.foggy2);
                    if(temp < 1) { this.sendNotification(); }
                    break;
            }

            String resultText = "Temp: " + tempC + " *C" + "\nMain info : " + main +
                    "\nDescription: " + description;
            result.setText( resultText );


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendNotification(){

        String title = "WeatherGo";
        String tip = "Better stay home!";
        String transition = "Visit website";

        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        PendingIntent mainPIntent = PendingIntent.getActivity( this, 0, mainIntent, PendingIntent.FLAG_ONE_SHOT );

        Intent switchIntent = new Intent(this, WebsiteSwitcher.class);
        switchIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        PendingIntent sendPIntent= PendingIntent.getActivity( this, 0, switchIntent, PendingIntent.FLAG_ONE_SHOT );

        Notification notification = new NotificationCompat.Builder( this, NotificationHandler.Channel_1_ID )
                .setSmallIcon( R.drawable.cloud_pic )
                .setContentTitle( title )
                .setContentText( tip )
                .setPriority( NotificationCompat.PRIORITY_HIGH )
                .setCategory( NotificationCompat.CATEGORY_MESSAGE )
                .setColor( Color.BLUE )
                .setContentIntent( mainPIntent )
                .setAutoCancel( true )
                .setOnlyAlertOnce( true )
                .addAction( R.mipmap.ic_launcher, transition, sendPIntent )
                .build();

        notificationManager.notify( NotificationHandler.NOTIFICATION_ID, notification);
    }

    private void emptyCity(){
        Toast.makeText( this, "No city given", Toast.LENGTH_SHORT ).show();
    }

}
