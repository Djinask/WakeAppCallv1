package com.example.wakeappcallv1.app.library;

/**
 * Created by lucamarconcini on 16/05/14.
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_db";

    // Login table name
    private static final String TABLE_LOGIN = "login";
    // Alarm table name
    private static final String TABLE_ALARM = "alarm";
    // Friendship table name
    private static final String TABLE_FRIENDSHIP = "friendship";
    private static final String TABLE_FRIENDS_DETAILS = "friends_details";
    private static final String TABLE_TASKS = "user_tasks";


    //LOGIN NAMES
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_BIRTHDATE = "birth_date";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_CITY = "city";
    private static final String KEY_IMAGE_PATH = "image_path";
    private static final String KEY_FACEBOOK_ID = "facebook_id";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_UPDATED_AT = "updated_at";


    // ALARM NAMES
    private static final String KEY_ALARM_UID = "alarm_uuid";
    private static final String KEY_ALARM_ID = "alarm_uid";
    private static final String KEY_ALARM_NAME = "alarm_name";
    private static final String KEY_ALARM_OWNER = "alarm_owner";
    private static final String KEY_ALARM_SETTED_TIME= "alarm_settedTime";
    private static final String KEY_ALARM_MODE = "alarm_mode";
    private static final String KEY_ALARM_STATUS = "alarm_status";
    private static final String KEY_ALARM_ACTIVE = "alarm_active";
    private static final String KEY_ALARM_SPECIAL = "alarm_special";
    private static final String KEY_ALARM_LIST = "alarm_list";
    // allarm path
    private static final String KEY_ALARM_REPEAT = "alarm_repeat";
    private static final String KEY_ALARM_PLAY_AFTER = "alarm_play_after";
    private static final String KEY_ALARM_VOLUME = "alarm_volume";
    private static final String KEY_ALARM_RING_DEFAULT = "alarm_ring_default";
    private static final String KEY_ALARM_CREATED_AT = "created_at";
    private static final String KEY_ALARM_UPDATED_AT = "updated_at";

    // FRIENDSHIP NAME
    private static final String KEY_FRIENDSHIP_UID = "friendship_uid";
    private static final String KEY_FRIENDSHIP_ID = "friendship_id";
    private static final String KEY_FRIENDSHIP_OWNER = "friendship_owner";
    private static final String KEY_FRIENDSHIP_TO = "friendship_to";
    private static final String KEY_FRIENDSHIP_ACCEPTED = "friendship_accepted";
    private static final String KEY_FRIENDSHIP_ACTIVE = "friendship_active";
    private static final String KEY_FRIENDSHIP_CREATED_AT = "created_at";
    private static final String KEY_FRIENDSHIP_UPDATED_AT = "updated_at";

    //TASK NAMES
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




    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_PHONE + " TEXT,"
                + KEY_BIRTHDATE + " TEXT,"
                + KEY_COUNTRY + " TEXT,"
                + KEY_CITY + " TEXT,"
                + KEY_IMAGE_PATH + " TEXT,"
                + KEY_FACEBOOK_ID + " TEXT,"
                + KEY_UID + " TEXT,"
                + KEY_UPDATED_AT + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";


        String CREATE_ALARM_TABLE = "CREATE TABLE " + TABLE_ALARM + "("
                + KEY_ALARM_ID + " INTEGER PRIMARY KEY,"
                + KEY_ALARM_UID + " TEXT,"
                + KEY_ALARM_NAME + " TEXT,"
                + KEY_ALARM_OWNER + " INTEGER,"
                + KEY_ALARM_SETTED_TIME + " TIME,"
                + KEY_ALARM_MODE + " INTEGER,"
                + KEY_ALARM_STATUS + " INTEGER,"
                + KEY_ALARM_ACTIVE + " INTEGER,"
                + KEY_ALARM_SPECIAL + " INTEGER,"
                + KEY_ALARM_LIST + " INTEGER,"
                + KEY_ALARM_REPEAT + " INTEGER,"
                + KEY_ALARM_PLAY_AFTER + " INTEGER,"
                + KEY_ALARM_VOLUME + " INTEGER,"
                + KEY_ALARM_RING_DEFAULT + " INTEGER,"
                + KEY_ALARM_CREATED_AT + " TEXT,"
                + KEY_ALARM_UPDATED_AT + " TEXT" + ")";

        String CREATE_FRIENDSHIP_TABLE = "CREATE TABLE " + TABLE_FRIENDSHIP + "("
                + KEY_FRIENDSHIP_ID + " INTEGER PRIMARY KEY,"
                + KEY_FRIENDSHIP_UID + " TEXT,"
                + KEY_FRIENDSHIP_OWNER + " TEXT,"
                + KEY_FRIENDSHIP_TO + " TEXT,"
                + KEY_FRIENDSHIP_ACCEPTED + " INTEGER,"
                + KEY_FRIENDSHIP_ACTIVE + " INTEGER,"
                + KEY_FRIENDSHIP_CREATED_AT + " TEXT,"
                + KEY_FRIENDSHIP_UPDATED_AT + " TEXT" + ")";

        String CREATE_FRIENDS_DETAILS_TABLE = "CREATE TABLE " + TABLE_FRIENDS_DETAILS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_PHONE + " TEXT,"
                + KEY_BIRTHDATE + " TEXT,"
                + KEY_COUNTRY + " TEXT,"
                + KEY_CITY + " TEXT,"
                + KEY_FACEBOOK_ID + " TEXT,"
                + KEY_IMAGE_PATH + " TEXT,"
                + KEY_UID + " TEXT,"
                + KEY_UPDATED_AT + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";

        String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + KEY_TASK_ID + " INTEGER PRIMARY KEY,"
                + KEY_TASK_UID + " TEXT,"
                + KEY_TASK_ALARM_NAME + " TEXT UNIQUE,"
                + KEY_TASK_ALARM_OWNER + " TEXT,"
                + KEY_TASK_ALARM_OWNER_NAME + " TEXT,"
                + KEY_TASK_ALARM_OWNER_FB_ID + " TEXT,"
                + KEY_TASK_ALARM_OWNER_MAIL + " TEXT,"
                + KEY_TASK_ALARM_OWNER_PHONE + " TEXT,"
                + KEY_TASK_SETTED_TIME + " TEXT,"
                + KEY_TASK_ALARM_ACTIVE + " TEXT,"
                + KEY_TASK_CREATED_AT +")";


        try {
            db.execSQL(CREATE_LOGIN_TABLE);
            db.execSQL(CREATE_ALARM_TABLE);
            db.execSQL(CREATE_FRIENDSHIP_TABLE);
            db.execSQL(CREATE_TASK_TABLE);

            db.execSQL(CREATE_FRIENDS_DETAILS_TABLE);

        }catch(android.database.sqlite.SQLiteException ex){
            ex.printStackTrace();
        }
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDSHIP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS_DETAILS);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email,String phone, String birthdate, String country, String city, String image_path, String uid, String Fb_id, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_PHONE, phone); // Phone
        values.put(KEY_BIRTHDATE, birthdate); // BirthDate
        values.put(KEY_COUNTRY, country); // country
        values.put(KEY_CITY, city); // City
        values.put(KEY_IMAGE_PATH, image_path);
        values.put(KEY_UID, uid); // Unique id
        values.put(KEY_FACEBOOK_ID, Fb_id);
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection
    }



    /**
     * Storing alarms details in database
     * */
    public void addAlarmLocal(JSONArray jsonArray) {
        SQLiteDatabase db = this.getWritableDatabase();


        try {

            db.execSQL("delete from " + TABLE_ALARM);


            for (int i=0;i<jsonArray.length();i++){
                ContentValues values = new ContentValues();
                values.put(KEY_ALARM_NAME, jsonArray.getJSONObject(i).getString(KEY_ALARM_NAME)); // Name
                values.put(KEY_ALARM_UID, jsonArray.getJSONObject(i).getString(KEY_ALARM_UID)); // id

                values.put(KEY_ALARM_OWNER, jsonArray.getJSONObject(i).getString(KEY_ALARM_OWNER)); // owner
                values.put(KEY_ALARM_SETTED_TIME, jsonArray.getJSONObject(i).getString(KEY_ALARM_SETTED_TIME)); // time
                values.put(KEY_ALARM_MODE, jsonArray.getJSONObject(i).getString(KEY_ALARM_MODE)); // mode
                values.put(KEY_ALARM_STATUS, jsonArray.getJSONObject(i).getString(KEY_ALARM_STATUS)); // status
                values.put(KEY_ALARM_ACTIVE,jsonArray.getJSONObject(i).getString(KEY_ALARM_ACTIVE) ); //active
                values.put(KEY_ALARM_SPECIAL, jsonArray.getJSONObject(i).getString(KEY_ALARM_SPECIAL)); // special
                values.put(KEY_ALARM_LIST, jsonArray.getJSONObject(i).getString(KEY_ALARM_LIST)); //  list
                values.put(KEY_ALARM_REPEAT, jsonArray.getJSONObject(i).getString(KEY_ALARM_REPEAT)); // repeat
                values.put(KEY_ALARM_PLAY_AFTER, jsonArray.getJSONObject(i).getString(KEY_ALARM_PLAY_AFTER)); // play_after
                values.put(KEY_ALARM_VOLUME, jsonArray.getJSONObject(i).getString(KEY_ALARM_VOLUME)); // Volume
                values.put(KEY_ALARM_RING_DEFAULT, jsonArray.getJSONObject(i).getString(KEY_ALARM_RING_DEFAULT)); // default ring
                values.put(KEY_ALARM_CREATED_AT, jsonArray.getJSONObject(i).getString(KEY_ALARM_CREATED_AT)); // Created At
                values.put(KEY_ALARM_UPDATED_AT, jsonArray.getJSONObject(i).getString(KEY_ALARM_UPDATED_AT)); // Updated At

                // Inserting Row
                db.insert(TABLE_ALARM, null, values);




            }}catch(android.database.sqlite.SQLiteException ex){
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        db.close(); // Closing database connection
    }
    /**
     * Storing  1 alarm details in database
     * */
    public void addOneAlarmLocal(JSONObject jo) {
        SQLiteDatabase db = this.getWritableDatabase();


        try {



            ContentValues values = new ContentValues();
            values.put(KEY_ALARM_NAME, jo.getJSONObject("alarm").getString(KEY_ALARM_NAME)); // Name
            values.put(KEY_ALARM_UID, jo.getString(KEY_ALARM_UID)); // id

            values.put(KEY_ALARM_OWNER, jo.getJSONObject("alarm").getString(KEY_ALARM_OWNER)); // owner
            values.put(KEY_ALARM_SETTED_TIME, jo.getJSONObject("alarm").getString(KEY_ALARM_SETTED_TIME)); // time
            values.put(KEY_ALARM_MODE, jo.getJSONObject("alarm").getString(KEY_ALARM_MODE)); // mode
            values.put(KEY_ALARM_STATUS, jo.getJSONObject("alarm").getString(KEY_ALARM_STATUS)); // status
            values.put(KEY_ALARM_ACTIVE, jo.getJSONObject("alarm").getString(KEY_ALARM_ACTIVE)); //active
            values.put(KEY_ALARM_SPECIAL, jo.getJSONObject("alarm").getString(KEY_ALARM_SPECIAL)); // special
            values.put(KEY_ALARM_LIST, jo.getJSONObject("alarm").getString(KEY_ALARM_LIST)); //  list
            values.put(KEY_ALARM_REPEAT, jo.getJSONObject("alarm").getString(KEY_ALARM_REPEAT)); // repeat
            values.put(KEY_ALARM_PLAY_AFTER, jo.getJSONObject("alarm").getString(KEY_ALARM_PLAY_AFTER)); // play_after
            values.put(KEY_ALARM_VOLUME, jo.getJSONObject("alarm").getString(KEY_ALARM_VOLUME)); // Volume
            values.put(KEY_ALARM_RING_DEFAULT, jo.getJSONObject("alarm").getString(KEY_ALARM_RING_DEFAULT)); // default ring
            values.put(KEY_ALARM_CREATED_AT, jo.getJSONObject("alarm").getString(KEY_ALARM_CREATED_AT)); // Created At
            values.put(KEY_ALARM_UPDATED_AT, jo.getJSONObject("alarm").getString(KEY_ALARM_UPDATED_AT)); // Updated At

            // Inserting Row
            db.insert(TABLE_ALARM, null, values);




        }catch(android.database.sqlite.SQLiteException ex){
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        db.close(); // Closing database connection
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("name", cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            user.put("email", cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
            user.put("phone", cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
            user.put("birthdate", cursor.getString(cursor.getColumnIndex(KEY_BIRTHDATE)));
            user.put("country", cursor.getString(cursor.getColumnIndex(KEY_COUNTRY)));
            user.put("city", cursor.getString(cursor.getColumnIndex(KEY_CITY)));
            user.put("image_path",cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH)));
            user.put(KEY_FACEBOOK_ID, cursor.getString(cursor.getColumnIndex(KEY_FACEBOOK_ID)));
            user.put("uid", cursor.getString(cursor.getColumnIndex(KEY_UID)));
            user.put("created_at", cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }

    /**
     * Getting alarms data from database
     * */
    public ArrayList<HashMap<String,String>> getAlarmsDetails(String owner){
        ArrayList<HashMap<String,String>> alarms = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM " + TABLE_ALARM + " WHERE "+ KEY_ALARM_OWNER + " LIKE '"+owner+"'" ;
        Log.i("query", selectQuery);


        SQLiteDatabase db = this.getReadableDatabase();
        try {


            Cursor cursor = db.rawQuery(selectQuery, null);
            // Move to first row
            while (cursor.moveToNext()) {
                HashMap<String, String> HM = new HashMap<String, String>();
                HM.put(KEY_ALARM_UID, cursor.getString(cursor.getColumnIndex(KEY_ALARM_UID)));
                HM.put(KEY_ALARM_ID, cursor.getString(cursor.getColumnIndex(KEY_ALARM_ID)));
                HM.put(KEY_ALARM_NAME, cursor.getString(cursor.getColumnIndex(KEY_ALARM_NAME)));
                HM.put(KEY_ALARM_OWNER, cursor.getString(cursor.getColumnIndex(KEY_ALARM_OWNER)));
                HM.put(KEY_ALARM_SETTED_TIME, cursor.getString(cursor.getColumnIndex(KEY_ALARM_SETTED_TIME)));
                HM.put(KEY_ALARM_MODE, cursor.getString(cursor.getColumnIndex(KEY_ALARM_MODE)));
                HM.put(KEY_ALARM_STATUS, cursor.getString(cursor.getColumnIndex(KEY_ALARM_STATUS)));
                HM.put(KEY_ALARM_ACTIVE, cursor.getString(cursor.getColumnIndex(KEY_ALARM_ACTIVE)));
                HM.put(KEY_ALARM_SPECIAL, cursor.getString(cursor.getColumnIndex(KEY_ALARM_SPECIAL)));
                HM.put(KEY_ALARM_LIST, cursor.getString(cursor.getColumnIndex(KEY_ALARM_LIST)));
                HM.put(KEY_ALARM_REPEAT, cursor.getString(cursor.getColumnIndex(KEY_ALARM_REPEAT)));
                HM.put(KEY_ALARM_PLAY_AFTER, cursor.getString(cursor.getColumnIndex(KEY_ALARM_PLAY_AFTER)));
                HM.put(KEY_ALARM_VOLUME, cursor.getString(cursor.getColumnIndex(KEY_ALARM_VOLUME)));
                HM.put(KEY_ALARM_RING_DEFAULT, cursor.getString(cursor.getColumnIndex(KEY_ALARM_RING_DEFAULT)));
                HM.put(KEY_ALARM_CREATED_AT, cursor.getString(cursor.getColumnIndex(KEY_ALARM_CREATED_AT)));
                HM.put(KEY_ALARM_UPDATED_AT, cursor.getString(cursor.getColumnIndex(KEY_ALARM_UPDATED_AT)));
                alarms.add(HM);
            }

            db.close();
        }catch(android.database.sqlite.SQLiteException ex){
            ex.printStackTrace();
        }

        // return user
        return alarms;
    }

    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * Re crate database
     * Delete all tables and create them again
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }


    /**
     * Storing one friend details in database
     * */
    public void addOneFriendLocal(JSONObject jo) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_FRIENDSHIP_UID, jo.getString(KEY_FRIENDSHIP_UID));
            values.put(KEY_FRIENDSHIP_ID, jo.getJSONObject("friendship").getString(KEY_FRIENDSHIP_ID));
            values.put(KEY_FRIENDSHIP_OWNER, jo.getJSONObject("friendship").getString(KEY_FRIENDSHIP_OWNER));
            values.put(KEY_FRIENDSHIP_TO, jo.getJSONObject("friendship").getString(KEY_FRIENDSHIP_TO));
            values.put(KEY_FRIENDSHIP_ACCEPTED, jo.getJSONObject("friendship").getString(KEY_FRIENDSHIP_ACCEPTED));
            values.put(KEY_FRIENDSHIP_ACTIVE, jo.getJSONObject("friendship").getString(KEY_FRIENDSHIP_ACTIVE));
            values.put(KEY_FRIENDSHIP_CREATED_AT, jo.getJSONObject("friendship").getString(KEY_FRIENDSHIP_CREATED_AT));
            values.put(KEY_FRIENDSHIP_UPDATED_AT, jo.getJSONObject("friendship").getString(KEY_FRIENDSHIP_UPDATED_AT));

            // Inserting Row
            db.insert(TABLE_FRIENDSHIP, null, values);

        }catch(android.database.sqlite.SQLiteException ex){
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        db.close(); // Closing database connection
    }

    public void setFriendAccepted(String owner, String to) {
        String setQuery = "UPDATE " + TABLE_FRIENDSHIP + " SET "+KEY_FRIENDSHIP_ACCEPTED+"=1 WHERE "+ KEY_FRIENDSHIP_OWNER + " LIKE '"+owner+"' AND "+ KEY_FRIENDSHIP_TO + " LIKE '"+to+"'" ;

        SQLiteDatabase db = this.getWritableDatabase();

        Log.i("query", setQuery);

        try {

            db.execSQL(setQuery);

        }
        catch(android.database.sqlite.SQLiteException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Storing friends details in database
     * */
    public void addFriendsLocal(JSONArray jsonFriendsArray) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {


            db.execSQL("delete from " + TABLE_FRIENDSHIP);

            for (int i=0;i<jsonFriendsArray.length();i++){
                ContentValues values = new ContentValues();
                values.put(KEY_FRIENDSHIP_UID, jsonFriendsArray.getJSONObject(i).getString(KEY_FRIENDSHIP_UID));
                values.put(KEY_FRIENDSHIP_OWNER, jsonFriendsArray.getJSONObject(i).getString(KEY_FRIENDSHIP_OWNER));
                values.put(KEY_FRIENDSHIP_TO, jsonFriendsArray.getJSONObject(i).getString(KEY_FRIENDSHIP_TO));
                values.put(KEY_FRIENDSHIP_ACCEPTED, jsonFriendsArray.getJSONObject(i).getString(KEY_FRIENDSHIP_ACCEPTED));
                values.put(KEY_FRIENDSHIP_ACTIVE, jsonFriendsArray.getJSONObject(i).getString(KEY_FRIENDSHIP_ACTIVE));
                values.put(KEY_FRIENDSHIP_CREATED_AT, jsonFriendsArray.getJSONObject(i).getString(KEY_FRIENDSHIP_CREATED_AT));
                values.put(KEY_FRIENDSHIP_UPDATED_AT, jsonFriendsArray.getJSONObject(i).getString(KEY_FRIENDSHIP_UPDATED_AT));

                // Inserting Row
                db.insert(TABLE_FRIENDSHIP, null, values);

            }}catch(android.database.sqlite.SQLiteException ex){
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        db.close(); // Closing database connection
    }

    /**
     * Getting friendship data from database
     * */
    public ArrayList<HashMap<String,String>> getFriendshipDetails(String owner){
        ArrayList<HashMap<String,String>> friendship = new ArrayList<HashMap<String, String>>();
        //String selectQuery = "SELECT  * FROM " + TABLE_FRIENDSHIP + " WHERE "+ KEY_FRIENDSHIP_OWNER + " LIKE '"+owner+"'" ;
        String selectQuery = "SELECT  * FROM " + TABLE_FRIENDSHIP + " WHERE ("+ KEY_FRIENDSHIP_OWNER + " LIKE '"+owner+"' AND "+KEY_FRIENDSHIP_ACTIVE+"=1) OR ("+KEY_FRIENDSHIP_TO+" LIKE '"+owner+"' AND "+KEY_FRIENDSHIP_ACCEPTED+"=1 AND "+KEY_FRIENDSHIP_ACTIVE+"=1)";

        Log.i("query", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            // Move to first row
            while (cursor.moveToNext()) {
                HashMap<String, String> HM = new HashMap<String, String>();
                HM.put(KEY_FRIENDSHIP_UID, cursor.getString(cursor.getColumnIndex(KEY_FRIENDSHIP_UID)));
                HM.put(KEY_FRIENDSHIP_ID, cursor.getString(cursor.getColumnIndex(KEY_FRIENDSHIP_ID)));
                HM.put(KEY_FRIENDSHIP_OWNER, cursor.getString(cursor.getColumnIndex(KEY_FRIENDSHIP_OWNER)));
                HM.put(KEY_FRIENDSHIP_TO, cursor.getString(cursor.getColumnIndex(KEY_FRIENDSHIP_TO)));
                HM.put(KEY_FRIENDSHIP_ACCEPTED, cursor.getString(cursor.getColumnIndex(KEY_FRIENDSHIP_ACCEPTED)));
                HM.put(KEY_FRIENDSHIP_ACTIVE, cursor.getString(cursor.getColumnIndex(KEY_FRIENDSHIP_ACTIVE)));
                HM.put(KEY_FRIENDSHIP_CREATED_AT, cursor.getString(cursor.getColumnIndex(KEY_FRIENDSHIP_CREATED_AT)));
                HM.put(KEY_FRIENDSHIP_UPDATED_AT, cursor.getString(cursor.getColumnIndex(KEY_FRIENDSHIP_UPDATED_AT)));
                friendship.add(HM);
            }

            db.close();
        }catch(android.database.sqlite.SQLiteException ex){
            ex.printStackTrace();
        }

        return friendship;
    }

    public void deleteFriendLocal(String owner, String to) {
        SQLiteDatabase db = this.getWritableDatabase();

        // delete friendship
        String delQuery = "DELETE FROM " + TABLE_FRIENDSHIP + " WHERE "+ KEY_FRIENDSHIP_OWNER + " LIKE '"+owner+"' AND "+ KEY_FRIENDSHIP_TO + " LIKE '"+to+"'" ;
        Log.i("owner, to ", owner+" "+to);

        try {

            db.execSQL(delQuery);

        }
        catch(android.database.sqlite.SQLiteException ex){
            ex.printStackTrace();
        }

        // delete friends detail
        delQuery = "DELETE FROM " + TABLE_FRIENDS_DETAILS + " WHERE "+ KEY_UID + " LIKE '"+to+"'" ;
        Log.i("owner, to ", owner+" "+to);

        try {

            db.execSQL(delQuery);

        }
        catch(android.database.sqlite.SQLiteException ex){
            ex.printStackTrace();
        }

        db.close(); // Closing database connection
    }

    /**
     * Storing friends details in database
     * */
    public void addFriendsDetailsLocal(JSONArray jsonFriendsDetailsArray) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            db.execSQL("delete from " + TABLE_FRIENDS_DETAILS);

            for (int i=0;i<jsonFriendsDetailsArray.length();i++){
                ContentValues values = new ContentValues();
                values.put(KEY_UID, jsonFriendsDetailsArray.getJSONObject(i).getString(KEY_UID));
                values.put(KEY_NAME, jsonFriendsDetailsArray.getJSONObject(i).getString(KEY_NAME));
                values.put(KEY_EMAIL, jsonFriendsDetailsArray.getJSONObject(i).getString(KEY_EMAIL));
                values.put(KEY_PHONE, jsonFriendsDetailsArray.getJSONObject(i).getString(KEY_PHONE));
                values.put(KEY_BIRTHDATE, jsonFriendsDetailsArray.getJSONObject(i).getString(KEY_BIRTHDATE));
                values.put(KEY_COUNTRY, jsonFriendsDetailsArray.getJSONObject(i).getString(KEY_COUNTRY));
                values.put(KEY_CITY, jsonFriendsDetailsArray.getJSONObject(i).getString(KEY_CITY));
                values.put(KEY_CREATED_AT, jsonFriendsDetailsArray.getJSONObject(i).getString(KEY_CREATED_AT));
                values.put(KEY_UPDATED_AT, jsonFriendsDetailsArray.getJSONObject(i).getString(KEY_UPDATED_AT));

                // Inserting Row
                db.insert(TABLE_FRIENDS_DETAILS, null, values);

            }}catch(android.database.sqlite.SQLiteException ex){
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        db.close(); // Closing database connection
    }

    public void addOneFriendDetailsLocal(Map<String,String> user) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_UID, user.get(KEY_UID));
            values.put(KEY_NAME, user.get(KEY_NAME));
            values.put(KEY_EMAIL, user.get(KEY_EMAIL));
            values.put(KEY_PHONE, user.get(KEY_PHONE));
            values.put(KEY_BIRTHDATE, user.get(KEY_BIRTHDATE));
            values.put(KEY_COUNTRY, user.get(KEY_COUNTRY));
            values.put(KEY_CITY, user.get(KEY_CITY));
            values.put(KEY_FACEBOOK_ID, user.get(KEY_FACEBOOK_ID));
            values.put(KEY_IMAGE_PATH, user.get(KEY_IMAGE_PATH));
            values.put(KEY_CREATED_AT, user.get(KEY_CREATED_AT));
            values.put(KEY_UPDATED_AT, user.get(KEY_UPDATED_AT));

            // Inserting Row
            db.insert(TABLE_FRIENDS_DETAILS, null, values);

        }catch(android.database.sqlite.SQLiteException ex){
            ex.printStackTrace();
        }

        db.close(); // Closing database connection
    }

    public ArrayList<HashMap<String,String>> getFriendsDetails(){
        ArrayList<HashMap<String,String>> friendsDetails = new ArrayList<HashMap<String, String>>();
        // all rows in the table are actual friends of the current user (no need of owner uid)
        String selectQuery = "SELECT * FROM " + TABLE_FRIENDS_DETAILS;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            // Move to first row
            while (cursor.moveToNext()) {
                HashMap<String, String> HM = new HashMap<String, String>();
                HM.put(KEY_UID, cursor.getString(cursor.getColumnIndex(KEY_UID)));
                HM.put(KEY_NAME, cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                HM.put(KEY_EMAIL, cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
                HM.put(KEY_PHONE, cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
                HM.put(KEY_BIRTHDATE, cursor.getString(cursor.getColumnIndex(KEY_BIRTHDATE)));
                HM.put(KEY_COUNTRY, cursor.getString(cursor.getColumnIndex(KEY_COUNTRY)));
                HM.put(KEY_CITY, cursor.getString(cursor.getColumnIndex(KEY_CITY)));
                HM.put(KEY_CREATED_AT, cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));
                HM.put(KEY_UPDATED_AT, cursor.getString(cursor.getColumnIndex(KEY_UPDATED_AT)));
                friendsDetails.add(HM);
            }

            db.close();
        }catch(android.database.sqlite.SQLiteException ex){
            ex.printStackTrace();
        }

        return friendsDetails;
    }


    public void addTaskLocal(JSONArray jsonTasks) {


        SQLiteDatabase db = this.getWritableDatabase();

        try {

            db.execSQL("delete from " + TABLE_TASKS);


            for (int i=0;i<jsonTasks.length();i++){
                ContentValues values = new ContentValues();
                values.put(KEY_TASK_UID, jsonTasks.getJSONObject(i).getString("alarm_uuid"));
                values.put(KEY_TASK_ALARM_NAME, jsonTasks.getJSONObject(i).getString("alarm_name"));
                values.put(KEY_TASK_ALARM_OWNER, jsonTasks.getJSONObject(i).getString("alarm_owner"));
                values.put(KEY_TASK_ALARM_OWNER_NAME, jsonTasks.getJSONObject(i).getString("alarm_owner_name"));
                values.put(KEY_TASK_ALARM_OWNER_FB_ID, jsonTasks.getJSONObject(i).getString("alarm_owner_fb_id"));
                values.put(KEY_TASK_ALARM_OWNER_MAIL, jsonTasks.getJSONObject(i).getString("alarm_owner_email"));
                values.put(KEY_TASK_ALARM_OWNER_PHONE, jsonTasks.getJSONObject(i).getString("alarm_owner_phone"));
                values.put(KEY_TASK_SETTED_TIME, jsonTasks.getJSONObject(i).getString("alarm_settedTime"));
                values.put(KEY_TASK_ALARM_ACTIVE, jsonTasks.getJSONObject(i).getString("alarm_active"));
                values.put(KEY_TASK_CREATED_AT, jsonTasks.getJSONObject(i).getString("created_at"));

                // Inserting Row
                db.insert(TABLE_TASKS, null, values);

            }}catch(android.database.sqlite.SQLiteException ex){
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        db.close(); // Closing database connection
    }
    public ArrayList<HashMap<String,String>> getTasksDetail(){
        ArrayList<HashMap<String,String>> tasksDetails = new ArrayList<HashMap<String, String>>();
        // all rows in the table are actual friends of the current user (no need of owner uid)
        String selectQuery = "SELECT * FROM " + TABLE_TASKS;
        Log.i("query", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            // Move to first row
            while (cursor.moveToNext()) {
                HashMap<String, String> HM = new HashMap<String, String>();
                HM.put(KEY_TASK_ID, cursor.getString(cursor.getColumnIndex(KEY_TASK_ID)));
                HM.put(KEY_TASK_UID, cursor.getString(cursor.getColumnIndex(KEY_TASK_UID)));
                HM.put(KEY_TASK_ALARM_NAME, cursor.getString(cursor.getColumnIndex(KEY_TASK_ALARM_NAME)));
                HM.put(KEY_TASK_ALARM_OWNER, cursor.getString(cursor.getColumnIndex(KEY_TASK_ALARM_OWNER)));
                HM.put(KEY_TASK_ALARM_OWNER_NAME, cursor.getString(cursor.getColumnIndex(KEY_TASK_ALARM_OWNER_NAME)));
                HM.put(KEY_TASK_ALARM_OWNER_FB_ID, cursor.getString(cursor.getColumnIndex(KEY_TASK_ALARM_OWNER_FB_ID)));
                HM.put(KEY_TASK_ALARM_OWNER_MAIL, cursor.getString(cursor.getColumnIndex(KEY_TASK_ALARM_OWNER_MAIL)));
                HM.put(KEY_TASK_ALARM_OWNER_PHONE, cursor.getString(cursor.getColumnIndex(KEY_TASK_ALARM_OWNER_PHONE)));
                HM.put(KEY_TASK_SETTED_TIME, cursor.getString(cursor.getColumnIndex(KEY_TASK_SETTED_TIME)));
                HM.put(KEY_TASK_ALARM_ACTIVE, cursor.getString(cursor.getColumnIndex(KEY_TASK_ALARM_ACTIVE)));
                HM.put(KEY_TASK_CREATED_AT, cursor.getString(cursor.getColumnIndex(KEY_TASK_CREATED_AT)));

                tasksDetails.add(HM);
            }

            db.close();
        }catch(android.database.sqlite.SQLiteException ex){
            ex.printStackTrace();
        }

        return tasksDetails;
    }

}
