package com.example.wakeappcallv1.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.wakeappcallv1.app.library.DatabaseHandler;
import com.example.wakeappcallv1.app.library.UserFunctions;

import org.json.JSONArray;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class RecordActivity extends Activity {
    Button b1;
    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
    private static final String AUDIO_RECORDER_FILE_EXT_AAC = ".aac";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private int serverResponseCode = 0;
    private String upLoadServerUri = null;

    private String imagepath=null;


    private MediaRecorder recorder = null;
    private int currentFormat = 0;
    private int output_formats[] = { MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.OutputFormat.THREE_GPP, MediaRecorder.OutputFormat.AAC_ADTS };
    private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP,AUDIO_RECORDER_FILE_EXT_AAC};
    Button play;
    //private PlayButton   mPlayButton = null;
    private MediaPlayer   mPlayer = null;
    private static String mFileName = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        b1=(Button)findViewById(R.id.recordbutton);
        Button send = (Button)findViewById(R.id.sendrecording);
        upLoadServerUri = "http://wakeappcall.net63.net/UploadToServer.php";

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    public void run() {

                     uploadFile(mFileName);

                    }
                }).start();
            }
        });

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
                new play(getApplicationContext()).execute();
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
        Intent intent = getIntent();

        String generatedFileName =  intent.getStringExtra("fileName");

        if(!file.exists()){
            file.mkdirs();
        }
        mFileName = file.getAbsolutePath() + "/" + generatedFileName + file_exts[currentFormat];

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



    class play extends AsyncTask

    {            AudioManager amanager;


        play(Context c) {
            this.amanager = (AudioManager) c.getSystemService(getApplicationContext().AUDIO_SERVICE);


        }

        @Override
        protected Object doInBackground(Object... arg0) {
            MediaPlayer mediaPlayer= new MediaPlayer();

            try {

                int maxVolume = amanager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
                amanager.setStreamVolume(AudioManager.STREAM_ALARM, maxVolume, 0);


                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM); // this is important.

                mediaPlayer.setDataSource(mFileName);
                Log.e("PLAY", "looking for " + mFileName);

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {
            //Bitmap photo = (Bitmap) data.getData().getPath();

            Uri selectedImageUri = data.getData();
            imagepath = getPath(selectedImageUri);

        }
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public int uploadFile(String sourceFileUri) {


        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {



            Log.e("uploadFile", "Source File not exist :"+imagepath);



            return 0;

        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){
                    runOnUiThread(new Runnable() {
                        public void run() {

                            Toast.makeText(getApplicationContext(), "File Upload Complete.", Toast.LENGTH_SHORT).show();

                            // mFileName
                            // salva path sul server ?

                        }
                    });

                    // add accepted notification (record)
                    UserFunctions userFunctions = new UserFunctions();
                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    userFunctions.addNotification(db.getUserDetails().get("uid"), getIntent().getStringExtra("owner"), String.valueOf(NotificationActivity.type_alarm_confirmation_record));
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                ex.printStackTrace();


            } catch (Exception e) {

            }
            return serverResponseCode;

        } // End else block
    }


}
