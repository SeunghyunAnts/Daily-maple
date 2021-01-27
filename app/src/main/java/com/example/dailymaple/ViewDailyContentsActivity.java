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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ViewDailyContentsActivity extends AppCompatActivity {

    public static final int LENGTH = 12;
    Intent intent;

    ImageView[] dailyContents = new ImageView[LENGTH];
    ImageView[] clearMark = new ImageView[LENGTH];
    boolean[] done = new boolean[LENGTH];
    String[] bossname = { // order is important.
            "zakum", "hilla",
            "bloodyqueen", "pierre",
            "banban", "vellum",
            "horntail", "vonleon",
            "magnus", "papulatus",
            "arkarium", "pinkbean"
    };

    String platform;
    String userId;
    String characterId;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference path;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_daily_contents);

        intent = getIntent();
        platform = intent.getStringExtra("platform");
        userId = intent.getStringExtra("userId");
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
                .document(characterId)
                .collection("dailycontents");
        receiveData();
        try { // TODO: change to Async or import loading process
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < LENGTH; i++) {
            ImageView _this = dailyContents[i];
            int finalI = i;
            System.out.println(i+"번째: "+done[i]);
            _this.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView iv_tmp = (ImageView) v;
                    if (done[finalI]) {
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
                    done[finalI] = !done[finalI];
                    Map<String, Object> data = new HashMap<>();
                    data.put("done", done[finalI]);
                    data.put("name", bossname[finalI]);
                    path.document(Integer.toString(finalI)).set(data, SetOptions.merge());
                }
            });
        }
    }

    void receiveData() {
        path.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int i = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("TAG", document.getId() + " => " + document.getData() + task.getResult().size());
                        if ((boolean)document.getData().get("done")) {
                            done[i] = true;
                            ColorMatrix matrix = new ColorMatrix();
                            matrix.setSaturation(0);

                            ColorFilter colorFilter = new ColorMatrixColorFilter(matrix);

                            dailyContents[i].setColorFilter(colorFilter);
                            dailyContents[i].setImageAlpha(200);

//                            Animation animation =  AnimationUtils.loadAnimation(ViewDailyContentsActivity.this, R.anim.fade_in);
//                            clearMark[i].startAnimation(animation);
                            clearMark[i].setVisibility(View.VISIBLE);
                        } else {
                            done[i] = false;
                            dailyContents[i].setColorFilter(null);
                            dailyContents[i].setImageAlpha(255);

                            clearMark[i].setVisibility(View.INVISIBLE);
                        }
                        i++;
                    }
                }
                else {
                    System.out.println("fail");
                }
            }
        });
    }
}