package com.example.wakeappcallv1.app;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.File;
import java.io.IOException;


public class RecordActivity extends Activity {
    Button b1;
    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private MediaRecorder recorder = null;
    private int currentFormat = 0;
    private int output_formats[] = { MediaRecorder.OutputFormat.MPEG_4,             MediaRecorder.OutputFormat.THREE_GPP };
    private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP };
    Button play;
    //private PlayButton   mPlayButton = null;
    private MediaPlayer   mPlayer = null;
    private static String mFileName = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        b1=(Button)findViewById(R.id.recordbutton);

        b1.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // TODO Auto-generated method stub
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:

                        b1.setBackgroundResource(R.drawable.mike_red);
                        startRecording();

                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e("REC"," Stop recording");
                        b1.setBackgroundResource(R.drawable.mike_grey);
                        //log stop recording
                        stopRecording();
                        break;
                }
                return false;
            }
        });

        play = (Button) findViewById(R.id.playrecording);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlaying();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getFilename(){
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath,AUDIO_RECORDER_FOLDER);

        if(!file.exists()){
            file.mkdirs();
        }
        mFileName = file.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat];

    }
    private void startRecording(){
        getFilename();

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(output_formats[currentFormat]);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(mFileName);
        recorder.setOnErrorListener(errorListener);
        recorder.setOnInfoListener(infoListener);
        Log.e("REC"," I start recording now at " + mFileName);

        try {
            recorder.prepare();
            recorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private MediaRecorder.OnErrorListener errorListener = new        MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            //TODO log warning
        }
    };

    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
        //TODO log warning
        }
    };
    private void stopRecording() {
        if (null != recorder) {
            recorder.stop();
            recorder.reset();
            recorder.release();

            recorder = null;
        }

    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            Log.e("PLAY", "looking for " + mFileName);
            mPlayer.prepare();

            mPlayer.start();
        } catch (IOException e) {
            Log.e("PLAY", "prepare() failed");
        }
    }



}
