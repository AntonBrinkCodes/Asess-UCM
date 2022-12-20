package com.example.asessucm.Model;

/**
 * Class to represent a result from a questionnare. Thinking scoreDelta is the difference in current
 * score vs previous questionnaire score. If scoreDelta > 13 then some kind of feedback shown to the user.
 * ... Maybe should be int and not float...
 */
public class QuestionnaireResult {
    private float score, scoreDelta;

    public QuestionnaireResult(float score) {
        this.score = score;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public float getScoreDelta() {
        return scoreDelta;
    }

    public void setScoreDelta(float scoreDelta) {
        this.scoreDelta = scoreDelta;
    }

   public boolean checkScoreDelta(float nextDelta){
       return this.scoreDelta - nextDelta > 13;
   }
}
