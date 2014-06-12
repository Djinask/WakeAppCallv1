package com.example.wakeappcallv1.app;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Arrays;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import com.example.wakeappcallv1.app.library.DatabaseHandler;
import com.example.wakeappcallv1.app.library.UserFunctions;
import com.facebook.*;
import com.facebook.model.*;
import com.facebook.widget.LoginButton;


/**
 * A login screen that offers login via email/password.

 */

public class LoginActivity extends Activity implements LoaderCallbacks<Cursor> {




    //Facebook login management
    private UiLifecycleHelper uiHelper;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private UserLoginTaskFB FbAuthTask = null;
    private GraphUser utente;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView loginErrorMsg;
    // Json Keywords

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_BIRTHDATE = "birth_date";
    private static final String KEY_IMAGE_PATH = "img_path";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_CITY = "city";
    private static final String KEY_UID = "uid";
    private static final String KEY_FACEBOOK_ID = "facebook_id";
    private static final String KEY_CREATED_AT = "created_at";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //FB login
        LoginButton fbbutton = (LoginButton) findViewById(R.id.facebook);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        loginErrorMsg = (TextView) findViewById(R.id.login_error);
        mPasswordView = (EditText) findViewById(R.id.password);
        populateAutoComplete();


        fbbutton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends"));

        fbbutton.setOnErrorListener(new LoginButton.OnErrorListener() {

            @Override
            public void onError(FacebookException error) {
                Log.i("DEBUG", "Error " + error.getMessage());
            }
        });

        // session state call back event
        fbbutton.setSessionStatusCallback(new Session.StatusCallback() {

            @Override
            public void call(Session session, SessionState state, Exception exception) {

                if (session.isOpened()) {
                    Log.i("DEBUG", "Access Token" + session.getAccessToken());
                    Request.executeMeRequestAsync(session,
                            new Request.GraphUserCallback() {
                                @Override
                                public void onCompleted(GraphUser user, Response response) {
                                    if (user != null) {
                                        Log.i("DEBUG", "User ID " + user.getId());
                                        Log.i("DEBUG", "Email " + user.asMap().get("email"));
//                                        lblEmail.setText(user.asMap().get("email").toString());
                                        attemptLoginFb(user);


                                    }
                                }
                            }
                    );
                }

            }
        });


        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        Button register = (Button) findViewById(R.id.register);

        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(getApplicationContext(), RegisterActivity.class);
                register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(register);

                // Close Login Screen
                finish();
            }
        });
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    //FB login
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }


    private void populateAutoComplete() {
        if (VERSION.SDK_INT >= 14) {
            // Use ContactsContract.Profile (API 14+)
            getLoaderManager().initLoader(0, null, this);
        } else if (VERSION.SDK_INT >= 8) {
            // Use AccountManager (API 8+)
            new SetupEmailAutoCompleteTask().execute(null, null);
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);


            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);

        }
    }

    public boolean attemptLoginFb(GraphUser u) {
        if (mAuthTask != null) {
            return true;
        }


        GraphUser user = u;


        showProgress(true);


        FbAuthTask = new UserLoginTaskFB(user);
        FbAuthTask.execute((Void) null);
        return true;


    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */

    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Use an AsyncTask to fetch the user's email addresses on a background thread, and update
     * the email text field with results on the main UI thread.
     */
    class SetupEmailAutoCompleteTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... voids) {
            ArrayList<String> emailAddressCollection = new ArrayList<String>();

            // Get all emails from the user's contacts and copy them to a list.
            ContentResolver cr = getContentResolver();
            Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                    null, null, null);
            while (emailCur.moveToNext()) {
                String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract
                        .CommonDataKinds.Email.DATA));
                emailAddressCollection.add(email);
            }
            emailCur.close();

            return emailAddressCollection;
        }

        @Override
        protected void onPostExecute(List<String> emailAddressCollection) {
            addEmailsToAutoComplete(emailAddressCollection);
        }
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }
     public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        String mEmail;
        String mPassword;

        @Override
        public boolean equals(Object o) {
            return super.equals(o);
        }

        UserLoginTask(String email, String password) {

            mEmail = email;
            mPassword = password;
        }

        JSONObject json;
        JSONArray jsonAlarms;
        JSONArray jsonFriends;
        JSONArray jsonFriendsDetails;
         JSONArray jsonTasks;

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            final UserFunctions userFunction = new UserFunctions();


            try {

                json = userFunction.loginUser(mEmail, mPassword);


                //  check for login response
                Log.e("JSON", json.getString(KEY_SUCCESS));
                if (json.getString(KEY_SUCCESS) != null) {


                    String res = json.getString(KEY_SUCCESS);
                    if (Integer.parseInt(res) == 1) {
                        try {


                            jsonAlarms = userFunction.getAlarms(mEmail, json.getString("uid"));

                            jsonFriends = userFunction.getFriends(mEmail, json.getString("uid"));

                            jsonFriendsDetails = userFunction.getFriendsDetails(mEmail, json.getString("uid"));

                            jsonTasks = userFunction.getTasks(mEmail, json.getString("uid"));
                            Log.i("TASK DAL LOGIN", jsonTasks.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.e("SUCCESS:", res);
                        // user successfully logged in
                        // Store user details in SQLite Database
                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        JSONObject json_user = json.getJSONObject("user");

                        // Clear all previous data in database
                        userFunction.logoutUser(getApplicationContext());
                        db.addUser(json_user.getString(KEY_NAME),
                                json_user.getString(KEY_EMAIL),
                                json_user.getString(KEY_PHONE),
                                json_user.getString(KEY_BIRTHDATE),
                                json_user.getString(KEY_COUNTRY),
                                json_user.getString(KEY_CITY),
                                json_user.getString(KEY_IMAGE_PATH),
                                json.getString(KEY_UID),
                                json_user.getString(KEY_FACEBOOK_ID),
                                json_user.getString(KEY_CREATED_AT));

                        db.addAlarmLocal(jsonAlarms);
                        db.addFriendsLocal(jsonFriends);
                        db.addFriendsDetailsLocal(jsonFriendsDetails);
                        db.addTaskLocal(jsonTasks);

                        // Launch Dashboard Screen
                        Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);

                        // Close all views before launching Dashboard
                        dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(dashboard);

                        // Close Login Screen
                        //finish();
                    } else {
                        // Error in login
                        Log.e("FAIL:", res);
                        //Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            Log.e("onPost", "onpost");
            Log.e("SUCCESS", success.toString());

            if (success) {
                Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);

                // Close all views before launching Dashboard
                dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(dashboard);

                // Close Login Screen
                finish();
            } else {
           try {


                Toast.makeText(getApplicationContext(), json.getString(KEY_ERROR_MSG), Toast.LENGTH_LONG).show();
           } catch (JSONException e) {
                e.printStackTrace();
            }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


    public class UserLoginTaskFB extends AsyncTask<Void, Void, Boolean> {
        String mEmail;
        String mId;
        JSONObject json;
        JSONArray jsonAlarms;
        JSONArray jsonFriends;
        JSONArray jsonFriendsDetails;
        JSONArray jsonTasks;
        UserFunctions userFunction;

        @Override
        public boolean equals(Object o) {
            return super.equals(o);
        }

        UserLoginTaskFB(GraphUser user) {
            userFunction = new UserFunctions();
            utente = user;
            mEmail = utente.asMap().get("email").toString();
            mId = utente.getId();

        }



        @Override
        protected Boolean doInBackground(Void... params) {

            try {


                json = userFunction.checkUser_if_exist(mEmail);
                Log.w("LOG W", "LOG W");
                Log.wtf("LOG WTF", "LOG WTF");

                //  check for login response
                Boolean Exists = Boolean.parseBoolean(json.getString(KEY_SUCCESS))?false:true;
                Log.i("USER EXIXSTS?", Exists.toString());
                if (json.getString(KEY_SUCCESS) != null) {


                    String res = json.getString(KEY_SUCCESS);
                    if (Integer.parseInt(res) == 1) {

                        json = userFunction.login_fb(mEmail);


                        try {


                            jsonAlarms = userFunction.getAlarms(mEmail, json.getString("uid"));

                            jsonFriends = userFunction.getFriends(mEmail, json.getString("uid"));

                            jsonFriendsDetails = userFunction.getFriendsDetails(mEmail, json.getString("uid"));
                            jsonTasks = userFunction.getTasks(mEmail, json.getString("uid"));
                            Log.i("TASK DAL LOGIN", jsonTasks.toString());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.e("SUCCESS:", res);
                        // user successfully logged in
                        // Store user details in SQLite Database
                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        JSONObject json_user = json.getJSONObject("user");




                        Bitmap user_image=null;

                        URL fbAvatarUrl = new URL("http://graph.facebook.com/" + utente.getId() + "/picture?type=large");
                        HttpGet httpRequest = new HttpGet(fbAvatarUrl.toString());
                        DefaultHttpClient httpclient = new DefaultHttpClient();
                        HttpResponse response = httpclient.execute(httpRequest);
                        HttpEntity entity = response.getEntity();
                        BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
                        user_image = BitmapFactory.decodeStream(bufHttpEntity.getContent());
                        httpRequest.abort();

                        String path =saveToInternalSorage(user_image,json.getString("uid"));
                        Log.e("path dell'utente ", path);


                        // Clear all previous data in database
                        userFunction.logoutUser(getApplicationContext());
                        db.addUser(json_user.getString(KEY_NAME),
                                json_user.getString(KEY_EMAIL),
                                json_user.getString(KEY_PHONE),
                                json_user.getString(KEY_BIRTHDATE),
                                json_user.getString(KEY_COUNTRY),
                                json_user.getString(KEY_CITY),
                                json_user.getString(KEY_IMAGE_PATH),
                                json.getString(KEY_UID),
                                json_user.getString(KEY_FACEBOOK_ID),
                                json_user.getString(KEY_CREATED_AT));

                        db.addAlarmLocal(jsonAlarms);
                        db.addFriendsLocal(jsonFriends);
                        db.addFriendsDetailsLocal(jsonFriendsDetails);
                        db.addTaskLocal(jsonTasks);

                        // Launch Dashboard Screen
                        Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);

                        // Close all views before launching Dashboard
                        dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(dashboard);

//                    Close Login Screen
                        finish();
                    } else {
                        // Usero Does Not Exist from FACEBOOK


                        JSONObject jsonImage;

                        json = userFunction.registerUser(
                                utente.getName(),                       //User Name
                                utente.asMap().get("email").toString(), //User Email
                                utente.getId(),                         //User Id
                                null,                                 //User Phone
                                null,                                     //User BirthDay
                                null,                                     //User Country
                                null,                                     //User City
                                utente.getId(),                                     //User Facebook Id
                                "http://graph.facebook.com/" + utente.getId() + "/picture"  //User avatar path
                        );
                         Log.i("REGISTERED:", json.getString(KEY_SUCCESS));

                        if (json.getString(KEY_SUCCESS).equals("1")) {
                            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                            JSONObject json_user = json.getJSONObject("user");
                            // After registration add user on local db

                            Bitmap user_image=null;

                            URL fbAvatarUrl = new URL("http://graph.facebook.com/" + utente.getId() + "/picture?type=large");
                            HttpGet httpRequest = new HttpGet(fbAvatarUrl.toString());
                            DefaultHttpClient httpclient = new DefaultHttpClient();
                            HttpResponse response = httpclient.execute(httpRequest);
                            HttpEntity entity = response.getEntity();
                            BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
                            user_image = BitmapFactory.decodeStream(bufHttpEntity.getContent());
                            httpRequest.abort();

                            String path =saveToInternalSorage(user_image,json.getString("uid"));
                            Log.e("path dell'utente ", path);

                            // Salvo l'avatar e lo carico nel db
                            db.addUser(json_user.getString(KEY_NAME),
                                    json_user.getString(KEY_EMAIL),
                                    json_user.getString(KEY_PHONE),
                                    json_user.getString(KEY_BIRTHDATE),
                                    json_user.getString(KEY_COUNTRY),
                                    json_user.getString(KEY_CITY),
                                    path+"/"+json.getString("uid")+".jpg",
                                    json.getString(KEY_UID),
                                    json_user.getString(KEY_FACEBOOK_ID),
                                    json_user.getString(KEY_CREATED_AT));
                            Log.e("REGISTERED:", json.toString());
                            // Launch Dashboard Screen
                            Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);
                            // Close all views before launching Dashboard
                            dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(dashboard);
                           finish();


                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            FbAuthTask = null;


        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private String saveToInternalSorage(Bitmap bitmapImage, String fileName){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("users", Context.MODE_PRIVATE);
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






