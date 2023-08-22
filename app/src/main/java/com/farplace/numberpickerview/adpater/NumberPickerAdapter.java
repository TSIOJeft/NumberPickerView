package com.farplace.numberpickerview.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.farplace.numberpickerview.R;

public class NumberPickerAdapter extends AbstractRecyclerviewAdapter<NumberPickerAdapter.mViewHolder, Integer> {

    public NumberPickerAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new mViewHolder(LayoutInflater.from(context).inflate(R.layout.numberpicker_number_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        holder.number.setText(String.valueOf(getData(position)));
    }


    public class mViewHolder extends AbstractRecyclerviewAdapter.AbstractViewHolder<Integer> {
        public TextView number = itemView.findViewById(R.id.number_item);

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}