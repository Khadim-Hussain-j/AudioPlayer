package com.khadim.audioplayer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SongAdapter adapter;
     MediaPlayer mediaPlayer;
    private ListView lst;
    private ContentResolver cr;
    private Button play;
    private SeekBar seekBar;
    private TextView tvprog,tvdur;
    Handler handler = new Handler();
    Cursor cursor;
    boolean check=false;
    int duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvprog = findViewById(R.id.tvprogress);
        tvdur = findViewById(R.id.tvduration);
        lst= findViewById(R.id.lv);
        seekBar = findViewById(R.id.sb);


        cr = getContentResolver();
        mediaPlayer =new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        play= findViewById(R.id.pause_play);

        cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.Media.DISPLAY_NAME);
        adapter = new SongAdapter(cursor);
        lst.setAdapter(adapter);

        View.OnClickListener clickListener =new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.backskip:
                        if(!mediaPlayer.isPlaying()){
                            play.setBackgroundResource(R.drawable.ic_pause);
                        }
                        if(cursor.moveToPrevious()){
                        setSong(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                    }
                        break;
                    case R.id.pause_play:
                            if(mediaPlayer.isPlaying()){
                                mediaPlayer.pause();
                                play.setBackgroundResource(R.drawable.ic_play);

                            }else if(check){
                                mediaPlayer.start();
                                play.setBackgroundResource(R.drawable.ic_pause);
                            }
                        break;
                    case R.id.nextskip:
                        if(!mediaPlayer.isPlaying()){
                            play.setBackgroundResource(R.drawable.ic_pause);
                        }
                        if(cursor.moveToNext()){
                            setSong(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                        }
                        break;
                }
            }
        };
        findViewById(R.id.backskip).setOnClickListener(clickListener);
        play.setOnClickListener(clickListener);
        findViewById(R.id.nextskip).setOnClickListener(clickListener);

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    play.setBackgroundResource(R.drawable.ic_pause);
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                    }

                cursor.moveToPosition(position);
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
//                int duration= Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));

                setSong(path);
            }
        });


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                seekBar.setProgress(mediaPlayer.getCurrentPosition());


                if(seekBar.getProgress()==mediaPlayer.getDuration()){
                    mediaPlayer.stop();
                    play.setBackgroundResource(R.drawable.ic_play);
                    check=false;
                }
                duration =mediaPlayer.getCurrentPosition();
                String sec = String.valueOf((int) (duration/1000)%60);
                String minutes = String.valueOf((int) ((duration/(1000*60))%60));
                tvprog.setText(minutes+":"+sec);
                handler.postDelayed(this,1);
            }

        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setSong(String path){
        try {

            mediaPlayer.stop();
//            mediaPlayer.release();
            mediaPlayer.reset();
//                mediaPlayer=null;
//                mediaPlayer=new MediaPlayer();

            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    mediaPlayer.start();
                }
            });

        } catch (IOException e) {
            Log.e("Errorr",e.getMessage());
        }
        duration =mediaPlayer.getDuration();
        String sec = String.valueOf((int) (duration/1000)%60);
        String minutes = String.valueOf((int) ((duration/(1000*60))%60));
//        int hour = (int) ((duration/1000*60*60))%24;
        tvdur.setText(minutes+":"+sec);
        seekBar.setMax(duration);
        check=true;

    }

}