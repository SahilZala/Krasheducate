package com.example.educate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterSubjectList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SubjectClass> items = new ArrayList<>();

    private Context ctx;
    private com.example.educate.AdapterSubjectList.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view,SubjectClass obj, int position);
    }

    public void setOnItemClickListener(final com.example.educate.AdapterSubjectList.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterSubjectList(Context context, List<SubjectClass> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public View lyt_parent;
        public Button subject_enter;

        public OriginalViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.item_subject_name);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            subject_enter = v.findViewById(R.id.subject_enter);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_recyclerview_item, parent, false);
        vh = new com.example.educate.AdapterSubjectList.OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof com.example.educate.AdapterSubjectList.OriginalViewHolder) {
            com.example.educate.AdapterSubjectList.OriginalViewHolder view = (com.example.educate.AdapterSubjectList.OriginalViewHolder) holder;

            SubjectClass p = items.get(position);
            view.name.setText(p.getName());

            //    Tools.displayImageRound(ctx, view.image, p.image);
            view.subject_enter.setOnClickListener(new View.OnClickListener() {
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