package com.example.q_moa.search_fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.q_moa.R;
import com.example.q_moa.activity.InfoActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.MyViewHolder> {
    private List<String> names;
    private Context context;

    SearchRecyclerAdapter(Context context, List<String> names) {
        this.context = context;
        this.names = names;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.Name.setText(names.get(position));
        holder.Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InfoActivity.class);
                intent.putExtra("certificate", names.get(position));
                context.startActivity(intent);

                ArrayList<String> list = getStringArrayPref(context, "history_list");
                if (list != null) {
                    for (int i = 0; i <= list.size(); i++) {
                        if (list.size() != 0) {
                            if (i == list.size()) {
                                break;
                            }
                            if (!names.get(position).equals(list.get(i))) {
                                list.add(names.get(position));
                                setStringArrayPref(context, "history_list", list);
                            }

                        }
                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Name;

        MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            Name = (TextView) itemView.findViewById(R.id.search_recycler_text_item);
        }
    }

    public void updateList(List<String> newList) {
        names = new ArrayList<>();
        names.addAll(newList);
        notifyDataSetChanged();
    }

    private void setStringArrayPref(Context context, String key, ArrayList<String> values) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
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
