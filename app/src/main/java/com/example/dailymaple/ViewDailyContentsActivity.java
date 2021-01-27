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

    public static final int ZAKUM = 0;
    public static final int HILLA = 1;
    public static final int BLOODYQUEEN = 2;
    public static final int PIERRE = 3;
    public static final int BANBAN = 4;
    public static final int VELLUM = 5;
    public static final int HORNTAIL = 6;
    public static final int VONLEON = 7;
    public static final int MAGNUS = 8;
    public static final int PAPULATUS = 9;
    public static final int ARKARIUM = 10;
    public static final int PINKBEAN = 11;


    ImageView[] dailyContents = new ImageView[LENGTH];
    ImageView[] clearMark = new ImageView[LENGTH];
    boolean[] done = new boolean[LENGTH];
    public static String[] bossname = { // order is important.
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

        dailyContents[ZAKUM] = findViewById(R.id.zakum);
        dailyContents[HILLA] = findViewById(R.id.hilla);
        dailyContents[BLOODYQUEEN] = findViewById(R.id.bloodyqueen);
        dailyContents[PIERRE] = findViewById(R.id.pierre);
        dailyContents[BANBAN] = findViewById(R.id.banban);
        dailyContents[VELLUM] = findViewById(R.id.vellum);
        dailyContents[HORNTAIL] = findViewById(R.id.horntail);
        dailyContents[VONLEON] = findViewById(R.id.vonleon);
        dailyContents[MAGNUS] = findViewById(R.id.magnus);
        dailyContents[PAPULATUS] = findViewById(R.id.papulatus);
        dailyContents[ARKARIUM] = findViewById(R.id.arkarium);
        dailyContents[PINKBEAN] = findViewById(R.id.pinkbean);

        clearMark[ZAKUM] = findViewById(R.id.clearzakum);
        clearMark[HILLA] = findViewById(R.id.clearhilla);
        clearMark[BLOODYQUEEN] = findViewById(R.id.clearbloodyqueen);
        clearMark[PIERRE] = findViewById(R.id.clearpierre);
        clearMark[BANBAN] = findViewById(R.id.clearbanban);
        clearMark[VELLUM] = findViewById(R.id.clearvellum);
        clearMark[HORNTAIL] = findViewById(R.id.clearhorntail);
        clearMark[VONLEON] = findViewById(R.id.clearvonleon);
        clearMark[MAGNUS] = findViewById(R.id.clearmagnus);
        clearMark[PAPULATUS] = findViewById(R.id.clearpapulatus);
        clearMark[ARKARIUM] = findViewById(R.id.cleararkarium);
        clearMark[PINKBEAN] = findViewById(R.id.clearpinkbean);

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
                        if ((boolean)document.getData().get("done")) {
                            done[i] = true;
                            ColorMatrix matrix = new ColorMatrix();
                            matrix.setSaturation(0);

                            ColorFilter colorFilter = new ColorMatrixColorFilter(matrix);

                            dailyContents[i].setColorFilter(colorFilter);
                            dailyContents[i].setImageAlpha(200);

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

    public static void initDB(CollectionReference path) {
        path.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Map <String, Object> data = new HashMap<>();
                    for (int i = 0; i < LENGTH; i++) {
                        data.put("done", false);
                        data.put("name", bossname[i]);
                        path.document(Integer.toString(i)).set(data, SetOptions.merge());
                    }
                }
                else {
                    System.out.println("fail");
                }
            }
        });
    }
}