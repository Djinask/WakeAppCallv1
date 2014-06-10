package com.example.wakeappcallv1.app;

/**
 * Created by Andrea on 21/05/2014.
 */
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wakeappcallv1.app.library.DatabaseHandler;
import com.example.wakeappcallv1.app.library.UserFunctions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class NotificationActivity extends Fragment {

    // values used to know what kind of notification we read from DB
    private String type_friend_request = "1";
    private String type_friend_confirmation = "2";
    private String type_alarm_request = "3";
    private String type_alarm_confirmation = "4";
    private String type_alarm_denial = "5";

    public static String[] events = {" wants to add you to his/her friends",
                                " accepted your friend request",
                                " wants you to wake him/her up for the Alarm ",
                                " has confirmed to wake you up for the Alarm ",
                                " can't wake you up for the Alarm "};

    public static String[] titles = {"Friend request", "Friend accepted", "Alarm request", "Alarm confirmed", "Alarm deny"};
    /*
    *name* wants you to wake him/her up. (confirm/reject) for the Alarm *x*
    *name* has confirmed to wake you up for Alarm *x*
    *name* can't wake you up for Alarm *x* (Choose someone else)
    *name* wants to add you to friends. (Confirm/Reject)
    *name* is your friend now
     */
    UserFunctions userFunction;

    Messenger mService = null;
    boolean mIsBound;

    Activity owner;
    static boolean active = false;

    String[] IDs;
    String[] names;
    String[] notif_ids;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        active = true;
        owner = activity;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // says to service to update
            sendMessageToService(1);
        }
        else {
        }
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

        active = true;
        userFunction = new UserFunctions();

        // starts service
        owner.startService(new Intent(owner, NotificationService.class));

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
                    IDs = msg.getData().getStringArray("id");
                    names = msg.getData().getStringArray("names");
                    notif_ids = msg.getData().getStringArray("notif_ids");
                    if(IDs != null & IDs.length > 0)
                        createGUI(notif_ids, IDs, names);
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
        outState.putStringArray("IDs", IDs);
        outState.putStringArray("names", names);
    }
    private void restoreMe(Bundle state) {
        if (state!=null) {
            // restore state
            IDs = (state.getStringArray("IDS"));
            names = (state.getStringArray("names"));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            doUnbindService();
            active = false;
        } catch (Throwable t) {
            Log.e("NotificationActivity", "Failed to unbind from the service.", t);
        }
    }

    // ---------------------- CREATE GUI FOR EACH NOTIFICATION ----------------------------------------
    public void createGUI(String[] notif_ids, String[] IDs, String[] names) {
        // linear layout inside scroll view
        // here add all notifications layouts
        LinearLayout listNotif = (LinearLayout) owner.findViewById(R.id.notifLayout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        if(listNotif==null)
            return;

        listNotif.removeAllViews(); // clear view before adding new notifications

        for(int i=0; i<IDs.length; i++) {
            Log.e("stringa", IDs[i] + "," + names[i]);
            listNotif.addView(notif(notif_ids[i], names[i], IDs[i]), params);
        }
    }

    /* creating views for each type of notification */
    public View notif(final String id, final String user_id, final String type_notif) {

        final int num_notif = Integer.parseInt(type_notif);
        final DatabaseHandler db = new DatabaseHandler(owner.getApplicationContext());

        // main layout
        final LinearLayout mLinLay = new LinearLayout(owner.getApplicationContext());
        mLinLay.setOrientation(LinearLayout.VERTICAL);

        LinearLayout layoutText = new LinearLayout(owner.getApplicationContext());
        LinearLayout layoutButton = new LinearLayout(owner.getApplicationContext());
        TextView sender = new TextView(owner.getApplicationContext());
        TextView event = new TextView(owner.getApplicationContext());
        Button ok = new Button(owner.getApplicationContext());
        Button no = new Button(owner.getApplicationContext());

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutText.setOrientation(LinearLayout.HORIZONTAL);
        layoutText.setPadding(0, 8, 0, 0);

        sender.setTextColor(Color.WHITE);
        sender.setTextSize(18);
        sender.setTypeface(Typeface.DEFAULT_BOLD);
        sender.setText(user_id);
        sender.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        event.setTextColor(Color.WHITE);
        event.setTextSize(18);
        event.setText(events[num_notif - 1]);
        event.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        layoutButton.setOrientation(LinearLayout.HORIZONTAL);

        ok.setTextColor(Color.WHITE);
        ok.setText("Accept");
        ok.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1));    // weight
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(owner.getApplicationContext(), String.valueOf(num_notif), Toast.LENGTH_SHORT).show();
                mLinLay.setVisibility(View.GONE);
                // set friendship accepted
                Log.e("CLICATO", "ENTRATO");

                switch (num_notif) {
                    case 1 :

                    new setFriendAccepted(db.getUserDetails().get("uid"), user_id).execute();


                    // add user details
                    new getFriendsDetails(db, user_id).execute();

                    // send notification
                    // current user accepted "name" request
                    new addNotification(db.getUserDetails().get("uid"), user_id, "2");
                    // remove from server
                    new setNotificationSeen(id).execute();
                    break;

                    case 3:
                        new setNotificationSeen(id).execute();


                        Intent wakeSomeOnUp = new Intent(getActivity(), WakeSomeOneActivity.class);

                        // Close all views before launching Dashboard
                        wakeSomeOnUp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(wakeSomeOnUp);
                        getActivity().finish();
                        break;




                }
            }
        });

        no.setTextColor(Color.WHITE);
        no.setText("Deny");
        no.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1));    // weight
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(owner.getApplicationContext(), String.valueOf(num_notif), Toast.LENGTH_SHORT).show();
                mLinLay.setVisibility(View.GONE);
                // set not accepted, active = 0 where friend_id = name, owner_id = id utente loggato
                // remove from server
                new setNotificationSeen(id).execute();
            }
        });

        layoutText.addView(sender);
        layoutText.addView(event);

        mLinLay.addView(layoutText, param);

        // if friend_request or alarm_request,
        // show button with OK, DENY
        if(num_notif == 1 || num_notif == 3) {
            layoutButton.addView(ok);
            layoutButton.addView(no);
            mLinLay.addView(layoutButton, param);
        }

        View v = new View(owner.getApplicationContext());
        v.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2));
        v.setBackgroundColor(Color.GRAY);

        mLinLay.addView(v);

        return mLinLay;
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

    // thread to set notifications seen
    private class addNotification extends AsyncTask {

        String id, from, to;
        addNotification(String from, String to, String id) {
            this.id = id;
            this.from = from;
            this.to = to;
        }

        @Override
        protected Object doInBackground(Object... arg0) {
            userFunction.addNotification(from, to, id);
            return null;
        }
    }

    // thread to set friendship accepted
    private class setFriendAccepted extends AsyncTask {

        String uid_from, uid_to;
        setFriendAccepted(String uid_from, String uid_to) {
            this.uid_from = uid_from;
            this.uid_to = uid_to;
        }

        @Override
        protected Object doInBackground(Object... arg0) {
            JSONObject j=userFunction.setFriendAccepted(uid_from, uid_to);

            return null;
        }
    }

    // thread to set friendship accepted
    private class getFriendsDetails extends AsyncTask {

        DatabaseHandler db;
        String uid;
        getFriendsDetails(DatabaseHandler db, String uid) {
            this.db = db;
            this.uid = uid;
        }

        @Override
        protected Object doInBackground(Object... arg0) {
            // add friends details (only when request accepted)
            JSONObject jsonSearch = userFunction.getUserDetails(uid);
            Map<String, String> user = new HashMap<String, String>();
            try


            {





                user.put("uid",jsonSearch.getString("uid"));
                user.put("name",jsonSearch.getString("name"));
                user.put("email",jsonSearch.getString("email"));
                user.put("phone",jsonSearch.getString("phone"));
                user.put("birth_date",jsonSearch.getString("birth_date"));
                user.put("country",jsonSearch.getString("country"));
                user.put("city",jsonSearch.getString("city"));
                user.put("image_path", "/data/data/com.example.wakeappcallv1.app/app_avatar_images/"+uid+".jpg");
                user.put("created_at", jsonSearch.getString("created_at"));
                user.put("updated_at", jsonSearch.getString("updated_at"));
            }
            catch (JSONException err)
            {
                Log.e("JSON error: ", err.toString());
            }
            // adds friend details
            db.addOneFriendDetailsLocal(user);


            return null;
        }
    }
}
