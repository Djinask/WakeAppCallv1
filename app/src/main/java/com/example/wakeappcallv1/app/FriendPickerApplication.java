package com.example.wakeappcallv1.app;

import android.app.Application;
import com.facebook.model.GraphUser;

import java.util.List;

// We use a custom Application class to store our minimal state data (which users have been selected).
// A real-world application will likely require a more robust data model.
public class FriendPickerApplication extends Application {
    private List<GraphUser> selectedUsers;

    public List<GraphUser> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<GraphUser> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }
}
