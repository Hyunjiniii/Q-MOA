package com.example.q_moa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toolbar;

public class LocationCheckActivity extends AppCompatActivity {

    ArrayAdapter<CharSequence> adapter1, adapter2;
    Spinner spinner1, spinner2;
    Button onBtn, unBtn;
    ImageButton btn_back;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_check);

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
                            text = adapter2.getItem(position).toString();
                            btn_back.setVisibility(View.INVISIBLE);
                            unBtn.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    }) ;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


}
