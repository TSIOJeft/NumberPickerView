package com.farplace.numberpickerview.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.viewpager2.widget.ViewPager2;


import com.farplace.numberpickerview.R;

import java.math.BigDecimal;

public class MultipleNumberPickerView extends FrameLayout {
    private int currentFullCount = 3;
    private LinearLayoutCompat parentView;
    private onValueChange valueChange;
    private NumberPickerView decimalNumberView;

    public MultipleNumberPickerView(@NonNull Context context) {
        super(context);
    }

    public void setValueChange(onValueChange valueChange) {
        this.valueChange = valueChange;
    }

    public interface onValueChange {
        void valueChange(double value);
    }

    public MultipleNumberPickerView(@NonNull Context context, @NonNull AttributeSet attrRes) {
        super(context, attrRes);
        inflate(context, R.layout.mutiple_numberpicker_layout, this);
        parentView = findViewById(R.id.parent_view);
        decimalNumberView = findViewById(R.id.decimal_number_view);
        decimalNumberView.setOnScrollListener(onPageChangeCallback);
        TransitionManager.beginDelayedTransition(parentView, new AutoTransition());
        parentView.addView(getChildPickerView(), 0);
        parentView.addView(getChildPickerView(), 0);
        parentView.addView(getChildPickerView(), 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setValue(double value) {
        int full = (int) value;
        int decimalF = (int) (BigDecimal.valueOf(value).subtract(BigDecimal.valueOf(full)).doubleValue() * 10);
        decimalNumberView.setValue(decimalF);
        int fullCount = String.valueOf(full).length();
//        TransitionManager.beginDelayedTransition(parentView, new Slide());
        if (fullCount > 1) {
            for (int i = currentFullCount; i < fullCount; i++) {
                parentView.addView(getChildPickerView(), 0);
                currentFullCount++;
            }
        }

        for (int i = currentFullCount - 1; i >= 0; i--) {
            NumberPickerView numberPickerView = (NumberPickerView) parentView.getChildAt(i);
            int v = full % 10;
            full = full / 10;
            numberPickerView.setValue(v);
        }
    }

    public void bindEditText(EditText count) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                double value = 0;
                if (s.length() > 0) value = Double.parseDouble(s.toString());
                setValue(value);
            }
        };
        count.addTextChangedListener(textWatcher);
        setValueChange(value -> {
            count.removeTextChangedListener(textWatcher);
            count.setText(String.format("%.1f", value));
            count.setSelection(String.valueOf(value).length());
            count.addTextChangedListener(textWatcher);
        });
    }

    private ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            double value = 0;
            int base = 1;
            for (int i = currentFullCount - 1; i >= 0; i--) {
                NumberPickerView numberPickerView = (NumberPickerView) parentView.getChildAt(i);
                value += numberPickerView.getValue() * base;
                base = base * 10;
            }
            NumberPickerView numberPickerView = (NumberPickerView) parentView.getChildAt(parentView.getChildCount() - 1);
            value += numberPickerView.getValue() * 0.1;
            if (valueChange != null) valueChange.valueChange(value);
        }
    };

    private NumberPickerView getChildPickerView() {
        NumberPickerView numberPickerView = new NumberPickerView(getContext());
        numberPickerView.setLayoutParams(decimalNumberView.getLayoutParams());
        numberPickerView.setOnScrollListener(onPageChangeCallback);
        return numberPickerView;
    }
}
