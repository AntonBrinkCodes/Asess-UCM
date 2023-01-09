package com.example.asessucm;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GraphFragment extends Fragment {
    private Viewport internalViewport, BTViewport;
    private GraphView internalGraph, BTGraph;
    int BTCounter = 0;
    private LineGraphSeries<DataPoint> BTseries, internalSeries;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_graph, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        BTGraph = (GraphView) view.findViewById(R.id.bt_graph);
        internalGraph = (GraphView) view.findViewById(R.id.internal_graph) ;
        BTViewport = BTGraph.getViewport();
        BTViewport.setScrollable(true);
        BTViewport.setYAxisBoundsManual(true);
        BTViewport.setXAxisBoundsManual(true);
        BTViewport.setMinX(0);

        internalViewport = internalGraph.getViewport();
        internalViewport.setScrollable(true);
        internalViewport.setYAxisBoundsManual(true);
        internalViewport.setXAxisBoundsManual(true);
        internalViewport.setMinX(0);
        internalViewport.setMaxY(45);

       BTseries = new LineGraphSeries<DataPoint>(new DataPoint[] {});
        BTGraph.addSeries(BTseries);
        internalSeries = new LineGraphSeries<DataPoint>(new DataPoint[]{});
        internalGraph.addSeries(internalSeries);
    }

    /**
     * Adds a new datapoint to the graph
     * @param x is the value in the x-axis. Should be shoulder angle from internal sensors
     * @param y is the value in the y-axis. Should be acceleration values from Movesense Device.
     *
     */
    public void addDataPointBT(double x, double[] y, int pointsPlotted){
        int revCounter = 4;
        for(int i = 0;i<4;i++) {
            BTseries.appendData(new DataPoint(BTCounter, y[i]), true, pointsPlotted);
            BTViewport.setMaxX(pointsPlotted-revCounter);
            BTCounter++;
            revCounter--;
        }
    }

    public void addDataPointInternal(double x, double y, int pointsPlotted){
        internalSeries.appendData(new DataPoint(x,y), true, pointsPlotted);
        internalViewport.setMaxX(pointsPlotted);
    }
}
