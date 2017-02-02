package com.jspark.android.musicplayer;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by jsPark on 2017. 2. 2..
 */

public class PlayerAdapter extends PagerAdapter {

    ArrayList<Music> music;
    Context context;
    LayoutInflater inflater;

    public PlayerAdapter(ArrayList<Music> music, Context context) {
        this.music = music;
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return music.size();
    }

    // 넘어온 오브젝트가 뷰가 맞는지 확인
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }


    // 리스트뷰의 getView와 같은 역할
    // view 자체를 만드는 메소드
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //return super.instantiateItem(container, position);

        View view = inflater.inflate(R.layout.player_card_item, null);

        ImageView playerImg = (ImageView)view.findViewById(R.id.playerImg);
        TextView playerTit = (TextView)view.findViewById(R.id.playerTitle);
        TextView playerArt = (TextView)view.findViewById(R.id.playerArtist);

        Music data = music.get(position);

        playerTit.setText(data.title);
        playerArt.setText(data.artist);


        Glide.with(context).load(data.album_img).placeholder(android.R.drawable.ic_dialog_alert).into(playerImg);

        container.addView(view);

        return view;
    }

    // 화면에서 사라진 뷰를 메모리에서 제거해 주는 함수
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }



}
