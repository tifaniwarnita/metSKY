package com.tifaniwarnita.metsky.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.tifaniwarnita.metsky.R;

import java.util.HashMap;

/**
 * Created by Tifani on 3/30/2016.
 */
public class HomeCarouselGraphView extends BaseSliderView {
    private HashMap<String,View> file_maps;
    private int currentId = 0;

    public HomeCarouselGraphView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).
                inflate(R.layout.template_home_carousel_graph, null);
        bindEventAndShow(v, new ImageView(getContext()));
        return v;
    }
}