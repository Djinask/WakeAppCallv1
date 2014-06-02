package com.example.wakeappcallv1.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wakeappcallv1.app.R;
import com.example.wakeappcallv1.app.library.DatabaseHandler;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Andrea on 21/05/2014.
 */
public class FriendsActivity extends Fragment {

    Activity owner;
    DatabaseHandler db;
    ArrayList<HashMap<String, String>> friends;
    ArrayList<String> names;
    ArrayAdapter<String> arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_friends, container, false);
        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final ListView listView = (ListView) owner.findViewById (R.id.friendlistView);
        final Button addFriend = (Button) owner.findViewById(R.id.addFriendButton);

        db = new DatabaseHandler(getActivity().getApplicationContext());

        // read friendship from local DB, given the owner uid
        friends = db.getFriendsDetails(db.getUserDetails().get("uid"));
        // array with friendship, will be used with adapter
        names = new ArrayList<String>(friends.size());

        for(int i=0;i<friends.size();i++) {
            names.add(friends.get(i).get("friendship_to"));
        }

        arrayAdapter = new ArrayAdapter<String>(owner, R.layout.friend_list_row, R.id.friendTextView, names);
        listView.setAdapter(arrayAdapter);
        registerForContextMenu(listView);

        // click on the list item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // selected item
            }
        });

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start the AddFriendsActivity activity (to add new friends)
                startActivity(new Intent(owner, AddFriendsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String friendName = arrayAdapter.getItem(info.position);

        menu.setHeaderTitle(friendName);
        menu.add(0, v.getId(), 0, "Delete");
    }

    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle()=="Delete") {
            // delete selected friend
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            String friendName = arrayAdapter.getItem(info.position);

            askConfirm("Confirm deletion","Are you sure you want to delete "+friendName+" from your friends?");
        }
        else {
            return false;
        }
        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        owner = activity;
    }


    public void askConfirm(String title, String message)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setTitle(title);
        alert.setMessage(message);

        // Set an EditText view to get user input
        final EditText input = new EditText(getActivity());
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // start thread to delete friend
                Toast.makeText(getActivity().getApplicationContext(), "IL TUO AMICO VERRÀ CANCELLATO!", Toast.LENGTH_SHORT).show();
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
