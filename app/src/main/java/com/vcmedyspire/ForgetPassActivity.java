package com.vcmedyspire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.customClass.MySliderAdapter;
import com.customClass.MySliderList;
import com.util.MyRetrofit;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ForgetPassActivity extends Activity {

    ViewPager2 viewPager;
    List<MySliderList> mySliderLists;
    MySliderAdapter adapter;
    private static int currentPage = 0;
    private static int NUM_PAGES = 3;
    LinearLayout indicatorlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        viewPager = findViewById(R.id.viewPager);
    //    indicatorlay=(LinearLayout)findViewById(R.id.lay_indicator);

        getdata();
/*
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setupCurrentIndicator(position);
            }
        });
*/
        //NUM_PAGES =onBordingLists.size();
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

    }

    private void getdata() {
        Call<List<MySliderList>> call= MyRetrofit.getInstance().getMyApi().getonbordingdata();
        call.enqueue(new Callback<List<MySliderList>>() {
            @Override
            public void onResponse(Call<List<MySliderList>> call, Response<List<MySliderList>> response) {
                mySliderLists=response.body();
                adapter=new MySliderAdapter(ForgetPassActivity.this,mySliderLists,viewPager);

                viewPager.setAdapter(adapter);
                setupIndicator();
                setupCurrentIndicator(0);
            }

            @Override
            public void onFailure(Call<List<MySliderList>> call, Throwable t) {

            }
        });
    }



    private void setupIndicator() {
        ImageView[] indicator=new ImageView[adapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
       layoutParams.setMargins(4,0,4,0);
        for (int i=0; i<indicator.length; i++){
            indicator[i]=new ImageView(getApplicationContext());indicator[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.indicator_inactive));
            indicator[i].setLayoutParams(layoutParams);
            indicatorlay.addView(indicator[i]);
        }

    }
    private void setupCurrentIndicator(int index) {
        int itemcildcount=indicatorlay.getChildCount();
        for (int i=0; i<itemcildcount; i++){
            ImageView imageView=(ImageView)indicatorlay.getChildAt(i);
            if (i==index){ imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.indicator_active));
            }else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.indicator_inactive));
            }
        }
    }




}