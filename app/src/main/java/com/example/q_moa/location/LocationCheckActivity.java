package com.example.q_moa.location;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.q_moa.R;

public class LocationCheckActivity extends AppCompatActivity {

    ArrayAdapter<CharSequence> adapter1, adapter2;
    Spinner spinner1, spinner2;
    Button btn, unBtn;
    ImageButton btn_back;
    String text = " ", title;
    double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_check);

//        btn = findViewById(R.id.btn);
        unBtn = findViewById(R.id.unbtn);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);

        adapter1 = ArrayAdapter.createFromResource(this, R.array.location_area, android.R.layout.simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (adapter1.getItem(position).equals("대전")) {
                    adapter2 = ArrayAdapter.createFromResource(LocationCheckActivity.this, R.array.location_area_Daejeon,
                            android.R.layout.simple_spinner_dropdown_item);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adapter2);
                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            text = adapter2.getItem(position).toString();
                            text = parent.getSelectedItem().toString();
                            Log.d("debug1", "onItemSelected: " + text);
                            if (text.equals("군/구 선택")) {
                                unBtn.setBackgroundResource(R.drawable.unbtn_location);
                                unBtn.setClickable(false);
                            } else {
                                unBtn.setBackgroundResource(R.drawable.btn_location);
                                unBtn.setClickable(true);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        unBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.equals("동구")) {
                    lat = 36.349488;
                    lng = 127.454909;
                    title = "대전보건대학교";

                } else if (text.equals("중구")) {
                    lat = 36.350926;
                    lng = 127.454233;
                    title = "한국폴리텍IV대학 둔산평생능력개발원";

                } else if (text.equals("서구")) {
                    lat = 36.310593;
                    lng = 127.407477;
                    title = "한국산업인력공단 대전지역본부";
                }

                Intent intent = new Intent(LocationCheckActivity.this, LocationActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                startActivity(intent);
            }
        });


    }


}
