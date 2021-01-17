package com.example.dailymaple;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

// Constant Import
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static com.example.dailymaple.Constants.COIN_NAME;
import static com.example.dailymaple.Constants.COIN_NOTIFICATION_CODE;
import static com.example.dailymaple.Constants.KOREA_TIMEZONE;
import static com.example.dailymaple.Constants.NOTIFICATION_CHANNEL_ID;
import static com.example.dailymaple.Constants.URSUS_NAME;
import static com.example.dailymaple.Constants.URSUS_NOTIFICATION_CODE;
import static com.example.dailymaple.Constants.URSUS_TIME;

public class NotificationHelper {
    private Context mContext;

    // initialize
    NotificationHelper(Context context) {
        mContext = context;
    }

    public static void setScheduledNotification(WorkManager workManager) {
        Log.d("notifi!", "set scheduled notification!");
        setUrsusSchedule(workManager);
    }

    // 현재시각이 알림 범위에 해당하지 않으면 딜레이 리턴
    public static long getNotificationDelay(String workName) {
        Log.d("notifi!", "get notification delay :" + workName);
        long pushDelayMillis = 0;
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(KOREA_TIMEZONE), Locale.KOREA);
        long currentMillis = cal.getTimeInMillis();
        if (workName.equals(Constants.URSUS_NAME)) {
            Log.d("nofifi!", "get delay : ursus : " + Integer.toString(cal.get(Calendar.HOUR_OF_DAY)));

            // 현재 시각이 22:00보다 크면 다음 날 알림, 작으면 오늘 22:00 알림
            if (cal.get(Calendar.HOUR_OF_DAY) >= Constants.URSUS_TIME) {
                Log.d("notifi!", "ursus time is exceed");
                Calendar nextDayCal = getScheduledCalender(Constants.URSUS_TIME);
                nextDayCal.add(Calendar.DAY_OF_YEAR, 1);
                pushDelayMillis = nextDayCal.getTimeInMillis() - currentMillis;
            } else {
                pushDelayMillis = getScheduledCalender(URSUS_TIME).getTimeInMillis() - currentMillis;
                Log.d("notifi!", "ursus time is ready. notify after : " + Long.toString(pushDelayMillis) + "millis");}
        }
        Log.d("notifi!", "delay : " + Long.toString(pushDelayMillis));
        return pushDelayMillis;
    }

    public static Calendar getScheduledCalender(Integer scheduledTime) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(KOREA_TIMEZONE), Locale.KOREA);
        cal.set(Calendar.HOUR_OF_DAY, scheduledTime);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal;
    }

    // 푸시 알림 허용 및 사용자에 의해 알림이 꺼진 상태가 아니라면 푸시 알림 백그라운드 갱신
    public static void refreshScheduledNotification(Context context) {
        try {
            Boolean isNotificationActivated = PreferenceHelper.getBoolean(context, Constants.SHARED_PREF_NOTIFICATION_KEY);
            if (isNotificationActivated) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                boolean isNotifyAllowed;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int channelImportance = notificationManager.getNotificationChannel(Constants.NOTIFICATION_CHANNEL_ID).getImportance();
                    isNotifyAllowed = channelImportance != NotificationManager.IMPORTANCE_NONE;
                } else {
                    isNotifyAllowed = NotificationManagerCompat.from(context).areNotificationsEnabled();
                }
                if (isNotifyAllowed) {
                    NotificationHelper.setScheduledNotification(WorkManager.getInstance(context));
                }
            }
        } catch (NullPointerException nullException) {
            Toast.makeText(context, "푸시 알림 기능에 문제가 발생했습니다. 앱을 재실행해주세요.", Toast.LENGTH_SHORT).show();
            nullException.printStackTrace();
        }
    }

    private static void setUrsusSchedule(WorkManager workManager) {
        Log.d("notifi!", "set ursus schedule");
        // Event 발생시 Ursus.class 호출
        // 알림 활성화 시점에서 반복 주기 이전에 있는 가장 빠른 알림 생성
        OneTimeWorkRequest ursusWorkerOneTimePushRequest = new OneTimeWorkRequest.Builder(Ursus.class).build();
        // 가장 가까운 알림시각까지 대기 후 실행, 12시간 간격 반복 5분 이내 완료
        PeriodicWorkRequest ursusWorkerPeriodicPushRequest =
                new PeriodicWorkRequest.Builder(Ursus.class, 12, TimeUnit.HOURS, 5, TimeUnit.MINUTES)
                        .build();
        try {
            // Ursus work 정보 조회
            Log.d("notifi!", "check ursus work");
            List<WorkInfo> ursusWorkerNotifyWorkInfoList = workManager.getWorkInfosForUniqueWorkLiveData(URSUS_NAME).getValue();
            for (WorkInfo workInfo : ursusWorkerNotifyWorkInfoList) {
                // worker의 동작이 종료된 상태라면 worker 재등록
                if (workInfo.getState().isFinished()) {
                    Log.d("notifi!", "worker is finished. (ursus)");
                    workManager.enqueue(ursusWorkerOneTimePushRequest);
                    workManager.enqueueUniquePeriodicWork(URSUS_NAME, ExistingPeriodicWorkPolicy.KEEP, ursusWorkerPeriodicPushRequest);
                }
            }
        } catch (NullPointerException nullPointerException) {
            // 알림 worker가 생성된 적이 없으면 worker 생성
            Log.d("notifi!", "there is no worker, create worker");
            workManager.enqueue(ursusWorkerOneTimePushRequest);
            workManager.enqueueUniquePeriodicWork(URSUS_NAME, ExistingPeriodicWorkPolicy.KEEP, ursusWorkerPeriodicPushRequest);
        }
    }

    // check channel is created
    public static Boolean isNotificationChannelCreated(Context context) {
        Log.d("notifi!", "check notification channel is created?");
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Log.d("notifi!", "channel is already created");
                return notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) != null;
            }
            return true;
        } catch (NullPointerException nullException) {
            Log.d("notifi!", "channel is not created");
            Toast.makeText(context, "푸시 알림 기능에 문제가 발생했습니다. 앱을 재실행해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void createNotification(String workName) {
        Log.d("notifi!", "create notification");
        // Notification 클릭시 실행
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT); // 대기열에 이미 있다면 MainActivity가 아닌 앱 활성화
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        // Notificatoin을 이루는 공통 부분 정의
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setSmallIcon(R.drawable.maplestory_mushroom_icon) // Set Image
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true); // Remove Notification

        if (workName.equals(URSUS_NAME)) { // Ursus Event Notification
            Log.d("notifi!", "ursus notification");

            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, URSUS_NOTIFICATION_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            notificationBuilder.setContentTitle("아 맞다 우르스!!").setContentText("우르스 골든타임이 1시간 남았어요!")
                    .setContentIntent(pendingIntent);

            if (notificationManager != null) {
                notificationManager.notify(URSUS_NOTIFICATION_CODE, notificationBuilder.build());
            }
        } else if (workName.equals(COIN_NAME)) { // Coin Event Notification
            Log.d("notifi!", "coin notification");

            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, COIN_NOTIFICATION_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            notificationBuilder.setContentTitle("아 맞다 코인!!").setContentText("코인은 다 캐셨나요?")
                    .setContentIntent(pendingIntent);

            if (notificationManager != null) {
                notificationManager.notify(COIN_NOTIFICATION_CODE, notificationBuilder.build());
            }
        }
    }

    public static void createNotificationChannel(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d("Notifi!", "initialize notification");
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                // NotificationChannel 초기화
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, context.getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);

                // Configure the notification channel
                notificationChannel.setDescription("푸시알림");
                notificationChannel.enableLights(true); // 화면활성화 설정
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500}); // 진동패턴 설정
                notificationChannel.enableVibration(true); // 진동 설정
                notificationManager.createNotificationChannel(notificationChannel); // channel 생성

                // 여기서부터 테스트 -> 잘됨
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
                notificationBuilder.setSmallIcon(R.drawable.maplestory_mushroom_icon) // Set Image
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                        .setAutoCancel(true); // Remove Notification

                notificationBuilder.setContentTitle("notification initial complete");

                Notification notifi = notificationBuilder.build();
                notificationManager.notify(1, notifi);
            }
        } catch (NullPointerException nullException) {
            // notificationManager null 오류 raise
            Log.d("Notifi!", "fail to initialize notification");
            Toast.makeText(context, "푸시 알림 채널 생성에 실패했습니다. 앱을 재실행하거나 재설치해주세요.", Toast.LENGTH_SHORT).show();
            nullException.printStackTrace();
        }
    }
}
