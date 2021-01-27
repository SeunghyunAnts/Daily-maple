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

import java.util.ArrayList;

public class ConfigActivity extends AppCompatActivity {
    String currentCharacterName = "Temp";
    TextView configCharacterTextView, mainCharacterTextView, ursusNotifyNameTextView;
    ImageView mainCharacterChangeButton;
    CompoundButton switchActivateUrsusNotify, switchActivateDailyBossNotify, switchActivateWeeklyBossNotify;
    ArrayList<CharacterInfo> characterInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        Intent intent = getIntent();

        // 툴바 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        // 현재 캐릭터 이름 TextView
//        configCharacterTextView = (TextView) findViewById(R.id.current_character_name);

        // 대표 캐릭터 관련 선언
        mainCharacterTextView = (TextView) findViewById(R.id.main_character_name);
        mainCharacterChangeButton = (ImageView) findViewById(R.id.update_character_btn);

        // 우르스 알림 선언
        ursusNotifyNameTextView = (TextView) findViewById(R.id.ursus_notify_name);
        switchActivateUrsusNotify = (CompoundButton) findViewById(R.id.ursus_notify_btn);
        switchActivateUrsusNotify.setChecked(PreferenceHelper.getBoolean(getApplicationContext(), Constants.SHARED_PREF_URSUS_KEY));

        // 일일 보스 알림 선언
        switchActivateDailyBossNotify = (CompoundButton) findViewById(R.id.daily_boss_notify_btn);
        switchActivateDailyBossNotify.setChecked(PreferenceHelper.getBoolean(getApplicationContext(), Constants.SHARED_PREF_DAILY_BOSS_KEY));

        // 주간 보스 알림 선언
        switchActivateWeeklyBossNotify = (CompoundButton) findViewById(R.id.weekly_boss_notify_btn);
        switchActivateWeeklyBossNotify.setChecked(PreferenceHelper.getBoolean(getApplicationContext(), Constants.SHARED_PREF_WEEKLY_BOSS_KEY));

        // 캐릭터 정보 초기화
        characterInfos = (ArrayList<CharacterInfo>) getIntent().getSerializableExtra("Characters");
        Log.d("config:character", "character infos get : "+ Integer.toString(characterInfos.size()));

        initSwitchLayout(WorkManager.getInstance(getApplicationContext()));
    }

    // 설정 클릭 초기화
    private void initSwitchLayout(final WorkManager workManager) {
        // 본캐 버튼 설정
        String mainCharacterName = PreferenceHelper.getString(getApplicationContext(), Constants.SHARED_PREF_MAIN_CHARACTER_KEY);
        Boolean btnEnable = false;

        // 메인 캐릭터가 선택되어야 우르스 알림 설정 가능
        if(mainCharacterName.equals("")) {
            mainCharacterTextView.setText("대표 캐릭터 미설정");
            switchActivateUrsusNotify.setEnabled(false);
            ursusNotifyNameTextView.setTextColor(Color.parseColor("#808080"));
        } else {
            mainCharacterTextView.setText(mainCharacterName);
            switchActivateUrsusNotify.setEnabled(true);
            ursusNotifyNameTextView.setTextColor(Color.parseColor("#000000"));
        }

        mainCharacterChangeButton.setEnabled(true);
        mainCharacterChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 대표 캐릭터 설정 액티비티로 이동
                Intent intent = new Intent(ConfigActivity.this,
                        MainCharacterChoosePopupActivity.class);
                intent.putExtra("Characters", characterInfos);
                startActivityForResult(intent, 1);
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

                currentCharacterName = characterInfos.get(result).getNickname();

                // 새로운 창에서 대표 캐릭터 선택
                PreferenceHelper.setString(getApplicationContext(), Constants.SHARED_PREF_MAIN_CHARACTER_KEY, currentCharacterName);

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