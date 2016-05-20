package com.tifaniwarnita.metsky;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tifaniwarnita.metsky.controllers.DatabaseHandler;
import com.tifaniwarnita.metsky.models.Awan;

import java.io.IOException;


public class DetailAwanFragment extends Fragment {
    private static final String ARG_NAMA_AWAN = "nama awan";

    private String namaAwan;

    public DetailAwanFragment() {
        // Required empty public constructor
    }

    public static DetailAwanFragment newInstance(String namaAwan) {
        DetailAwanFragment fragment = new DetailAwanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAMA_AWAN, namaAwan);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            namaAwan = getArguments().getString(ARG_NAMA_AWAN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_detail_awan, container, false);

        DatabaseHandler dbHandler = new DatabaseHandler(getContext());
        try {
            dbHandler.createDB();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dbHandler.openDB();
        Awan awan = dbHandler.getInformasiAwan(namaAwan);
        ((TextView) v.findViewById(R.id.detail_awan_nama)).setText(awan.getNama());
        ((TextView) v.findViewById(R.id.detail_awan_deskripsi_singkat)).setText(awan.getDeskripsiSingkat());
        ((TextView) v.findViewById(R.id.detail_awan_ketinggian)).setText(": " + awan.getKetinggian());
        ((TextView) v.findViewById(R.id.detail_awan_bentuk)).setText(": " + awan.getBentuk());
        ((TextView) v.findViewById(R.id.detail_awan_nama_latin)).setText(": " + awan.getNamaLatin());
        ((TextView) v.findViewById(R.id.detail_awan_prespitasi)).setText(": " + awan.getPrespitasi());
        int id = getContext().getResources().getIdentifier(namaAwan.toLowerCase(),
                "drawable", getContext().getPackageName());
        ((ImageView) v.findViewById(R.id.detail_awan_gambar)).setImageResource(id);
        ((TextView) v.findViewById(R.id.detail_awan_sumber_gambar)).setText(awan.getSumberGambar());
        ((TextView) v.findViewById(R.id.detail_awan_deskripsi_lengkap)).setText(awan.getDeskripsiLengkap());
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
