package com.jspark.android.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by jsPark on 2017. 2. 1..
 */

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.Holder> {

    ArrayList<Music> data;
    Context context;
    Intent i = null;


    public MusicAdapter(ArrayList<Music> data, Context context) {
        this.data = data;
        this.context = context;
        i = new Intent(context, PlayActivity.class);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final Music music = this.data.get(position);

        int dur_min = music.length/1000/60;
        int dur_sec = music.length/1000 - (dur_min)*60;

        // Bitmap으로 넣어주기
        //holder.img.setImageBitmap(music.album_art);

        // Glide를 활용하여 이미지 삽입해주기
        Glide.with(context).load(music.album_img).placeholder(android.R.drawable.ic_dialog_alert).into(holder.img);
        //글라이드.활용(context).load(uri).into(위젯);

        holder.id.setText(music.id);
        holder.album_id.setText(""+music.album_id);
        holder.title.setText(music.title);
        holder.artist.setText(music.artist);
        holder.length.setText("0"+dur_min+":"+dur_sec);
        holder.position = position;

        Animation anime = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        holder.card.setAnimation(anime);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView id, album_id, title, artist, length;
        CardView card;

        int position;

        public Holder(View itemView) {
            super(itemView);

            img = (ImageView)itemView.findViewById(R.id.imgView);
            id = (TextView)itemView.findViewById(R.id.txtId);
            album_id = (TextView)itemView.findViewById(R.id.txtAlbumId);
            title = (TextView)itemView.findViewById(R.id.txtTitle);
            artist = (TextView)itemView.findViewById(R.id.txtArtist);
            length = (TextView)itemView.findViewById(R.id.txtLength);
            card = (CardView)itemView.findViewById(R.id.cardView);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    i.putExtra("position", position);
                    context.startActivity(i);
                }
            });

        }
    }
}
