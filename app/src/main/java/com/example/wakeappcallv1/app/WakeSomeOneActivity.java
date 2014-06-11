package com.example.wakeappcallv1.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wakeappcallv1.app.library.DatabaseHandler;
import com.example.wakeappcallv1.app.library.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class WakeSomeOneActivity extends Activity {
    private static final String KEY_TASK_ID = "task_id";
    private static final String KEY_TASK_UID = "task_uid";
    private static final String KEY_TASK_ALARM_NAME = "task_alarm_name";
    private static final String KEY_TASK_ALARM_OWNER = "task_alarm_owner";
    private static final String KEY_TASK_ALARM_OWNER_NAME = "task_alarm_owner_name";
    private static final String KEY_TASK_ALARM_OWNER_FB_ID = "task_alarm_owner_fb_id";
    private static final String KEY_TASK_ALARM_OWNER_MAIL = "task_alarm_owner_mail";
    private static final String KEY_TASK_ALARM_OWNER_PHONE = "task_alarm_owner_phone";
    private static final String KEY_TASK_SETTED_TIME = "task_alarm_setted_time";
    private static final String KEY_TASK_ALARM_ACTIVE = "task_alarm_active";
    private static final String KEY_TASK_CREATED_AT = "task_alarm_created_at";
//    private static String KEY_SUCCESS = "success";
//    private static String KEY_ERROR = "error";
//    private static String KEY_ERROR_MSG = "error_msg";


//
    WakeSomeOneAdapter adapter;
//
    DatabaseHandler db;
    ProgressBar bar = null;
    HashMap<String,ArrayList<String>> task_details;

//    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wake_someone_layout);

        db=new DatabaseHandler(this);
        task_details = new HashMap<String, ArrayList<String>>();
        final ListView listView = (ListView) findViewById(R.id.alarm_list_view);
        bar = (ProgressBar) findViewById(R.id.deleteProgress);
        UserFunctions f = new UserFunctions();
        String MyUid=db.getUserDetails().get("uid");




        ArrayList<HashMap<String,String>> tasks= db.getTasksDetail();




        // array with friends details, will be used with adapter
        final ArrayList<String> ids = new ArrayList<String>(tasks.size());
        final ArrayList<String> alarmNames = new ArrayList<String>(tasks.size());
        final ArrayList<String> alarmOwners = new ArrayList<String>(tasks.size());
        final ArrayList<String> alarmOwnerNames = new ArrayList<String>(tasks.size());
        final ArrayList<String> alarmOwnerFbs = new ArrayList<String>(tasks.size());
        final ArrayList<String> alarmOwnerMails = new ArrayList<String>(tasks.size());
        final ArrayList<String> alarmOwnerPhones = new ArrayList<String>(tasks.size());
        final ArrayList<String> alarmSettedTimes = new ArrayList<String>(tasks.size());
        final ArrayList<String> alarmActives = new ArrayList<String>(tasks.size());

        final ArrayList<String> alarmCreatedAts = new ArrayList<String>(tasks.size());


        for(int i=0;i<tasks.size();i++) {
            ids.add(tasks.get(i).get(KEY_TASK_ID));

            alarmNames.add(tasks.get(i).get(KEY_TASK_ALARM_NAME));
            alarmOwners.add(tasks.get(i).get(KEY_TASK_ALARM_OWNER));
            alarmOwnerNames.add(tasks.get(i).get(KEY_TASK_ALARM_OWNER_NAME));
            alarmOwnerFbs.add(tasks.get(i).get(KEY_TASK_ALARM_OWNER_FB_ID));
            alarmOwnerMails.add(tasks.get(i).get(KEY_TASK_ALARM_OWNER_MAIL));
            alarmOwnerPhones.add(tasks.get(i).get(KEY_TASK_ALARM_OWNER_PHONE));
            alarmSettedTimes.add(tasks.get(i).get(KEY_TASK_SETTED_TIME));
            alarmCreatedAts.add(tasks.get(i).get(KEY_TASK_CREATED_AT));
            alarmActives.add(tasks.get(i).get(KEY_TASK_ALARM_ACTIVE));




        }
        if(!alarmNames.isEmpty()){
    task_details.put("task_id", ids);
    task_details.put("task_alarm_name", alarmNames);
    task_details.put("task_alarm_owner", alarmOwners);
    task_details.put("task_alarm_owner_name", alarmOwnerNames);
    task_details.put("task_alarm_owner_fb_id", alarmOwnerFbs);
    task_details.put("task_alarm_owner_mail", alarmOwnerMails);
    task_details.put("task_alarm_owner_phone", alarmOwnerPhones);
    task_details.put("task_alarm_setted_time", alarmSettedTimes);
    task_details.put("task_alarm_created_at", alarmCreatedAts);
    task_details.put("task_alarm_active", alarmActives);
    Log.i("TASK DETAIL FROM WAKSO", task_details.toString());


    adapter = new WakeSomeOneAdapter(this, task_details);
}
        registerForContextMenu(listView);  // non so a cosa serva
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.someone_special, menu);
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



//
//    public void saveAlarm(String special) {
//        bar.setVisibility(View.VISIBLE);
//        mAddTask = new AddAlarmTask(alarm,special);
//        mAddTask.execute((Void) null);
//
//
//    }


//
//    public class AddAlarmTask extends AsyncTask<Void, Void, Boolean> {
//        HashMap<String, String> alarm;
//        String who;
//
//        @Override
//        public boolean equals(Object o) {
//            return super.equals(o);
//        }
//
//        AddAlarmTask(HashMap<String,String> hm, String uid) {
//
//
//            alarm=hm;
//            who=uid;
//            Log.e("chi", who);
//        }
//
//        JSONObject json;
//        JSONArray jsonAlarms;
//
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            // TODO: attempt authentication against a network service.
//
//            final UserFunctions userFunction = new UserFunctions();
//
//            alarm.put("alarm_special", who);
//
//            try {
//
//                json = userFunction.addAlarm(
//                        alarm.get("alarm_name"),
//                        alarm.get("alarm_owner"),
//                        alarm.get("alarm_setted_time"),
//                        alarm.get("alarm_mode"),
//                        alarm.get("alarm_status"),
//                        alarm.get("alarm_special"),
//                        alarm.get("alarm_list"),
//                        alarm.get("alarm_repeat"),
//                        alarm.get("alarm_play_after"),
//                        alarm.get("alarm_volume"),
//                        alarm.get("alarm_ring_default"));
////            jsonAlarms = userFunction.getAlarms(mEmail,json.getString("uid"));
//
//                //  check for add response
//                Log.e("JSON response", json.toString());
//                if (json.getString(KEY_SUCCESS) != null) {
//
//
//                    String res = json.getString(KEY_SUCCESS);
//                    if(Integer.parseInt(res) == 1){
////                    addAlarmLocal
//
//
//                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//                        // Allarm added on the dataBase
//                        // Store user details in SQLite Database
//
//
//                        Log.e("JSObj TO UPDATE", json.toString());
//
//                        // Close Login Screen
//                        //finish();
//                    }else {
//                        // Error in login
//                        Log.e("FAIL:", res);
//                        //Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_LONG).show();
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//
//
//
//
//
//
//
//
//            // TODO: register the new account here.
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//            mAddTask = null;
//            bar.setVisibility(View.INVISIBLE);
//            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//            try {
//                Log.e("alarm_name_:",json.getJSONObject("alarm").getString("alarm_name"));
//                db.addOneAlarmLocal(json);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            Intent AlarmList = new Intent(getApplicationContext(), AlarmListActivity.class);
//            AlarmList.putExtra("extra", "added");
//
//            AlarmList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(AlarmList);
//
//            // Close Login Screen
//            finish();
//
//        }
//
//        @Override
//        protected void onCancelled() {
//            mAddTask = null;
//            bar.setVisibility(View.INVISIBLE);        }
//    }
//

}
