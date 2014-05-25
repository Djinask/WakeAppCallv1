package com.example.wakeappcallv1.app;

/**
 * Created by lucamarconcini on 16/05/14.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import library.DatabaseHandler;

public class HomeActivity extends Fragment {

    Activity owner;
    private Fragment me;
    private Button wakeMe ;
    private Button wakeSo;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_home, container, false);



       me= this.getTargetFragment();




        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        wakeMe = (Button)owner.findViewById(R.id.wakeMe);




        wakeMe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(owner, "WakeAppCall", Toast.LENGTH_LONG).show();




                Intent AlarmActivity = new Intent(owner, AlarmListActivity.class);
                AlarmActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(AlarmActivity);






            }


        });









    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        owner = activity;


    }


    }