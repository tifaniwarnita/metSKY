package com.tifaniwarnita.metsky;

import android.app.Activity;
import android.content.Context;
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
import com.tifaniwarnita.metsky.models.Cuaca;
import com.tifaniwarnita.metsky.models.InformasiCuaca;

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

    private InformasiCuaca informasiCuaca;

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

        // Update color based on mood
        updateBackgroundColor();

        final HomeFragment homeFragment = this;

        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(getActivity());
        locationProvider.getLastKnownLocation()
                .subscribe(new Action1<Location>() {
                    @Override
                    public void call(Location location) {
                        informasiCuaca = new InformasiCuaca(String.valueOf(location.getLatitude()),
                                String.valueOf(location.getLongitude()), getActivity(), homeFragment);
                    }
                });


        return v;
    }

    public void updateBackgroundColor() {
        int primaryColor;
        int barColor;

        switch (emotion) {
            case "twink":
                primaryColor = R.color.primaryBlue;
                barColor = R.color.barBlue;
                break;
            case "surprised":
                primaryColor = R.color.primaryLightBlue;
                barColor = R.color.barLightBlue;
                break;
            case "happy":
                primaryColor = R.color.primaryOrange;
                barColor = R.color.barOrange;
                break;
            case "flat":
                primaryColor = R.color.primaryGreen;
                barColor = R.color.barGreen;
                break;
            default: //angry
                primaryColor = R.color.primaryPink;
                barColor = R.color.barPink;
        }
        /* linearLayoutBackground.setBackgroundColor(ContextCompat.getColor(getActivity(), primaryColor));
        relativeLayoutBarOne.setBackgroundColor(ContextCompat.getColor(getActivity(), barColor));
        linearLayoutBarTwo.setBackgroundColor(ContextCompat.getColor(getActivity(), barColor));

        // Change status bar color
        Window window = getActivity().getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(getResources().getColor(barColor));*/
    }

    public void updateUI(Cuaca cuaca) {
        textViewLokasi.setText(cuaca.getKota());
        textViewDerajat.setText(String.valueOf(cuaca.getCurrentSuhu()));
        textViewKelembaban.setText(String.valueOf(cuaca.getCurrentKelembaban()) + "%");

        Context context = imageViewAwan.getContext();
        int id = context.getResources().getIdentifier("icon_" + cuaca.getCurrentAwan() + "64",
                "drawable", context.getPackageName());
        imageViewAwan.setImageResource(id);

        context = imageViewArahAngin.getContext();
        // id = context.getResources().getIdentifier("icon_arrow_" + cuaca.getCurrentArahAngin(),
                // "drawable", context.getPackageName());
        // imageViewArahAngin.setImageResource(id);

        textViewKecepatanAngin.setText(String.valueOf(cuaca.getCurrentKecepatanAngin()) + "m/s");

        context = imageViewMood.getContext();
        id = context.getResources().getIdentifier("icon_" + emotion + "48",
                "drawable", context.getPackageName());
        imageViewMood.setImageResource(id);
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
    }

}
