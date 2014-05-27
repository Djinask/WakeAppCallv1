package com.example.wakeappcallv1.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by Andrea on 21/05/2014.
 */
public class FriendsActivity extends Fragment {

    Activity owner;

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

        // example array
        String [] array = {"Antonio","Giovanni","Michele","Giuseppe", "Leonardo", "Alessandro"};
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(owner, R.layout.friend_list_row, R.id.textViewFriendName, array);
        listView.setAdapter(arrayAdapter);

        // click on the list item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // selected item
                //String selected = ((TextView) view.findViewById(R.id.textViewList)).getText().toString();
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        owner = activity;
    }
}
