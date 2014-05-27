package com.example.wakeappcallv1.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Andrea on 26/05/14.
 */
public class AddFriendsActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        Button addFb = (Button) findViewById(R.id.addFriendFromFb);
        Button addName = (Button) findViewById(R.id.addFriendByName);

        addName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
