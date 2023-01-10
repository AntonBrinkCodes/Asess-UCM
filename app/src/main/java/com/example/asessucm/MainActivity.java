package com.example.asessucm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity {
    AppCompatButton testBtn, resultBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Hook views
        testBtn = findViewById(R.id.new_test_main_btn);
        resultBtn = findViewById(R.id.result_main_btn);

        testBtn.setOnClickListener(this::testBtn);
        resultBtn.setOnClickListener(this::resultBtn);

    }

    /**
     * Start activity that shows the results.
     * @param view
     */
    private void resultBtn(View view) {
        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        startActivity(intent);
    }

    /**
     * Go to activity that starts to connect device and start UCM test.
     * @param view
     */
    private void testBtn(View view) {
        Intent intent = new Intent(MainActivity.this, SensorActivity.class);
        startActivity(intent);
    }
}