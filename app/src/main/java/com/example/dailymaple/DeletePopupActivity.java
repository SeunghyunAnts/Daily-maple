package com.example.dailymaple;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class DeletePopupActivity extends Activity implements View.OnClickListener {
    Button button_cancel;
    Button button_delete;
    Intent intent;
    Intent intent2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_delete_popup);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * 0.9); // Display 사이즈의 90%
        int height = (int) (dm.heightPixels * 0.4); // Display 사이즈의 50%
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;
        button_cancel = findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(this);
        button_delete = findViewById(R.id.button_delete);
        button_delete.setOnClickListener(this);

        intent2 = getIntent();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_cancel:
                finish();
            case R.id.button_delete:
                intent = new Intent();
                intent.putExtra("btn_id",intent2.getIntExtra("btn_id",0));
                setResult(RESULT_OK, intent);
                finish();
        }
    }
}