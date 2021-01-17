package com.example.dailymaple;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.WorkManager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ConfigActivity extends AppCompatActivity {
    String currentCharacterName = "Temp";
    TextView configCharacterTextView, mainCharacterTextView, ursusNotifyNameTextView;
    ImageView mainCharacterChangeButton;
    CompoundButton switchActivateUrsusNotify, switchActivateDailyBossNotify, switchActivateWeeklyBossNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        // 현재 캐릭터 이름 TextView
        configCharacterTextView = (TextView) findViewById(R.id.current_character_name);

        // 대표 캐릭터 관련 선언
        mainCharacterTextView = (TextView) findViewById(R.id.main_character_name);
        mainCharacterChangeButton = (ImageView) findViewById(R.id.main_character_btn);

        // 우르스 알림 선언
        ursusNotifyNameTextView = (TextView) findViewById(R.id.ursus_notify_name);
        switchActivateUrsusNotify = (CompoundButton) findViewById(R.id.ursus_notify_btn);
        switchActivateUrsusNotify.setChecked(PreferenceHelper.getBoolean(getApplicationContext(), Constants.SHARED_PREF_URSUS_KEY));

        // 일일 보스 알림 선언
        switchActivateDailyBossNotify = (CompoundButton) findViewById(R.id.daily_boss_notify_btn);
        switchActivateDailyBossNotify.setChecked(PreferenceHelper.getBoolean(getApplicationContext(), Constants.SHARED_PREF_DAILY_BOSS_KEY));

        // 주간 보스 알림 선언
        switchActivateWeeklyBossNotify = (CompoundButton) findViewById(R.id.weekly_boss_notify_btn);
        switchActivateDailyBossNotify.setChecked(PreferenceHelper.getBoolean(getApplicationContext(), Constants.SHARED_PREF_WEEKLY_BOSS_KEY));

        initSwitchLayout(WorkManager.getInstance(getApplicationContext()));
    }

    // 설정 클릭 초기화
    private void initSwitchLayout(final WorkManager workManager) {
        // 맨 위 글씨 설정
        configCharacterTextView.setText("환경설정 (전체)");

        // 본캐 버튼 설정
        String mainCharacterName = PreferenceHelper.getString(getApplicationContext(), Constants.SHARED_PREF_MAIN_CHARACTER_KEY);
        Boolean btnEnable = false;

        // 여기에 캐릭터 이름마다 조건 추가해야함.
        if(mainCharacterName.equals("")) {
            btnEnable = true;
        } else if (mainCharacterName.equals(currentCharacterName)) {
            mainCharacterTextView.setText(mainCharacterName);
             btnEnable = false;
        } else {
            mainCharacterTextView.setText(mainCharacterName);
            btnEnable = true;
        }

        mainCharacterChangeButton.setEnabled(btnEnable);
        mainCharacterChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 새로운 창에서 대표 캐릭터 선택
                PreferenceHelper.setString(getApplicationContext(), Constants.SHARED_PREF_MAIN_CHARACTER_KEY, currentCharacterName);
                
                // 대표 캐릭터 변경 후 환경설정 창 UI 변경
                switchActivateUrsusNotify.setEnabled(true);
                ursusNotifyNameTextView.setTextColor(Color.parseColor("#000000"));
            }
        });

        // 우르스 버튼 설정
        // 대표 캐릭터만 우르스 알림을 띄우도록 설정
        if(!mainCharacterName.equals(currentCharacterName)) {
            switchActivateUrsusNotify.setEnabled(false);
            ursusNotifyNameTextView.setTextColor(Color.parseColor("#808080"));
        } else {
            switchActivateUrsusNotify.setEnabled(true);
            ursusNotifyNameTextView.setTextColor(Color.parseColor("#000000"));
        }
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

        // 주간 보스 알림 설정

    }

}