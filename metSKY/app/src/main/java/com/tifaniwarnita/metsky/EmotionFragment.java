package com.tifaniwarnita.metsky;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class EmotionFragment extends Fragment {
    public static final int EMOTION_TWINK = 1;
    public static final int EMOTION_SURPRISED = 2;
    public static final int EMOTION_HAPPY = 3;
    public static final int EMOTION_FLAT = 4;
    public static final int EMOTION_ANGRY = 5;


    private ImageButton imageButtonTwink;
    private ImageButton imageButtonSurprised;
    private ImageButton imageButtonHappy;
    private ImageButton imageButtonFlat;
    private ImageButton imageButtonAngry;

    private EmotionFragmentListener emotionFragmentListener;

    public interface EmotionFragmentListener {
        public void onEmotionSelected(int emotion);
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
                emotionFragmentListener.onEmotionSelected(EMOTION_TWINK);
            }
        });

        imageButtonSurprised.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emotionFragmentListener.onEmotionSelected(EMOTION_SURPRISED);
            }
        });

        imageButtonHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emotionFragmentListener.onEmotionSelected(EMOTION_HAPPY);
            }
        });

        imageButtonFlat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emotionFragmentListener.onEmotionSelected(EMOTION_FLAT);
            }
        });

        imageButtonAngry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emotionFragmentListener.onEmotionSelected(EMOTION_ANGRY);
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
