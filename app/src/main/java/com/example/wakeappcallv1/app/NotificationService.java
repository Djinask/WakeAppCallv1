package com.example.wakeappcallv1.app;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.example.wakeappcallv1.app.library.DatabaseHandler;
import com.example.wakeappcallv1.app.library.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Andrea on 07/06/14.
 */
public class NotificationService extends Service {

    static final int register_client = 0;
    static final int unregister_client = 1;
    static final int msg_service_ui = 2;
    static final int set_alarm = 9;

    private NotificationManager nm;
    private Timer timer = new Timer();
    private static boolean isRunning = false;

    Messenger mClients;

    private String[] notif_ids;
    private String[] IDs;
    private String[] from_ids;

    UserFunctions userFunction;

    final Messenger mMessenger = new Messenger(new Handler() { // Handler of incoming messages from clients.
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case register_client:
                    Log.i("service", "register");
                    mClients = msg.replyTo;
                    break;
                case unregister_client:
                    Log.i("service", "unregister");
                    //mClients = null;
                    break;
                case msg_service_ui:
                    // when the UI is created, check if there are notifications
                    Log.i("service", String.valueOf(msg.arg1));
                    if(msg.arg1 == 1)
                        new checkNewNotifications().execute();
                    break;
                case set_alarm:// allarm code
//

                default:
                    super.handleMessage(msg);
            }
        }
    });
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("MyService", "Service Created.");

        int delay = 10000;  // ms
        // here the functions to repeat cyclically
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    // check if there are new notifications
                  //  new checkNewNotifications().execute();

                } catch (Throwable t) {
                    Log.e("TimerTick", "Timer Tick Failed.", t);
                }
            }
        }, 0, delay);

        isRunning = true;
    }

    // thread to check new notifications
    private class checkNewNotifications extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0) {

            userFunction = new UserFunctions();
            final DatabaseHandler db = new DatabaseHandler(getApplicationContext());

            // --- notification on the Android system bar



            JSONArray jsonNotif = userFunction.getNotificationUnseen(db.getUserDetails().get("uid"));


            if(!jsonNotif.isNull(0)){
                IDs = new String[jsonNotif.length()];
                from_ids = new String[jsonNotif.length()];
                notif_ids = new String[jsonNotif.length()];

                for(int i=0; i<jsonNotif.length(); i++)
                {
                    try {
                        notif_ids[i] = jsonNotif.getJSONObject(i).getString("id");
                        IDs[i] = jsonNotif.getJSONObject(i).getString("id_n");
                        from_ids[i] = jsonNotif.getJSONObject(i).getString("from_id");

                        // send Android notification
                        showNotification(notif_ids[i], IDs[i], from_ids[i]);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }



            // --- notification on the app UI


                // reset arrays
                IDs = null;
                from_ids = null;
                notif_ids = null;

                jsonNotif = userFunction.getNotificationActive(db.getUserDetails().get("uid"));

            if (!jsonNotif.isNull(0)) {
                    IDs = new String[jsonNotif.length()];
                    from_ids = new String[jsonNotif.length()];
                    notif_ids = new String[jsonNotif.length()];

                    for (int i = 0; i < jsonNotif.length(); i++) {
                        try {
                            notif_ids[i] = jsonNotif.getJSONObject(i).getString("id");
                            IDs[i] = jsonNotif.getJSONObject(i).getString("id_n");
                            from_ids[i] = jsonNotif.getJSONObject(i).getString("from_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    // send data to UI (if active)
                    if(NotificationActivity.active) {
                        sendMessageToUI();
                    }
                } else {

                    return null;
                }


            return null;
        }

    }

    private void showNotification(String notif_id, String id, String from) {
        nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // activity to be opened when clicked on notification
        Intent dashIntent = new Intent(this, DashboardActivity.class);
        dashIntent.putExtra("fromNotification", true);

        int intid = Integer.parseInt(id);
        int intnotif = Integer.parseInt(notif_id);

        String name = "n/a";
        try {
            name = userFunction.getUserDetails(from).get("name").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.logo_md, name + NotificationActivity.events[intid-1], System.currentTimeMillis());
        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, dashIntent, 0);
        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, NotificationActivity.titles[intid-1], name + NotificationActivity.events[intid-1], contentIntent);

        // Hide the notification after its selected
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        // LED lights to notification
        notification.defaults |= Notification.DEFAULT_LIGHTS;

        // Send the notification to the Android bar
        nm.notify(intnotif, notification);

        // set notification seen, so it will not be show again
        new setNotificationSeen(notif_id).execute();
    }

    // ---------------------- SEND MESSAGE FROM SERVICE TO UI ----------------------------------------
    private void sendMessageToUI() {
        try {
            //Send data as a String
            Bundle b = new Bundle();
            b.putStringArray("id", IDs);
            b.putStringArray("names", from_ids);
            b.putStringArray("notif_ids", notif_ids);

            Message msg = Message.obtain(null, msg_service_ui);
            msg.setData(b);

            mClients.send(msg);

        } catch (RemoteException e) {
            // The client is dead. Remove it from the list; we are going through the list from back to front so this is safe to do inside the loop.
            //mClients = null;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("MyService", "Received start id " + startId + ": " + intent);
        //Toast.makeText(this,"Service Started "+ startId, Toast.LENGTH_LONG).show();
        return START_STICKY; // run until explicitly stopped.
    }

    public static boolean isRunning()
    {
        return isRunning;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        //nm.cancel(R.string.service_started); // Cancel the persistent notification.
        Log.i("MyService", "Service Stopped.");
        Toast.makeText(this,"Service Stopped ", Toast.LENGTH_LONG).show();
        isRunning = false;
    }



    // thread to set notifications seen
    private class setNotificationSeen extends AsyncTask {

        String id;
        setNotificationSeen(String id) {
            this.id = id;
        }

        @Override
        protected Object doInBackground(Object... arg0) {
            userFunction.setNotificationSeen(id);
            return null;
        }
    }
}
