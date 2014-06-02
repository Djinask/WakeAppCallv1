package com.example.wakeappcallv1.app.library;

/**
 * Created by lucamarconcini on 16/05/14.
 */


import java.util.ArrayList;
import java.util.HashMap;

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

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_BIRTHDATE = "birth_date";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_CITY = "city";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_UPDATED_AT = "updated_at";


    // Alarm Table Columns names
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
    private static final String KEY_ALARM_REPEAT = "alarm_repeat";
    private static final String KEY_ALARM_PLAY_AFTER = "alarm_play_after";
    private static final String KEY_ALARM_VOLUME = "alarm_volume";
    private static final String KEY_ALARM_RING_DEFAULT = "alarm_ring_default";
    private static final String KEY_ALARM_CREATED_AT = "created_at";
    private static final String KEY_ALARM_UPDATED_AT = "updated_at";

    // Friendship Table Columns names
    private static final String KEY_FRIENDSHIP_UID = "friendship_uid";
    private static final String KEY_FRIENDSHIP_ID = "friendship_id";
    private static final String KEY_FRIENDSHIP_OWNER = "friendship_owner";
    private static final String KEY_FRIENDSHIP_TO = "friendship_to";
    private static final String KEY_FRIENDSHIP_ACCEPTED = "friendship_accepted";
    private static final String KEY_FRIENDSHIP_ACTIVE = "friendship_active";
    private static final String KEY_FRIENDSHIP_CREATED_AT = "created_at";
    private static final String KEY_FRIENDSHIP_UPDATED_AT = "updated_at";

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


        try {
            db.execSQL(CREATE_LOGIN_TABLE);
            db.execSQL(CREATE_ALARM_TABLE);
            db.execSQL(CREATE_FRIENDSHIP_TABLE);

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

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email,String phone, String birthdate, String country, String city, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_PHONE, phone); // Phone
        values.put(KEY_BIRTHDATE, birthdate); // BirthDate
        values.put(KEY_COUNTRY, country); // country
        values.put(KEY_CITY, city); // City
        values.put(KEY_UID, uid); // Unique id
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
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("phone", cursor.getString(3));
            user.put("birthdate", cursor.getString(4));
            user.put("country", cursor.getString(5));
            user.put("city", cursor.getString(6));
            user.put("uid", cursor.getString(7));
            user.put("created_at", cursor.getString(8));
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
        Log.e("owner", owner);
        Log.e("query", selectQuery);


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
     * Getting alarms data from database
     * */
    public ArrayList<HashMap<String,String>> getFriendsDetails(String owner){
        ArrayList<HashMap<String,String>> friendship = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM " + TABLE_FRIENDSHIP + " WHERE "+ KEY_FRIENDSHIP_OWNER + " LIKE '"+owner+"'" ;
        Log.e("owner", owner);
        Log.e("query", selectQuery);

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

        String delQuery = "DELETE FROM " + TABLE_FRIENDSHIP + " WHERE "+ KEY_FRIENDSHIP_OWNER + " AND "+ KEY_FRIENDSHIP_TO + " LIKE '"+to+"'" ;
        Log.e("owner, to ", owner+" "+to);
        Log.e("query", delQuery);

        try {

            Cursor cursor = db.rawQuery(delQuery, null);
            Log.e("CURSOR = ",cursor.toString());

            }
        catch(android.database.sqlite.SQLiteException ex){
            ex.printStackTrace();
        }

        db.close(); // Closing database connection
    }







}
