package com.farplace.numberpickerview.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;


import com.farplace.numberpickerview.R;
import com.farplace.numberpickerview.adpater.NumberPickerAdapter;

import java.util.Arrays;

public class NumberPickerView extends LinearLayout {
    ViewPager2 viewPager2;
    float previousValue = 0f;
    ValueAnimator animator;
    int nextItem;
    boolean handScroll = true;
    int scroll_old;
    MediaPlayer mediaPlayer;
    AssetFileDescriptor descriptor;

    public NumberPickerView(Context context) {
        super(context);
        initView();
        initData();
    }

    private void initData() {
        try {
            mediaPlayer = new MediaPlayer();
            descriptor = getContext().getAssets().openFd("ticker.wav");
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            mediaPlayer.prepare();
        } catch (Exception e) {
            Log.e("TEST===>>", e.toString());
            e.printStackTrace();
        }
    }

    public NumberPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initData();
    }

    public void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.numberpicker_layout, this, false);
        viewPager2 = view.findViewById(R.id.number_picker);
        NumberPickerAdapter numberPickerAdapter = new NumberPickerAdapter(getContext());
        viewPager2.setAdapter(numberPickerAdapter);
        Integer[] numbers = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        numberPickerAdapter.insertDatas(Arrays.asList(numbers));
        addView(view);
    }

    public void setValue(Integer v) {
        setCurrentItem(v);
    }

    public int getValue() {
        return viewPager2.getCurrentItem();
    }

    public void setOnScrollListener(ViewPager2.OnPageChangeCallback onPageChangeCallback) {
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position != scroll_old) {
                    playWav();
                    scroll_old = position;

                }
            }

            @Override
            public void onPageSelected(int position) {
                if (handScroll) {
                    super.onPageSelected(position);
                    onPageChangeCallback.onPageSelected(position);
                }
            }
        });


    }

    private void playWav() {
        new Thread(() -> {
            try {

                mediaPlayer.start();
            } catch (Exception e) {
                Log.e("TEST===>>", e.getMessage().toString());
            }

        }).start();
    }

    public void setCurrentItem(int item) {
        previousValue = 0f;
        nextItem = item;
        handScroll = false;
        if (animator != null && animator.isRunning()) {
            return;
        }
        float pxToDrag = viewPager2.getWidth() * (item - viewPager2.getCurrentItem());
        animator = ValueAnimator.ofFloat(0, pxToDrag);
        animator.addUpdateListener(animation -> {
            float currentValue = (float) animation.getAnimatedValue();
            float currentPxToDrag = (currentValue - previousValue);
            viewPager2.fakeDragBy(-currentPxToDrag);
            if (previousValue != currentValue) previousValue = currentValue;

        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {
                viewPager2.beginFakeDrag();
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                viewPager2.endFakeDrag();

                viewPager2.postDelayed(() -> {
                    if (viewPager2.getCurrentItem() != nextItem) setCurrentItem(nextItem);
                    else handScroll = true;
                }, 100);

            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {

            }
        });
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(500);
        animator.start();
    }
}
