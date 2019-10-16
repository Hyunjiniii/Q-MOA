package com.example.q_moa.lottie_fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.airbnb.lottie.LottieAnimationView;
import com.example.q_moa.R;

public class FifthFragment extends Fragment {
    public static LottieAnimationView fifth_lottie;
    TextView text;

    public FifthFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.lottie_fragment_fifth, container, false);
        fifth_lottie = view.findViewById(R.id.fifth_lottie);
        fifth_lottie.playAnimation();
        fifth_lottie.loop(true);

        text = view.findViewById(R.id.text1);

        String content = text.getText().toString();
        SpannableString spannableString = new SpannableString(content);

        String word = "팁";
        int start = content.indexOf(word);
        int end = start + word.length();

        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FFED4E")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(1.3f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 4
        text.setText(spannableString);

        return view;
    }

}
