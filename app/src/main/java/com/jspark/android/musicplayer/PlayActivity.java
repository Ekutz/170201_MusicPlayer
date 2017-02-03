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
import android.widget.TextView;

import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity {

    ArrayList<Music> music;
    ViewPager vp;

    ImageButton btnRew, btnPlay, btnFF;
    int position = 0;
    MediaPlayer audio;
    SeekBar sbar;
    TextView duration, time;

    private static final int PLAY = 0;
    private static final int PAUSE = 1;
    private static final int STOP = 2;

    private static int playStatus = STOP;

    private class OneSec extends Thread {

        @Override
        public void run() {
            while (playStatus < STOP) {
                if(audio!=null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                sbar.setProgress(audio.getCurrentPosition());
                                time.setText(String.format("%02d", audio.getCurrentPosition() / 1000 / 60) + ":" + String.format("%02d", (audio.getCurrentPosition() / 1000 - ((int) audio.getCurrentPosition() / 1000 / 60) * 60)));
                            } catch(Exception e) {

                            }
                        }
                    });
                }
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e) {

                }
            }
        }
    }



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
        duration = (TextView)findViewById(R.id.txtDu);
        time = (TextView)findViewById(R.id.spendTime);

        btnRew.setOnClickListener(clickListener);
        btnPlay.setOnClickListener(clickListener);
        btnFF.setOnClickListener(clickListener);

        music = DataLoader.load(this);

        PlayerAdapter adapter = new PlayerAdapter(music, PlayActivity.this);

        vp.setAdapter(adapter);

        vp.addOnPageChangeListener(pageListener);

        sbar.setOnSeekBarChangeListener(sbarListener);

        // 특정 페이지 호출
        Intent i = getIntent();
        if(i!=null) {
            Bundle bundle = i.getExtras();
            position = bundle.getInt("position");
            vp.setCurrentItem(position);

            if(position==0) {
                init();
                duration.setText(String.format("%02d", music.get(position).length / 1000 / 60) + ":" + String.format("%02d", (music.get(position).length / 1000 - ((int) music.get(position).length / 1000 / 60) * 60)));
                //time.setText(String.format("%02d",audio.getCurrentPosition()/1000/60)+":"+String.format("%02d",(audio.getCurrentPosition()/1000-((int)audio.getCurrentPosition()/1000/60)*60)));
                time.setText("00:00");
                play();
            }
        }
    }

    SeekBar.OnSeekBarChangeListener sbarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if(audio!=null&&b==true)
            audio.seekTo(i);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            PlayActivity.this.position = position;
            if(audio!=null) {
                audio.release();
            }

            playStatus=STOP;
            init();
            play();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };



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
        switch(playStatus) {
            case STOP :
                audio.start();
                btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                playStatus=PLAY;

                OneSec one = new OneSec();
                one.start();

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
        if(position>0)position -= 1;
        vp.setCurrentItem(position);
    }

    private void next() {
        if(position<music.size())position += 1;
        vp.setCurrentItem(position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(audio!=null) {
            audio.release();
        }
        playStatus = STOP;
    }

    private void init() {
        if(audio!=null) {
            audio.release(); // 뷰페이저로 이동할 경우 미디어 해제하고 실행한다
        }
        Uri uri = music.get(position).uri;
        audio = MediaPlayer.create(this, uri);
        sbar.setMax(audio.getDuration());
        duration.setText(String.format("%02d", audio.getDuration()/1000/60)+":"+String.format("%02d", (audio.getDuration()/1000-((int)audio.getDuration()/1000/60)*60)));
        audio.setLooping(false);

        Log.w("status", String.valueOf(playStatus));
    }


}
