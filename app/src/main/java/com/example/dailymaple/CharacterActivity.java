package com.example.dailymaple;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.Toast;

public class CharacterActivity extends AppCompatActivity {

    Intent intent;
    ImageView[] imageViews_plus;
    FrameLayout[] frameLayouts;
    int[] imageViews_plus_id = {R.id.imageView_plus1, R.id.imageView_plus2, R.id.imageView_plus3,
                                R.id.imageView_plus4, R.id.imageView_plus5, R.id.imageView_plus6,
                                R.id.imageView_plus7, R.id.imageView_plus8, R.id.imageView_plus9,
                                R.id.imageView_plus10, R.id.imageView_plus11, R.id.imageView_plus12};
    int[] framelayouts_id = {R.id.framelayout1, R.id.framelayout2, R.id.framelayout3,
            R.id.framelayout4, R.id.framelayout5, R.id.framelayout6,
            R.id.framelayout7, R.id.framelayout8, R.id.framelayout9,
            R.id.framelayout10, R.id.framelayout11, R.id.framelayout12};
    TableRow tableRow1;
    TableRow tableRow2;
    TableRow tableRow3;
    TableRow tableRow4;
    TableRow[] tableRows;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        imageViews_plus = new ImageView[]{findViewById(imageViews_plus_id[0]), findViewById(imageViews_plus_id[1]), findViewById(imageViews_plus_id[2]),
                                    findViewById(imageViews_plus_id[3]), findViewById(imageViews_plus_id[4]), findViewById(imageViews_plus_id[5]),
                                    findViewById(imageViews_plus_id[6]), findViewById(imageViews_plus_id[7]), findViewById(imageViews_plus_id[8]),
                                    findViewById(imageViews_plus_id[9]), findViewById(imageViews_plus_id[10]), findViewById(imageViews_plus_id[11])};

        frameLayouts = new FrameLayout[]{findViewById(framelayouts_id[1]), findViewById(framelayouts_id[1]), findViewById(framelayouts_id[2]),
                findViewById(framelayouts_id[3]), findViewById(framelayouts_id[4]), findViewById(framelayouts_id[5]),
                findViewById(framelayouts_id[6]), findViewById(framelayouts_id[7]), findViewById(framelayouts_id[8]),
                findViewById(framelayouts_id[9]), findViewById(framelayouts_id[10]), findViewById(framelayouts_id[11])};
        for(int i=0;i<imageViews_plus_id.length;i++){
//            imageViews_plus[i] = findViewById(imageViews_plus_id[i]);
            imageViews_plus[i].setOnClickListener(this::onClickPlus);

        }
        tableRow1 = findViewById(R.id.tableRow1);
        tableRow2 = findViewById(R.id.tableRow2);
        tableRow3 = findViewById(R.id.tableRow3);
        tableRow4 = findViewById(R.id.tableRow4);

        tableRows = new TableRow[]{tableRow1,tableRow2, tableRow3, tableRow4};

    }

    public void onClickPlus(View v){
        intent = new Intent(this, AddPopupActivity.class);
//        startActivity(intent);
        intent.putExtra("btn_id",v.getId());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                String name = data.getStringExtra("name");
                int btn_id = data.getIntExtra("btn_id",0);
                for(int i=0;i<imageViews_plus_id.length;i++){
                    if(imageViews_plus_id[i]==btn_id){
                        imageViews_plus[i].setVisibility(View.INVISIBLE);
                        if((i+1)%3==0 && i!=imageViews_plus_id.length-1){
                            tableRows[(i+1)/3+1].setVisibility(View.VISIBLE);
                            break;
                        }
                        if(i!=imageViews_plus_id.length-1){
                            frameLayouts[i+1].setVisibility(View.VISIBLE);
                        }

                        break;
                    }
                }

            }
        }

    }

}