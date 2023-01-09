package com.example.asessucm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GraphFragment extends Fragment {

    private GraphView graph;
    private LineGraphSeries<DataPoint> series;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_graph, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        graph = (GraphView) view.findViewById(R.id.graph);
       series = new LineGraphSeries<DataPoint>(new DataPoint[] {
        });
        graph.addSeries(series);
    }

    /**
     * Adds a new datapoint to the graph
     * @param x is the value in the x-axis. Should be shoulder angle from internal sensors
     * @param y is the value in the y-axis. Should be acceleration values from Movesense Device.
     *
     */
    public void addDataPoint(double x, double y){

    }
}
