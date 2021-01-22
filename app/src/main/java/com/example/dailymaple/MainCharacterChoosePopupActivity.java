package com.example.dailymaple;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class MainCharacterChoosePopupActivity extends Activity {

    ArrayList<CharacterInfo> characterInfos;
    ViewPager2 viewPager2;
    private int mainCharacterIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE); // 타이틀 제거
        setContentView(R.layout.activity_main_character_choose_popup);

        viewPager2 = findViewById(R.id.pager);

        // 캐릭터 정보 받아오기
        Intent intent = getIntent();
        characterInfos = (ArrayList<CharacterInfo>) intent.getSerializableExtra("Characters");

        viewPager2.setAdapter(new ViewPagerAdapter(characterInfos));
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mainCharacterIndex = position;
                Log.d("viewpager!", Integer.toString(position));
            }
        });
    }

    public void mOnConfirm(View v) {
        Intent intent = new Intent();
        intent.putExtra("main_character", mainCharacterIndex);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void mOnClose(View v) {
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if(e.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        return;
    }
}