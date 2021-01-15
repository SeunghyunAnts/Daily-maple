package com.example.dailymaple;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CharacterActivity extends AppCompatActivity {

    Intent intent, intentFromLogin;
    ImageView[] imageViews_plus;
    FrameLayout[] frameLayouts;
    TextView[] textViews_name;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    View character; // TODO: should be changed array
    int[] imageViews_plus_id = {R.id.imageView_plus1, R.id.imageView_plus2, R.id.imageView_plus3,
                                R.id.imageView_plus4, R.id.imageView_plus5, R.id.imageView_plus6,
                                R.id.imageView_plus7, R.id.imageView_plus8, R.id.imageView_plus9,
                                R.id.imageView_plus10, R.id.imageView_plus11, R.id.imageView_plus12};
    int[] framelayouts_id = {R.id.framelayout1, R.id.framelayout2, R.id.framelayout3,
            R.id.framelayout4, R.id.framelayout5, R.id.framelayout6,
            R.id.framelayout7, R.id.framelayout8, R.id.framelayout9,
            R.id.framelayout10, R.id.framelayout11, R.id.framelayout12};
    int[] textViews_name_id = {R.id.textView_name1, R.id.textView_name2, R.id.textView_name3,
                            R.id.textView_name4, R.id.textView_name5, R.id.textView_name6,
                            R.id.textView_name7, R.id.textView_name8, R.id.textView_name9,
                            R.id.textView_name10, R.id.textView_name11, R.id.textView_name12};
    TableRow tableRow1;
    TableRow tableRow2;
    TableRow tableRow3;
    TableRow tableRow4;
    TableRow[] tableRows;
    String platform = "";
    String userId = "";
    ArrayList<CharacterInfo> characterInfos;
    CharacterInfo characterInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        characterInfos = new ArrayList<CharacterInfo>();

        intentFromLogin = getIntent();
        Log.d("platform", intentFromLogin.getStringExtra("platform"));
        Log.d("id", intentFromLogin.getStringExtra("id"));
        platform = intentFromLogin.getStringExtra("platform");
        userId = intentFromLogin.getStringExtra("id");



        db.collection(platform + "_users")
                .document(userId)
                .collection("characters")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            setContentView(R.layout.activity_character);
                            imageViews_plus = new ImageView[]{findViewById(imageViews_plus_id[0]), findViewById(imageViews_plus_id[1]), findViewById(imageViews_plus_id[2]),
                                    findViewById(imageViews_plus_id[3]), findViewById(imageViews_plus_id[4]), findViewById(imageViews_plus_id[5]),
                                    findViewById(imageViews_plus_id[6]), findViewById(imageViews_plus_id[7]), findViewById(imageViews_plus_id[8]),
                                    findViewById(imageViews_plus_id[9]), findViewById(imageViews_plus_id[10]), findViewById(imageViews_plus_id[11])};

                            frameLayouts = new FrameLayout[]{findViewById(framelayouts_id[0]), findViewById(framelayouts_id[1]), findViewById(framelayouts_id[2]),
                                    findViewById(framelayouts_id[3]), findViewById(framelayouts_id[4]), findViewById(framelayouts_id[5]),
                                    findViewById(framelayouts_id[6]), findViewById(framelayouts_id[7]), findViewById(framelayouts_id[8]),
                                    findViewById(framelayouts_id[9]), findViewById(framelayouts_id[10]), findViewById(framelayouts_id[11])};

                            textViews_name = new TextView[]{findViewById(textViews_name_id[0]),findViewById(textViews_name_id[1]),findViewById(textViews_name_id[2]),
                                    findViewById(textViews_name_id[3]),findViewById(textViews_name_id[4]),findViewById(textViews_name_id[5]),
                                    findViewById(textViews_name_id[6]),findViewById(textViews_name_id[7]),findViewById(textViews_name_id[8]),
                                    findViewById(textViews_name_id[9]),findViewById(textViews_name_id[10]),findViewById(textViews_name_id[11])};
                            for(int i=0;i<imageViews_plus_id.length;i++){
//            imageViews_plus[i] = findViewById(imageViews_plus_id[i]);
                                imageViews_plus[i].setOnClickListener(CharacterActivity.this::onClickPlus);

                            }
                            tableRow1 = findViewById(R.id.tableRow1);
                            tableRow2 = findViewById(R.id.tableRow2);
                            tableRow3 = findViewById(R.id.tableRow3);
                            tableRow4 = findViewById(R.id.tableRow4);

                            tableRows = new TableRow[]{tableRow1,tableRow2, tableRow3, tableRow4};
                            for(int i=0;i<imageViews_plus_id.length;i++){
                                System.out.println("i : "+imageViews_plus_id[i]);
                            }


                            character = findViewById(R.id.character);
                            character.setOnClickListener(CharacterActivity.this::onClickCharacter);
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("",document.getId() + " => " + document.getData());
                                characterInfo = new CharacterInfo(document.getData().get("nickname").toString());
                                characterInfos.add(characterInfo);
                                System.out.println(characterInfo.getNickname());
                            }

                            for(int i=0;i<characterInfos.size();i++){
                                if((i+1)%3==0 && i!=imageViews_plus_id.length-1){
                                    tableRows[(i+1)/3].setVisibility(View.VISIBLE);
                                }
                                frameLayouts[i].setVisibility(View.VISIBLE);
                                imageViews_plus[i].setVisibility(View.INVISIBLE);
                                textViews_name[i].setText(characterInfos.get(i).getNickname());

                            }
                            if(characterInfos.size()!=imageViews_plus_id.length){
                                frameLayouts[characterInfos.size()].setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.w("", "Error getting documents.", task.getException());
                        }
                    }
                });


    }

    public void onClickPlus(View v){
        intent = new Intent(this, AddPopupActivity.class);
//        startActivity(intent);
        intent.putExtra("btn_id",v.getId());
        startActivityForResult(intent, 1);
    }

    public void onClickCharacter(View v){
        intent = new Intent(this, ViewProgressActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                String name = data.getStringExtra("name");
                int btn_id = data.getIntExtra("btn_id",0);
                System.out.println("btn_id : "+btn_id);

                Map<String, Object> user = new HashMap<>();
                user.put("nickname", name);
                db.collection(platform + "_users")
                        .document(userId)
                        .collection("characters")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("Success", "DocumentSnapshot added with ID: "+documentReference.getId());
                                for(int i=0;i<imageViews_plus_id.length;i++){
                                    if(imageViews_plus_id[i]==btn_id){
//                                        System.out.println("i2 : "+i);
                                        characterInfos.add(new CharacterInfo(name));
                                        textViews_name[i].setText(name);
                                        imageViews_plus[i].setVisibility(View.INVISIBLE);
                                        if((i+1)%3==0 && i!=imageViews_plus_id.length-1){
                                            tableRows[(i+1)/3].setVisibility(View.VISIBLE);
                                            break;
                                        }
                                        if(i!=imageViews_plus_id.length-1){
                                            frameLayouts[i+1].setVisibility(View.VISIBLE);
                                        }


                                        break;
                                    }
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Fail", "Error adding document", e);
                            }
                        });


            }
        }

    }

}