package com.example.wakeappcallv1.app;

/**
 * Created by lucamarconcini on 16/05/14.
 */



import org.json.JSONException;
import org.json.JSONObject;


import library.DatabaseHandler;
import library.UserFunctions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



public class RegisterActivity extends Activity {
    Button btnRegister;
    //Button btnLinkToLogin;
    EditText inputFullName;
    EditText inputEmail;
    EditText inputPhone;
    EditText inputPassword;
    EditText inputBirthDate;
    EditText inputCountry;
    EditText inputCity;

    TextView registerErrorMsg;

    // JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_BIRTHDATE = "birth_date";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_CITY = "city";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);

        // Importing all assets like buttons, text fields
        inputFullName = (EditText) findViewById(R.id.registerName);
        inputEmail = (EditText) findViewById(R.id.registerEmail);
        inputPhone = (EditText) findViewById(R.id.registerPhone);
        inputPassword = (EditText) findViewById(R.id.registerPassword);
        inputBirthDate =  (EditText) findViewById(R.id.registerBirthDate);
        inputCountry = (EditText) findViewById(R.id.registerCountry);
        inputCity =  (EditText) findViewById(R.id.registerCity);

        btnRegister = (Button) findViewById(R.id.button);
        //btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        registerErrorMsg = (TextView) findViewById(R.id.register_error);


        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
             JSONObject json = new JSONObject();
             UserFunctions userFunction = new UserFunctions();
            public void onClick(View view) {


                Thread thread1 = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            final String email = inputEmail.getText().toString();
                            final String password = inputPassword.getText().toString();
                            final String name = inputFullName.getText().toString();
                            final String phone = inputPhone.getText().toString();
                            final String birthDate = inputBirthDate.getText().toString();
                            final String country = inputCountry.getText().toString();
                            final String city = inputCity.getText().toString();


                            json = userFunction.registerUser(name, email, password, phone, birthDate, country, city);

                            registerErrorMsg.post(
                                    new Runnable() {
                                        @Override
                                        public void run() {

                                            //  check for login response
                                            try {
                                                if (json.getString(KEY_SUCCESS) != null) {
                                                    registerErrorMsg.setText("");
                                                    String res = json.getString(KEY_SUCCESS);
                                                    if(Integer.parseInt(res) == 1){
                                                        // user successfully registred
                                                        // Store user details in SQLite Database
                                                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                                                        JSONObject json_user = json.getJSONObject("user");

                                                        // Clear all previous data in database
                                                        userFunction.logoutUser(getApplicationContext());


                                                        db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL),json_user.getString(KEY_PHONE),json_user.getString(KEY_BIRTHDATE),json_user.getString(KEY_COUNTRY),json_user.getString(KEY_CITY) ,json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));
                                                        // Launch Dashboard Screen
                                                        Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);
                                                        // Close all views before launching Dashboard
                                                        dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(dashboard);
                                                        // Close Registration Screen
                                                        finish();
                                                    }else{
                                                        // Error in registration
                                                        registerErrorMsg.setText("Error occured in registration");
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }




                                        }
                                    }
                            );


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread1.start();





            }
        });


    }
}