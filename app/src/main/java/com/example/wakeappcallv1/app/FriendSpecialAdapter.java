package com.example.wakeappcallv1.app;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wakeappcallv1.app.library.DatabaseHandler;
import com.example.wakeappcallv1.app.library.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Luca Marconcini on 03/06/14.
 */
public class FriendSpecialAdapter extends BaseAdapter{

    private Context context;
    Map<String,ArrayList<String>> friends_details;
    private int position;

    public FriendSpecialAdapter(Context context, Map<String,ArrayList<String>> friends_details) {
        this.context = context;
        this.friends_details = friends_details;
    }

    @Override
    public int getCount() {
        return friends_details.get("names").size();
    }

    @Override
    public Object getItem(int position) {
        return friends_details.get(position);
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        this.position = position;
        final String name = friends_details.get("names").get(position);
        final String mail = friends_details.get("email").get(position);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.friend_special_row, null);
        }

        TextView friendName = (TextView) view.findViewById(R.id.friendTextView);
        friendName.setText(name);

        TextView friendMail = (TextView) view.findViewById(R.id.emailTextView);
        friendMail.setText(mail);
//
//        CheckBox accept = (CheckBox) view.findViewById(R.id.accepted);
//        accept.setChecked(friends_details.get("accepted").get(position).equals("1"));


        return view;
    }

}