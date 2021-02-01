package com.example.dailymaple;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class ViewProgressActivity extends AppCompatActivity {

    Intent intent;
    View DailyContents;
    View WeeklyContents;
    String platform;
    String userId;
    String characterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_progress);

        // 툴바 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DailyContents = findViewById(R.id.daily);
        DailyContents.setOnClickListener(this::onClickDaily);

        WeeklyContents = findViewById(R.id.weekly);
        WeeklyContents.setOnClickListener(this::onClickWeekly);

        intent = getIntent();

        platform = PreferenceHelper.getString(getApplicationContext(), Constants.SHARED_PREF_PLATFORM_KEY);
        userId = PreferenceHelper.getString(getApplicationContext(), Constants.SHARED_PREF_USER_KEY);

        characterId = intent.getStringExtra("characterId");

        Log.i("ViewProgressActivity", platform + " " + userId + " " + characterId);

    }

    public void onClickDaily(View v) {
        intent = new Intent(this, ViewDailyContentsActivity.class);
        intent.putExtra("characterId", characterId);
        startActivity(intent);
    }

    public void onClickWeekly(View v) {
        intent = new Intent(this, ViewWeeklyContentsActivity.class);
        intent.putExtra("platform", platform);
        intent.putExtra("userId", userId);
        intent.putExtra("characterId", characterId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        Log.d("memo", "asdfdsa");
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.character_menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
            case R.id.action_setting:
                Intent intent = new Intent(this, CharacterConfigActivity.class);

                intent.putExtra("platform", platform);
                intent.putExtra("userId", userId);
                intent.putExtra("characterId", characterId);

                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}