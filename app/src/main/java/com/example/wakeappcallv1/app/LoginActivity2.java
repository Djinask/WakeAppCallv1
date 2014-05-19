
        package com.example.wakeappcallv1.app;


        import org.json.JSONException;
        import org.json.JSONObject;


        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;

        import library.DatabaseHandler;
        import library.UserFunctions;

        public class LoginActivity2 extends Activity {
            Button btnLogin;
            Button btnLinkToRegister;
            EditText inputEmail;
            EditText inputPassword;
            TextView loginErrorMsg;

            // JSON Response node names
            private static String KEY_SUCCESS = "success";
            private static String KEY_ERROR = "error";
            private static String KEY_ERROR_MSG = "error_msg";
            private static String KEY_UID = "uid";
            private static String KEY_NAME = "name";
            private static String KEY_EMAIL = "email";
            private static String KEY_CREATED_AT = "created_at";

            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.login);

                // Importing all assets like buttons, text fields
                inputEmail = (EditText) findViewById(R.id.loginEmail);
                inputPassword = (EditText) findViewById(R.id.loginPassword);
                btnLogin = (Button) findViewById(R.id.btnLogin);
                btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
                loginErrorMsg = (TextView) findViewById(R.id.login_error);

                // Login button Click Event
                btnLogin.setOnClickListener(new View.OnClickListener() {
                    JSONObject json;
                    UserFunctions userFunction = new UserFunctions();
                    public void onClick(View view) {



                        Thread thread1 = new Thread(new Runnable(){
                            @Override
                            public void run() {
                                try {
                                    final String email = inputEmail.getText().toString();
                                    final String password = inputPassword.getText().toString();

                                    json = userFunction.loginUser(email, password);

                                    loginErrorMsg.post(
                                            new Runnable() {
                                                @Override
                                                public void run() {

                                                    //  check for login response
                                                    try {
                                                        if (json.getString(KEY_SUCCESS) != null) {
                                                            loginErrorMsg.setText("");
                                                            String res = json.getString(KEY_SUCCESS);
                                                            if(Integer.parseInt(res) == 1){
                                                                // user successfully logged in
                                                                // Store user details in SQLite Database
                                                                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                                                                JSONObject json_user = json.getJSONObject("user");

                                                                // Clear all previous data in database
                                                                userFunction.logoutUser(getApplicationContext());
                                                                db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));

                                                                // Launch Dashboard Screen
                                                                Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);

                                                                // Close all views before launching Dashboard
                                                                dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                startActivity(dashboard);

                                                                // Close Login Screen
                                                                finish();
                                                            }else{
                                                                // Error in login
                                                                loginErrorMsg.setText("Incorrect username/password");
                                                            }
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }




                                                }
                                            }
                                    );


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        thread1.start();





                    }
                });

                // Link to Register Screen
                btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View view) {
                        Intent i = new Intent(getApplicationContext(),
                                RegisterActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
            }
        }