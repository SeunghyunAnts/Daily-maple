package com.example.dailymaple;

import androidx.annotation.NonNull;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class CharacterConfigActivity extends AppCompatActivity {
    // layout
    CompoundButton characterAlertButton;
    Button updateButton;
    LinearLayout dailyNotifyLinearLayout, weeklyNotifyLinearLayout;

    // user
    String characterID;
    String platform;
    String userId;

    // config
    Boolean character_alert;

    // firestore
    DocumentReference path;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_config);

        // intent from ViewProgressActivity
        Intent intent = getIntent();

        // 툴바 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 계정 정보 초기화
        platform = PreferenceHelper.getString(getApplicationContext(), Constants.SHARED_PREF_PLATFORM_KEY);
        userId = PreferenceHelper.getString(getApplicationContext(), Constants.SHARED_PREF_USER_KEY);

        // 캐릭터 정보 초기화
        characterID = intent.getStringExtra("characterId");

        // 캐릭터 정보 업데이트 버튼
        updateButton = findViewById(R.id.character_update_btn);

        // Firestore 경로
        path = db.collection(platform+"_users")
                .document(userId)
                .collection("characters")
                .document(characterID);

        // 데이터 불러오는 스레드
        Thread thread = new Thread() {
            public void run() {
                try {
                    Log.d("thread :", "start thread");
                    Task task = path.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("thread : ", "DocumentSnapshot data: " + document.getData().get("daily_contents"));
                                    character_alert = (Boolean) document.getData().get("total_alert");
                                    Log.d("thread : ", "save data to character_alert");
                                    initLayout(WorkManager.getInstance(getApplicationContext()));
                                } else {
                                    Log.d("TAG", "No such document");
                                }
                            }
                            else {
                                Log.d("Error", "get data failed");
                            }
                        }
                    });
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
    }

    // 설정 클릭 초기화
    private void initLayout(final WorkManager workManager) {
//        // 캐릭터 정보 갱신 버튼 설정
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 캐릭터 정보 갱신
                Toast.makeText(getApplicationContext(), "환경 설정", Toast.LENGTH_LONG).show();
            }
        });

        characterAlertButton = (CompoundButton) findViewById(R.id.charater_notify_btn);
        characterAlertButton.setChecked(character_alert);

        // 캐릭터 알림 설정
        characterAlertButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // 여기에 이벤트 추가 (알람 설정 등)
                } else {
                    // 여기에 이벤트 추가 ( "" )
                }
                // firestore에 변경사항 업데이트
                Map<String, Object> data = new HashMap<>();
                character_alert = !character_alert;
                data.put("total_alert", character_alert);
                path.set(data, SetOptions.merge());
            }
        });

        dailyNotifyLinearLayout = findViewById(R.id.character_daily_contents);
        dailyNotifyLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 클릭시 일일 컨텐츠 설정 팝업으로 이동
                Intent intent = new Intent(CharacterConfigActivity.this,
                        DailyContentsConfigPopupActivity.class);
                intent.putExtra("CharacterID", characterID);
                startActivityForResult(intent, 1);
            }
        });

        weeklyNotifyLinearLayout = findViewById(R.id.character_weekly_contents);
        weeklyNotifyLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 클릭시 주간 컨텐츠 설정 팝업으로 이동
                Intent intent = new Intent(CharacterConfigActivity.this,
                        WeeklyContentsConfigPopupActivity.class);
                intent.putExtra("CharacterID", characterID);
                startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            // 일일 컨텐츠 팝업 결과
            if (resultCode == RESULT_OK) {

            }
        } else if (requestCode == 2) {
            // 주간 컨텐츠 팝업 결과
            if (resultCode == RESULT_OK) {

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