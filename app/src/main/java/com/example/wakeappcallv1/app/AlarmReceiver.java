package com.example.wakeappcallv1.app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
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
    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
    private static final String AUDIO_RECORDER_FILE_EXT_AAC = ".aac";    private int output_formats[] = { MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.OutputFormat.THREE_GPP, MediaRecorder.OutputFormat.AAC_ADTS };
    private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP,AUDIO_RECORDER_FILE_EXT_AAC};


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
                   AudioManager amanager;

            Context context;


        play(Context c) {
            this.amanager = (AudioManager) c.getSystemService(context.AUDIO_SERVICE);
            this.context=c;


        }

        @Override
        protected Object doInBackground(Object... arg0) {
            MediaPlayer mediaPlayer= new MediaPlayer();



            try {

                int maxVolume = amanager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
                amanager.setStreamVolume(AudioManager.STREAM_ALARM, maxVolume, 0);
                String url="http://wakeappcall.net63.net/uploads/2.53978e65698350.87393046-53978e65698350.87393046.mp4"; // your URL here
                Uri uri =  Uri.parse(url);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM); // this is important.

                mediaPlayer.setDataSource(context,uri);
                Log.e("PLAY", "looking for " + uri);

                mediaPlayer.prepare();

                mediaPlayer.start();
            } catch (IOException e) {
                Log.e("PLAY", "prepare() failed");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);


        }
    }

}
