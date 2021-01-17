package com.example.dailymaple;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ViewProgressActivity extends AppCompatActivity {

    Intent intent;
    View DailyContents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_progress);
        DailyContents = findViewById(R.id.daily);
        DailyContents.setOnClickListener(this::onClickDaily);
    }

    public void onClickDaily(View v) {
        intent = new Intent(this, ViewDailyContentsActivity.class);
        startActivity(intent);
    }
}