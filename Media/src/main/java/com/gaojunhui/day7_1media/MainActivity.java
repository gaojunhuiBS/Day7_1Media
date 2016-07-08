package com.gaojunhui.day7_1media;

import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.gaojunhui.day7_1media.R.id.bt_OVER;
import static com.gaojunhui.day7_1media.R.id.cancel_action;

public class MainActivity extends AppCompatActivity {
    private Button button, bt_over;
    private SeekBar seekBar;
    private MediaPlayer player;
    private TextView tv_time;
    private TextView tv_progress;
    public static final int MUSIC_DURACTION = 1;
    public static final int CURRENT_POSITION = 2;
    private SimpleDateFormat format = new SimpleDateFormat("mm:ss");
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MUSIC_DURACTION:
                    if (player != null) {
                        seekBar.setMax(player.getDuration() / 1000);
                        String time = format.format(new Date(player.getDuration()));
                        tv_time.setText(time);
                    }
                    break;
                case CURRENT_POSITION:
                    if (player != null) {
                        int progress=(player.getCurrentPosition() / 1000);
                        String progress_str=format.format(new Date(player.getCurrentPosition()));
                        seekBar.setProgress(progress);
                        tv_progress.setText(progress_str);
                        handler.sendEmptyMessageDelayed(CURRENT_POSITION, 1000);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_time = (TextView) findViewById(R.id.tv_time);
        button = (Button) findViewById(R.id.bt_media);
        bt_over = (Button) findViewById(bt_OVER);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        tv_progress= (TextView) findViewById(R.id.tv_progress);
        seekBarLisener();
//        playMp3();

    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_OVER:
                player.stop();
                player.release();
                player=null;
                Log.i("tag", "onClick: over");
                break;
            case R.id.bt_media:
                playMp3();
                Log.i("tag", "onClick: start");
                break;
        }
    }
    private void seekBarLisener() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    player.seekTo(progress * 1000);
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

    private void playMp3() {
        player = new MediaPlayer();
        player.reset();
        try {
            player.setDataSource(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + File.separator + "aa.mp3");
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    handler.sendEmptyMessage(MUSIC_DURACTION);
                    handler.sendEmptyMessage(CURRENT_POSITION);
                }
            });
            player.prepareAsync();//异步加载
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
