package com.example.dailymaple;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private final int resource;
    private Context context;
    private int[] list;
    private String character;
    private boolean daily;

    ArrayList<Boolean> isAlert = new ArrayList<>();

    String platform;
    String userId;
    DocumentReference path;
    String route;

    public RecyclerAdapter(Context context, @AnyRes int resource, boolean daily, String character) {
        this.resource = resource;
        this.context = context;
        this.character = character;
        this.daily = daily;

        if(daily) {
            list = Constants.DailyContentsLayoutId;
        } else {
            list = Constants.WeeklyContentsLayoutId;
        }

        platform = PreferenceHelper.getString(context.getApplicationContext(), Constants.SHARED_PREF_PLATFORM_KEY);
        userId = PreferenceHelper.getString(context.getApplicationContext(), Constants.SHARED_PREF_USER_KEY);

        // 데이터 불러오는 스레드
        path = FirebaseFirestore.getInstance().collection(platform+"_users")
                .document(userId)
                .collection("characters")
                .document(character);

        Log.d("path :", platform+"_users/" + userId+ "/characters/" + character);

        Thread thread = new Thread() {
            public void run() {
                try {
                    Log.d("thread :", "start thread");
                    if(daily) {
                        route = "daily_contents_alert";
                    }
                    else {
                        route = "weekly_contents_alert";
                    }

                    Log.d("thread : ", route);
                    Task task = path.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                Log.d("thread : ", "DocumentSnapshot data: " + document.getData().get(route));
                                if (document.exists()) {
                                    isAlert = (ArrayList<Boolean>) document.getData().get(route);
                                    Log.d("get data :", "complete " + isAlert.size());
                                } else {
                                    Log.d("TAG", "No such document");
                                }
                            }
                            else {
                                Log.d("Error", "get data failed");
                            }
                        }
                    });

                    // onComplete가 끝날때 까지 대기
                    Tasks.await(task);
                } catch (Exception e) {
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
        Log.d("ccc", "data loading complete");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("ccc", "view holder create");
        View view = LayoutInflater.from(context).inflate(resource, null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("asfd", "bind");
        final int item = getItem(position);

        holder.img.setImageResource(item);
        
        // 초기 색상 지정
        switcher(holder.img, isAlert.get(position));
        
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click!", "Clear 표시");
                isAlert.set(position, !isAlert.get(position));
                switcher(holder.img, isAlert.get(position));

                // 변경된 정보 업데이트
                Map<String, Object> data = new HashMap<>();
                data.put(route, isAlert);
                path.set(data, SetOptions.merge());
            }
        });
    }

    private void switcher(ImageView iv, boolean change) {
        if(change) {
            iv.setColorFilter(null);
            iv.setImageAlpha(255);
        } else {
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);

            ColorFilter colorFilter = new ColorMatrixColorFilter(matrix);
            iv.setColorFilter(colorFilter);
            iv.setImageAlpha(200);
        }
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    private int getItem(int position) {
        return list[position];
    }

//    public void clear() {
//        if(null != list) {
//            list = new int[];
//        }
//    }

    public void addAll(int[] list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final FrameLayout frameLayout;
        public final ImageView img;

        public ViewHolder(View parent) {
            super(parent);

            frameLayout = (FrameLayout) parent.findViewById(R.id.recycle_frame);
            img = (ImageView) parent.findViewById(R.id.recycler_img);
        }
    }
}