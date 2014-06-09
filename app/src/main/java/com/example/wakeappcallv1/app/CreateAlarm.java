package com.example.wakeappcallv1.app;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.wakeappcallv1.app.R;
import com.example.wakeappcallv1.app.library.DatabaseHandler;

import java.util.Calendar;
import java.util.HashMap;


public class CreateAlarm extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alarm);

        final TimePicker myTimePicker  = (TimePicker) findViewById(R.id.timePicker);
        final DatePicker myDatePicker  = (DatePicker) findViewById(R.id.datePicker);
        final EditText name = (EditText)findViewById(R.id.AlarmName);

        Button nextButton = (Button) findViewById(R.id.NextBtn);

        final HashMap<String,String> alarm= new HashMap<String,String>();



        myTimePicker.setIs24HourView(true);





        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alarm.put("alarm_name", name.getText().toString());
                alarm.put("alarm_setted_time",myDatePicker.getYear()+
                        "-"+
                        (myDatePicker.getMonth()+1)+
                        "-"+
                        myDatePicker.getDayOfMonth()+
                        " "+
                        myTimePicker.getCurrentHour()+":"+myTimePicker.getCurrentMinute() );


                





                Intent AlarmChoice = new Intent(getApplicationContext(), AlarmChoiceActivity.class);
                AlarmChoice.putExtra("extra", alarm);

                AlarmChoice.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(AlarmChoice);

                // Close Login Screen
                finish();



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
