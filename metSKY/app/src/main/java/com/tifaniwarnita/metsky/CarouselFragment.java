package com.tifaniwarnita.metsky;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.HashMap;


public class CarouselFragment extends Fragment {

    private Button buttonLanjut;
    private SliderLayout slider;
    private CarouselFragmentListener carouselFragmentListener;

    public interface CarouselFragmentListener {
        public void onLanjutButtonPressed();
    }

    public CarouselFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_carousel, container, false);
        slider = (SliderLayout) v.findViewById(R.id.slider);

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("NOTIF_WELCOME_1", R.drawable.notif_welcome_1);
        file_maps.put("NOTIF_WELCOME_2", R.drawable.notif_welcome_2);
        file_maps.put("NOTIF_WELCOME_3", R.drawable.notif_welcome_3);
        file_maps.put("NOTIF_WELCOME_4", R.drawable.notif_welcome_4);
        file_maps.put("NOTIF_WELCOME_5", R.drawable.notif_welcome_5);
        file_maps.put("NOTIF_WELCOME_6", R.drawable.notif_welcome_6);
        file_maps.put("NOTIF_WELCOME_7", R.drawable.notif_welcome_7);

        for(int i=1; i<=7; i++){
            DefaultSliderView defaultSliderView = new DefaultSliderView(getActivity());
            // initialize a SliderLayout
            defaultSliderView
                    .image(file_maps.get("NOTIF_WELCOME_" + i))
                    .setScaleType(BaseSliderView.ScaleType.CenterInside);

            slider.addSlider(defaultSliderView);
            slider.setPresetTransformer(SliderLayout.Transformer.FlipHorizontal);
            slider.setCustomAnimation(new DescriptionAnimation());
            slider.setCustomIndicator((PagerIndicator) v.findViewById(R.id.custom_indicator));
            slider.setDuration(4000);
        }

        buttonLanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carouselFragmentListener.onLanjutButtonPressed();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        //Make sure that the container activity has implemented
        //the interface
        try {
            carouselFragmentListener = (CarouselFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement CarouselFragmentListener methods");
        }
    }

    @Override
    public void onDetach() {
        slider.stopAutoCycle();
        super.onDetach();
        carouselFragmentListener = null;
    }

    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        slider.stopAutoCycle();
        super.onStop();
    }
}
