package com.example.wakeappcallv1.app;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


/**
 * An activity representing a list of Alarms. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link AlarmDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link AlarmListFragment} and the item details
 * (if present) is a {@link AlarmDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link AlarmListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class AlarmListActivity extends Activity
        implements AlarmListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private Button createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);

        createBtn = (Button)findViewById(R.id.footerBtn);




        createBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {






                Intent AlarmActivity = new Intent(getApplicationContext(), CreateAlarm.class);
                AlarmActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(AlarmActivity);






            }


        });


        if (findViewById(R.id.alarm_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((AlarmListFragment) getFragmentManager()
                    .findFragmentById(R.id.alarm_list))
                    .setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

    /**
     * Callback method from {@link AlarmListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(AlarmDetailFragment.ARG_ITEM_ID, id);
            AlarmDetailFragment fragment = new AlarmDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.alarm_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, AlarmDetailActivity.class);
            detailIntent.putExtra(AlarmDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, DashboardActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
