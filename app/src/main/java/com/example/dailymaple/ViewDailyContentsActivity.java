package com.example.dailymaple;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewDailyContentsActivity extends AppCompatActivity {

//    public static final int LENGTH = 12;
    Intent intent;

    ImageView[] dailyContents = new ImageView[Constants.DailyContentsLength];
    ImageView[] clearMark = new ImageView[Constants.DailyContentsLength];
    boolean[] done = new boolean[Constants.DailyContentsLength];
    ArrayList<Boolean> newDone = new ArrayList<>();

    String platform;
    String userId;
    String characterId;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_daily_contents);

        intent = getIntent();

        platform = PreferenceHelper.getString(getApplicationContext(), Constants.SHARED_PREF_PLATFORM_KEY);
        userId = PreferenceHelper.getString(getApplicationContext(), Constants.SHARED_PREF_USER_KEY);

        characterId = intent.getStringExtra("characterId");

        dailyContents[0] = findViewById(R.id.zakum);
        dailyContents[1] = findViewById(R.id.hilla);
        dailyContents[2] = findViewById(R.id.bloodyqueen);
        dailyContents[3] = findViewById(R.id.pierre);
        dailyContents[4] = findViewById(R.id.banban);
        dailyContents[5] = findViewById(R.id.vellum);
        dailyContents[6] = findViewById(R.id.magnus);
        dailyContents[7] = findViewById(R.id.papulatus);
        dailyContents[8] = findViewById(R.id.horntail);
        dailyContents[9] = findViewById(R.id.vonleon);
        dailyContents[10] = findViewById(R.id.arkarium);
        dailyContents[11] = findViewById(R.id.pinkbean);

        clearMark[0] = findViewById(R.id.clearzakum);
        clearMark[1] = findViewById(R.id.clearhilla);
        clearMark[2] = findViewById(R.id.clearbloodyqueen);
        clearMark[3] = findViewById(R.id.clearpierre);
        clearMark[4] = findViewById(R.id.clearbanban);
        clearMark[5] = findViewById(R.id.clearvellum);
        clearMark[6] = findViewById(R.id.clearmagnus);
        clearMark[7] = findViewById(R.id.clearpapulatus);
        clearMark[8] = findViewById(R.id.clearhorntail);
        clearMark[9] = findViewById(R.id.clearvonleon);
        clearMark[10] = findViewById(R.id.cleararkarium);
        clearMark[11] = findViewById(R.id.clearpinkbean);

        path = db.collection(platform+"_users")
                .document(userId)
                .collection("characters")
                .document(characterId);

        // 데이터 로딩 -> View 순서로 실행하는 스레드
        Thread thread = new Thread() {
            public void run() {
                try {
                    Log.d("thread :", "start thread");
                    path.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("thread : ", "DocumentSnapshot data: " + document.getData().get("daily_contents"));
                                    newDone = (ArrayList<Boolean>) document.getData().get("daily_contents");
                                    Log.d("thread : ", "save data to newDone[]");
                                    draw();
                                    setClick();
                                } else {
                                    Log.d("TAG", "No such document");
                                }
                            }
                            else {
                                Log.d("Error", "get data failed");
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 불러온 정보를 바탕으로 그리기
    protected void draw() {
        for(int i = 0; i < Constants.DailyContentsLength; i++) {
            if (newDone.get(i)) {
//                done[i] = true;
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);

                ColorFilter colorFilter = new ColorMatrixColorFilter(matrix);

                dailyContents[i].setColorFilter(colorFilter);
                dailyContents[i].setImageAlpha(200);

                Animation animation =  AnimationUtils.loadAnimation(ViewDailyContentsActivity.this, R.anim.fade_in);
                clearMark[i].startAnimation(animation);
                clearMark[i].setVisibility(View.VISIBLE);
            } else {
//                done[i] = false;
                dailyContents[i].setColorFilter(null);
                dailyContents[i].setImageAlpha(255);

                clearMark[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    // 클릭 이벤트 설정
    protected void setClick() {
        // 가져온 배열 로깅
        Log.d("callback : ", newDone.toString());

        for (int i = 0; i < Constants.DailyContentsLength; i++) {
            ImageView _this = dailyContents[i];
            int finalI = i;

            Log.d("callback : ", Integer.toString(finalI)+"번째 Array : " + newDone.get(finalI).toString());

            _this.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("callback, click : ", Integer.toString(finalI) + "번째 클릭됨");
                    ImageView iv_tmp = (ImageView) v;
                    if (newDone.get(finalI)) {
                        iv_tmp.setColorFilter(null);
                        iv_tmp.setImageAlpha(255);

                        clearMark[finalI].setVisibility(View.INVISIBLE);
                    } else {
                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);

                        ColorFilter colorFilter = new ColorMatrixColorFilter(matrix);

                        iv_tmp.setColorFilter(colorFilter);
                        iv_tmp.setImageAlpha(200);

                        Animation animation =  AnimationUtils.loadAnimation(ViewDailyContentsActivity.this, R.anim.fade_in);
                        clearMark[finalI].startAnimation(animation);
                        clearMark[finalI].setVisibility(View.VISIBLE);
                    }
                    newDone.set(finalI, !newDone.get(finalI));
                    Map<String, Object> data = new HashMap<>();
                    data.put("daily_contents", newDone);
                    path.set(data, SetOptions.merge());
//                    data.put("done", done[finalI]);
//                    data.put("name", bossname[finalI]);
//                    path.document(Integer.toString(finalI)).set(data, SetOptions.merge());
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}