package com.example.dailymaple;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.common.util.ScopeUtil;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static com.example.dailymaple.Constants.KOREA_TIMEZONE;
import static com.example.dailymaple.Constants.URSUS_TIME;

public class Ursus extends Worker {
    public Ursus(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("notifi!", "Ursus worker dowork");
        NotificationHelper mNotificationHelper = new NotificationHelper(getApplicationContext());
        long currentMillis = Calendar.getInstance(TimeZone.getTimeZone(KOREA_TIMEZONE), Locale.KOREA).getTimeInMillis();
//        Calendar cal = Calendar.getInstance();
//        cal.setTimeInMillis(System.currentTimeMillis());

//
//        long currentMillis = cal.getTimeInMillis();

        // 알림 범위(21:00 ~ 22:00)에 해당하는지 기준 설정
        Calendar eventCal = NotificationHelper.getScheduledCalender(URSUS_TIME);
        long minUrsusNotificationTime = eventCal.getTimeInMillis();

        eventCal.add(Calendar.HOUR_OF_DAY, Constants.NOTIFICATION_INTERVAL_HOUR);
        long maxUrsusNotificationTime = eventCal.getTimeInMillis();

        Log.d("notifi!", "Current millis :" + Long.toString(currentMillis));
        Log.d("notifi!", "Ursus worker delay check : \n" +
                Long.toString(minUrsusNotificationTime) + "\n" +
                Long.toString(maxUrsusNotificationTime));

        // 현재 시각이 우르스 알림 범위에 해당하는지
        boolean isUrsusNotifyRange = (minUrsusNotificationTime <= currentMillis && currentMillis <= maxUrsusNotificationTime);

        if (isUrsusNotifyRange) {
            // 현재 시각이 알림 범위에 해당하면 알림 생성 (22:00 ~ 23:00)
            Log.d("notifi!", "Ursus worker time good");
            mNotificationHelper.createNotification(Constants.URSUS_NAME);
        } else {
            // 그 외의 경우 가장 빠른 A 이벤트 예정 시각까지의 notificationDelay 계산하여 딜레이 호출
            Log.d("notifi!", "Ursus worker time bad, call delay");
            long notificationDelay = NotificationHelper.getNotificationDelay(Constants.URSUS_NAME);
            OneTimeWorkRequest workRequest =
                    new OneTimeWorkRequest.Builder(Ursus.class)
                            .setInitialDelay(notificationDelay, TimeUnit.MILLISECONDS)
                            .build();
            WorkManager.getInstance(getApplicationContext()).enqueue(workRequest);
        }
        return Result.success();
    }
}