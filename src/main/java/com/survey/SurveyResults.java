package com.survey;

import java.util.ArrayList;

public class SurveyResults {
    private String teamName;
    private String portfolioName;
    private int totalNumberOfBIO;
    private ArrayList<SurveyResult> surveyResults;

    public SurveyResults(String teamName, String portfolioName, int totalNumberOfBIO, ArrayList<SurveyResult> surveyResults) {
        this.teamName = teamName;
        this.portfolioName = portfolioName;
        this.totalNumberOfBIO = totalNumberOfBIO;
        this.surveyResults = surveyResults;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SurveyResults that = (SurveyResults) o;

        if (!teamName.equals(that.teamName)) return false;
        if (!portfolioName.equals(that.portfolioName)) return false;
        return surveyResults.equals(that.surveyResults);
    }

    @Override
    public int hashCode() {
        int result = teamName.hashCode();
        result = 31 * result + portfolioName.hashCode();
        result = 31 * result + surveyResults.hashCode();
        return result;
    }
}
