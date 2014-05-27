package com.example.wakeappcallv1.app;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.wakeappcallv1.app.R;
import com.example.wakeappcallv1.app.library.DatabaseHandler;

import java.util.Calendar;


public class CreateAlarm extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alarm);
        final int timeH;  //get hour
        final int timeM; // get minute
        int timeD; // get Day
        int timeW; // get Week
        int timeY; // get year
        Time time = new Time();
        Calendar calendar = Calendar.getInstance();




        TimePicker myTimePicker = (TimePicker) findViewById(R.id.timePicker);
        myTimePicker.setIs24HourView(true);
//        myTimePicker.setCurrentHour(calendar.);


        timeH=myTimePicker.getCurrentHour();
        timeM=myTimePicker.getCurrentMinute();


        Button nextButton = (Button) findViewById(R.id.NextBtn);











        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent AlarmChoice = new Intent(getApplicationContext(), AlarmChoiceActivity.class);
//                getIntent().putExtra();
//                register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(AlarmChoice);

                // Close Login Screen
//                finish();

                Toast.makeText(getApplicationContext(), timeH+" "+timeM, Toast.LENGTH_LONG).show();


            }
        });







    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
