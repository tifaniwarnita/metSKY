package com.tifaniwarnita.metsky.views;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.tifaniwarnita.metsky.R;
import com.tifaniwarnita.metsky.models.Cuaca;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Tifani on 3/30/2016.
 */
public class HomeCarouselWeatherView extends BaseSliderView {
    private View v;
    Cuaca cuaca;
    TextView currentDate;
    List<ImageView> imageViewAwanList = new ArrayList<>();
    List<TextView> textViewWaktuList = new ArrayList<>();
    ImageView imageView = new ImageView(getContext());

    public HomeCarouselWeatherView(Context context, Cuaca cuaca) {
        super(context);
        this.cuaca = cuaca;
    }

    @Override
    public View getView() {
        if (v == null) {
            v = LayoutInflater.from(getContext()).
                    inflate(R.layout.template_home_carousel_weather, null);

            currentDate = (TextView) v.findViewById(R.id.home_carousel_weathertitle);
            DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH);
            Date date = new Date();
            currentDate.setText(dateFormat.format(date));

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

            updateAwanWaktu(cuaca);

            bindEventAndShow(v, imageView);
        }
        return v;
    }

    public void updateAwanWaktu(Cuaca cuaca) {
        ArrayList<ArrayList<String>> awanWaktuList = cuaca.getSixAwanWaktu();
        for (int i=0; i<awanWaktuList.size(); i++) {
            Context context = imageViewAwanList.get(i).getContext();
            int id = context.getResources().getIdentifier("icon_" + awanWaktuList.get(i).get(1) + "64",
                    "drawable", context.getPackageName());
            imageViewAwanList.get(i).setImageResource(id);
            textViewWaktuList.get(i).setText(awanWaktuList.get(i).get(0));
        }

        if (awanWaktuList.size() <6) {
            for(int i=awanWaktuList.size(); i<6; i++) {
                imageViewAwanList.get(i).setBackgroundColor(Color.TRANSPARENT);
                textViewWaktuList.get(i).setText("");
            }
        }
    }
}