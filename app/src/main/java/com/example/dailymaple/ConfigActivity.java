package com.example.dailymaple;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;

public class ConfigActivity extends AppCompatActivity {
    Button btnSecondToMain;
    CompoundButton switchActivateNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        initSwitchLayout(WorkManager.getInstance(getApplicationContext()));
    }

    // 푸시알림 설정
    private void initSwitchLayout(final WorkManager workManager) {
        switchActivateNotify = (CompoundButton) findViewById(R.id.switch_second_notify);
        switchActivateNotify.setChecked(PreferenceHelper.getBoolean(getApplicationContext(), Constants.SHARED_PREF_NOTIFICATION_KEY));
        switchActivateNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d("notifi", "button boolean : true");
                    boolean isChannelCreated = NotificationHelper.isNotificationChannelCreated(getApplicationContext());
                    if (isChannelCreated) {
                        Log.d("notifi", "channel exist");
                        PreferenceHelper.setBoolean(getApplicationContext(), Constants.SHARED_PREF_NOTIFICATION_KEY, true);
                        NotificationHelper.setScheduledNotification(workManager);
                    } else {
                        Log.d("notifi", "channel not exist");
                        NotificationHelper.createNotificationChannel(getApplicationContext());
                    }
                } else {
                    Log.d("notifi", "button boolean : false");
                    PreferenceHelper.setBoolean(getApplicationContext(), Constants.SHARED_PREF_NOTIFICATION_KEY, false);
                    workManager.cancelAllWork();
                }
            }
        });
    }

}