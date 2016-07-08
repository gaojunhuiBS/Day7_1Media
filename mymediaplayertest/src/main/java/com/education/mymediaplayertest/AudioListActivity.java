package com.education.mymediaplayertest;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AudioListActivity extends AppCompatActivity {

    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    ContentResolver mResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list);
        ButterKnife.inject(this);
        mResolver = getContentResolver();
        getData();
    }

    private void getData() {
        String[] data = new String[]{MediaStore.Audio.Media._ID
                , MediaStore.Audio.Media.DISPLAY_NAME
                , MediaStore.Audio.Media.DATA
                , MediaStore.Audio.Media.DURATION};
        Cursor audioCursor = mResolver
                .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        , data, null, null, null);
        ArrayList<MusicBean> allAudioPaths = new ArrayList<>();
        while (audioCursor.moveToNext()) {
            Log.d("", "音频的id" + audioCursor.getString(0) + "   音频名称" + audioCursor.getString(1) + "   音频绝对路径" + audioCursor.getString(2));
            allAudioPaths.add(new MusicBean(audioCursor.getString(1), audioCursor.getString(2)));
        }

    }
}
