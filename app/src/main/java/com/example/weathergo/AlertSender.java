package com.example.weathergo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AlertSender extends AppCompatActivity {

    TextView viewCity;
    EditText friendsMail;
    Button sendBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_alert_sender );

        friendsMail = (EditText) findViewById( R.id.friendsMail );
        viewCity = (TextView) findViewById( R.id.viewCity );
        final String chosenCity = getIntent().getExtras().getString( "city" );
        viewCity.setText( chosenCity );


        sendBtn = findViewById( R.id.sendBtn );
        sendBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = friendsMail.getText().toString();
                if(address.contains( "@" ) ) {
                    sendMail();
                }
                else {
                    wrongMail();
                }
            }
        } );
    }

    private void sendMail(){
        String address = friendsMail.getText().toString().replace(" ", "");
        String topic = "Weather Tips";
        String cityCapital =  viewCity.getText().toString().replace(" ", "");
        String city = cityCapital.substring( 0,1 ) + cityCapital.substring(1).toLowerCase();
        String message = "Are you in " + city + "? Then you better stay home! \n" +
                "For more info download WeatherGO app to be aware and prepare! \n " +
                "\n \n Best Regards, \n Your WeatherGo Team";

        MailBuilder mailBuilder = new MailBuilder( this, address, topic, message );
        mailBuilder.execute();

        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);
    }

    private void wrongMail(){
        Toast.makeText( this, "Wrong mail address", Toast.LENGTH_SHORT ).show();
    }

    public void back(View view) {
        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);
    }
}
