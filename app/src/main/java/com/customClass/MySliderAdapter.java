package com.customClass;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.vcmedyspire.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

public class MySliderAdapter extends RecyclerView.Adapter<com.customClass.MySliderAdapter.ViewHolder> {
    private List<com.customClass.MySliderList> mySliderLists;
    private LayoutInflater mInflater;
    private ViewPager2 viewPager;
    Context context;
 
    public MySliderAdapter(Context context, List<com.customClass.MySliderList> mySliderLists, ViewPager2 viewPager) {
        this.mInflater = LayoutInflater.from(context);
        this.mySliderLists = mySliderLists;
        this.viewPager = viewPager;
        this.context=context;
    }
 
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.slider_list, parent, false);
        return new ViewHolder(view);
    }
 
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       final com.customClass.MySliderList ob= mySliderLists.get(position);
        Glide.with(context).load(ob.getImage_url()).into(holder.myimage);


      /*  holder.myimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        Intent intent=new Intent(context,SecondActivity.class);
        intent.putExtra("imageurl",ob.image_url);
        context.startActivity(intent);
            }
        });*/

    }
 
    @Override
    public int getItemCount() {
        return mySliderLists.size();
    }
 
    public class ViewHolder extends RecyclerView.ViewHolder{
       ImageView myimage;
        RelativeLayout relativeLayout;
        Button button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myimage = itemView.findViewById(R.id.myimage);
            relativeLayout = itemView.findViewById(R.id.container);
 
        }
    }
}