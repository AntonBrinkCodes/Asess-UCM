package com.example.asessucm.uiutils;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asessucm.Model.ResultItem;
import com.example.asessucm.Model.ResultList;
import com.example.asessucm.R;
import com.example.asessucm.utils.DateCleaner;

import org.w3c.dom.Text;

import java.util.List;

public class ResultRecyclerAdapter extends RecyclerView.Adapter<ResultRecyclerAdapter.ViewHolder>{
    private List<ResultItem> results;



    public ResultRecyclerAdapter(List<ResultItem> results){
        super();
        this.results = results;
    }



    @NonNull
    @Override
    public ResultRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.results_item, parent, false);
        final ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onBindViewHolder(@NonNull ResultRecyclerAdapter.ViewHolder holder, int position) {
    //This is where we set symbols etc to be shown
        ResultItem result = results.get(position);
        holder.date.setText(DateCleaner.cleanDateFormat(result.getDate()));
        holder.UCMImage.setImageResource(setUCMimage(result.getUCM()));
        holder.questionnaireImage.setImageResource(setQuestImage(result.getQuestionnaireResult().getScoreDelta()));
        holder.UCMText.setText(setUcmText(result.getUCMAngle()));
    }

    private String setUcmText(double angle) {
        if(angle == 0){
            return "No Ucm!";
        }
        else{
            String angleString = String.format("%.2f", angle);
            return "UCM happened at: "+angleString+" degrees!";
        }
    }

    private int setQuestImage(float scoreDelta) {
        if(scoreDelta>13){
            return R.drawable.ic_baseline_sentiment_very_dissatisfied_24;
        }
        else if(scoreDelta<-13){
            return R.drawable.ic_baseline_sentiment_satisfied_alt_24;
        }
        return R.drawable.ic_baseline_sentiment_neutral_24;
    }

    private int setUCMimage(boolean ucm) {
        if(ucm){
            return R.drawable.ucm_image_red;
        }
        return R.drawable.ucm_image_green;
    }



    @Override
    public int getItemCount() {return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView date, UCMText;
        ImageView UCMImage, questionnaireImage;
        public ViewHolder(@NonNull View itemView) {
            //Hooks views
            super(itemView);
            itemView.setOnClickListener(this);
            date = itemView.findViewById(R.id.Date_textview);
            UCMText = itemView.findViewById(R.id.UCM_Textview);
            UCMImage = itemView.findViewById(R.id.sensor_result_image);
            questionnaireImage = itemView.findViewById(R.id.questionnaire_result_image);
        }

        @Override
        public void onClick(View view) {
            //TODO: inflate a detailed view of the history on click?
        }
    }
}
