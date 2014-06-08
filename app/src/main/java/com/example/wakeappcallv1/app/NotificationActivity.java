package com.example.wakeappcallv1.app;

/**
 * Created by Andrea on 21/05/2014.
 */
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wakeappcallv1.app.R;

public class NotificationActivity extends Fragment {

    // values used to know what kind of notification we read from DB
    private int type_friend_request = 1;
    private int type_friend_confirmation = 2;
    private int type_alarm_request = 3;
    private int type_alarm_confirmation = 4;
    private int type_alarm_denial = 5;

    private String FRIEND_REQUEST_TITLE = "Friend Request";
    private String FRIEND_CONFIRM_TITLE = "Friend Confirmation";
    private String ALARM_REQUEST_TITLE = "Alarm Request";
    private String ALARM_CONFIRM_TITLE = "Alarm Confirmation";
    private String ALARM_DENIAL_TITLE = "Alarm Denial";

    private String[] events = {" wants to add you to its friends",
                                " is your friend now",
                                " wants you to wake him/her up",
                                " has confirmed to wake you up for Alarm ",
                                " can't wake you up for Alarm "};

    /*private String FRIEND_REQUEST = " wants to add you to its friends";
    private String FRIEND_CONFIRM = " is your friend now";
    private String ALARM_REQUEST = " wants you to wake him/her up";
    private String ALARM_CONFIRM = " has confirmed to wake you up for Alarm ";
    private String ALARM_DENIAL = " can't wake you up for Alarm ";*/

    /*
    *name* wants you to wake him/her up. (confirm/reject) - for the
    *name* has confirmed to wake you up for Alarm *x*
    *name* can't wake you up for Alarm *x* (Choose someone else)
    *name* wants to add you to friends. (Confirm/Reject)
    you and *name* are now friends
     */

    Messenger mService = null;
    boolean mIsBound;

    Activity owner;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        owner = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_notifications, container, false);
        return rootView;
    }

    // ---------------------- ACTIVITY CREATED ----------------------------------------
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // starts service
        owner.startService(new Intent(owner, NotificationService.class));

        // linear layout inside scroll view
        // here add all notifications layouts
        LinearLayout listNotif = (LinearLayout) owner.findViewById(R.id.notifLayout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        //listNotif.addView(view, params);

        Button b = (Button) owner.findViewById(R.id.stopserv);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                owner.stopService(new Intent(owner, NotificationService.class));
            }
        });

        restoreMe(savedInstanceState);

        CheckIfServiceIsRunning();

        // bind the service
        doBindService();
    }

    // ---------------------- MESSENGER ----------------------------------------
    final Messenger mMessenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NotificationService.msg_service_ui:
                    Log.e("activity", "msg_service_ui");
                    String str = msg.getData().getString("str");
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    });

    // ---------------------- SERVICE CONNECTION ----------------------------------------
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            Log.e("STATUS:", "Attached.");
            try {
                Message msg = Message.obtain(null, NotificationService.register_client);
                msg.replyTo = mMessenger;
                mService.send(msg);
            } catch (RemoteException e) {
                // In this case the service has crashed before we could even do anything with it
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been unexpectedly disconnected - process crashed.
            mService = null;
            Log.e("STATUS:", "Disconnected.");
        }
    };

    // ---------------------- BINDING/UNBINDING ----------------------------------------
    void doBindService() {
        owner.bindService(new Intent(owner, NotificationService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
        Log.e("STATUS:", "Binding.");
    }
    void doUnbindService() {
        if (mIsBound) {
            // If we have received the service, and hence registered with it, then now is the time to unregister.
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null, NotificationService.unregister_client);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                } catch (RemoteException e) {
                    // There is nothing special we need to do if the service has crashed.
                }
            }
            // Detach our existing connection.
            owner.unbindService(mConnection);
            mIsBound = false;
            Log.e("STATUS:", "Unbinding.");
        }
    }

    // ---------------------- SEND MESSAGE FROM UI TO SERVICE ----------------------------------------
    private void sendMessageToService(int valueToSend) {
        if (mIsBound) {
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null, NotificationService.msg_service_ui, valueToSend, 0);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                } catch (RemoteException e) {
                }
            }
        }
    }

    private void CheckIfServiceIsRunning() {
        //If the service is running when the activity starts, we want to automatically bind to it.
        if (NotificationService.isRunning()) {
            doBindService();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save state
        //outState.putString("name", value);
    }
    private void restoreMe(Bundle state) {
        if (state!=null) {
            // restore state
            //value = (state.getString("name"));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            doUnbindService();
        } catch (Throwable t) {
            Log.e("NotificationActivity", "Failed to unbind from the service.", t);
        }
    }

    /* creating views for each type of notification */
    public View notif_friendRequest() {
        return null;
    }
    public View notif_friendConfirm() {
        return null;
    }
    public View notif_alarmRequest() {
        return null;
    }
    public View notif_alarmConfirm() {
        return null;
    }
    public View notif_alarmDenial() {
        return null;
    }
}
