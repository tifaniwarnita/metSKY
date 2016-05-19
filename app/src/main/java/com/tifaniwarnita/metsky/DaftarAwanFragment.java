package com.tifaniwarnita.metsky;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tifaniwarnita.metsky.controllers.DatabaseHandler;

import java.io.IOException;
import java.util.ArrayList;


public class DaftarAwanFragment extends Fragment {
    private static final String ARG_JENIS_AWAN_PARAM = "jenis awan";
    private String jenisAwanParam;

    private DaftarAwanFragmentListener fragmentListener;
    private MainActivityListener mainActivityListener;

    public interface DaftarAwanFragmentListener {
        void onAwanClicked(String namaAwan);
    }

    public DaftarAwanFragment() {
        // Required empty public constructor
    }

    public static DaftarAwanFragment newInstance(String jenisAwanParam) {
        DaftarAwanFragment fragment = new DaftarAwanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_JENIS_AWAN_PARAM, jenisAwanParam);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jenisAwanParam = getArguments().getString(ARG_JENIS_AWAN_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_daftar_awan, container, false);
        LinearLayout daftarAwanContainer = (LinearLayout) v.findViewById(R.id.daftar_awan_container);

        DatabaseHandler dbHandler = new DatabaseHandler(getContext());
        try {
            dbHandler.createDB();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dbHandler.openDB();
        ArrayList<String> daftarAwan = dbHandler.getDaftarAwan(jenisAwanParam);
        for (final String awan : daftarAwan) {
            View daftarAwanView = inflater.inflate(R.layout.template_daftar_awan, daftarAwanContainer, false);
            ((TextView) daftarAwanView.findViewById(R.id.daftar_awan_nama)).setText(awan);
            daftarAwanView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentListener.onAwanClicked(awan);
                }
            });
            daftarAwanContainer.addView(daftarAwanView);
        }
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DaftarAwanFragmentListener) {
            fragmentListener = (DaftarAwanFragmentListener) context;
            mainActivityListener = (MainActivityListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
        mainActivityListener.backToMainActivity();
        mainActivityListener = null;
    }
}
