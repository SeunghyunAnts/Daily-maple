package com.example.dailymaple;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.primitives.Ints;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ViewProgressActivity extends AppCompatActivity {

    Intent intent;
    View DailyContents;
    View WeeklyContents;
    String platform;
    String userId;
    String characterId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference characterPath;

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
        platform = intent.getStringExtra("platform");
        userId = intent.getStringExtra("userId");
        characterId = intent.getStringExtra("characterId");
//        Log.i("characterId", characterId);
        characterPath = db.collection(platform+"_users")
                .document(userId)
                .collection("characters")
                .document(characterId);
//        updateProgressBar(characterPath);
    }

    @Override
    protected void onStart(){
        super.onStart();
        updateProgressBar(characterPath);
    }

    public void onClickDaily(View v) {
        intent = new Intent(this, ViewDailyContentsActivity.class);
        intent.putExtra("platform", platform);
        intent.putExtra("userId", userId);
        intent.putExtra("characterId", characterId);
        startActivity(intent);
    }

    public void onClickWeekly(View v) {
        intent = new Intent(this, ViewWeeklyContentsActivity.class);
        intent.putExtra("platform", platform);
        intent.putExtra("userId", userId);
        intent.putExtra("characterId", characterId);
        startActivity(intent);
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
                // User chose the "Settings" item, show the app settings UI...
                Toast.makeText(getApplicationContext(), "환경설정 버튼 클릭됨", Toast.LENGTH_LONG).show();


                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateProgressBar(DocumentReference characterPath) {
        characterPath.collection("dailycontents").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ProgressBar dailyProgressbar = findViewById(R.id.dailyprogress);
                    int cnt = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if ((boolean)document.getData().get("done")) cnt++;
                    }
                    dailyProgressbar.setProgress(100*cnt/ViewDailyContentsActivity.LENGTH);
                }
                else {
                    System.out.println("fail");
                }
            }
        });
        characterPath.collection("weeklycontents").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ProgressBar weeklyProgressbar = findViewById(R.id.weeklyprogress);
                    int cnt = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if ((boolean)document.getData().get("done")) cnt++;
                    }
                    weeklyProgressbar.setProgress(100*cnt/ViewWeeklyContentsActivity.LENGTH);
                }
                else {
                    System.out.println("fail");
                }
            }
        });
    }
}