package com.example.user.emergencycall.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.user.emergencycall.R;
import com.example.user.emergencycall.Services.BackgroundService;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class GraphFragment extends Fragment {

    public static final String mBroadcastFloatAction = "com.truiton.broadcast.float";
    BroadcastReceiver mReceiver;
    Activity activity;
    double data = 0d;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        final LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        series.setColor(Color.RED);

        graph.setBackgroundColor(Color.rgb(246, 246, 246));
        graph.addSeries(series);
        Viewport viewport = graph.getViewport();
        viewport.setXAxisBoundsManual(true);
        viewport.setMinX(17);
        viewport.setMaxX(20);
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(-80);
        viewport.setMaxY(80);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);


        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mBroadcastFloatAction);

        activity = getActivity();
        Intent serviceIntent = new Intent(activity, BackgroundService.class);

        mReceiver = new BroadcastReceiver() {

            private double counter = 0;

            @Override
            public void onReceive(Context context, Intent intent) {

                try {
                    counter += 0.04;
                    double y = (double) intent.getFloatExtra("Data", 0);
                    if (data < y) {
                        data = y;
                    }
                    series.appendData(new DataPoint(counter, y), true, 100);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        activity.startService(serviceIntent);
        activity.registerReceiver(mReceiver, mIntentFilter);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (activity != null && mReceiver != null) {
            activity.unregisterReceiver(mReceiver);
        }
    }

}