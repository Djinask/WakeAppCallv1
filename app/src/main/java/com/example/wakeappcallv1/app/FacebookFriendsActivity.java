package com.example.wakeappcallv1.app;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;
import com.facebook.FacebookException;
import com.facebook.model.GraphUser;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.PickerFragment;

import java.util.List;

// This class provides an example of an Activity that uses FriendPickerFragment to display a list of
// the user's friends. It takes a programmatic approach to creating the FriendPickerFragment with the
// desired parameters -- see PickPlaceActivity in the PlacePickerSample project for an example of an
// Activity creating a fragment (in this case a PlacePickerFragment) via XML layout rather than
// programmatically.
public class FacebookFriendsActivity extends FragmentActivity {

    FriendPickerFragment friendPickerFragment;

    // A helper to simplify life for callers who want to populate a Bundle with the necessary
    // parameters. A more sophisticated Activity might define its own set of parameters; our needs
    // are simple, so we just populate what we want to pass to the FriendPickerFragment.
    /*public static void populateParameters(Intent intent, String userId, boolean multiSelect, boolean showTitleBar) {
        intent.putExtra(FriendPickerFragment.USER_ID_BUNDLE_KEY, userId);
        intent.putExtra(FriendPickerFragment.MULTI_SELECT_BUNDLE_KEY, multiSelect);
        intent.putExtra(FriendPickerFragment.SHOW_TITLE_BAR_BUNDLE_KEY, showTitleBar);

    }
*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pickers);

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
                                finishActivity();
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
}
