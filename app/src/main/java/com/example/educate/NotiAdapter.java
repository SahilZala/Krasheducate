package com.example.educate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NotificationClass> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnItemClickListener mOnItemClickListener1;

    public interface OnItemClickListener {
        void onItemClick(View view, NotificationClass obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }


    public NotiAdapter(Context context, List<NotificationClass> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView name,index,desc;
        public CardView layout_parent;

        public OriginalViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.noti_topic_name);
            index = v.findViewById(R.id.noti_index);
            desc = v.findViewById(R.id.noti_desc);
            layout_parent = v.findViewById(R.id.lyt_parent2);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.noti_item, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof NotiAdapter.OriginalViewHolder) {
           NotiAdapter.OriginalViewHolder view = (NotiAdapter.OriginalViewHolder) holder;

            NotificationClass p = items.get(position);
            view.name.setText(p.head);
            view.desc.setText(p.mess);
            view.index.setText(String.valueOf(position+1));



            //    Tools.displayImageRound(ctx, view.image, p.image);
            view.layout_parent.setOnClickListener(new View.OnClickListener() {
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

