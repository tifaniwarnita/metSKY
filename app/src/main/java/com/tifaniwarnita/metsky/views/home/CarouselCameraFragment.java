package com.tifaniwarnita.metsky.views.home;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.tifaniwarnita.metsky.R;


public class CarouselCameraFragment extends Fragment {
    private CarouselCameraFragmentListener  carouselCameraFragmentListener;
    private ImageButton cameraButton;

    public interface CarouselCameraFragmentListener {
        public void onCameraButtonClicked();
    }

    public CarouselCameraFragment() {
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
        View v =  inflater.inflate(R.layout.fragment_carousel_camera, container, false);
        cameraButton = (ImageButton) v.findViewById(R.id.home_carousel_button_ambilfoto);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carouselCameraFragmentListener.onCameraButtonClicked();
            }
        });
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Make sure that the container activity has implemented
        //the interface
        try {
            carouselCameraFragmentListener = (CarouselCameraFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SignUpFragmentListener methods");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        carouselCameraFragmentListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
