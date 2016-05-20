package com.tifaniwarnita.metsky.views.templates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.tifaniwarnita.metsky.R;

import java.util.HashMap;

/**
 * Created by Tifani on 3/30/2016.
 */
public class HomeCarouselCameraView extends BaseSliderView {
    private View v;
    private ImageView imageView = new ImageView(getContext());
    private ImageButton imageButtonCamera;
    private CameraViewListener cameraViewListener;

    public interface CameraViewListener {
        public void onCameraButtonClicked();
    }

    public HomeCarouselCameraView(Context context) {
        super(context);
    }

    public ImageView getImage() {
        return imageView;
    }

    @Override
    public View getView() {
        if (v == null) {
            v = LayoutInflater.from(getContext()).
                    inflate(R.layout.template_home_carousel_camera, null);
        }
        bindEventAndShow(v,imageView);
        return v;
    }
}