package com.example.dailymaple;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public class ViewDailyContentsActivity extends AppCompatActivity {

    ImageView[] dailyContents = new ImageView[12];
    boolean done[] = new boolean[12];
    String bossname[] = {"zacum", "hilla", "bloodyqueen", "pierre", "banban", "vellum", "horntail", "magnus", "papulatus", "arkarium", "pinkbean"};


    String platform = "Kakao"; // TODO: should be replaced
    String userId = "1593345655";
    String characterId = "YgK5QEI2qeqoTg8Sm6lG";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference path =
            db.collection(platform+"_users")
                    .document(userId)
                    .collection("characters")
                    .document(characterId)
                    .collection("dailycontents");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_daily_contents);
        dailyContents[0] = findViewById(R.id.zakum);
        dailyContents[1] = findViewById(R.id.hilla);
        dailyContents[2] = findViewById(R.id.bloodyqueen);
        dailyContents[3] = findViewById(R.id.pierre);
        dailyContents[4] = findViewById(R.id.banban);
        dailyContents[5] = findViewById(R.id.vellum);
        dailyContents[6] = findViewById(R.id.horntail);
        dailyContents[7] = findViewById(R.id.magnus);
        dailyContents[8] = findViewById(R.id.papulatus);
        dailyContents[9] = findViewById(R.id.arkarium);
        dailyContents[10] = findViewById(R.id.pinkbean);

        receiveData();

        for (int i = 0; i < 11; i++) {
            ImageView _this = dailyContents[i];
            int finalI = i;
            System.out.println(i+"번째: "+done[i]);
            _this.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (done[finalI]) {
                        _this.setBackground(getDrawable(R.drawable.content_deactivate));
                    } else {
                        _this.setBackground(getDrawable(R.drawable.content_activate));
                    }
                    done[finalI] = !done[finalI];
                    Map<String, Object> data = new HashMap<>();
                    data.put("done", done[finalI]);
                    data.put("name", bossname[finalI]); // TODO: move code to other file
                    path.document(Integer.toString(finalI)).set(data, SetOptions.merge());
                }
            });
        }
    }

    void receiveData() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();

//        String platform = "Kakao"; // should be replaced
//        String userId = "1593345655";
//        String characterId = "YgK5QEI2qeqoTg8Sm6lG";
        path.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int i = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("TAG", document.getId() + " => " + document.getData() + task.getResult().size());
                        if ((boolean)document.getData().get("done")) {
                            done[i] = true;
                            dailyContents[i].setBackground(getDrawable(R.drawable.content_activate));
                        } else {
                            done[i] = false;
                            dailyContents[i].setBackground(getDrawable(R.drawable.content_deactivate));
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

    void print1() {
        for (int i = 0; i < 11; i++) {
            System.out.print(done[i]+" ");
        }
        System.out.println();
    }
}