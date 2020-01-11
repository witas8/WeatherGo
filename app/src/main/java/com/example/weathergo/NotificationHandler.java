package com.example.weathergo;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;


public class NotificationHandler extends Application {
    public static final String Channel_1_ID = "channel1";
    public static final int NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();

    }

    public void createNotificationChannel(){

        //channels object
        NotificationChannel channel1 = new NotificationChannel( Channel_1_ID, "Channel 1",
                NotificationManager.IMPORTANCE_HIGH );

        channel1.setDescription( "This is Channel 1" );

        NotificationManager manager = getSystemService( NotificationManager.class );
        manager.createNotificationChannel( channel1 );

        //now add to manifest in application area - android:name=".NotificationHandler"
    }
}
