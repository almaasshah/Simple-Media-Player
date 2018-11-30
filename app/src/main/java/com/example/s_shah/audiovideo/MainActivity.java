package com.example.s_shah.audiovideo;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {
    //Ui Components

    private VideoView myVideoView;
    private Button play_btn;
    private MediaController mediaController;
    private Button playmusicbtn;
    private Button stopmusicbtn;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private SeekBar seekBarTime;
    private AudioManager audioManager;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myVideoView = findViewById(R.id.myVideoView);
        play_btn = findViewById(R.id.play_btn);
        playmusicbtn = findViewById(R.id.playmusicbtn);
        stopmusicbtn = findViewById(R.id.stopmusicbtn);
        seekBar = findViewById(R.id.seekBar);
        seekBarTime = findViewById(R.id.seekBarTime);

       play_btn.setOnClickListener(MainActivity.this);
       playmusicbtn.setOnClickListener(MainActivity.this);
       stopmusicbtn.setOnClickListener(MainActivity.this);


       mediaController = new MediaController(MainActivity.this);
       mediaPlayer = MediaPlayer.create(this,R.raw.heartbeat);
       audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

       int maximumVolumeOfUserDevice = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
       int currentVolumeOfUserDevice = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

       seekBar.setMax(maximumVolumeOfUserDevice);
       seekBar.setProgress(currentVolumeOfUserDevice);

       mediaPlayer.setOnCompletionListener(this);

       seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

               if (fromUser) {
                   //Toast.makeText(MainActivity.this, Integer.toString(progress), Toast.LENGTH_SHORT).show();
                   audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0 );

               }

           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {

           }

       });

       seekBarTime.setOnSeekBarChangeListener(this);
       seekBarTime.setMax(mediaPlayer.getDuration());
    }

    @Override
    public void onClick(View buttonView) {

        switch (buttonView.getId()){
            case R.id.play_btn:
                Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.phlexlabs);
                myVideoView.setVideoURI(videoUri);
                myVideoView.setMediaController(mediaController);
                mediaController.setAnchorView(myVideoView);
                myVideoView.start();
                break;
            case R.id.playmusicbtn:
                mediaPlayer.start();
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        seekBarTime.setProgress(mediaPlayer.getCurrentPosition());
                    }
                }, 0, 1000);


                break;
            case R.id.stopmusicbtn:
                mediaPlayer.pause();
                break;
        }
    }
    @Override
    public void onCompletion(MediaPlayer mp) {
        timer.cancel();

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser){
            //Toast.makeText(this, Integer.toString(progress), Toast.LENGTH_SHORT).show();
            mediaPlayer.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mediaPlayer.pause();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mediaPlayer.start();
    }
}
