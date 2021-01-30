package com.example.dailymaple;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.work.WorkManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CharacterConfigActivity extends AppCompatActivity {
    TextView mainCharacterTextView, ursusNotifyNameTextView;
    ImageView updateCharacterButton;
    CompoundButton switchActivateUrsusNotify, switchActivateDailyBossNotify, switchActivateWeeklyBossNotify;
    String characterID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_config);

        Intent intent = getIntent();

        // 툴바 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 캐릭터 정보 초기화
        characterID = intent.getStringExtra("characterId");

        initSwitchLayout(WorkManager.getInstance(getApplicationContext()));
    }

    // 설정 클릭 초기화
    private void initSwitchLayout(final WorkManager workManager) {
        // 캐릭터 정보 갱신 버튼 설정
        updateCharacterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 캐릭터 정보 갱신
                Toast.makeText(getApplicationContext(), "환경 설정", Toast.LENGTH_LONG).show();
            }
        });

        // 우르스 버튼 설정
        switchActivateUrsusNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d("notifi!", "ursus button boolean : true");
                    boolean isChannelCreated = NotificationHelper.isNotificationChannelCreated(getApplicationContext());
                    if (isChannelCreated) {
                        Log.d("notifi!", "channel exist");
                        PreferenceHelper.setBoolean(getApplicationContext(), Constants.SHARED_PREF_URSUS_KEY, true);
                        NotificationHelper.setScheduledNotification(workManager);
                    } else {
                        Log.d("notifi!", "channel not exist");
                        NotificationHelper.createNotificationChannel(getApplicationContext());
                    }
                } else {
                    Log.d("!", "ursus button boolean : false");
                    PreferenceHelper.setBoolean(getApplicationContext(), Constants.SHARED_PREF_URSUS_KEY, false);
                    workManager.cancelAllWork();
                }
            }
        });

        // 일일 보스 알림 설정
        switchActivateDailyBossNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d("notifi!", "weekly boss button boolean : true");
                    boolean isChannelCreated = NotificationHelper.isNotificationChannelCreated(getApplicationContext());
                    if (isChannelCreated) {
                        Log.d("notifi!", "channel exist");
                        PreferenceHelper.setBoolean(getApplicationContext(), Constants.SHARED_PREF_DAILY_BOSS_KEY, true);
                        NotificationHelper.setScheduledNotification(workManager);
                    } else {
                        Log.d("notifi!", "channel not exist");
                        NotificationHelper.createNotificationChannel(getApplicationContext());
                    }
                } else {
                    Log.d("!", "weekly boss button boolean : false");
                    PreferenceHelper.setBoolean(getApplicationContext(), Constants.SHARED_PREF_DAILY_BOSS_KEY, false);
                    workManager.cancelAllWork();
                }
            }
        });

        // 주간 보스 알림 설정
        switchActivateWeeklyBossNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d("notifi!", "weekly boss button boolean : true");
                    boolean isChannelCreated = NotificationHelper.isNotificationChannelCreated(getApplicationContext());
                    if (isChannelCreated) {
                        Log.d("notifi!", "channel exist");
                        PreferenceHelper.setBoolean(getApplicationContext(), Constants.SHARED_PREF_WEEKLY_BOSS_KEY, true);
                        NotificationHelper.setScheduledNotification(workManager);
                    } else {
                        Log.d("notifi!", "channel not exist");
                        NotificationHelper.createNotificationChannel(getApplicationContext());
                    }
                } else {
                    Log.d("!", "weekly boss button boolean : false");
                    PreferenceHelper.setBoolean(getApplicationContext(), Constants.SHARED_PREF_WEEKLY_BOSS_KEY, false);
                    workManager.cancelAllWork();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //데이터 받기
                int result = data.getIntExtra("main_character", 0);
                Log.d("onActivityResult", "ok :" + Integer.toString(result));

//                currentCharacterName = characterInfos.get(result).getNickname();

                // 새로운 창에서 대표 캐릭터 선택
//                PreferenceHelper.setString(getApplicationContext(), Constants.SHARED_PREF_MAIN_CHARACTER_KEY, currentCharacterName);

                // 대표 캐릭터 변경 후 환경설정 창 UI 변경
                switchActivateUrsusNotify.setEnabled(true);
                ursusNotifyNameTextView.setTextColor(Color.parseColor("#000000"));

                // 이후 대표캐릭터 닉네임 변경
                mainCharacterTextView.setText(PreferenceHelper.getString(getApplicationContext(), Constants.SHARED_PREF_MAIN_CHARACTER_KEY));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.main_menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}