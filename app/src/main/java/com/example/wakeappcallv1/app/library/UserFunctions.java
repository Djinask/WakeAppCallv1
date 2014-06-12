package com.example.wakeappcallv1.app.library;

/**
 * Created by lucamarconcini on 16/05/14.
 */


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

public class UserFunctions {

    private PARSER jsonParser;

    // Testing in localhost using wamp or xampp
    // use http://10.0.2.2/ to connect to your localhost ie http://localhost/
    private static String loginURL = "http://wakeappcall.net63.net/index.php";
    private static String registerURL = "http://wakeappcall.net63.net/index.php";
//    private static String loginURL = "http://10.0.2.2/ApiWac/index.php";
//    private static String registerURL = "http://10.0.2.2/ApiWac/index.php";



    private static String login_tag = "login";
    private static String check_user= "check_user";
    private static String loginFb_tag = "login_fb";
    private static String register_tag = "register";
    private static String update_img = "update_img";
    private static String add_alarm_tag = "new_alarm";
    private static String get_alarm_tag = "get_alarms";
    private static String add_friends_tag = "add_friends";
    private static String search_friends_tag = "search_friend";
    private static String get_friends_tag = "get_friends";
    private static String get_tasks_tag = "get_tasks";
    private static String delete_friends_tag = "delete_friends";
    private static String get_friends_details_tag = "get_friends_details";
    private static String add_notification = "add_notification";
    private static String get_notification_unseen = "get_notification_unseen";
    private static String set_notification_seen = "set_notification_seen";
    private static String get_notification_active = "get_notification_active";
    private static String set_notification_not_active = "set_notification_not_active";
    private static String set_friendship_accepted = "set_friendship_accepted";
    private static String get_user_details = "get_user_details";

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
    public JSONObject checkUser_if_exist(String email){
        // Building Parameters
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("tag", check_user));
        params.add(new BasicNameValuePair("email", email));

        PARSER JP = new PARSER();
        JSONObject json =JP.getJSONFromUrl(loginURL, params);

        return json;
    }
    public JSONObject login_fb(String email){
        // Building Parameters
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("tag", loginFb_tag));
        params.add(new BasicNameValuePair("email", email));

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
    public JSONObject registerUser(String name, String email, String password, String phone, String birthDate, String country, String city, String FB_id, String avatar_path ){
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
        params.add(new BasicNameValuePair("fb_id", FB_id));
        params.add(new BasicNameValuePair("image_path", avatar_path));


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

    public JSONObject getUserDetails(String uid) {
        // Building Parameters
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_user_details));
        params.add(new BasicNameValuePair("uid", uid));

        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return json;
    }

    public JSONObject searchFriend(String email, String search_mail) {
        // Building Parameters
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", search_friends_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("searched_mail", search_mail));

        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return json;
    }

    public JSONObject lookupFB(String facebook_id){
        // Building Parameters
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "lookup_fb"));
        Log.e("UF","I got called with id "+facebook_id);
        params.add(new BasicNameValuePair("facebook_id", ""+facebook_id));
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return json;
    }

    public JSONObject addFriend(String email, String friend_owner, String friend_to) {
        // Building Parameters
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", add_friends_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("friendship_owner", friend_owner));
        params.add(new BasicNameValuePair("friendship_to", friend_to));

        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return json;
    }


     public JSONArray getFriends(String email, String friend_owner){
        // Building Parameters
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_friends_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("friendship_owner", friend_owner));


        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        JSONArray jsArr = new JSONArray();

        try {
            jsArr = json.getJSONArray("friendship");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsArr;
    }


    public JSONObject deleteFriend(String email, String friend_owner, String friend_to) {
        // Building Parameters
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", delete_friends_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("friendship_owner", friend_owner));
        params.add(new BasicNameValuePair("friendship_to", friend_to));

        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return json;
    }

    // given the friendship_owner, return all the details about his/her friends
    public JSONArray getFriendsDetails(String email, String friend_owner){
        // Building Parameters
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_friends_details_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("friendship_owner", friend_owner));

        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        JSONArray jsArr = new JSONArray();

        try {
            jsArr = json.getJSONArray("friends_details");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsArr;
    }
    // given the friendship_owner, return all the details about his/her friends
    public JSONArray getTasks(String mail, String myUid){
        // Building Parameters
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_tasks_tag));
        params.add(new BasicNameValuePair("email", mail));

        params.add(new BasicNameValuePair("uid", myUid));

        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        JSONArray jsArr = new JSONArray();

        try {
            jsArr = json.getJSONArray("tasks");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsArr;
    }


    public JSONObject updateProfileImage(String url_image, String owner ){
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.


//
//        byte [] byte_arr = stream.toByteArray();
//        String image_str = Base64.encodeToString(byte_arr,Base64.DEFAULT);
//
//        Log.e("IMAGE ARRAY", image_str);
//


        ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("tag", update_img));
        params.add(new BasicNameValuePair("owner", owner));
        params.add(new BasicNameValuePair("image", url_image));

        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return json;
    }


    public JSONObject addNotification(String notif_from, String notif_to, String type) {
        // Building Parameters
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", add_notification));
        params.add(new BasicNameValuePair("notif_from", notif_from));
        params.add(new BasicNameValuePair("notif_to", notif_to));
        params.add(new BasicNameValuePair("type", type));

        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return json;
    }

    public JSONArray getNotificationActive(String notif_to){
        // Building Parameters
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_notification_active));
        params.add(new BasicNameValuePair("notif_to", notif_to));

        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        JSONArray jsArr = new JSONArray();

        try {

            jsArr = json.getJSONArray("notification");
            if(jsArr.isNull(0))return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsArr;
    }

    public JSONObject setNotificationNotActive(String id) {
        // Building Parameters
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", set_notification_not_active));
        params.add(new BasicNameValuePair("notif_id", id));

        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return json;
    }


    public JSONArray getNotificationUnseen(String notif_to){
        // Building Parameters
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_notification_unseen));
        params.add(new BasicNameValuePair("notif_to", notif_to));

        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        JSONArray jsArr = new JSONArray();

        try {

            jsArr = json.getJSONArray("notification");
            if(jsArr.isNull(0))return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsArr;
    }

    public JSONObject setNotificationSeen(String id) {
        // Building Parameters
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", set_notification_seen));
        params.add(new BasicNameValuePair("notif_id", id));

        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return json;
    }

    public JSONObject setFriendAccepted(String from, String to) {
        // Building Parameters
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", set_friendship_accepted));
        params.add(new BasicNameValuePair("friendship_owner", from));
        params.add(new BasicNameValuePair("friendship_to", to));

        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);

        // return json
        return json;
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