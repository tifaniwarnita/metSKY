package com.tifaniwarnita.metsky;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.tifaniwarnita.metsky.models.CuacaSerializable;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CarouselGraphFragment extends Fragment {
    private static final String ARG_CUACA = "cuaca";

    CuacaSerializable cuaca = null;

    public static CarouselGraphFragment newInstance(CuacaSerializable cuaca) {
        CarouselGraphFragment fragment = new CarouselGraphFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CUACA, cuaca);
        fragment.setArguments(args);
        return fragment;
    }

    public CarouselGraphFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cuaca = (CuacaSerializable) getArguments().getSerializable(ARG_CUACA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_carousel_graph, container, false);
        LineChart chart = (LineChart) v.findViewById(R.id.graphcarousel_chart);
        chart.setDragEnabled(true);
        chart.setPinchZoom(true);
        chart.setDescription("");
        chart.setDescriptionColor(Color.WHITE);
        chart.setBorderColor(Color.WHITE);
        if (cuaca != null) {
            ArrayList<Entry> valsSuhu = new ArrayList<>();
            int i = 0;
            for(String suhu : cuaca.getSuhu()) {
                valsSuhu.add(new Entry(Integer.parseInt(suhu), i));
                i++;
            }
            LineDataSet setSuhu = new LineDataSet(valsSuhu, "Suhu");
            setSuhu.setAxisDependency(YAxis.AxisDependency.LEFT);
            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(setSuhu);
            LineData lineData = new LineData(cuaca.getWaktu(), dataSets);
            chart.setData(lineData);
            chart.invalidate();
        }
        return v;
    }
}
