package com.vubird.vudio;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

import javax.xml.validation.Validator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener{

    private VideoView  videoView;
    private Button myBtn, playmusic, pausemusic;
    private Timer timer;
    private MediaController mediaController;
    private MediaPlayer mediaPlayer;
    private SeekBar volumeSeekbar;
    private AudioManager audioManager;
    private SeekBar musicProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView =findViewById(R.id.myvideoview);
        myBtn = findViewById(R.id.playVideobutton);
        playmusic = findViewById(R.id.playMusicBtn);
        pausemusic = findViewById(R.id.pauseMusicBtn);
        volumeSeekbar = findViewById(R.id.seekBar);
        musicProgress = findViewById(R.id.musicProgressSeekbar);




        volumeSeekbar.setOnSeekBarChangeListener(this);
        musicProgress.setOnSeekBarChangeListener(this);


        videoView.setOnClickListener(this);
        playmusic.setOnClickListener(this);
        pausemusic.setOnClickListener(this);
        mediaController = new MediaController(this);
        mediaPlayer = MediaPlayer.create(this, R.raw.rain);

        // For volume
        audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        mediaPlayer.setOnCompletionListener(this);


        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        int currentVoumne = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);


        volumeSeekbar.setMax(maxVolume);
        // setting current volume
        volumeSeekbar.setProgress(currentVoumne);


        volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                if(b)
                {
                    Toast.makeText(MainActivity.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i,0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        musicProgress.setMax(mediaPlayer.getDuration());


    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.playMusicBtn:


                mediaPlayer.start();
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run()
                    {
                        musicProgress.setProgress(mediaPlayer.getCurrentPosition());
                        //Toast.makeText(MainActivity.this,musicProgress.getMax()., Toast.LENGTH_SHORT).show();
                    }
                }, 0,1000);
                break;
            case R.id.pauseMusicBtn:
                mediaPlayer.pause();
                timer.cancel();
                break;
            case R.id.playVideobutton:
                Uri uri = Uri.parse("android.resource://"+getPackageName() + "/"+ R.raw.snow);
                videoView.setVideoURI(uri);

                videoView.setMediaController(mediaController);
                mediaController.setAnchorView(videoView);

                videoView.start();
                break;
        }


    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        switch (seekBar.getId())
        {
            case R.id.musicProgressSeekbar:
                mediaPlayer.seekTo(i);
                break;

            case R.id.seekBar:

                Toast.makeText(MainActivity.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i,0);
                break;
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId())
        {
            case R.id.musicProgressSeekbar:
                    mediaPlayer.pause();
                break;



        }

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId())
        {
            case R.id.musicProgressSeekbar:
                mediaPlayer.start();
                break;



        }

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer)
    {
        timer.cancel();
        Toast.makeText(this,"done",Toast.LENGTH_SHORT).show();
    }
}
