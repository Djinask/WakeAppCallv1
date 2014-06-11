package com.example.wakeappcallv1.app;

import android.app.Activity;
import android.app.AlertDialog;
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
import java.util.Map;

/**
 * Created by Andrea on 03/06/14.
 */
public class FriendListAdapter extends BaseAdapter {

    private Context context;
    Map<String,ArrayList<String>> friends_details;
    private int position;

    public FriendListAdapter(Context context, Map<String,ArrayList<String>> friends_details) {
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

    public void remove(int position) {
        friends_details.get("names").remove(position);
        friends_details.get("email").remove(position);
        friends_details.get("UIDs").remove(position);
        friends_details.get("accepted").remove(position);
        friends_details.get("birth_date").remove(position);
        friends_details.get("country").remove(position);
        friends_details.get("city").remove(position);
        notifyDataSetChanged();
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
        final String uid = friends_details.get("UIDs").get(position);
        final String accepted = friends_details.get("accepted").get(position);

        Log.e("************AMICO",name+","+mail+","+uid+"->"+accepted);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.friend_list_row, null);
        }

        TextView friendName = (TextView) view.findViewById(R.id.friendTextView);
        friendName.setText(name);

        TextView friendMail = (TextView) view.findViewById(R.id.emailTextView);
        friendMail.setText(mail);

        ImageView image = (ImageView) view.findViewById(R.id.accepted);
        ImageView blur_img = (ImageView) view.findViewById(R.id.blur);
        if (accepted.equals("1")) {
            image.setVisibility(View.VISIBLE);
            blur_img.setVisibility(View.INVISIBLE);
        }
        else {
            image.setVisibility(View.INVISIBLE);
            blur_img.setVisibility(View.VISIBLE);
        }



        try {
            File f=new File("/data/data/com.example.wakeappcallv1.app/app_avatar_images/"+uid+".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            RoundedImageView avatar = (RoundedImageView) view.findViewById(R.id.profile_pic);
            avatar.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }





        return view;
    }


}