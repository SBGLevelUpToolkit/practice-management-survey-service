package com.survey;

import java.util.ArrayList;

public class SurveyResults {
    private String teamName;
    private int totalNumberOfBIO;
    private ArrayList<SurveyResult> surveyResults;

    public SurveyResults(String teamName, int totalNumberOfBIO, ArrayList<SurveyResult> surveyResults) {
        this.teamName = teamName;
        this.totalNumberOfBIO = totalNumberOfBIO;
        this.surveyResults = surveyResults;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SurveyResults that = (SurveyResults) o;

        if (!teamName.equals(that.teamName)) return false;
        return surveyResults.equals(that.surveyResults);
    }

    @Override
    public int hashCode() {
        int result = teamName.hashCode();
        result = 31 * result + surveyResults.hashCode();
        return result;
    }
}
