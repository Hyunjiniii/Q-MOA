package com.example.q_moa.lottie_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.airbnb.lottie.LottieAnimationView;
import com.example.q_moa.R;

public class LastFragment extends Fragment {
    public static LottieAnimationView last_lottie;

    public LastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.lottie_fragment_last, container, false);
        last_lottie = view.findViewById(R.id.last_lottie);
        last_lottie.playAnimation();
        last_lottie.loop(true);

        return view;
    }
}