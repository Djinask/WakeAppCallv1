package com.example.wakeappcallv1.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.wakeappcallv1.app.library.DatabaseHandler;
import com.example.wakeappcallv1.app.library.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrea on 21/05/2014.
 */
public class FriendsActivity extends Fragment {

    Activity owner;
    DatabaseHandler db;
    ArrayList<HashMap<String, String>> friends;
    ArrayList<HashMap<String, String>> friendships;
    Map<String,ArrayList<String>> friends_details;
    ArrayAdapter<String> arrayAdapter;

    FriendListAdapter adapter;

    DeleteFriendTask mDeleteTask;
    ProgressBar bar = null;
    String friendUid = null;

    int position;

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
        bar = (ProgressBar) owner.findViewById(R.id.deleteProgress);

        db = new DatabaseHandler(getActivity().getApplicationContext());

        // read details of friends from local DB (all row in the table are friends of current user)
        friends = db.getFriendsDetails();
        friendships = db.getFriendshipDetails(db.getUserDetails().get("uid"));

        // array with friends details, will be used with adapter
        ArrayList<String> names = new ArrayList<String>(friends.size());
        ArrayList<String> UIDs = new ArrayList<String>(friends.size());
        ArrayList<String> mail = new ArrayList<String>(friends.size());
        ArrayList<String> accepted = new ArrayList<String>(friendships.size());

        for(int i=0;i<friends.size();i++) {
            names.add(friends.get(i).get("name"));
            UIDs.add(friends.get(i).get("uid"));
            mail.add(friends.get(i).get("email"));
        }

        for(int i=0;i<friendships.size();i++) {
            accepted.add(friendships.get(i).get("friendship_accepted"));
        }

        friends_details = new HashMap<String, ArrayList<String>>();
        friends_details.put("names",names);
        friends_details.put("UIDs",UIDs);
        friends_details.put("mail",mail);
        friends_details.put("accepted",accepted);

        adapter = new FriendListAdapter(getActivity(), friends_details);

        /*arrayAdapter = new ArrayAdapter<String>(owner, R.layout.friend_list_row, R.id.friendTextView, names);
        listView.setAdapter(arrayAdapter);*/

        registerForContextMenu(listView);

        // click on the list item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Toast.makeText(getActivity().getApplicationContext(), names.get(position), Toast.LENGTH_SHORT).show();
                // finestra con i dettagli
            }
        });

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start the AddFriendsActivity activity (to add new friends)
                startActivity(new Intent(owner, AddFriendsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        listView.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        position = info.position;

        //String friendName = arrayAdapter.getItem(info.position);
        String friendName = friends_details.get("names").get(position);

        menu.setHeaderTitle(friendName);
        menu.add(0, v.getId(), 0, "Delete");
    }

    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle()=="Delete") {
            // delete selected friend
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            position = info.position;

            //String friendName = arrayAdapter.getItem(info.position);
            String friendName = friends_details.get("names").get(position);

            friendUid = friends_details.get("UIDs").get(position);

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
        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Dialog));

        alert.setTitle(title);
        alert.setMessage(message);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // start thread to delete friend
                attemptDelete();
                bar.setVisibility(View.VISIBLE);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // do nothing
            }
        });

        alert.show();
    }

    /***** FUNCTION THAT CALLS ASYNC TASK TO DELETE FRIEND *****/
    public void attemptDelete()
    {
        DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());

        final String myMail = db.getUserDetails().get("email");
        final String myUid = db.getUserDetails().get("uid");

        if (!TextUtils.isEmpty(myMail) && !TextUtils.isEmpty(myUid) && !TextUtils.isEmpty(friendUid))
        {
            mDeleteTask = new DeleteFriendTask(myMail, myUid, friendUid);
            mDeleteTask.execute((Void) null);
        }

    }

    /***** ASYNC TASK TO DELETE FRIEND *****/
    public class DeleteFriendTask extends AsyncTask<Void, Void, Boolean> {

        String mMyEmail;
        String mMyUid;
        String mFriendUid;
        JSONObject jsonDelete;

        DeleteFriendTask(String myMail, String myUid, String friendUid) {

            mMyEmail = myMail;
            mMyUid = myUid;
            mFriendUid = friendUid;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            UserFunctions userFunction = new UserFunctions();

            try
            {
                jsonDelete = userFunction.deleteFriend(mMyEmail, mMyUid, mFriendUid);

                // delete this friendship from the local DB
                DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
                db.deleteFriendLocal(mMyUid, mFriendUid);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mMyEmail = null;
            bar.setVisibility(View.GONE);

            int succ = 0;

            try
            {
                succ = jsonDelete.getInt("success");
            }
            catch (JSONException err)
            {
                Log.e("JSON error: ", err.toString());
            }

            if(succ == 1)
            {
                Toast.makeText(getActivity().getApplicationContext(), "Friend correctly deleted!", Toast.LENGTH_SHORT).show();
                // remove from adapter the deleted friend
                adapter.remove(position);
            }
            else
            {
                Toast.makeText(getActivity().getApplicationContext(), "Error deleting friend!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mMyEmail = null;
            bar.setVisibility(View.GONE);
        }
    }








}
