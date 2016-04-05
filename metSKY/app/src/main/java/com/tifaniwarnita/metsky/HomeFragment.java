package com.tifaniwarnita.metsky;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
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
import com.tifaniwarnita.metsky.models.CuacaSerializable;
import com.tifaniwarnita.metsky.views.HomeCarouselCameraView;
import com.tifaniwarnita.metsky.views.HomeCarouselGraphView;
import com.tifaniwarnita.metsky.views.HomeCarouselWeatherView;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

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
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private SliderLayout slider;
    private Cuaca cuaca;
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
        cuaca = dbHandler.getCuaca();

        viewPager = (ViewPager) v.findViewById(R.id.home_carousel_viewpager);
        setupViewPager(viewPager, cuaca);
        //Bind the title indicator to the adapter
        CirclePageIndicator circlePageIndicator = (CirclePageIndicator) v.findViewById(R.id.home_carousel_pageindicator);
        circlePageIndicator.setViewPager(viewPager);

        updateUI(cuaca);

        intent = new Intent(getActivity(), WeatherInformationService.class);
        WeatherInformationService.initialize(homeFragment, homeActivity);
        getActivity().startService(intent);
        return v;
    }

    private void setupViewPager(ViewPager viewPager, Cuaca cuaca) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new CarouselGraphFragment(), "graph");
        adapter.addFragment(CarouselWeatherPredictionFragment.newInstance(new CuacaSerializable(cuaca)), "weather_prediction");
        adapter.addFragment(new CarouselCameraFragment(), "camera");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<android.support.v4.app.Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(android.support.v4.app.Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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

    private void updateWeatherPrediction(Cuaca cuaca) {
        if (adapter.getItem(1) != null) { //weather prediction
            CarouselWeatherPredictionFragment fragment = (CarouselWeatherPredictionFragment) adapter.getItem(1);
            fragment.updateAwanWaktu(new CuacaSerializable(cuaca));
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
