package com.example.asessucm;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class ConnectFragment extends Fragment {
    AppCompatButton connectBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_connect, container, false);

    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    connectBtn = view.findViewById(R.id.connect_button);
    connectBtn.setOnClickListener(this::connectBtn);
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void connectBtn(View view) {
        Intent intent = new Intent(getActivity(), BluetoothScanActivity.class);
        startActivity(intent);
    }
}