package com.example.dailymaple;

public class Constants {
    // Preference Key
    public static final String SHARED_PREF_URSUS_KEY = "Notification Value";
    public static final String SHARED_PREF_MAIN_CHARACTER_KEY = "Main Character";
    public static final String SHARED_PREF_DAILY_BOSS_KEY = "Daily Boss";
    public static final String SHARED_PREF_WEEKLY_BOSS_KEY = "Weekly Boss";

    public static final String SHARED_PREF_PLATFORM_KEY = "Platform";
    public static final String SHARED_PREF_USER_KEY = "User";

    // Notification Channel ID
    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    // Korea TimeZone
    public static final String KOREA_TIMEZONE = "Asia/Seoul";

    // Notification Allow Interval Time
    public static final Integer NOTIFICATION_INTERVAL_HOUR = 1;

    // Notification Work Name
    public static final String URSUS_NAME = "Ursus Notification";
    public static final String COIN_NAME = "Coin Notification";
    public static final String DAILY_QUEST_NAME = "Daily Quest Notification";
    public static final String DAILY_BOSS_NAME = "Daily Boss Notification";
    public static final String WEEKLY_BOSS_NAME = "Weekly Boss Notification";

    // Notification Code
    public static final Integer URSUS_NOTIFICATION_CODE = 0;
    public static final Integer COIN_NOTIFICATION_CODE = 1;
    public static final Integer DAILY_QUEST_NOTIFICATION_QUEST = 2;
    public static final Integer DAILY_BOSS_NOTIFICATION_CODE = 3;
    public static final Integer WEEKLY_BOSS_NOTIFICATION_CODE = 4;

    // Weekly Boss Notification Date
    public static final String WEEKLY_BOSS_DATE = "WED";

    // Notification Time (Can be chnaged)
    public static Integer URSUS_TIME = 22;
    public static Integer COIN_TIME = 23;
    public static Integer DAILY_QUEST_TIME = 23;
    public static Integer DAILY_BOSS_TIME = 23;
    public static Integer WEEKLY_BOSS_TIME = 20;

    // Notification Time Change Method
    public static void setUrsusTime(Integer ursusTime) {
        URSUS_TIME = ursusTime;
    }
    public static void setCoinTime(Integer coinTime) {
        COIN_TIME = coinTime;
    }
    public static void setDailyQuestTime(Integer dailyQuestTime) {
        DAILY_QUEST_TIME = dailyQuestTime;
    }
    public static void setDailyBossTime(Integer dailyBossTime) {
        DAILY_BOSS_TIME = dailyBossTime;
    }
    public static void setWeeklyBossTime(Integer weeklyBossTime) { WEEKLY_BOSS_TIME = weeklyBossTime; }

    // Daily Contents
    public static Integer DailyContentsLength = 12;
    public static String[] DailyContentsName = {
            "zakum", "hilla",
            "bloodyqueen", "pierre",
            "banban", "vellum",
            "horntail", "vonleon",
            "magnus", "papulatus",
            "arkarium", "pinkbean"
    };

    // Weekly Contents
    public static Integer WeeklyContentsLength = 10;
    public static String[] WeeklyContentsName = { // order is important.
            "zakum", "hilla",
            "bloodyqueen", "pierre",
            "banban", "vellum",
            "magnus", "papulatus",
            "lotus", "damien"
    };
}
