package com.example.dailymaple;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.primitives.Ints;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.URL;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import static com.example.dailymaple.Constants.DailyContentsLength;
import static com.example.dailymaple.Constants.SHARED_PREF_PLATFORM_KEY;
import static com.example.dailymaple.Constants.SHARED_PREF_USER_KEY;
import static com.example.dailymaple.Constants.WeeklyContentsLength;

public class CharacterActivity extends AppCompatActivity {

    Intent intent, intentFromLogin;
    Intent intentDelete;
    ImageView[] imageViews_plus;
    FrameLayout[] frameLayouts;
    TextView[] textViews_name;
    ImageView[] imageViews_chr;
    LinearLayout[] characters;
    TextView[] textViews_lv;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;
    Map<String, Object> refreshUser = new HashMap<>();
    int level = 240; // sample
    public static final int LENGTH = 12;


    int delete_i = 0;

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
    int[] imageViews_chr_id = {R.id.imageView_chr1, R.id.imageView_chr2, R.id.imageView_chr3,
            R.id.imageView_chr4, R.id.imageView_chr5, R.id.imageView_chr6,
            R.id.imageView_chr7, R.id.imageView_chr8, R.id.imageView_chr9,
            R.id.imageView_chr10, R.id.imageView_chr11, R.id.imageView_chr12};
    int[] characters_id = {R.id.character, R.id.character2, R.id.character3,
                            R.id.character4, R.id.character5, R.id.character6,
                            R.id.character7, R.id.character8, R.id.character9,
                            R.id.character10, R.id.character11, R.id.character12};
    int[] textViews_lv_id = {R.id.textView_lv1, R.id.textView_lv2, R.id.textView_lv3,
            R.id.textView_lv4, R.id.textView_lv5, R.id.textView_lv6,
            R.id.textView_lv7, R.id.textView_lv8, R.id.textView_lv9,
            R.id.textView_lv10, R.id.textView_lv11, R.id.textView_lv12};
    String[] characterId = new String[LENGTH];

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
        setContentView(R.layout.activity_character);
        
        // DB에서 불러올 캐릭터 정보 배열
        characterInfos = new ArrayList<CharacterInfo>();

        // 툴바 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Intent from LoginActivity
        intentFromLogin = getIntent();

//        Intent를 이용한 로그인정보 전달
//        Log.d("platform", intentFromLogin.getStringExtra("platform"));
//        Log.d("id", intentFromLogin.getStringExtra("id"));
//        platform = intentFromLogin.getStringExtra("platform");
//        userId = intentFromLogin.getStringExtra("id");

        // 로그인 정보를 저장
        platform = PreferenceHelper.getString(getApplicationContext(), SHARED_PREF_PLATFORM_KEY);
        userId = PreferenceHelper.getString(getApplicationContext(), SHARED_PREF_USER_KEY);

        Log.d("CharacterActivity", platform + " " + userId);

        // Progress dialog 설정
        progressDialog = new ProgressDialog(this);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();
        progressDialog.setCancelable(false);

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

                            imageViews_chr = new ImageView[]{findViewById(imageViews_chr_id[0]), findViewById(imageViews_chr_id[1]), findViewById(imageViews_chr_id[2]),
                                    findViewById(imageViews_chr_id[3]), findViewById(imageViews_chr_id[4]), findViewById(imageViews_chr_id[5]),
                                    findViewById(imageViews_chr_id[6]), findViewById(imageViews_chr_id[7]), findViewById(imageViews_chr_id[8]),
                                    findViewById(imageViews_chr_id[9]), findViewById(imageViews_chr_id[10]), findViewById(imageViews_chr_id[11])};

                            characters = new LinearLayout[]{findViewById(characters_id[0]), findViewById(characters_id[1]), findViewById(characters_id[2]),
                                    findViewById(characters_id[3]), findViewById(characters_id[4]), findViewById(characters_id[5]),
                                    findViewById(characters_id[6]), findViewById(characters_id[7]), findViewById(characters_id[8]),
                                    findViewById(characters_id[9]), findViewById(characters_id[10]), findViewById(characters_id[11])};

                            textViews_lv = new TextView[]{findViewById(textViews_lv_id[0]),findViewById(textViews_lv_id[1]),findViewById(textViews_lv_id[2]),
                                    findViewById(textViews_lv_id[3]),findViewById(textViews_lv_id[4]),findViewById(textViews_lv_id[5]),
                                    findViewById(textViews_lv_id[6]),findViewById(textViews_lv_id[7]),findViewById(textViews_lv_id[8]),
                                    findViewById(textViews_lv_id[9]),findViewById(textViews_lv_id[10]),findViewById(textViews_lv_id[11])};

                            for(int i=0;i<imageViews_plus_id.length;i++){
//            imageViews_plus[i] = findViewById(imageViews_plus_id[i]);
                                imageViews_plus[i].setOnClickListener(CharacterActivity.this::onClickPlus);
                                characters[i].setOnLongClickListener(CharacterActivity.this::onLongClickCharacter);
                                characters[i].setOnClickListener(CharacterActivity.this::onClickCharacter);
                            }
                            tableRow1 = findViewById(R.id.tableRow1);
                            tableRow2 = findViewById(R.id.tableRow2);
                            tableRow3 = findViewById(R.id.tableRow3);
                            tableRow4 = findViewById(R.id.tableRow4);

                            tableRows = new TableRow[]{tableRow1,tableRow2, tableRow3, tableRow4};
                            for(int i=0;i<imageViews_plus_id.length;i++){
                                System.out.println("i : "+imageViews_plus_id[i]);
                            }

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("", document.getId() + " => " + document.getData());
                                Thread thread = new Thread() {
                                    public void run() {
                                        try {
                                            System.out.println("before : " + document.getData().get("nickname").toString());
                                            refreshUser = new HashMap<>();
                                            refreshUser = refresh("https://maplestory.nexon.com/Ranking/World/Total?c="+document.getData().get("nickname").toString(), document.getData().get("nickname").toString());
                                            System.out.println("after : " + refreshUser.get("img_url"));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                thread.start();
                                try {
                                    thread.join();
                                    db.collection(platform + "_users")
                                            .document(userId)
                                            .collection("characters")
                                            .document(document.getId())
                                            .set(refreshUser, SetOptions.merge());
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                if(document.getData().get("img_url")==null || document.getData().get("level")==null){

                                if(refreshUser.get("img_url").toString() == null || refreshUser.get("level").toString() == null) {
                                    characterInfo = new CharacterInfo(document.getId(), document.getData().get("nickname").toString());
                                }
                                else{
                                    characterInfo = new CharacterInfo(document.getId(), document.getData().get("nickname").toString(), refreshUser.get("img_url").toString(), refreshUser.get("level").toString());
                                }

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
                                textViews_lv[i].setText(characterInfos.get(i).getLevel());
                                Glide.with(getApplicationContext()).load(characterInfos.get(i).getImgUrl()).centerCrop().into(imageViews_chr[i]);


                            }
                            if(characterInfos.size()!=imageViews_plus_id.length){
                                frameLayouts[characterInfos.size()].setVisibility(View.VISIBLE);
                            }
                            progressDialog.dismiss();
                          
                            // 툴바 설정 : dismiss() 실행 후 기본 툴바 없어지는 오류 있음
                            Toolbar toolbar = findViewById(R.id.toolbar);
                            setSupportActionBar(toolbar);
                            getSupportActionBar().setDisplayShowTitleEnabled(false);

                            for(int j=0;j<characterInfos.size();j++){
                                System.out.println("Current characterID : "+characterInfos.get(j).getCharacterId());
                                characterId[j] = characterInfos.get(j).getCharacterId();
                            }
                        } else {
                            Log.w("", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    public void onClickPlus(View v){
        intent = new Intent(this, AddPopupActivity.class);
        intent.putExtra("btn_id",v.getId());
        startActivityForResult(intent, 1);
    }

    public void onClickCharacter(View v){
        intent = new Intent(this, ViewProgressActivity.class);
//        intent.putExtra("platform", platform);
//        intent.putExtra("userId", userId);
        intent.putExtra("characterId", characterId[Ints.indexOf(characters_id, v.getId())]);
        startActivity(intent);
    }

    public Boolean onLongClickCharacter(View v){
        intentDelete = new Intent(this, DeletePopupActivity.class);
        intentDelete.putExtra("btn_id", v.getId());

        startActivityForResult(intentDelete, 2);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                String name = data.getStringExtra("name");
                int btn_id = data.getIntExtra("btn_id",0);
                String img_url = data.getStringExtra("img_url");
                String level = data.getStringExtra("level");
                System.out.println("btn_id : "+btn_id);

                // Firestore 문서에 삽입할 데이터 선택
                Map<String, Object> user = new HashMap<>();

                // 저장할 문서 설정
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
                String docuName = sdf.format(new Date())+"";
                user.put("doc_name", docuName);
                user.put("timestamp", FieldValue.serverTimestamp());

                // 캐릭터 정보
                user.put("nickname", name);
                user.put("img_url", img_url);
                user.put("level",level);

                // 컨텐츠 정보
                ArrayList<Integer> daily_content = new ArrayList<>();
                ArrayList<Boolean> daily_content_alert = new ArrayList<>();
                ArrayList<Integer> weekly_content = new ArrayList<>();
                ArrayList<Boolean> weekly_content_alert = new ArrayList<>();

                // List 초기화
                for(int temp = 0; temp < DailyContentsLength; temp++) {
                    daily_content.add(0);
                    daily_content_alert.add(false);
                }

                for(int temp = 0; temp < WeeklyContentsLength; temp++) {
                    weekly_content.add(0);
                    weekly_content_alert.add(false);
                }

                user.put("daily_contents", daily_content);
                user.put("daily_contents_alert", daily_content_alert);
                user.put("weekly_contents", weekly_content);
                user.put("weekly_contents_alert", weekly_content_alert);

                // 캐릭터 정보를 삽입할 때 까지 Progress dialog 표시
                progressDialog.show();

                // 새로운 캐릭터 추가 한 문서(docuName)에 구현
               db.collection(platform + "_users")
                        .document(userId)
                        .collection("characters")
                        .document(docuName)
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Success", "DocumentSnapshot added with ID: "+ docuName);
                                for(int i=0;i<imageViews_plus_id.length;i++){
                                    if(imageViews_plus_id[i]==btn_id){
//                                        System.out.println("i2 : "+i);
                                        characterInfos.add(new CharacterInfo(docuName, name, img_url, level));
                                        characterId[characterInfos.size() - 1] = characterInfos.get(characterInfos.size() - 1).getCharacterId();
//                                        Log.d("characer ID check : ", characterId[characterInfos.size() - 1]);
                                        textViews_name[i].setText(name);
                                        textViews_lv[i].setText(level);

                                        Glide.with(getApplicationContext()).load(img_url).centerCrop().into(imageViews_chr[i]);
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
                                // 삽입 이후 Progress dialog 숨김
                                progressDialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Fail", "Error adding document", e);
                                progressDialog.dismiss();
                            }
                        });
            }
        }
        if(requestCode==2){
            if(resultCode==RESULT_OK){
                int btn_id = data.getIntExtra("btn_id",0);
                progressDialog.show();
                for(int i=0;i<characters_id.length;i++){
                    if(btn_id==characters_id[i]){
//                        for(int j=0;j<characterInfos.size();j++){
//                            System.out.println("Befroe characterID : "+characterInfos.get(j).getCharacterId());
//                        }
                        delete_i = i;
                        db.collection(platform + "_users")
                                .document(userId)
                                .collection("characters")
                                .document(characterInfos.get(i).getCharacterId())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        characterInfos.remove(delete_i);
                                        for(int i=0;i<characterInfos.size();i++){
                                            if((i+1)%3==0 && i!=imageViews_plus_id.length-1){
                                                tableRows[(i+1)/3].setVisibility(View.VISIBLE);
                                            }
                                            frameLayouts[i].setVisibility(View.VISIBLE);
                                            imageViews_plus[i].setVisibility(View.INVISIBLE);
                                            textViews_name[i].setText(characterInfos.get(i).getNickname());
                                            textViews_lv[i].setText(characterInfos.get(i).getLevel());
                                            Glide.with(getApplicationContext()).load(characterInfos.get(i).getImgUrl()).centerCrop().into(imageViews_chr[i]);
                                        }
                                        if(characterInfos.size()%3==2 && (characterInfos.size()!=imageViews_plus.length-1)){
                                            tableRows[(characterInfos.size())/3+1].setVisibility(View.GONE);
                                        }
                                        else{
                                            if((characterInfos.size()+1) < imageViews_plus.length){
                                                frameLayouts[characterInfos.size()+1].setVisibility(View.INVISIBLE);
                                            }
                                        }
                                        imageViews_plus[characterInfos.size()].setVisibility(View.VISIBLE);

//                                        if(characterInfos.size()!=imageViews_plus_id.length){
//                                            frameLayouts[characterInfos.size()].setVisibility(View.VISIBLE);
//                                        }
                                        progressDialog.dismiss();
//                                        for(int j=0;j<characterInfos.size();j++){
//                                            System.out.println("After characterID : "+characterInfos.get(j).getCharacterId());
//                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("", "Error deleting document", e);
                                    }
                                });

                        break;
                    }

                }

            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu_toolbar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setting:
                Intent intent = new Intent(this, ConfigActivity.class);
                intent.putExtra("Characters", characterInfos);
                startActivity(intent);

                return true;

            default:
                Toast.makeText(getApplicationContext(), "나머지 버튼 클릭됨", Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);

        }
    }

    Map<String,Object> refresh(String url, String name) throws IOException {
        System.out.println(url);
        Document doc = Jsoup.connect(url).get();
        ArrayList<Element> elems = new ArrayList<>();
        String level = "";
        String Job = "";
        Map<String, Object> user = new HashMap<>();
        user.put("nickname",name);

        Elements elem = doc.select("tr[class=\"search_com_chk\"]");

        int save_dt = 0;
        int i = 0;
        for(Element e: elem.select("td")){
            if(e.text().length() < 3) continue;

            if(e.text().substring(0,3).equals("Lv.")) {
                // 레벨 정보인 경우 cnt == 2인 경우?
                level = e.text();
            } else if(e.text().contains("/")) {
                // 직업 정보인 경우 cnt == 3인 경우?
                Job = e.text().substring(e.text().indexOf("/") + 1);
            }
        }
        user.put("img_url",elem.select("img[class=\"\"]").attr("src"));
        user.put("level",level);

        return user;
    }

    @Override
    public void onBackPressed() {
//        return;
    }

}