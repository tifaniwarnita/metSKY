package com.tifaniwarnita.metsky;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.tifaniwarnita.metsky.controllers.DatabaseHandler;
import com.tifaniwarnita.metsky.models.Cuaca;
import com.tifaniwarnita.metsky.views.HomeCarouselCameraView;
import com.tifaniwarnita.metsky.views.HomeCarouselGraphView;
import com.tifaniwarnita.metsky.views.HomeCarouselWeatherView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private static final int REQUEST_LOCATION = 2;
    private static final String ARG_EMOTION = "emotion";
    private String emotion = "";

    private HomeFragmentListener homeFragmentListener;
    private TextView textViewLokasi;
    private TextView textViewDerajat;
    private TextView textViewKelembaban;
    private ImageView imageViewAwan;
    private ImageView imageViewArahAngin;
    private TextView textViewKecepatanAngin;
    private ImageView imageViewMood;
    private TextView textViewInfoWaktu;
    private SliderLayout slider;
    HomeCarouselWeatherView carouselWeatherView = null;

    private Intent intent;

    public interface HomeFragmentListener {
        public void onCameraButtonClicked();
    }

    public static HomeFragment newInstance(String emotion) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EMOTION, emotion);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            emotion = getArguments().getString(ARG_EMOTION);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home, container, false);

        textViewLokasi = (TextView) v.findViewById(R.id.home_textview_lokasi);
        textViewDerajat = (TextView) v.findViewById(R.id.home_textview_derajat);
        textViewKelembaban = (TextView) v.findViewById(R.id.home_textview_kelembaban);
        imageViewAwan = (ImageView) v.findViewById(R.id.home_imageview_awan);
        imageViewArahAngin = (ImageView) v.findViewById(R.id.home_imageview_arahangin);
        textViewKecepatanAngin = (TextView) v.findViewById(R.id.home_textview_kecepatanangin);
        imageViewMood = (ImageView) v.findViewById(R.id.home_imageview_mood);
        textViewInfoWaktu = (TextView) v.findViewById(R.id.home_textview_infowaktu);

        final HomeFragment homeFragment = this;
        final Activity homeActivity = getActivity();

        DatabaseHandler dbHandler = new DatabaseHandler(getContext());
        try {
            dbHandler.createDB();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dbHandler.openDB();
        Cuaca cuaca = dbHandler.getCuaca();

        updateUI(cuaca);

        slider = (SliderLayout) v.findViewById(R.id.home_carouselslider);
        List<BaseSliderView> carouselObject = new ArrayList<>();
        carouselObject.add(new HomeCarouselGraphView(getActivity()));
        carouselWeatherView = new HomeCarouselWeatherView(getActivity(), cuaca);
        carouselObject.add(carouselWeatherView);
        HomeCarouselCameraView carouselCameraView = new HomeCarouselCameraView(getActivity());
//        carouselCameraView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
//            @Override
//            public void onSliderClick(BaseSliderView slider) {
//                homeFragmentListener.onCameraButtonClicked();
//            }
//        });
        carouselObject.add(carouselCameraView);

        for(int i=0; i<carouselObject.size(); i++){
            // initialize a SliderLayout
            slider.addSlider(carouselObject.get(i));
            slider.setPresetTransformer(SliderLayout.Transformer.FlipHorizontal);
            slider.setCustomIndicator((PagerIndicator) v.findViewById(R.id.home_customindicator));
        }

        slider.stopAutoCycle();

        intent = new Intent(getActivity(), WeatherInformationService.class);
        WeatherInformationService.initialize(homeFragment, homeActivity);
        getActivity().startService(intent);
        return v;
    }

    public void updateUI(Cuaca cuaca) {
        textViewLokasi.setText(cuaca.getKota());
        textViewDerajat.setText(String.valueOf(cuaca.getCurrentSuhu()));
        textViewKelembaban.setText(String.valueOf(cuaca.getCurrentKelembaban()) + "%");

        Context context = imageViewAwan.getContext();
        int id = context.getResources().getIdentifier("icon_" + cuaca.getCurrentAwan() + "64",
                "drawable", context.getPackageName());
        imageViewAwan.setImageResource(id);

        context = imageViewArahAngin.getContext(); //TODO NUNGGU ANGIN
        // id = context.getResources().getIdentifier("icon_arrow_" + cuaca.getCurrentArahAngin(),
                // "drawable", context.getPackageName());
        // imageViewArahAngin.setImageResource(id);

        textViewKecepatanAngin.setText(String.valueOf(cuaca.getCurrentKecepatanAngin()) + "m/s");

        context = imageViewMood.getContext();
        id = context.getResources().getIdentifier("icon_" + emotion + "48",
                "drawable", context.getPackageName());
        imageViewMood.setImageResource(id);

        textViewInfoWaktu.setText("Dikeluarkan: " + cuaca.getDikeluarkan() + "\nBerlaku mulai: " + cuaca.getBerlaku());

        if(carouselWeatherView != null) {
            carouselWeatherView.updateAwanWaktu(cuaca);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            homeFragmentListener = (HomeFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement HomeFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        homeFragmentListener = null;
        getContext().stopService(intent);
    }

}
