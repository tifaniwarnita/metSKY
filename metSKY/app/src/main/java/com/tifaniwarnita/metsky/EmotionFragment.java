package com.tifaniwarnita.metsky;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class EmotionFragment extends Fragment {
    private ImageButton imageButtonTwink;
    private ImageButton imageButtonSurprised;
    private ImageButton imageButtonHappy;
    private ImageButton imageButtonFlat;
    private ImageButton imageButtonAngry;

    private EmotionFragmentListener emotionFragmentListener;

    public interface EmotionFragmentListener {
        public void onTwinkEmotionSelected();
        public void onSurprisedEmotionSelected();
        public void onHappyEmotionSelected();
        public void onFlatEmotionSelected();
        public void onAngryEmotionSelected();
    }

    public EmotionFragment() {
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
        View v = inflater.inflate(R.layout.fragment_emotion, container, false);

        imageButtonTwink = (ImageButton) v.findViewById(R.id.emotion_twink);
        imageButtonSurprised = (ImageButton) v.findViewById(R.id.emotion_surprised);
        imageButtonHappy = (ImageButton) v.findViewById(R.id.emotion_happy);
        imageButtonFlat = (ImageButton) v.findViewById(R.id.emotion_flat);
        imageButtonAngry = (ImageButton) v.findViewById(R.id.emotion_angry);

        createListener();

        return v;
    }

    private void createListener() {
        imageButtonTwink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emotionFragmentListener.onTwinkEmotionSelected();
            }
        });

        imageButtonSurprised.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emotionFragmentListener.onSurprisedEmotionSelected();
            }
        });

        imageButtonHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emotionFragmentListener.onHappyEmotionSelected();
            }
        });

        imageButtonFlat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emotionFragmentListener.onFlatEmotionSelected();
            }
        });

        imageButtonAngry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emotionFragmentListener.onAngryEmotionSelected();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        //Make sure that the container activity has implemented
        //the interface
        try {
            emotionFragmentListener = (EmotionFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement EmotionFragmentListener methods");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        emotionFragmentListener = null;
    }

}
