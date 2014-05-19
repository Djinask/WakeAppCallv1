package com.example.wakeappcallv1.app;

/**
 * Created by lucamarconcini on 16/05/14.
 */


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import library.UserFunctions;

public class DashboardActivity extends Activity {
    UserFunctions userFunctions;
    Button btnLogout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Dashboard Screen for the application
         * */
        // Check login status in database
        userFunctions = new UserFunctions();
        if(userFunctions.isUserLoggedIn(getApplicationContext())){
            // user already logged in show databoard
            setContentView(R.layout.activity_home);


        }else{
            // user is not logged in show login screen
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            // Closing dashboard screen
            finish();
        }
    }



    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        int base=Menu.FIRST;
        MenuItem item1=menu.add(base,1,1,"Log out");
        MenuItem item2=menu.add(base,2,2,"Prova");
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==1){
            userFunctions.logoutUser(getApplicationContext());
                    Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);
                    // Closing dashboard screen
                    finish();
        }
        else if (item.getItemId()==2){}
        else return super.onOptionsItemSelected(item);

        return true;
    }



}