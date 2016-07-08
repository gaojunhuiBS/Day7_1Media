package com.education.mymediaplayertest;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @InjectView(R.id.button_play)
    Button buttonPlay;
    @InjectView(R.id.button_stop)
    Button buttonStop;
    @InjectView(R.id.activity_main)
    LinearLayout activityMain;
    @InjectView(R.id.seek_bar)
    SeekBar seekBar;
    @InjectView(R.id.textview_time)
    TextView textviewTime;
    @InjectView(R.id.button_list)
    Button buttonList;
    private MediaPlayer player;
    public static final int MUSIC_DURATION = 0X324;
    public static final int MUSIC_POSITON = 0X325;

    private SimpleDateFormat format = new SimpleDateFormat("mm:ss");
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MUSIC_DURATION:
                    if (player != null) {
                        seekBar.setMax(player.getDuration() / 1000);
                        String time = format.format(new Date(player.getDuration()));
                        textviewTime.setText(time);
                    }
                    break;
                case MUSIC_POSITON:
                    if (player != null) {
                        seekBar.setProgress(player.getCurrentPosition() / 1000);
                        handler.sendEmptyMessageDelayed(MUSIC_POSITON, 1000);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
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
        //创建mediaplayer
        player = new MediaPlayer();
        //重置,第一次不需要重置
        player.reset();
        try {
            //设置播放的文件的路径
            player.setDataSource(
                    Environment
                            .getExternalStoragePublicDirectory
                                    (Environment.DIRECTORY_DOWNLOADS)
                            + File.separator
                            + "bbb.mp3");
            //设置准备完成的监听器
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    //播放
                    mp.start();
                    handler.sendEmptyMessage(MUSIC_DURATION);
                    handler.sendEmptyMessage(MUSIC_POSITON);
                }
            });
            //异步加载
            player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.button_play, R.id.button_stop, R.id.button_list})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_play:
                playMp3();
                break;
            case R.id.button_stop:
                player.stop();
                player.release();
                player = null;
                break;
            case R.id.button_list:
                startActivity(new Intent(getApplicationContext(), AudioListActivity.class));
                break;
        }
    }


}
