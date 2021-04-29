package com.fcodex.videoplayer.RecyclerViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fcodex.videoplayer.Modal.Modal;
import com.fcodex.videoplayer.R;
import com.fcodex.videoplayer.Activities.VideoPlayerActivity;

import java.util.ArrayList;

public class VideoRecyclerViewAdapter extends RecyclerView.Adapter<video> {

    private final ArrayList<Modal> videosList;
    private final Context context;

    public VideoRecyclerViewAdapter(Context context, ArrayList<Modal> videosList) {
        this.videosList = videosList;
        this.context = context;
    }

    @NonNull
    @Override
    public video onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_video_item_view, parent, false);
        return new video(view);
    }

    @Override
    public void onBindViewHolder(@NonNull video holder, int position) {
        holder.titleTextView.setText(videosList.get(position).getTitle());
        holder.durationTextView.setText(videosList.get(position).getDuration());
        Glide.with(context).load(videosList.get(position).getData())
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageViewThumbnail);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), VideoPlayerActivity.class);
            intent.putExtra("videoId", videosList.get(position).getId());
            v.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return videosList.size();
    }
}
class video extends RecyclerView.ViewHolder {

    ImageView imageViewThumbnail;
    TextView titleTextView, durationTextView;

    public video(@NonNull View itemView) {
        super(itemView);

        titleTextView = itemView.findViewById(R.id.titleTextView);
        durationTextView = itemView.findViewById(R.id.durationTextView);
        imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail);
    }
}
