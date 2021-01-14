package com.example.dailymaple

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannel(applicationContext)
        KakaoSdk.init(this, getString(R.string.kakao_app_key))
    }

}