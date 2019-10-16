package com.example.q_moa.login.login_custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.q_moa.R;

public class Custom_login_btn extends LinearLayout {
    LinearLayout bg;
    ImageView symbol;
    TextView text;

    public Custom_login_btn(Context context) {
        super(context);
        initView();
    }

    public Custom_login_btn(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }


    private void initView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.custom_login_button, this, false);
        addView(v);
        bg = findViewById(R.id.bg);
        symbol = findViewById(R.id.symbol);
        text = findViewById(R.id.text);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.Custom_login_btn, 0, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        int bg_resID = typedArray.getColor(R.styleable.Custom_login_btn_bg, 0);
        bg.setBackgroundColor(bg_resID);
        int symbol_resID = typedArray.getResourceId(R.styleable.Custom_login_btn_symbol, R.drawable.btn_google);
        symbol.setImageResource(symbol_resID);
        String text_string = typedArray.getString(R.styleable.Custom_login_btn_text);
        text.setText(text_string);
        int textColor = typedArray.getColor(R.styleable.Custom_login_btn_textColor, 0);
        text.setTextColor(textColor);
    }

}