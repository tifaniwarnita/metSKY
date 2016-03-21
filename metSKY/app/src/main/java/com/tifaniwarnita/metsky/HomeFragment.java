package com.tifaniwarnita.metsky;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.tifaniwarnita.metsky.controllers.DatabaseHandler;
import com.tifaniwarnita.metsky.models.Cuaca;
import com.tifaniwarnita.metsky.models.InformasiCuaca;

import java.io.IOException;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.functions.Action1;


public class HomeFragment extends Fragment {

    private static final int REQUEST_LOCATION = 2;
    private static final String ARG_EMOTION = "emotion";
    private String emotion = "";

    private HomeFragmentListener homeFragmentListener;
    private LinearLayout linearLayoutBackground;
    private RelativeLayout relativeLayoutBarOne;
    private LinearLayout linearLayoutBarTwo;
    private TextView textViewLokasi;
    private TextView textViewDerajat;
    private TextView textViewKelembaban;
    private ImageView imageViewAwan;
    private ImageView imageViewArahAngin;
    private TextView textViewKecepatanAngin;
    private ImageView imageViewMood;
    private TextView textViewInfoWaktu;

    private InformasiCuaca informasiCuaca;
    private Intent intent;

    public interface HomeFragmentListener {

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

        linearLayoutBackground = (LinearLayout) v.findViewById(R.id.home_background);
        relativeLayoutBarOne = (RelativeLayout) v.findViewById(R.id.home_bar_one);
        linearLayoutBarTwo = (LinearLayout) v.findViewById(R.id.home_bar_two);
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
        //updateUI(new Cuaca("Kucing", "99", "<table><tr style=\"font-size:9px;\"><td><b>Waktu</b></td><td><b>16-19</b></td><td><b>19-22</b></td><td><b>22-01</b></td><td><b>01-04</b></td><td><b>04-07</b></td><td><b>07-10</b></td><td><b>10-13</b></td><td><b>13-16</b></td></tr><tr><td>Suhu (&deg;C): </td><td>26</td><td>26</td><td>25</td><td>26</td><td>26</td><td>27</td><td>28</td><td>27</td></tr><tr><td>Kelembaban (%): </td><td>84</td><td>80</td><td>80</td><td>78</td><td>79</td><td>79</td><td>75</td><td>72</td></tr><tr><td>Kec. angin (m/s): </td><td>7</td><td>5</td><td>4</td><td>8</td><td>5</td><td>7</td><td>8</td><td>7</td></tr><tr><td>Arah angin: </td><td><img src=\"images/WSW.gif\" alt=\"BBD\" /></td><td><img src=\"images/SSW.gif\" alt=\"SBD\" /></td><td><img src=\"images/SW.gif\" alt=\"BD\" /></td><td><img src=\"images/W.gif\" alt=\"B\" /></td><td><img src=\"images/W.gif\" alt=\"B\" /></td><td><img src=\"images/WSW.gif\" alt=\"BBD\" /></td><td><img src=\"images/W.gif\" alt=\"B\" /></td><td><img src=\"images/WNW.gif\" alt=\"BBL\" /></td></tr><tr><td>Awan/Hujan: </td><td><img src=\"images/berawan.png\" alt=\"berawan\" /></td><td><img src=\"images/berawan.png\" alt=\"berawan\" /></td><td><img src=\"images/cerah_berawan_malam.png\" alt=\"cerah_berawan\" /></td><td><img src=\"images/hujan_ringan.png\" alt=\"hujan_ringan\" /></td><td><img src=\"images/hujan_ringan.png\" alt=\"hujan_ringan\" /></td><td><img src=\"images/hujan_ringan.png\" alt=\"hujan_ringan\" /></td><td><img src=\"images/cerah_siang.png\" alt=\"cerah\" /></td><td><img src=\"images/berawan.png\" alt=\"berawan\" /></td></tr></table><br />Dikeluarkan: Mon, 21 Mar 2016 13:00:02<br />Berlaku mulai: 21-03-2016 1600 WIB<br />Sumber: <a href=\"http://weather.meteo.itb.ac.id\">WCPL - Weather Forecast Table</a>", "-6.117428", "106.870008"));
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
