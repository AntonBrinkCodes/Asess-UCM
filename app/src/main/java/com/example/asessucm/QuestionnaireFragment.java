package com.example.asessucm;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.slider.Slider;


public class QuestionnaireFragment extends Fragment implements Slider.OnSliderTouchListener{
    private SeekBar[] seekBars = new SeekBar[13];
    private Slider[] sliders = new Slider[13];
    private float result;
    private TextView[] progressTextViews = new TextView[13];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_questionnaire, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        Resources r = view.getResources();
        String name = view.getContext().getPackageName();
        int[] cardIds = new int[13];
        for(int i = 0; i<13;i++){
            //Log.i("QuestionnaireFragment", String.valueOf(name));
            cardIds[i] = r.getIdentifier("seekBar_" +(i+1), "id", name);
            //Log.i("QuestionnaireFragment", "seekBar_" + (i + 1));
            sliders[i] = view.findViewById(cardIds[i]);
            if(sliders[i] !=null) {
                sliders[i].addOnSliderTouchListener(this);
            }
        }
    }




    public float getResult() {
        return result*100/13;
    }

    @Override
    public void onStartTrackingTouch(@NonNull Slider slider) {

    }

    @Override
    public void onStopTrackingTouch(@NonNull Slider slider) {
        this.result = 0;
        for(int i = 0;i<13;i++){
            result +=sliders[i].getValue();
        }
    }
}