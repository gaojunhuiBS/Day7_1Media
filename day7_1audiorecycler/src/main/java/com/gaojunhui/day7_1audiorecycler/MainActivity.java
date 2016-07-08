package com.gaojunhui.day7_1audiorecycler;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.gaojunhui.adapter.MyRecylerAdapter;
import com.gaojunhui.bean.MusicBean;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.seekBar)
    SeekBar seekBar;
    @InjectView(R.id.tv_time)
    TextView tvTime;
    @InjectView(R.id.tv_tv_progress)
    TextView tvTvProgress;
    @InjectView(R.id.tv_name_bottom)
    TextView tvNameBottom;


    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.bt_back)
    ImageView btBack;
    @InjectView(R.id.bt_go)
    ImageView btGo;
    @InjectView(R.id.bt_stop)
    ImageView btStop;

    private ArrayList<MusicBean> list;
    private MyRecylerAdapter adapter;
    private MediaPlayer player = new MediaPlayer();
    public static final int MUSIC_DURATION = 1;
    public static final int MUSIC_POSITION = 2;
    private int currentPosition = 0;
    private boolean isStop;
    ContentResolver resolver;
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
                        tvTime.setText(time);
                        break;
                    }
                case MUSIC_POSITION:
                    if (player != null) {
                        seekBar.setProgress(player.getCurrentPosition() / 1000);
                        String currentTime = format.format(new Date(player.getCurrentPosition()));
                        tvTvProgress.setText(currentTime);
                        handler.sendEmptyMessageDelayed(MUSIC_POSITION, 1000);
                        break;

                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        btStop.setImageResource(R.drawable.bofang);
        resolver = getContentResolver();
        getData();
        //适配
        setAdapter();
        adapterLisener();
        seekBarLis();
    }

    /**
     * 适配
     */
    private void setAdapter() {
        adapter = new MyRecylerAdapter(list, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 获得数据源
     */
    public void getData() {
        String[] data = new String[]{MediaStore.Audio.Media._ID
                , MediaStore.Audio.Media.DISPLAY_NAME
                , MediaStore.Audio.Media.DATA
                , MediaStore.Audio.Media.DURATION
        };
        Cursor audioCursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                , data, null, null, null);
        list = new ArrayList<>();
        while (audioCursor.moveToNext()) {
            String musicName = audioCursor.getString(1);
            String path = audioCursor.getString(2);
            MusicBean musicBean = new MusicBean(musicName, path);
            list.add(musicBean);
        }
        Log.i("tag", "getData: " + list.size());
        tvNameBottom.setText(list.get(0).getMusicName());   //默认显示
    }

    /**
     * 监听Item事件
     */
    private void adapterLisener() {
        adapter.setLisener(new MyRecylerAdapter.OnItemLisener() {
            @Override
            public void onMusicNameClick(View view, int position) {

                if (player.isPlaying()) {
                    btStop.setImageResource(R.drawable.zanting);
                    player.stop();//先暂停
                    player.release();//释放资源
                    currentPosition = position;//再播放
                    playMp3(list.get(currentPosition).getPath());
                    tvNameBottom.setText(list.get(currentPosition).getMusicName());
                } else {
                    btStop.setImageResource(R.drawable.zanting);
                    currentPosition = position;
                    playMp3(list.get(currentPosition).getPath());
                    tvNameBottom.setText(list.get(currentPosition).getMusicName());
                }
            }

            @Override
            public void onPathClick(View view, int position) {

            }
        });
    }

    /**
     * seekBar监听
     */
    public void seekBarLis() {
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

    /**
     * 播放音乐
     */
    public void playMp3(String path) {
        player = new MediaPlayer();
        player.reset();
        try {
            player.setDataSource(path);
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    handler.sendEmptyMessage(MUSIC_DURATION);
                    handler.sendEmptyMessage(MUSIC_POSITION);
                }
            });
            player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @OnClick({R.id.bt_back, R.id.bt_go, R.id.bt_stop})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                if (player.isPlaying()) {
                    player.pause();
                    player.release();
                    if (currentPosition != 0) {
                        currentPosition -= 1;
                        playMp3(list.get(currentPosition).getPath());
                        tvNameBottom.setText(list.get(currentPosition).getMusicName());
                    } else if (currentPosition == 0) {
                        currentPosition = list.size() - 1;
                        playMp3(list.get(currentPosition).getPath());
                        tvNameBottom.setText(list.get(list.size() - 1).getMusicName());
                    }
                } else if (!player.isPlaying() && player != null) {
                    playMp3(list.get(currentPosition).getPath());
                    tvNameBottom.setText(list.get(currentPosition).getMusicName());
                }
                break;

            case R.id.bt_go:

                if (player.isPlaying()) {
                    player.pause();
                    player.release();
                    if (currentPosition != list.size() - 1) {
                        currentPosition += 1;
                        playMp3(list.get(currentPosition).getPath());
                        tvNameBottom.setText(list.get(currentPosition).getMusicName());
                    } else {
                        currentPosition = 0;
                        playMp3(list.get(currentPosition).getPath());
                        tvNameBottom.setText(list.get(currentPosition).getMusicName());
                    }
                } else if (!player.isPlaying()) {
                    if (currentPosition != list.size() - 1) {
                        currentPosition += 1;
                        playMp3(list.get(currentPosition).getPath());
                        tvNameBottom.setText(list.get(currentPosition).getMusicName());
                    } else {

                        playMp3(list.get(currentPosition).getPath());
                        tvNameBottom.setText(list.get(currentPosition).getMusicName());
                    }
                } else if (player == null) {
                    currentPosition = 0;
                    playMp3(list.get(currentPosition).getPath());
                    tvNameBottom.setText(list.get(currentPosition).getMusicName());
                }
                break;
            case R.id.bt_stop:
                if (player.isPlaying() && player != null) {
                    player.pause();
                    btStop.setImageResource(R.drawable.bofang);
                }
                else {
                    player.start();
                    btStop.setImageResource(R.drawable.zanting);
                }
                break;
        }
    }



}
