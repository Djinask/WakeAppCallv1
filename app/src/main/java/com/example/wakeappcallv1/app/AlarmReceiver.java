package com.example.wakeappcallv1.app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.wakeappcallv1.app.library.DatabaseHandler;
import com.example.wakeappcallv1.app.library.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by lucamarconcini on 12/06/14.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";


    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar now = GregorianCalendar.getInstance();
        int dayOfWeek = now.get(Calendar.DATE);
        if (dayOfWeek != 1 && dayOfWeek != 7) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle("title")
                            .setContentText("TESTO");
            Intent resultIntent = new Intent(context, DashboardActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(DashboardActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, mBuilder.build());
        }

        new play(context).execute();

    }

    class play extends AsyncTask

    {
        Context context;


        play(Context c) {
            this.context=c;


        }

        @Override
        protected Object doInBackground(Object... arg0) {
            MediaPlayer mediaPlayer= new MediaPlayer();

            String url="http://wakeappcall.net63.net/uploads/2.53978e65698350.87393046-53978e65698350.87393046.mp4"; // your URL here
            Uri uri =  Uri.parse(url);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(context, uri);
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    mediaPlayer.prepare();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            mediaPlayer.start();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);


        }
    }

}
