package com.example.q_moa.search_fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.q_moa.R;
import com.example.q_moa.activity.InfoActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class SearchHistoryFragment extends Fragment {
    private TextView[] textView = new TextView[5];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.search_history_fragment, container, false);

        int[] text_id = {R.id.search_history_text1, R.id.search_history_text2, R.id.search_history_text3, R.id.search_history_text4, R.id.search_history_text5};

        for (int i = 0; i <= 4; i++) {
            textView[i] = (TextView) rootView.findViewById(text_id[i]);
            textView[i].setSingleLine(true);
            textView[i].setEllipsize(TextUtils.TruncateAt.END);
            textView[i].setSelected(true);
        }


        ArrayList<String> list = getStringArrayPref(getContext(), "history_list");
        if (list != null) {
            for (int i = 0; i <= list.size(); i++) {
                if (list.size() == 0) {
                    for (int j = 0; j <= 4; j++) {
                        textView[i].setVisibility(View.GONE);
                    }
                } else if (list.size() <= 5) {
                    if (i == list.size())
                        break;
                    textView[i].setVisibility(View.VISIBLE);
                    textView[i].setText(list.get(i));
                } else {
                    int j = i % 5;
                    if (i == list.size())
                        break;
                    textView[j].setVisibility(View.VISIBLE);
                    textView[j].setText(list.get(i));
                }
            }
        }

        for (int i = 0; i <= 4; i++) {
            final int finalI = i;
            textView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text = String.valueOf(textView[finalI].getText());
                    Intent intent = new Intent(getContext(), InfoActivity.class);
                    intent.putExtra("certificate", text);
                    startActivity(intent);

                }
            });
        }

        return rootView;
    }

    private ArrayList<String> getStringArrayPref(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }
}
