package com.example.wakeappcallv1.app;

/**
 * Created by Andrea on 21/05/2014.
 */
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import com.example.wakeappcallv1.app.library.UserFunctions;
import com.facebook.Session;

public class DashboardActivity extends FragmentActivity implements ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private Button wakeMe;
    private Fragment fragment;

    UserFunctions userFunctions;


    @Override
    public void onBackPressed() {
        //NOT GO BACK FROM DASHBOARD

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check login status in database
        userFunctions = new UserFunctions();
        wakeMe = (Button)findViewById(R.id.wakeMe);

        // user already logged in show databoard
        setContentView(R.layout.activity_dashboard);

        if (!userFunctions.isUserLoggedIn(getApplicationContext())) {
            // user is not logged in show login screen
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            // Closing dashboard screen
            finish();
        }

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.home).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.notify).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.profile).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.friends).setTabListener(this));

        // if the Dashboard is called by the AddFriendsActivity,
        // it sets the Friends tab
        try {
            if(getCallingActivity().getClassName().equals("com.example.wakeappcallv1.app.AddFriendsActivity"))
            {
                actionBar.selectTab(actionBar.getTabAt(3));
            }
        }catch(NullPointerException e){}

        // if the Dashboard is called by the notification
        // it sets the Notification tab
        if (getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            if(b.getBoolean("fromNotification")){
                actionBar.selectTab(actionBar.getTabAt(1));
            }
        }

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        int base=Menu.FIRST;
        MenuItem item1=menu.add(base,1,1,"Log out");


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_custom, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==1){
            userFunctions.logoutUser(getApplicationContext());
            /**
             * Logout From Facebook
             */


                Session session = Session.getActiveSession();

                if (session != null) {

                    if (!session.isClosed()) {
                        session.closeAndClearTokenInformation();
                        //clear your preferences if saved
                    }
                } else {

                    session = new Session(getApplicationContext());
                    Session.setActiveSession(session);

                    session.closeAndClearTokenInformation();
                    //clear your preferences if saved

                }
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            // Closing dashboard screen
            finish();
        }
        else if (item.getItemId()==2){

        }
        else return super.onOptionsItemSelected(item);

        return true;
    }

    public class TabsPagerAdapter extends FragmentPagerAdapter {

        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {

            switch (index) {
                case 0:
                    return new HomeActivity();
                case 1:
                    return new NotificationActivity();
                case 2:
                    return new ProfileActivity();
                case 3:
                    return new FriendsActivity();
            }

            return null;
        }

        @Override
        public int getCount() {
            // get item count - equal to number of tabs
            return 4;
        }
    }

}





