package com.tifaniwarnita.metsky;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;


public class AmbilFotoFragment extends Fragment {
    private static final String ARG_PHOTO = "photo";

    private Bitmap photo;
    private AmbilFotoFragmentListener ambilFotoFragmentListener;
    private MainActivityListener mainActivityListener;

    public interface AmbilFotoFragmentListener {
        void onSimpanButtonClicked(Bitmap bitmap);
        void onLaporkanButtonClicked(Bitmap photo);
    }

    public static AmbilFotoFragment newInstance(Bitmap bitmap) {
        AmbilFotoFragment fragment = new AmbilFotoFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PHOTO, bitmap);
        fragment.setArguments(args);
        return fragment;
    }

    public AmbilFotoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            photo = getArguments().getParcelable(ARG_PHOTO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_ambil_foto, container, false);
        ImageView imageViewPhoto = (ImageView) v.findViewById(R.id.ambilfoto_imageview_foto);
        ImageButton imageButtonSimpan = (ImageButton) v.findViewById(R.id.ambilfoto_imagebutton_simpan);
        ImageButton imageButtonLaporkan = (ImageButton) v.findViewById(R.id.ambilfoto_imagebutton_laporkan);

        if (photo != null) {
            imageViewPhoto.setImageBitmap(photo);
        }
        imageButtonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ambilFotoFragmentListener.onSimpanButtonClicked(photo);
            }
        });

        imageButtonLaporkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ambilFotoFragmentListener.onLaporkanButtonClicked(photo);
            }
        });
        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            ambilFotoFragmentListener = (AmbilFotoFragmentListener) activity;
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
        ambilFotoFragmentListener = null;
        mainActivityListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
