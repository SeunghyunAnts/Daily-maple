package com.example.dailymaple;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
                intent = new Intent();
                intent.putExtra("name",editText_name.getText().toString());
                intent.putExtra("btn_id",intent2.getIntExtra("btn_id",0));
                intent.putExtra("img_url",img_url);
                // 이미지도 함께 보내야?
                setResult(RESULT_OK, intent);
                finish();
            case R.id.button_search:
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

        Elements elem = doc.select("table[class=\"rank_table\"]");
        Elements elem2 = doc.select("span[class=\"char_img\"]");

        int cnt = 0;
        int save_dt = 0;
        int i = 0;
        for(Element e: elem.select("dt")){
//            System.out.println("e : " + e.text());
            if(e.text().equals(name)){
                save_dt = cnt;
            }
            cnt++;
        }
        for(Element e: elem.select(".char_img")){
            if(i == save_dt){
                return e.select("img[class=\"\"]").attr("src");
            }
            i++;
//            System.out.println("src : "+e.select("img[class=\"\"]").attr("src"));
        }
        return "";
    }
}