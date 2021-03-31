package com.example.educate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class AdapterTopicList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

private List<TopicClass> items = new ArrayList<>();

private Context ctx;
private com.example.educate.AdapterTopicList.OnItemClickListener mOnItemClickListener;
private com.example.educate.AdapterTopicList.OnItemClickListener mOnItemClickListener1;

public interface OnItemClickListener {
    void onItemClick(View view, TopicClass obj, int position);
}

    public interface OnItemClickListener1 {
        void onItemClick1(View view, TopicClass obj, int position);
    }

    public void setOnItemClickListener(final com.example.educate.AdapterTopicList.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public void setOnItemClickListener1(final com.example.educate.AdapterTopicList.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener1 = mItemClickListener;
    }


    public AdapterTopicList(Context context, List<TopicClass> items) {
        this.items = items;
        ctx = context;
    }

public class OriginalViewHolder extends RecyclerView.ViewHolder {
    public TextView name,index,desc;
    public CardView layout_parent;
    public ImageButton enter_button;

    public OriginalViewHolder(View v) {
        super(v);
        name = (TextView) v.findViewById(R.id.item_topic_name);
        index = v.findViewById(R.id.topic_index);
        desc = v.findViewById(R.id.topic_desc);
        layout_parent = v.findViewById(R.id.lyt_parent1);

        enter_button = v.findViewById(R.id.topic_enter);
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
            view.desc.setText(p.description);
            view.index.setText(String.valueOf(position+1));

            if(p.type.equals("link")){
                view.enter_button.setImageResource(R.drawable.ic_baseline_video_library_24);
            }
            else
            {
                view.enter_button.setImageResource(R.drawable.ic_baseline_insert_drive_file_24);

            }

            //    Tools.displayImageRound(ctx, view.image, p.image);
            view.layout_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

            view.enter_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener1 != null) {
                        mOnItemClickListener1.onItemClick(view, items.get(position), position);
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
