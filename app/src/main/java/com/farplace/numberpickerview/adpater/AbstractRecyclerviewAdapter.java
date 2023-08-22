package com.farplace.numberpickerview.adpater;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRecyclerviewAdapter<VH extends AbstractRecyclerviewAdapter.AbstractViewHolder<V>, V> extends RecyclerView.Adapter<VH> {
    List<V> data = new ArrayList<>();
    Context context;
    RecyclerView recyclerView;

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public AbstractRecyclerviewAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.context = recyclerView.getContext();
    }

    public AbstractRecyclerviewAdapter(Context context) {
        this.context = context;
    }

    public void insertData(int position, V item) {
        data.add(position, item);
        notifyItemInserted(position);
    }
    public void changeData(int position) {
        notifyItemRangeChanged(0, 2);
    }

    public void setData(List<V> datas) {
        this.data.clear();
        this.data.addAll(datas);
        notifyItemRangeChanged(0, datas.size());
    }

    public void insertDatas(List<V> datas) {
        this.data.addAll(datas);
        notifyItemRangeChanged(0, datas.size());
    }

    public void replaceData(List<V> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyItemRangeChanged(0, data.size());
    }

    public List<V> getData() {
        return data;
    }

    public V getData(int position) {
        return data.get(position);
    }

    public void remove(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }


    public void clear() {
        notifyItemRangeRemoved(0, getItemCount());
        data.clear();

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class AbstractViewHolder<V> extends RecyclerView.ViewHolder implements View.OnClickListener {
        AbstractRecyclerviewAdapter<? extends AbstractViewHolder<V>, V> adapter;

        public AbstractViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public AbstractViewHolder(AbstractRecyclerviewAdapter<? extends AbstractViewHolder<V>, V> abstractRecyclerviewAdapter, @NonNull View itemView) {
            super(itemView);
            this.adapter = abstractRecyclerviewAdapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (adapter.onItemClickListener != null)
                adapter.onItemClickListener.onClick(v, getAdapterPosition());
        }
    }
}
