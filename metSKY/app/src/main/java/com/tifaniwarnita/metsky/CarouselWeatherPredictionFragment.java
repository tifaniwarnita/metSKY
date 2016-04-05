package com.tifaniwarnita.metsky;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tifaniwarnita.metsky.models.Cuaca;
import com.tifaniwarnita.metsky.models.CuacaSerializable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CarouselWeatherPredictionFragment extends Fragment {
    // The fragment initialization parameters
    private static final String ARG_CUACA = "cuaca";

    CuacaSerializable cuaca = null;
    TextView currentDate;
    List<ImageView> imageViewAwanList = new ArrayList<>();
    List<TextView> textViewWaktuList = new ArrayList<>();

    public static CarouselWeatherPredictionFragment newInstance(CuacaSerializable cuaca) {
        CarouselWeatherPredictionFragment fragment = new CarouselWeatherPredictionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CUACA, cuaca);
        fragment.setArguments(args);
        return fragment;
    }

    public CarouselWeatherPredictionFragment() {
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
        View v =  inflater.inflate(R.layout.fragment_carousel_weather_prediction, container, false);
        currentDate = (TextView) v.findViewById(R.id.home_carousel_weathertitle);

        ImageView awan1 = (ImageView) v.findViewById(R.id.home_carousel_awan1);
        imageViewAwanList.add(awan1);
        ImageView awan2 = (ImageView) v.findViewById(R.id.home_carousel_awan2);
        imageViewAwanList.add(awan2);
        ImageView awan3 = (ImageView) v.findViewById(R.id.home_carousel_awan3);
        imageViewAwanList.add(awan3);
        ImageView awan4 = (ImageView) v.findViewById(R.id.home_carousel_awan4);
        imageViewAwanList.add(awan4);
        ImageView awan5 = (ImageView) v.findViewById(R.id.home_carousel_awan5);
        imageViewAwanList.add(awan5);
        ImageView awan6 = (ImageView) v.findViewById(R.id.home_carousel_awan6);
        imageViewAwanList.add(awan6);

        TextView waktu1 = (TextView) v.findViewById(R.id.home_carousel_waktu1);
        textViewWaktuList.add(waktu1);
        TextView waktu2 = (TextView) v.findViewById(R.id.home_carousel_waktu2);
        textViewWaktuList.add(waktu2);
        TextView waktu3 = (TextView) v.findViewById(R.id.home_carousel_waktu3);
        textViewWaktuList.add(waktu3);
        TextView waktu4 = (TextView) v.findViewById(R.id.home_carousel_waktu4);
        textViewWaktuList.add(waktu4);
        TextView waktu5 = (TextView) v.findViewById(R.id.home_carousel_waktu5);
        textViewWaktuList.add(waktu5);
        TextView waktu6 = (TextView) v.findViewById(R.id.home_carousel_waktu6);
        textViewWaktuList.add(waktu6);

        if (cuaca != null) {
            updateAwanWaktu(cuaca);
        }

        return v;
    }

    public void updateAwanWaktu(CuacaSerializable cuaca) {
        ArrayList<ArrayList<String>> awanWaktuList = cuaca.getSixAwanWaktu();
        for (int i=0; i<awanWaktuList.size(); i++) {
            Context context = imageViewAwanList.get(i).getContext();
            int id = context.getResources().getIdentifier("icon_" + awanWaktuList.get(i).get(1) + "64",
                    "drawable", context.getPackageName());
            imageViewAwanList.get(i).setImageResource(id);
            textViewWaktuList.get(i).setText(awanWaktuList.get(i).get(0));
        }

        DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        currentDate.setText(dateFormat.format(date) + " - " + dateFormat.format(c.getTime()));

        if (awanWaktuList.size() <6) {
            for(int i=awanWaktuList.size(); i<6; i++) {
                imageViewAwanList.get(i).setBackgroundColor(Color.TRANSPARENT);
                textViewWaktuList.get(i).setText("");
            }
        }
    }
}
