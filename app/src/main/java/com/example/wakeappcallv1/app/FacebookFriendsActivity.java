package com.example.wakeappcallv1.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wakeappcallv1.app.library.DatabaseHandler;
import com.example.wakeappcallv1.app.library.UserFunctions;
import com.facebook.FacebookException;
import com.facebook.model.GraphUser;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.PickerFragment;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class FacebookFriendsActivity extends FragmentActivity {

    DatabaseHandler db;

    SearchFriendTask mSearchTask;
    AddFriendTask mAddTask;

    UserFunctions userFunction;
    ProgressBar bar = null;
    ArrayList<String> friendUids = null;

    String friendUid = null;



    FriendPickerFragment friendPickerFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_friends   );
        bar = (ProgressBar) this.findViewById(R.id.add_friends_progress);

            Bundle args = getIntent().getExtras();
            FragmentManager manager = getSupportFragmentManager();
            FriendPickerFragment fragmentToShow = null;
            Uri intentUri = getIntent().getData();

                if (savedInstanceState == null) {
                    friendPickerFragment = new FriendPickerFragment(args);
                } else {
                    friendPickerFragment =
                            (FriendPickerFragment) manager.findFragmentById(R.id.picker_fragment);
                }
                // Set the listener to handle errors
                friendPickerFragment.setOnErrorListener(new PickerFragment.OnErrorListener() {
                    @Override
                    public void onError(PickerFragment<?> fragment,
                                        FacebookException error) {
                        FacebookFriendsActivity.this.onError(error);
                    }
                });
                // Set the listener to handle button clicks
                friendPickerFragment.setOnDoneButtonClickedListener(
                        new PickerFragment.OnDoneButtonClickedListener() {
                            @Override
                            public void onDoneButtonClicked(PickerFragment<?> fragment) {
                                List<GraphUser> users = friendPickerFragment.getSelection();
                                if (users.size() > 0) {

                                    ArrayList<String> userids = new ArrayList<String>(users.size());
                                    for (GraphUser user:users){
                                        String fn = user.getId();
                                        attemptSearch(fn+"");
                                        Log.e("FB","Id of user  "+ user.getName()+ " " + fn);

                                    }


                                    finish();
                                } else {
                                    setResult(RESULT_CANCELED);
                                    finish();
                                }
                            }
                        });
                fragmentToShow = friendPickerFragment;



            manager.beginTransaction()
                    .replace(R.id.picker_fragment, fragmentToShow)
                    .commit();
        }

    private void onError(Exception error) {
        onError(error.getLocalizedMessage(), false);
    }

    private void onError(String error, final boolean finishActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error_dialog_title).
                setMessage(error).
                setPositiveButton(R.string.error_dialog_button_text,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (finishActivity) {
                                    finishActivity();
                                }
                            }
                        });
        builder.show();
    }

    private void finishActivity() {
        setResult(RESULT_OK, null);
        //add friends logic
        finish();
    }
    @Override
    protected void onStart() {
        super.onStart();

            try {
                friendPickerFragment.loadData(false);
            } catch (Exception ex) {
                onError(ex);
            }

    }
    public void attemptSearch(String fbid)
    {
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        final String myMail = db.getUserDetails().get("email");

        if (!TextUtils.isEmpty(myMail) && !TextUtils.isEmpty(fbid))
        {
            mSearchTask = new SearchFriendTask(myMail, fbid);
            mSearchTask.execute((Void) null);
        }

    }

    public class SearchFriendTask extends AsyncTask<Void, Void, Boolean> {

        String mMyEmail;
        String mFbId;
        JSONObject jsonSearch;
        Bitmap user_image;

        SearchFriendTask(String myMail, String fbid) {

            mMyEmail = myMail;
            mFbId = fbid;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            UserFunctions userFunction = new UserFunctions();

            try
            {
                jsonSearch = userFunction.lookupFB(mFbId);


                URL fbAvatarUrl = new URL(jsonSearch.getString("image_path")+"?type=large");
                HttpGet httpRequest = new HttpGet(fbAvatarUrl.toString());
                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httpRequest);
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

                /*user.put("uid",friendUid);
                user.put("name",name);
                user.put("email",email);
                user.put("phone",jsonSearch.getString("phone"));
                user.put("birth_date",jsonSearch.getString("birth_date"));
                user.put("country",jsonSearch.getString("country"));
                user.put("city",jsonSearch.getString("city"));
                user.put("img_path", jsonSearch.getString("image_path"));
                user.put("created_at", jsonSearch.getString("created_at"));
                user.put("updated_at",jsonSearch.getString("updated_at"));*/



            }
            catch (JSONException err)
            {
                Log.e("JSON error: ", err.toString());
            }

            if(succ == 1)
            {



            }
            else
            {
                new AlertDialog.Builder(new ContextThemeWrapper(FacebookFriendsActivity.this, android.R.style.Theme_Holo_Dialog))
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



    public class AddFriendTask extends AsyncTask<Void, Void, Boolean> {

        String mMyEmail;
        String ownUid, toUid;
        JSONObject jsonAdd;
        Bitmap friend_avatar;

        AddFriendTask(String myMail, String owner, String friendUid, Bitmap user_image) {

            mMyEmail = myMail;
            ownUid = owner;
            toUid = friendUid;
            friend_avatar=user_image;

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

                // send notification
                userFunction.addNotification(db.getUserDetails().get("uid"), friendUid, "1");
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
                //save avatar in the internal storage device
                final String fileName = friendUid;

                saveToInternalSorage(friend_avatar,fileName);


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

    private String saveToInternalSorage(Bitmap bitmapImage, String fileName){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("avatar_images", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,fileName+".jpg");

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            Log.e("FILE CREATED", mypath.toString());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }

}
