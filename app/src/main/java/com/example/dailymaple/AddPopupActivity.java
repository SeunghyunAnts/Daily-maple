package com.example.dailymaple;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class AddPopupActivity extends Activity implements View.OnClickListener {
    EditText editText_name;
    ImageView imageView_chr;
    Button button_add;
    Button button_cancel;
    Intent intent;
    Intent intent2;
    String img_url;
    Button button_search;

    // 크롤링 결과
    String level;
    String Job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_popup);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * 0.9); // Display 사이즈의 90%
        int height = (int) (dm.heightPixels * 0.6); // Display 사이즈의 50%
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        editText_name = findViewById(R.id.editText_name);
        imageView_chr = findViewById(R.id.imageView_chr);
        button_search = findViewById(R.id.button_search);
        button_add = findViewById(R.id.button_add);
        button_cancel = findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(this);
        button_add.setOnClickListener(this);
        button_search.setOnClickListener(this);

        intent2 = getIntent();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_cancel:
                finish();
            case R.id.button_add:
                // 중복확인하는 코드 작성해야 함

                if(img_url == null) {
                    // 이미지 검색을 안한 경우
                    Toast.makeText(this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(img_url.equals("")) {
                    // 없는 닉네임일 경우
                    Toast.makeText(this, "존재하지 않는 캐릭터입니다.", Toast.LENGTH_SHORT).show();
                    break;
                }

                intent = new Intent();
                intent.putExtra("name",editText_name.getText().toString());
                intent.putExtra("btn_id",intent2.getIntExtra("btn_id",0));
                intent.putExtra("img_url",img_url);
                intent.putExtra("level",level);
                intent.putExtra("job",Job);
                // 이미지도 함께 보내야?
                setResult(RESULT_OK, intent);
                finish();
            case R.id.button_search:
                if(editText_name.getText().toString().equals("") || editText_name.getText().toString() == null) {
                    Toast.makeText(this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }

                Thread thread = new Thread() {
                    public void run() {
                        try {
                            img_url = crawling("https://maplestory.nexon.com/Ranking/World/Total?c="+editText_name.getText().toString(),editText_name.getText().toString());

                        } catch (IOException e) {
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
                Glide.with(getApplicationContext()).load(img_url).fitCenter().into(imageView_chr);
        }
    }

    String crawling(String url, String name) throws IOException {
        Document doc = Jsoup.connect(url).get();
        ArrayList<Element> elems = new ArrayList<>();

        Elements elem = doc.select("tr[class=\"search_com_chk\"]");

        // 없는 닉네임일 경우 예외처리
        // 예외처리시 코드 필요? : 지금 없는 닉네임도 추가하면 만들어짐
        if(elem.text().isEmpty()) {
            Log.d("text", "empty!");
            return "";
        }

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

        return elem.select("img[class=\"\"]").attr("src");
    }
}