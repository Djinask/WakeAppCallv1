package com.example.wakeappcallv1.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wakeappcallv1.app.library.DatabaseHandler;
import com.example.wakeappcallv1.app.library.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class AlarmChoiceActivity extends Activity {
    HashMap<String, String> alarm;
    private AddAlarmTask mAddTask = null;
    private View mProgressView;
    private View mView;

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_choice);
        Intent intent = getIntent();
        mProgressView = findViewById(R.id.login_progress);
        alarm = (HashMap<String, String>) intent.getSerializableExtra("extra");

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        String owner=db.getUserDetails().get("uid");

        alarm.put("alarm_owner", owner);
        alarm.put("alarm_setted_time", "7:00");
                alarm.put("alarm_active", "1");
                alarm.put("alarm_mode","0");
                alarm.put("alarm_status","0");
                alarm.put("alarm_special", "0");
                alarm.put("alarm_list", "0");
                alarm.put("alarm_repeat", "0");
                alarm.put("alarm_play_after", "0");
                alarm.put("alarm_volume","50");
                alarm.put("alarm_ring_default","0");

        Button random = (Button) findViewById(R.id.random);


        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                saveAlarm(0);  // mode 0 means "random"


            }


        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.alarm_choice, menu);     DOVE SI TROVA STO MENU SILVIA?
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


    public void saveAlarm(int mode) {

            showProgress(true);
            mAddTask = new AddAlarmTask(alarm);
            mAddTask.execute((Void) null);


    }



public class AddAlarmTask extends AsyncTask<Void, Void, Boolean> {
    HashMap<String, String> alarm;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    AddAlarmTask(HashMap<String,String> hm) {

        alarm=hm;
    }

    JSONObject json;


    @Override
    protected Boolean doInBackground(Void... params) {
        // TODO: attempt authentication against a network service.

        final UserFunctions userFunction = new UserFunctions();



        try {

            json = userFunction.addAlarm(
                    alarm.get("alarm_name"),
                    alarm.get("alarm_owner"),
                    alarm.get("alarm_setted_time"),
                    alarm.get("alarm_mode"),
                    alarm.get("alarm_status"),
                    alarm.get("alarm_special"),
                    alarm.get("alarm_list"),
                    alarm.get("alarm_repeat"),
                    alarm.get("alarm_play_after"),
                    alarm.get("alarm_volume"),
                    alarm.get("alarm_ring_default"));


            //  check for add response
            Log.e("JSON", json.getString(KEY_SUCCESS));
            if (json.getString(KEY_SUCCESS) != null) {


                String res = json.getString(KEY_SUCCESS);
                if(Integer.parseInt(res) == 1){



                    // user successfully logged in
                    // Store user details in SQLite Database
                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    JSONObject json_user = json.getJSONObject("user");

                    // Clear all previous data in database
                    userFunction.logoutUser(getApplicationContext());
                    //db.addFriendsLocal();

                    // Launch Dashboard Screen
                    Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);

                    // Close all views before launching Dashboard
                    dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(dashboard);

                    // Close Login Screen
                    //finish();
                }else {
                    // Error in login
                    Log.e("FAIL:", res);
                    //Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_LONG).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }








        // TODO: register the new account here.
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        mAddTask = null;
        showProgress(false);

        Intent AlarmList = new Intent(getApplicationContext(), AlarmListActivity.class);
        AlarmList.putExtra("extra", "added");

        AlarmList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(AlarmList);

        // Close Login Screen
        finish();

    }

    @Override
    protected void onCancelled() {
        mAddTask = null;
        showProgress(false);
    }
}



    public void showProgress(final boolean show) {

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

    }
}



