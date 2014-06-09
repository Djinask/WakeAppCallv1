package com.example.wakeappcallv1.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wakeappcallv1.app.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.wakeappcallv1.app.library.DatabaseHandler;
import com.example.wakeappcallv1.app.library.UserFunctions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrea on 26/05/14.
 */
public class AddFriendsActivity extends Activity {

    Map<String, String> user;

    SearchFriendTask mSearchTask;
    AddFriendTask mAddTask;

    EditText mail = null;
    ProgressBar bar = null;

    String friendUid = null;

    UserFunctions userFunction;
    DatabaseHandler db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        // array with user details
        user = new HashMap<String, String>();

        Button addFb = (Button) findViewById(R.id.addFriendFromFb);
        Button addName = (Button) findViewById(R.id.addFriendByName);

        bar = (ProgressBar) findViewById(R.id.searchProgress);
        bar.setVisibility(View.INVISIBLE);

        mail = (EditText) findViewById(R.id.friendMail);

        // pressed search button on keyboard
        mail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.search || id == EditorInfo.IME_NULL) {

                    attemptSearch();
                    bar.setVisibility(View.VISIBLE);

                    // hide keyboard
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(mail.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

        // pressed search button
        addName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try
                {
                    // hide keyboard
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(mail.getWindowToken(), 0);

                    // if mail field is empty, don't start searching
                    if(mail.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Email can't be empty!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        attemptSearch();
                        bar.setVisibility(View.VISIBLE);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    public void attemptSearch()
    {
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        final String myMail = db.getUserDetails().get("email");
        final String friendMail = mail.getText().toString();

        if (!TextUtils.isEmpty(myMail) && !TextUtils.isEmpty(friendMail))
        {
            mSearchTask = new SearchFriendTask(myMail, friendMail);
            mSearchTask.execute((Void) null);
        }

    }

    // ASYNC TASK to SEARCH USER
    public class SearchFriendTask extends AsyncTask<Void, Void, Boolean> {

        String mMyEmail;
        String mFriendMail;
        JSONObject jsonSearch;
        Bitmap user_image;

        SearchFriendTask(String myMail, String friendMail) {

            mMyEmail = myMail;
            mFriendMail = friendMail;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            UserFunctions userFunction = new UserFunctions();

            try
            {
                jsonSearch = userFunction.searchFriend(mMyEmail, mFriendMail);
                Log.e("PATH DEL BITMAP", jsonSearch.getString("image_path"));

                URL fbAvatarUrl = new URL(jsonSearch.getString("image_path")+"?type=large");
                HttpGet httpRequest = new HttpGet(fbAvatarUrl.toString());
                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
                HttpEntity entity = response.getEntity();
                BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
                user_image = BitmapFactory.decodeStream(bufHttpEntity.getContent());
                httpRequest.abort();
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
            bar.setVisibility(View.INVISIBLE);

            int succ = 0;
            String name = null;
            String email = null;
            String avatar_path = null;
            BitmapDrawable icon = null;

            try
            {
                succ = jsonSearch.getInt("success");
                name = jsonSearch.getString("name");
                email = jsonSearch.getString("email");
                friendUid = jsonSearch.getString("unique_id");

                user.put("uid",friendUid);
                user.put("name",name);
                user.put("email",email);
                user.put("phone",jsonSearch.getString("phone"));
                user.put("birth_date",jsonSearch.getString("birth_date"));
                user.put("country",jsonSearch.getString("country"));
                user.put("city",jsonSearch.getString("city"));
                user.put("img_path", jsonSearch.getString("image_path"));
                user.put("created_at", jsonSearch.getString("created_at"));
                user.put("updated_at",jsonSearch.getString("updated_at"));



            }
            catch (JSONException err)
            {
                Log.e("JSON error: ", err.toString());
            }

            if(succ == 1)
            {
//                new AlertDialog.Builder(new ContextThemeWrapper(AddFriendsActivity.this, android.R.style.Theme_Holo_Dialog))
//                    .setTitle("Search result")
//
//                    .setMessage("Are you sure you want to add\n\t\t" + name + "\nto your friends?")
//                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            attemptAdd();
//                            bar.setVisibility(View.VISIBLE);
//                        }
//                    })
//                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            // do nothing
//                        }
//                    })
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .show();


                /// mio dialog per prova custom



                // Create custom dialog object
                final Dialog dialog = new Dialog(AddFriendsActivity.this);
                // Include dialog.xml file
                dialog.setContentView(R.layout.custom_dialog);
                // Set dialog title
                dialog.setTitle("Search result:");

                // set values for custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.textDialog);
                text.setText("Are you sure you want to add\n" + name + "\nto your friends?");
                ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);

                image.setImageBitmap(user_image);

                dialog.show();



            }
            else
            {
                new AlertDialog.Builder(new ContextThemeWrapper(AddFriendsActivity.this, android.R.style.Theme_Holo_Dialog))
                        .setTitle("Search result")
                        .setMessage("Not found!")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
            }
        }

        @Override
        protected void onCancelled() {
            mMyEmail = null;
            bar.setVisibility(View.INVISIBLE);
        }
    }


    //---------------------------------------------------------------------------------------------

    public void attemptAdd()
    {
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        final String myMail = db.getUserDetails().get("email");
        final String owner = db.getUserDetails().get("uid");
        // friendship_to = friendUid

        if (!TextUtils.isEmpty(myMail) && !TextUtils.isEmpty(owner) && !TextUtils.isEmpty(friendUid))
        {
            mAddTask = new AddFriendTask(myMail, owner, friendUid);
            mAddTask.execute((Void) null);
        }

    }

    // ASYNC TASK to ADD FRIENDSHIP
    public class AddFriendTask extends AsyncTask<Void, Void, Boolean> {

        String mMyEmail;
        String ownUid, toUid;
        JSONObject jsonAdd;

        AddFriendTask(String myMail, String owner, String friendUid) {

            mMyEmail = myMail;
            ownUid = owner;
            toUid = friendUid;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            userFunction = new UserFunctions();

            try
            {
                // add friendship to the online DB
                jsonAdd = userFunction.addFriend(mMyEmail, ownUid, toUid);

                // add the new friendship to the local DB
                // (it has to be confirmed)
                db = new DatabaseHandler(getApplicationContext());
                // adds friendship relationship
                db.addOneFriendLocal(jsonAdd);
                // adds friend details
                db.addOneFriendDetailsLocal(user);

                // send notification
                userFunction.addNotification(db.getUserDetails().get("uid"), user.get("uid"), "1");
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
            bar.setVisibility(View.INVISIBLE);

            int succ = 0;

            try
            {
                succ = jsonAdd.getInt("success");
            }
            catch (JSONException err)
            {
                Log.e("JSON error: ", err.toString());
            }

            if(succ == 1)
            {
                Toast.makeText(getApplicationContext(), "Friend request correctly sent!", Toast.LENGTH_LONG).show();
                startActivityForResult(new Intent(getApplicationContext(), DashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), 0);
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Unable to send request!", Toast.LENGTH_LONG).show();
                startActivityForResult(new Intent(getApplicationContext(), DashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), 0);
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mMyEmail = null;
            bar.setVisibility(View.INVISIBLE);
        }
    }


    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            input.reset();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
