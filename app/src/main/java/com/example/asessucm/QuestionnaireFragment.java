package com.example.asessucm;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class QuestionnaireFragment extends Fragment implements SeekBar.OnSeekBarChangeListener{
    private SeekBar[] seekBars = new SeekBar[13];
    private int result;

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
        seekBars[0] = view.findViewById(R.id.seekBar_1);
        for(int i = 0; i<13;i++){
            //Log.i("QuestionnaireFragment", String.valueOf(name));
            cardIds[i] = r.getIdentifier("seekBar_" +(i+1), "id", name);
            //Log.i("QuestionnaireFragment", "seekBar_" + (i + 1));
            seekBars[i] = view.findViewById(cardIds[i]);
            if(seekBars[i] !=null) {
                seekBars[i].setOnSeekBarChangeListener(this);
            }
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
    //TODO: Maybe hook this to some textView or similar to show current progress.
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        this.result = 0;
        for(int i = 0;i<13;i++){
            result +=seekBars[i].getProgress();
        }

    }

    public int getResult() {
        return result;
    }
}