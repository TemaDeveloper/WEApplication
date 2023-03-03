package com_we.java_we.weapplication.bottomFragments;

import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import com_we.java_we.weapplication.R;

public class LearnMoreFragment extends BottomSheetDialogFragment {

    //widgets
    private PieChart chart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_learn_more, container, false);
        chart = view.findViewById(R.id.pie_chart);
        setUpDataChart();
        loadPieChartData();
        return view;
    }

    private void loadPieChartData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(0.32f, ""));
        entries.add(new PieEntry(0.28f, ""));
        entries.add(new PieEntry(0.32f, ""));
        entries.add(new PieEntry(0.03f, ""));
        entries.add(new PieEntry(0.05f, ""));

        chart.setCenterText("100%");
        chart.setCenterTextSize(20);
        chart.setCenterTextColor(R.color.black);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.dark_blue));
        colors.add(getResources().getColor(R.color.purple_200));
        colors.add(getResources().getColor(R.color.green));
        colors.add(getResources().getColor(R.color.yellow));
        colors.add(getResources().getColor(R.color.red));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(false);
        data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        chart.setData(data);
        chart.invalidate();

        chart.animateY(1400, Easing.EaseInOutQuad);
    }

    private void setUpDataChart(){

        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        chart.animateY(1400, Easing.EaseInOutQuad);

        Legend l = chart.getLegend();
        l.setEnabled(false);

        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTextSize(12f);

    }

}