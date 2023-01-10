package com.example.asessucm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asessucm.Model.ResultItem;
import com.example.asessucm.Model.SensorResultList;
import com.example.asessucm.uiutils.MsgUtils;
import com.example.asessucm.uiutils.NoticeDialogFragment;
import com.example.asessucm.uiutils.ResultRecyclerAdapter;
import com.example.asessucm.utils.FileHandler;

import java.io.File;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity implements NoticeDialogFragment.NoticeDialogListener{
    private ResultRecyclerAdapter recyclerAdapter;
    private FileHandler fileHandler;

    private final String[] dialogString = new String[]{"Delete all Results", "Warning: Results can not be restored", "No", "Yes"};

    AppCompatButton newTestBtn, clearResultsBtn;
    RecyclerView recyclerView;


    ArrayList<ResultItem> results;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        fileHandler = new FileHandler();

        checkResults();


        newTestBtn = findViewById(R.id.new_test_result_btn);
        clearResultsBtn = findViewById(R.id.clear_results_btn);
        newTestBtn.setOnClickListener(this::newTestBtn);

        recyclerAdapter = new ResultRecyclerAdapter(results);
        recyclerView = findViewById(R.id.results_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);

        clearResultsBtn.setOnClickListener(this::clearResultsBtn);

    }

    private void checkResults() {
        if(fileHandler.loadResults(this)==null){
            results = new ArrayList<ResultItem>();

        } else{
            results = (ArrayList<ResultItem>) fileHandler.loadResults(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkResults();
        recyclerAdapter.notifyDataSetChanged();
    }

    private void newTestBtn(View view) {
        Intent intent = new Intent(ResultActivity.this, SensorActivity.class);
        startActivity(intent);
        finish();
    }

    private void clearResultsBtn(View view) {
        NoticeDialogFragment dialog = new NoticeDialogFragment(dialogString);
        dialog.show(getSupportFragmentManager(), "dialog");
    }


    @Override
    public void onDialogPositiveClick() {
        results.clear();
        FileHandler.saveResults(results, this);
        MsgUtils.showToast("Results deleted", this);
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogNegativeClick() {
    //Do nothing!
    }
}
