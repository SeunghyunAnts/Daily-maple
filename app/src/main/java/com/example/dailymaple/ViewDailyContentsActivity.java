package com.example.dailymaple;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ViewDailyContentsActivity extends AppCompatActivity {

    ImageView[] dailyContents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_daily_contents);
        dailyContents = new ImageView[12];
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

        for (int i = 0; i < 11; i++) {
            ImageView _this = dailyContents[i];
            _this.setTag(R.drawable.content_deactivate);
            _this.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    System.out.println(_this.getDrawable());
                    System.out.println(getDrawable(R.drawable.content_activate));
                    if ((int)_this.getTag() == R.drawable.content_deactivate) {
                        _this.setBackground(getDrawable(R.drawable.content_activate));
                        _this.setTag(R.drawable.content_activate);
                    } else {
                        _this.setBackground(getDrawable(R.drawable.content_deactivate));
                        _this.setTag(R.drawable.content_deactivate);
                    }
                }
            });
        }
    }
}