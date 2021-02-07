package com.example.dailymaple;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WeeklyContentsConfigPopupActivity extends Activity {

    RecyclerView listView;
    String characterId;
    ArrayList<Boolean> prevArrayList = new ArrayList<>();
    DocumentReference path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE); // 타이틀 제거
        setContentView(R.layout.activity_weekly_contents_config_popup);

        Intent intent = getIntent();
        characterId = intent.getStringExtra("CharacterID");

        listView = (RecyclerView) findViewById(R.id.list_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(WeeklyContentsConfigPopupActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(layoutManager);

        String platform = PreferenceHelper.getString(getApplicationContext(), Constants.SHARED_PREF_PLATFORM_KEY);
        String userId = PreferenceHelper.getString(getApplicationContext(), Constants.SHARED_PREF_USER_KEY);

        // 데이터 불러오는 스레드
        path = FirebaseFirestore.getInstance().collection(platform+"_users")
                .document(userId)
                .collection("characters")
                .document(characterId);

        Thread thread = new Thread() {
            public void run() {
                try {
                    Log.d("thread :", "start thread");

                    Task task = path.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                Log.d("thread : ", "DocumentSnapshot data: " + document.getData().get("weekly_contents_alert"));
                                if (document.exists()) {
                                    prevArrayList = (ArrayList<Boolean>) document.getData().get("weekly_contents_alert");
                                } else {
                                    Log.d("TAG", "No such document");
                                }
                            }
                            else {
                                Log.d("Error", "get data failed");
                            }
                        }
                    });
                    // onComplete가 끝날때 까지 대기
                    Tasks.await(task);
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
        Log.d("ccc", "data loading complete");

        RecyclerAdapter adapter = new RecyclerAdapter(WeeklyContentsConfigPopupActivity.this, R.layout.recycle_item, false, characterId);
        listView.setAdapter(adapter);
    }

    public void mOnConfirm(View v) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    public void mOnClose(View v) {
        // 변경된 정보 롤백
        Map<String, Object> data = new HashMap<>();
        data.put("weekly_contents_alert", prevArrayList);
        path.set(data, SetOptions.merge());
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if(e.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        return;
    }
}