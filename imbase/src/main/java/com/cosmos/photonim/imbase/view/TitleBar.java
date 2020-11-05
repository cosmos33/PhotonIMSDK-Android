package com.cosmos.photonim.imbase.view;

import android.content.Context;
import androidx.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TitleBar extends FrameLayout {
    @BindView(R2.id.ivLeft)
    ImageView ivLeft;
    @BindView(R2.id.ivRight)
    ImageView ivRight;
    @BindView(R2.id.tvTitle)
    TextView tvTitle;
    @BindView(R2.id.tvLeft)
    TextView tvLeft;
    @BindView(R2.id.tvRight)
    TextView tvRight;

    public TitleBar(Context context) {
        super(context);
        init();
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.view_titlevar, this);
        ButterKnife.bind(this);
    }

    public TitleBar setTitle(String title) {
        tvTitle.setText(title);
        return this;
    }

    public TitleBar setTitle(String title, OnClickListener onClickListener) {
        tvTitle.setText(title);
        tvTitle.setOnClickListener(onClickListener);
        return this;
    }

    public String getTitle(){
        return tvTitle.getText().toString();
    }

    public TitleBar setLeftImageEvent(@DrawableRes int resId, OnClickListener onClickListener) {
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setImageResource(resId);
        ivLeft.setOnClickListener(onClickListener);
        return this;
    }

    public TitleBar setLeftTextEvent(String leftContent, OnClickListener onClickListener) {
        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setText(leftContent);
        tvLeft.setOnClickListener(onClickListener);
        return this;
    }

    public TitleBar setRightImageEvent(@DrawableRes int resId, OnClickListener onClickListener) {
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(resId);
        ivRight.setOnClickListener(onClickListener);
        return this;
    }

    public TitleBar setRightTextEvent(String leftContent, OnClickListener onClickListener) {
        return setRightTextEvent(leftContent, R.color.black, 0, onClickListener);
    }

    public TitleBar setRightTextEvent(String leftContent, int colorId, int drawableId, OnClickListener onClickListener) {
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(leftContent);
        tvRight.setTextColor(colorId);
        tvRight.setBackgroundResource(drawableId);
        tvRight.setOnClickListener(onClickListener);
        return this;
    }


    public TitleBar setLeftVisible(int visible) {
        ivLeft.setVisibility(visible);
        return this;
    }

    public TitleBar setRightVisible(int visible) {
        ivRight.setVisibility(visible);
        return this;
    }


}
