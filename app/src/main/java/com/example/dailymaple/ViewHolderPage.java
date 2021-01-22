package com.example.dailymaple;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class ViewHolderPage extends RecyclerView.ViewHolder {

    private TextView name, lv, job;
    private ImageView image;
    private LinearLayout rl_layout;
    private View __itemView;

    CharacterInfo data;

    ViewHolderPage(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.choose_character_name);
        lv = itemView.findViewById(R.id.choose_character_level);
        job = itemView.findViewById(R.id.choose_character_job);
        image = itemView.findViewById(R.id.character_image);

        __itemView = itemView;

//        rl_layout = itemView.findViewById(R.id.rl_layout);
    }

    public void onBind(CharacterInfo data){
        this.data = data;

        name.setText(data.getNickname());
//        lv.setText(data.getLevel());
//        job.setText(data.getClass());
        Glide.with(__itemView).load(data.getImgUrl()).into(image);
    }

}