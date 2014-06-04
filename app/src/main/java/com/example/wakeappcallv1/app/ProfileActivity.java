package com.example.wakeappcallv1.app;

import android.app.Activity;
import com.example.wakeappcallv1.app.Classes.MyImageButton;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wakeappcallv1.app.Classes.RoundedImageView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import com.example.wakeappcallv1.app.library.DatabaseHandler;


public class ProfileActivity extends Fragment {

    Activity owner;
    DatabaseHandler db;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_profile, container, false);











        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

      owner = activity;
        db = new DatabaseHandler(owner.getApplicationContext());



//

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final RoundedImageView iconaUtente = (RoundedImageView)owner.findViewById(R.id.icona_utente);
        final EditText userName = (EditText) owner.findViewById(R.id.profilename);
        final EditText email = (EditText) owner.findViewById(R.id.email);
        final EditText phone = (EditText) owner.findViewById(R.id.phone);
        final EditText password = (EditText) owner.findViewById(R.id.password);
        final EditText birthdate = (EditText) owner.findViewById(R.id.birthDate);
        final EditText country = (EditText) owner.findViewById(R.id.country);
        final EditText city = (EditText) owner.findViewById(R.id.city);
        final ImageView shape_button = (ImageView)owner.findViewById(R.id.shape);
        //Buttons
         final MyImageButton nameBt;

                 nameBt =(MyImageButton)owner.findViewById(R.id.nameButton);
//        final MyButtonImage mailBt = (MyButtonImage) owner.findViewById(R.id.emailButton);
//        final MyButtonImage phoneBt = (MyButtonImage) owner.findViewById(R.id.phoneButton);
//        final MyButtonImage passwordBt = (MyButtonImage) owner.findViewById(R.id.passwordButton);
//        final MyButtonImage birthdateBt = (MyButtonImage) owner.findViewById(R.id.birthDateButto);
//        final MyButtonImage countryBt = (MyButtonImage) owner.findViewById(R.id.countryButton);
//        final MyButtonImage cityBt = (MyButtonImage) owner.findViewById(R.id.cityButton);



        HashMap user= db.getUserDetails();

       userName.setText(user.get("name").toString());
        email.setText(user.get("email").toString());
        phone.setText(user.get("phone").toString());
        password.setText("**********");
        birthdate.setText(user.get("birthdate").toString());
        country.setText(user.get("country").toString());
        city.setText(user.get("city").toString());

        shape_button.setClickable(true);
        shape_button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                try {
                    PackageInfo info = owner.getPackageManager().getPackageInfo(
                            "com.example.wakeappcallv1.app",
                            PackageManager.GET_SIGNATURES);
                    for (Signature signature : info.signatures) {
                        MessageDigest md = MessageDigest.getInstance("SHA");
                        md.update(signature.toByteArray());
                        Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                    }
                } catch (PackageManager.NameNotFoundException e) {

                } catch (NoSuchAlgorithmException e) {

                }


                new AlertDialog.Builder(new ContextThemeWrapper(owner, android.R.style.Theme_Holo_Dialog))
                        .setTitle("Edit Image")
                        .setMessage("Are you sure you want to edit the profile image?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                Toast.makeText(owner, "Cambio image", Toast.LENGTH_LONG).show();






                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();


                return true;

            }
        });




        nameBt.setClickable(true);
        nameBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(owner, owner.getString(R.string.edit_profile), Toast.LENGTH_LONG).show();
                userName.setClickable(true);
                userName.setCursorVisible(true);
                userName.setFocusable(true);
                userName.setEnabled(true);
                userName.setFocusableInTouchMode(true);
                nameBt.setState(true);//active state

                nameBt.changeButtonState(nameBt, android.R.drawable.ic_menu_save);



            }


        });
        userName.setLongClickable(true);
        userName.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                Toast.makeText(owner, "Edit Profile", Toast.LENGTH_LONG).show();
                userName.setClickable(true);
                userName.setCursorVisible(true);
                userName.setFocusable(true);
                userName.setEnabled(true);
                userName.setFocusableInTouchMode(true);
                nameBt.setState(true);//active state

                nameBt.changeButtonState(nameBt, android.R.drawable.ic_menu_save);



                return false;
            }


        });

        email.setLongClickable(true);
        email.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                Toast.makeText(owner, "Edit Profile", Toast.LENGTH_LONG).show();
                email.setClickable(true);
                email.setCursorVisible(true);
                email.setFocusable(true);
                email.setEnabled(true);
                email.setFocusableInTouchMode(true);



                return false;
            }
        });

        phone.setLongClickable(true);
        phone.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                Toast.makeText(owner, "Edit Profile", Toast.LENGTH_LONG).show();
                phone.setClickable(true);
                phone.setCursorVisible(true);
                phone.setFocusable(true);
                phone.setEnabled(true);
                phone.setFocusableInTouchMode(true);




                return false;
            }
        });


        phone.setLongClickable(true);
        phone.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                Toast.makeText(owner, "Edit Profile", Toast.LENGTH_LONG).show();
                phone.setClickable(true);
                phone.setCursorVisible(true);
                phone.setFocusable(true);
                phone.setEnabled(true);
                phone.setFocusableInTouchMode(true);



                return false;
            }
        });



























    }









}

