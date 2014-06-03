package com.example.wakeappcallv1.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Andrea on 03/06/14.
 */
public class FriendListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> listName;
    private int position;

    public FriendListAdapter(Context context, ArrayList<String> listName) {
        this.context = context;
        this.listName = listName;
    }

    @Override
    public int getCount() {
        return listName.size();
    }

    @Override
    public Object getItem(int position) {
        return listName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        this.position = position;
        final String name = listName.get(position);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.friend_list_row, null);
        }

        //String name = listName.get(position);
        ImageButton delFriendButton = (ImageButton) view.findViewById(R.id.deleteFriendButton);
        delFriendButton.setFocusableInTouchMode(false);
        delFriendButton.setFocusable(false);
        delFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askConfirm("Confirm deletion","Are you sure you want to delete " + name + " from your friends?");
                notifyDataSetChanged();
            }
        });

        TextView friendName = (TextView) view.findViewById(R.id.friendTextView);
        friendName.setText(name);

        return view;
    }

    public void askConfirm(String title, String message)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper((Activity)context, android.R.style.Theme_Holo_Dialog));

        alert.setTitle(title);
        alert.setMessage(message);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // start thread to delete friend
                //attemptDelete();
                //bar.setVisibility(View.VISIBLE);
                listName.remove(position);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // do nothing
            }
        });

        alert.show();
    }


}