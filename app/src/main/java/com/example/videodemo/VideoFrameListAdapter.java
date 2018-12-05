package com.example.videodemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;

public class VideoFrameListAdapter extends RecyclerView.Adapter<VideoFrameListAdapter.VideoFrameItemHolder> {

    private ArrayList<VideoMotionData> videoMotionDataArrayList;
    private ArrayList<String> labelList;
    private Context context;

    public VideoFrameListAdapter(ArrayList<VideoMotionData> videoMotionDataArrayList, ArrayList<String> labelList, Context context) {
        this.videoMotionDataArrayList = videoMotionDataArrayList;
        this.labelList = labelList;
        this.context = context;
    }

    public void setLabelList(ArrayList<String> labelList) {
        this.labelList = labelList;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @NonNull
    @Override
    public VideoFrameItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new VideoFrameItemHolder(LayoutInflater.from(context).inflate(R.layout.layout_footage_item, null, false));
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull VideoFrameItemHolder holder, int position) {
//        if(position != RecyclerView.NO_POSITION){
        // Do your binding here

        holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, videoMotionDataArrayList.get(position).getImageOfVideoFrame()));
        holder.imageView.setVisibility(View.GONE);
        holder.tvForTitle.setText(videoMotionDataArrayList.get(position).getTitle());
        holder.tvforTimeStamp.setText(videoMotionDataArrayList.get(position).getTimeStamp());
        holder.tvforTimeFrame.setText("Video frame time : " + videoMotionDataArrayList.get(position).getTimeFrame() + " sec");
        if (videoMotionDataArrayList.get(position).getTimeFrame() >= 47) {
            TextView tv = new TextView(context);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMarginEnd(10);
            tv.setLayoutParams(params);
            tv.setText("falling");
            tv.setTextSize(12);
            tv.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_for_video_label_tv));
//                tv.setPadding(5,5,5,5);
            holder.flexboxLayout.addView(tv);
        }else{
           if (holder.flexboxLayout.getChildCount()>labelList.size())
               holder.flexboxLayout.removeViewAt(labelList.size());
        }
//        }
    }

    @Override
    public int getItemCount() {
        return videoMotionDataArrayList.size();
    }

    public class VideoFrameItemHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvforTimeStamp, tvForTitle, tvforTimeFrame;
        FlexboxLayout flexboxLayout;


        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public VideoFrameItemHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgAtFootageItem);
            tvforTimeFrame = itemView.findViewById(R.id.tvForMotionDurationAtITem);
            tvforTimeStamp = itemView.findViewById(R.id.tvForTimeStampAtITem);
            tvForTitle = itemView.findViewById(R.id.tvForHeadingAtITem);
            flexboxLayout = itemView.findViewById(R.id.flexlayoutForLabels);
            for (String label : labelList) {
                TextView tv = new TextView(context);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMarginEnd(10);
                tv.setLayoutParams(params);
                tv.setText(label);
                tv.setTextSize(12);
                tv.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_for_video_label_tv));
//                tv.setPadding(5,5,5,5);
                flexboxLayout.addView(tv);
            }
        }
    }
}
