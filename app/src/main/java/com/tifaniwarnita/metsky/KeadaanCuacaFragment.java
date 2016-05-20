package com.tifaniwarnita.metsky;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.tifaniwarnita.metsky.views.home.MainActivityListener;


public class KeadaanCuacaFragment extends Fragment {

    private KeadaanCuacaFragmentListener keadaaanCuacaFragmentListener;
    private MainActivityListener mainActivityListener;

    public interface KeadaanCuacaFragmentListener {
        void onKeadaanCuacaClicked(String pilihan);
    }

    public KeadaanCuacaFragment() {
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
        View v =  inflater.inflate(R.layout.fragment_keadaan_cuaca, container, false);
        ImageButton imageButtonCerah = (ImageButton) v.findViewById(R.id.keadaancuaca_imagebutton_cerah);
        ImageButton imageButtonBerawan = (ImageButton) v.findViewById(R.id.keadaancuaca_imagebutton_berawan);
        ImageButton imageButtonCerahBerawan = (ImageButton) v.findViewById(R.id.keadaancuaca_imagebutton_cerahberawan);
        ImageButton imageButtonCerahHujanRingan = (ImageButton) v.findViewById(R.id.keadaancuaca_imagebutton_cerahhujanringan);
        ImageButton imageButtonHujanRingan = (ImageButton) v.findViewById(R.id.keadaancuaca_imagebutton_cerahhujanringan);
        ImageButton imageButtonHujanSedang = (ImageButton) v.findViewById(R.id.keadaancuaca_imagebutton_hujansedang);
        ImageButton imageButtonHujanLebat = (ImageButton) v.findViewById(R.id.keadaancuaca_imagebutton_hujanlebat);

        imageButtonCerah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keadaaanCuacaFragmentListener.onKeadaanCuacaClicked("4");
            }
        });

        imageButtonBerawan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keadaaanCuacaFragmentListener.onKeadaanCuacaClicked("6");
            }
        });

        imageButtonCerahBerawan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keadaaanCuacaFragmentListener.onKeadaanCuacaClicked("5");
            }
        });

        imageButtonCerahHujanRingan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keadaaanCuacaFragmentListener.onKeadaanCuacaClicked("7");
            }
        });

        imageButtonHujanRingan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keadaaanCuacaFragmentListener.onKeadaanCuacaClicked("1");
            }
        });

        imageButtonHujanSedang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keadaaanCuacaFragmentListener.onKeadaanCuacaClicked("2");
            }
        });

        imageButtonHujanLebat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keadaaanCuacaFragmentListener.onKeadaanCuacaClicked("3");
            }
        });

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            keadaaanCuacaFragmentListener = (KeadaanCuacaFragmentListener) activity;
            mainActivityListener = (MainActivityListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivityListener.backToMainActivity();
        keadaaanCuacaFragmentListener = null;
        mainActivityListener = null;
    }

}
