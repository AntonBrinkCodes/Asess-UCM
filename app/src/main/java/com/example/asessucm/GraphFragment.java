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

       BTseries = new LineGraphSeries<DataPoint>(new DataPoint[] {
        });
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
    public void addDataPoint(double[] x, double[] y, int pointsPlotted){
        int revCounter = 4;
        for(int i = 0;i<4;i++) {

            Log.i("Graph", "x: " + x[i] + " y: "+ y[i] + " nr of points= " + pointsPlotted);
            BTseries.appendData(new DataPoint(x[i], y[i]), true, 45);
            BTViewport.setMaxX(pointsPlotted-revCounter);
            BTGraph.addSeries(series);
            revCounter--;
        }
    }
}
