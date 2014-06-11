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
import java.util.HashMap;
import java.util.Map;


public class SomeoneSpecialActivity extends Activity {

    HashMap<String, String> alarm;

    DatabaseHandler db;
    ArrayList<HashMap<String, String>> friends;
    ArrayList<HashMap<String, String>> friendships;
    Map<String,ArrayList<String>> friends_details;
    private AddAlarmTask mAddTask = null;

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";



    FriendSpecialAdapter adapter;


    ProgressBar bar = null;
    String friendUid = null;

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_someone_special);


        final ListView listView = (ListView) findViewById(R.id.friendlistView);
        bar = (ProgressBar) findViewById(R.id.deleteProgress);

        db = new DatabaseHandler(this);
        String owner=db.getUserDetails().get("uid");

        Intent intent = getIntent();
        alarm = (HashMap<String, String>)intent.getSerializableExtra("extra");
        Log.v("alarmNAme", alarm.get("alarm_name"));
        Log.v("alarmNAme", alarm.get("alarm_setted_time"));



        alarm.put("alarm_owner", owner);

        alarm.put("alarm_active", "1");
        alarm.put("alarm_mode","0");
        alarm.put("alarm_status","0");

        alarm.put("alarm_list", "0");
        alarm.put("alarm_repeat", "0");
        alarm.put("alarm_play_after", "0");
        alarm.put("alarm_volume","50");
        alarm.put("alarm_ring_default","0");

        // read details of friends from local DB (all row in the table are friends of current user)
        friends = db.getFriendsDetails(); // Arralyst<HashMap<String,String>>

        // array with friends details, will be used with adapter
        final ArrayList<String> names = new ArrayList<String>(friends.size());
        final ArrayList<String> UIDs = new ArrayList<String>(friends.size());
        final ArrayList<String> mail = new ArrayList<String>(friends.size());
        final ArrayList<String> birthdate = new ArrayList<String>(friends.size());
        final ArrayList<String> country = new ArrayList<String>(friends.size());
        final ArrayList<String> city = new ArrayList<String>(friends.size());


        for(int i=0;i<friends.size();i++) {
            names.add(friends.get(i).get("name"));
            UIDs.add(friends.get(i).get("uid"));
            mail.add(friends.get(i).get("email"));
            birthdate.add(friends.get(i).get("birth_date"));
            country.add(friends.get(i).get("country"));
            city.add(friends.get(i).get("city"));
        }



        friends_details = new HashMap<String, ArrayList<String>>();
        friends_details.put("names",names);
        friends_details.put("UIDs",UIDs);
        friends_details.put("email",mail);
        friends_details.put("birth_date",birthdate);
        friends_details.put("country",country);
        friends_details.put("city",city);
        Log.e("QUANTI SONO ?", friends.size()+" ");

        adapter = new FriendSpecialAdapter(this, friends_details);

        listView.setAdapter(adapter);
        // click on the list item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {

                // Create custom dialog object
                final Dialog dialog = new Dialog(SomeoneSpecialActivity.this, android.R.style.Theme_Holo_Dialog);
                // Include dialog.xml file
                dialog.setContentView(R.layout.custom_dialog);
                // Set dialog title

                dialog.setTitle("Search result:");


                // set values for custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.textDialog);
                Button ok = (Button) dialog.findViewById(R.id.ok);
                Button cancel = (Button) dialog.findViewById(R.id.cancel);
                ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);


                text.setText("Want you be woken up by "+ friends_details.get("names").get(position)+" ?\n");
                //image.setImageBitmap(user_image);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        // atempt request
                        saveAlarm(friends_details.get("UIDs").get(position));
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bar.setVisibility(View.INVISIBLE);
                        dialog.dismiss();

                    }
                });

                dialog.show();

            }
        });

        registerForContextMenu(listView);
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




    public void saveAlarm(String special) {
        bar.setVisibility(View.VISIBLE);
        mAddTask = new AddAlarmTask(alarm,special);
        mAddTask.execute((Void) null);


    }



    public class AddAlarmTask extends AsyncTask<Void, Void, Boolean> {
        HashMap<String, String> alarm;
        String who;

        @Override
        public boolean equals(Object o) {
            return super.equals(o);
        }

        AddAlarmTask(HashMap<String,String> hm, String uid) {


            alarm=hm;
            who=uid;
            Log.e("chi", who);
        }

        JSONObject json;
        JSONArray jsonAlarms;


        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            final UserFunctions userFunction = new UserFunctions();

            alarm.put("alarm_special", who);

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

                userFunction.addNotification(db.getUserDetails().get("uid"), who, "3");
//            jsonAlarms = userFunction.getAlarms(mEmail,json.getString("uid"));

                //  check for add response
                Log.e("JSON response", json.toString());
                if (json.getString(KEY_SUCCESS) != null) {


                    String res = json.getString(KEY_SUCCESS);
                    if(Integer.parseInt(res) == 1){
//                    addAlarmLocal


                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        // Allarm added on the dataBase
                        // Store user details in SQLite Database


                        Log.e("JSObj TO UPDATE", json.toString());

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
            bar.setVisibility(View.INVISIBLE);
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            try {
                Log.e("alarm_name_:",json.getJSONObject("alarm").getString("alarm_name"));
                db.addOneAlarmLocal(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }

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
            bar.setVisibility(View.INVISIBLE);        }
    }


}
