package com.example.wakeappcallv1.app.library;

/**
 * Created by lucamarconcini on 16/05/14.
 */


import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class UserFunctions {

    private PARSER jsonParser;

    // Testing in localhost using wamp or xampp
    // use http://10.0.2.2/ to connect to your localhost ie http://localhost/
    private static String loginURL = "http://wakeappcall.net63.net/index.php";
    private static String registerURL = "http://wakeappcall.net63.net/index.php";



    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String add_alarm_tag = "new_alarm";
    private static String get_alarm_tag = "get_alarms";
    private static String add_friend = "add_friend";



    // constructor
    public UserFunctions(){
        jsonParser = new PARSER();
    }

    /**
     * function make Login Request
     * @param email
     * @param password
     * */
    public JSONObject loginUser(String email, String password){
        // Building Parameters
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        PARSER JP = new PARSER();
        JSONObject json =JP.getJSONFromUrl(loginURL, params);

        return json;
    }

    /**
     * function make Login Request
     * @param name
     * @param email
     * @param password
     * @param phone
     * @param birthDate
     * @param country
     * @param city
     * */
    public JSONObject registerUser(String name, String email, String password, String phone, String birthDate, String country, String city){
        // Building Parameters
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("birthDate", birthDate));
        params.add(new BasicNameValuePair("country", country));
        params.add(new BasicNameValuePair("city", city));

        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return json;
    }
    /**
     * function Add an Alarm
     * @param alarm_name
     * @param alarm_owner
     * @param alarm_setted_time
     * @param alarm_mode
     * @param alarm_status
     * @param alarm_special
     * @param alarm_list
     * @param alarm_repeat
     * @param alarm_play_after
     * @param alarm_volume
     * @param alarm_ring_default
     * */
    public JSONObject addAlarm(String alarm_name, String alarm_owner, String alarm_setted_time, String alarm_mode, String alarm_status, String alarm_special, String alarm_list, String alarm_repeat, String alarm_play_after, String alarm_volume, String alarm_ring_default){
        // Building Parameters
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", add_alarm_tag));
        params.add(new BasicNameValuePair("alarm_name", alarm_name));
        params.add(new BasicNameValuePair("alarm_owner", alarm_owner));
        params.add(new BasicNameValuePair("alarm_setted_time", alarm_setted_time));
        params.add(new BasicNameValuePair("alarm_mode", alarm_mode));
        params.add(new BasicNameValuePair("alarm_status", alarm_status));
        params.add(new BasicNameValuePair("alarm_special", alarm_special));
        params.add(new BasicNameValuePair("alarm_list", alarm_list));
        params.add(new BasicNameValuePair("alarm_repeat", alarm_repeat));
        params.add(new BasicNameValuePair("alarm_play_after", alarm_play_after));
        params.add(new BasicNameValuePair("alarm_volume", alarm_volume));
        params.add(new BasicNameValuePair("alarm_ring_default", alarm_ring_default));

        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return json;
    }

    /**
     * function Get all Alarms

     * @param alarm_owner

     * */
    public JSONArray getAlarms(String email, String alarm_owner){
        // Building Parameters
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_alarm_tag));

        params.add(new BasicNameValuePair("alarm_owner", alarm_owner));
        params.add(new BasicNameValuePair("email", email));


        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        JSONArray js=new JSONArray();

        try {
            js = json.getJSONArray("alarm");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        // return json
        return js;
    }

    /**
     * Function get Login status
     * */
    public boolean isUserLoggedIn(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        int count = db.getRowCount();
        if(count > 0){
            // user logged in
            return true;
        }
        return false;
    }

    /**
     * Function to logout user
     * Reset Database
     * */
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }

}