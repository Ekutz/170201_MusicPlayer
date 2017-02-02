package com.jspark.android.musicplayer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity {

    ArrayList<Music> music;
    ViewPager vp;

    ImageButton btnRew, btnPlay, btnFF;
    int position = 0;
    MediaPlayer audio;
    SeekBar sbar;

    private static final int PLAY = 0;
    private static final int PAUSE = 1;
    private static final int STOP = 2;

    private static int playStatus = STOP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        vp = (ViewPager)findViewById(R.id.viewPager);
        btnRew = (ImageButton)findViewById(R.id.btnRewind);
        btnPlay = (ImageButton)findViewById(R.id.btnPlay);
        btnFF = (ImageButton)findViewById(R.id.btnForwardF);
        sbar = (SeekBar)findViewById(R.id.seekBar);

        btnRew.setOnClickListener(clickListener);
        btnPlay.setOnClickListener(clickListener);
        btnFF.setOnClickListener(clickListener);

        music = DataLoader.load(this);

        PlayerAdapter adapter = new PlayerAdapter(music, PlayActivity.this);

        vp.setAdapter(adapter);

        // 특정 페이지 호출
        Intent i = getIntent();
        if(i!=null) {
            Bundle bundle = i.getExtras();
            position = bundle.getInt("position");
            vp.setCurrentItem(position);
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnRewind :
                    preview();
                    break;
                case R.id.btnPlay:
                    play();
                    break;
                case R.id.btnForwardF:
                    next();
                    break;
            }
        }
    };

    private void play() {
        Log.w("position", String.valueOf(position));
        switch(playStatus) {
            case STOP :
                Uri uri = music.get(position).uri;
                audio = MediaPlayer.create(this, uri);
                audio.setLooping(false);
                audio.start();
                btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                playStatus=PLAY;
                break;
            case PLAY :
                audio.pause();
                playStatus = PAUSE;
                btnPlay.setImageResource(android.R.drawable.ic_media_play);
                break;
            case PAUSE :
                audio.start();
                playStatus = PLAY;
                btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                break;
        }
    }

    private void preview() {
        audio.stop();
        position -= 1;
        vp.setCurrentItem(position);
        Uri uri = music.get(position).uri;
        audio = MediaPlayer.create(this, uri);
        audio.setLooping(false);
        audio.start();
        btnPlay.setImageResource(android.R.drawable.ic_media_pause);
        playStatus=PLAY;
        Log.w("position", String.valueOf(position));
    }

    private void next() {
        audio.stop();
        position += 1;
        vp.setCurrentItem(position);
        Uri uri = music.get(position).uri;
        audio = MediaPlayer.create(this, uri);
        audio.setLooping(false);
        audio.start();
        btnPlay.setImageResource(android.R.drawable.ic_media_pause);
        playStatus=PLAY;
        Log.w("position", String.valueOf(position));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(audio!=null) {
            audio.release();
        }
        playStatus = STOP;
    }
}
