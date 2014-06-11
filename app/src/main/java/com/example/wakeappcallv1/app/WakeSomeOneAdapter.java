package com.example.wakeappcallv1.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wakeappcallv1.app.Classes.RoundedImageView;
import com.example.wakeappcallv1.app.library.DatabaseHandler;
import com.example.wakeappcallv1.app.library.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Luca Marconcini on 03/06/14.
 */
public class WakeSomeOneAdapter extends BaseAdapter{


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

    private Context context;
    HashMap<String,ArrayList<String>> task_details = new HashMap<String,ArrayList<String>>();
    private int position;
    public WakeSomeOneAdapter(){}

    public WakeSomeOneAdapter(Context context, HashMap<String,ArrayList<String>> tasks) {
        this.context = context;
        this.task_details = tasks;
    }

    @Override
    public int getCount() {
       return task_details.get("task_alarm_name").size();

    }

    @Override
    public Object getItem(int position) {

        return task_details.get(position);


    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        this.position = position;
        final String alarmId = task_details.get(KEY_TASK_ID).get(position);
        final String alarmName = task_details.get(KEY_TASK_ALARM_NAME).get(position);
        final String alarmOwner = task_details.get(KEY_TASK_ALARM_OWNER).get(position);
        final String alarmOwnerName = task_details.get(KEY_TASK_ALARM_OWNER_NAME).get(position);
        final String alarmOwnerFb = task_details.get(KEY_TASK_ALARM_OWNER_FB_ID).get(position);
        final String alarmOwnerMail = task_details.get(KEY_TASK_ALARM_OWNER_MAIL).get(position);
        final String alarmOwnerPhone = task_details.get(KEY_TASK_ALARM_OWNER_PHONE).get(position);
        final String alarmSettedTime = task_details.get(KEY_TASK_SETTED_TIME).get(position);
        final String alarmActive = task_details.get(KEY_TASK_ALARM_ACTIVE).get(position);
        final String alarmCreatedAt = task_details.get(KEY_TASK_CREATED_AT).get(position);
        Log.e("ALARM OWNER   ", alarmOwnerName);



        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.wake_someone_row, null);
        }

        TextView friendName = (TextView) view.findViewById(R.id.friendName);
        TextView alarmTime = (TextView) view.findViewById(R.id.time);
        RoundedImageView profilePicture = (RoundedImageView) view.findViewById(R.id.profile_pic);
        Button yes = (Button) view.findViewById(R.id.yes);
        Button no = (Button) view.findViewById(R.id.no);



        friendName.setText(alarmOwnerName);
        alarmTime.setText(alarmSettedTime);


        try {
            File f=new File("/data/data/com.example.wakeappcallv1.app/app_avatar_images/"+alarmOwner+".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            profilePicture.setImageBitmap(b);

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog dialog = new Dialog(context,android.R.style.Theme_Holo_Dialog);
                // Include dialog.xml file
                dialog.setContentView(R.layout.call_or_record);
                // Set dialog title

                dialog.setTitle("How do you want to wake me up?:");


                // set values for custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.textDialog);
                Button phone = (Button) dialog.findViewById(R.id.phone);
                final Button record = (Button) dialog.findViewById(R.id.record);
                ImageView image = (ImageView) dialog.findViewById(R.id.icona_utente);


                try {
                    File f=new File("/data/data/com.example.wakeappcallv1.app/app_avatar_images/"+alarmOwner+".jpg");
                    Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                    image.setImageBitmap(b);

                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                    phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();


                    }
                });
                record.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseHandler db = new DatabaseHandler(context);
                        String from = db.getUserDetails().get("uid");
                        String fileName = alarmId+"."+from+"-"+alarmOwner;

                        Intent Record = new Intent(context, RecordActivity.class);
                        Record.putExtra("fileName", fileName);

                        // Close all views before launching Dashboard
                        Record.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(Record);
                        dialog.dismiss();

                    }
                });

                dialog.show();


//                AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Dialog));
//
//                alert.setTitle(friends_details.get("names").get(position));
//                alert.setMessage("E-MAIL: "+friends_details.get("email").get(position)+"\n"
//                        +"BIRTHDATE: "+(birthdate.get(position)==null?"n/a":birthdate.get(position))+"\n"
//                        +"COUNTRY: "+(country.get(position)==null?"n/a":country.get(position))+"\n"
//                        +"CITY: "+(city.get(position)==null?"n/a":city.get(position)));
//
//                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                    }
//                });
//
//                alert.show();

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        return view;
    }

}