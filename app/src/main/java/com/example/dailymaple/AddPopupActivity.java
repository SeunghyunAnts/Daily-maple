package com.example.dailymaple;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class AddPopupActivity extends Activity implements View.OnClickListener {
    EditText editText_name;
    ImageView imageView_chr;
    Button button_add;
    Button button_cancel;
    Intent intent;
    Intent intent2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_popup);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * 0.9); // Display 사이즈의 90%
        int height = (int) (dm.heightPixels * 0.5); // Display 사이즈의 50%
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        editText_name = findViewById(R.id.editText_name);
        imageView_chr = findViewById(R.id.imageView_chr);
        button_add = findViewById(R.id.button_add);
        button_cancel = findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(this);
        button_add.setOnClickListener(this);

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
                // 이미지도 함께 보내야?
                setResult(RESULT_OK, intent);
                finish();

        }
    }
}