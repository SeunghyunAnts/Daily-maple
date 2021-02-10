package com.example.dailymaple;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ViewProgressActivity extends AppCompatActivity {

    Intent intent;
    View DailyContents;
    View WeeklyContents;
    String platform;
    String userId;
    String characterId;

    int[] daily = {0, 0};
    int[] weekly = {0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_progress);

        // 툴바 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DailyContents = findViewById(R.id.daily);
        DailyContents.setOnClickListener(this::onClickDaily);

        WeeklyContents = findViewById(R.id.weekly);
        WeeklyContents.setOnClickListener(this::onClickWeekly);

        intent = getIntent();

        platform = PreferenceHelper.getString(getApplicationContext(), Constants.SHARED_PREF_PLATFORM_KEY);
        userId = PreferenceHelper.getString(getApplicationContext(), Constants.SHARED_PREF_USER_KEY);

        characterId = intent.getStringExtra("characterId");

        Log.i("ViewProgressActivity", platform + " " + userId + " " + characterId);

        callFirestore();
    }

    public void onClickDaily(View v) {
        intent = new Intent(this, ViewDailyContentsActivity.class);
        intent.putExtra("characterId", characterId);
        startActivityForResult(intent, 1);
    }

    public void onClickWeekly(View v) {
        intent = new Intent(this, ViewWeeklyContentsActivity.class);
        intent.putExtra("characterId", characterId);
        startActivityForResult(intent, 2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        Log.d("memo", "asdfdsa");
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.character_menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
            case R.id.action_setting:
                Intent intent = new Intent(this, CharacterConfigActivity.class);
                intent.putExtra("characterId", characterId);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void callFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference path = FirebaseFirestore.getInstance().collection(platform+"_users")
                .document(userId)
                .collection("characters")
                .document(characterId);

        Task task = path.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<Boolean> daily_conts = (ArrayList<Boolean>) document.getData().get("daily_contents");
                        ArrayList<Boolean> daily_alerts = (ArrayList<Boolean>) document.getData().get("daily_contents_alert");
                        ArrayList<Boolean> weekly_conts = (ArrayList<Boolean>) document.getData().get("weekly_contents");
                        ArrayList<Boolean> weekly_alerts = (ArrayList<Boolean>) document.getData().get("weekly_contents_alert");

                        int i, total, complete;
                        daily[0] = daily[1] = 0;
                        weekly[0] = weekly[1] = 0;

                        for(i = total = complete = 0; i < Constants.DailyContentsLength; i++) {
                            if(daily_alerts.get(i)) {
                                // 알람을 해야하는 컨텐츠인 경우
                                if(daily_conts.get(i)) {
                                    complete++;
                                }
                                total++;
                            }
                        }
                        daily[0] = complete;
                        daily[1] = total;

                        for(i = total = complete = 0; i < Constants.WeeklyContentsLength; i++) {
                            if(weekly_alerts.get(i)) {
                                // 알람을 해야하는 컨텐츠인 경우
                                if(weekly_conts.get(i)) {
                                    complete++;
                                }
                                total++;
                            }
                        }
                        weekly[0] = complete;
                        weekly[1] = total;

                        Log.d("task :", "ok");

                        drawActivity();
                    } else {
                        Log.d("TAG", "No such document");
                    }
                }
                else {
                    Log.d("Error", "get data failed");
                }

            }
        });
    }

    public void drawActivity() {
        Log.d("Draw Activity : ", "Start");
        int daily_contents_clear = 100 * daily[0] / daily[1];
        int weekly_contents_clear = 100 * weekly[0] / weekly[1];

        ProgressBar daily_progress = findViewById(R.id.dailyprogress);
        ProgressBar weekly_progress = findViewById(R.id.weeklyprogress);

        TextView daily_percent = findViewById(R.id.daily_progress_percent);
        TextView weekly_percent = findViewById(R.id.weekly_progress_percent);

        daily_progress.setProgress(daily_contents_clear);
        weekly_progress.setProgress(weekly_contents_clear);

        daily_percent.setText(Integer.toString(daily_contents_clear) + "%");
        weekly_percent.setText(Integer.toString(weekly_contents_clear) + "%");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            // 일일 컨텐츠 클릭 결과
            if (resultCode == RESULT_OK) {
                callFirestore();
            }
        } else if (requestCode == 2) {
            // 주간 컨텐츠 클릭 결과
            if (resultCode == RESULT_OK) {
                callFirestore();
            }
        }
    }
}