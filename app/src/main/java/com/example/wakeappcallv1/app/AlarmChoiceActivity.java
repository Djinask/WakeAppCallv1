package com.example.wakeappcallv1.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wakeappcallv1.app.library.DatabaseHandler;
import com.example.wakeappcallv1.app.library.UserFunctions;
import com.facebook.UiLifecycleHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class AlarmChoiceActivity extends Activity {
    private UiLifecycleHelper uiHelper;

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

        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);

        alarm = (HashMap<String, String>)intent.getSerializableExtra("extra");
        Log.v("alarmNAme", alarm.get("alarm_name"));
        Log.v("alarmNAme", alarm.get("alarm_setted_time"));





        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        String owner=db.getUserDetails().get("uid");
        Log.e("User hm",db.getUserDetails().toString());

                alarm.put("alarm_owner", owner);

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
        Button special = (Button) findViewById(R.id.special);


        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                saveAlarm(0);  // mode 0 means "random"







            }


        });

        special.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SomeOneSpecial = new Intent(getApplicationContext(), SomeoneSpecialActivity.class);
                SomeOneSpecial.putExtra("extra", alarm);

                SomeOneSpecial.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(SomeOneSpecial);
            }
        });


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
    JSONArray jsonAlarms;

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
//            jsonAlarms = userFunction.getAlarms(mEmail,json.getString("uid"));

            //  check for add response
            Log.e("JSON response", json.toString());
            if (json.getString(KEY_SUCCESS) != null) {


                String res = json.getString(KEY_SUCCESS);
                if(Integer.parseInt(res) == 1){
//                    addAlarmLocal


                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    // Alarm added on the dataBase
                    // Store user details in SQLite Database


                    Log.e("JSObj TO UPDATE", json.toString());


                }else {

                    Log.e("FAIL:", res);
                    //Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_LONG).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }








        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        mAddTask = null;
        showProgress(false);
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());



            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(AlarmChoiceActivity.this, android.R.style.Theme_Holo_Dialog));
            builder.setMessage(getString(R.string.share_confirm)).setTitle(getString(R.string.share));
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    String msg=getString(R.string.settings_share_text);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, msg );
//                    Uri path = Uri.fromFile(new File("android.resource://"+ getApplicationContext().getPackageName()
//                    +"/" + R.drawable.logo_md));
//                    shareIntent.putExtra(Intent.EXTRA_STREAM, path);
//                    shareIntent.setType("image/png");
                    finish();
                    shareIntent.setType("text/plain");

                    startActivity(shareIntent);


                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent AlarmList = new Intent(getApplicationContext(), AlarmListActivity.class);

                    AlarmList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(AlarmList);


                    finish();


                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();



            db.addOneAlarmLocal(json);






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



