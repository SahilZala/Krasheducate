package com.example.educate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class AdapterTopicList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

private List<TopicClass> items = new ArrayList<>();

private Context ctx;
private com.example.educate.AdapterTopicList.OnItemClickListener mOnItemClickListener;

public interface OnItemClickListener {
    void onItemClick(View view, TopicClass obj, int position);
}

    public void setOnItemClickListener(final com.example.educate.AdapterTopicList.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterTopicList(Context context, List<TopicClass> items) {
        this.items = items;
        ctx = context;
    }

public class OriginalViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public View lyt_parent;
    public MaterialButton enter_button;

    public OriginalViewHolder(View v) {
        super(v);
        name = (TextView) v.findViewById(R.id.item_topic_name);
        lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        enter_button = v.findViewById(R.id.enter_button);
    }
}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_recyclerview_item, parent, false);
        vh = new com.example.educate.AdapterTopicList.OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof com.example.educate.AdapterTopicList.OriginalViewHolder) {
            com.example.educate.AdapterTopicList.OriginalViewHolder view = (com.example.educate.AdapterTopicList.OriginalViewHolder) holder;

            TopicClass p = items.get(position);
            view.name.setText(p.name);

            if(p.type.equals("link")){
                view.enter_button.setIconResource(R.drawable.ic_baseline_video_library_24);
            }
            else
            {
                view.enter_button.setIconResource(R.drawable.ic_baseline_insert_drive_file_24);

            }

            //    Tools.displayImageRound(ctx, view.image, p.image);
            view.enter_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
