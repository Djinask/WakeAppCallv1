package com.example.wakeappcallv1.app;

/**
 * Created by lucamarconcini on 16/05/14.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.wakeappcallv1.app.library.DatabaseHandler;
import com.example.wakeappcallv1.app.library.UserFunctions;

import org.json.JSONArray;

public class HomeActivity extends Fragment {

    Activity owner;
    private Fragment me;
    private Button wakeMe ;
    private Button wakeSo;
    UserFunctions f;
    DatabaseHandler db;

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
        f= new UserFunctions();
        db= new DatabaseHandler(getActivity());


        wakeMe = (Button)owner.findViewById(R.id.wakeMe);
        wakeSo = (Button)owner.findViewById(R.id.wakeSo);




        wakeMe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent AlarmActivity = new Intent(owner, AlarmListActivity.class);
                AlarmActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(AlarmActivity);
            }
        });

        wakeSo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                new updateTask().execute();


            }
        });









    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        owner = activity;


    }

    class updateTask extends AsyncTask {


        updateTask() {

        }

        @Override
        protected Object doInBackground(Object... arg0) {
//            showProgress(true);

            JSONArray jsonTasks = f.getTasks(db.getUserDetails().get("email"), db.getUserDetails().get("uid"));
            Log.i("DA WAKESO JSON ARRAY", jsonTasks.toString());
            db.addTaskLocal(jsonTasks);

            Log.i("TASKS LOCAL :", db.getTasksDetail().toString());

            Intent WakeSomeOne = new Intent(owner, WakeSomeOneActivity.class);
            WakeSomeOne.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(WakeSomeOne);

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);


        }
    }


    }
