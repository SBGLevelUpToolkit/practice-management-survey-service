package com.survey;

public class SurveyResult {


    private String BIO, softwareScore, agileCoachingScore, changeAndReleaseScore, qualityEngineeringScore,
    enterpriseArchitectureScore, solutionsArchitectureScore, dataServicesScore, periodOfYear, teamName, rawData;

    public void setBIO(String BIO) {
        this.BIO = BIO;
    }

    public void setPeriodOfYear(String periodOfYear) {
        this.periodOfYear = periodOfYear;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getPeriodOfYear() {
        return periodOfYear;
    }

    public void setSoftwareScore(String softwareScore) {
        this.softwareScore = softwareScore;
    }

    public void setAgileCoachingScore(String agileCoachingScore) {
        this.agileCoachingScore = agileCoachingScore;
    }

    public void setChangeAndReleaseScore(String changeAndReleaseScore) {
        this.changeAndReleaseScore = changeAndReleaseScore;
    }

    public void setQualityEngineeringScore(String qualityEngineeringScore) {
        this.qualityEngineeringScore = qualityEngineeringScore;
    }

    public void setEnterpriseArchitectureScore(String enterpriseArchitectureScore) {
        this.enterpriseArchitectureScore = enterpriseArchitectureScore;
    }

    public void setSolutionsArchitectureScore(String solutionsArchitectureScore) {
        this.solutionsArchitectureScore = solutionsArchitectureScore;
    }

    public void setDataServicesScore(String dataServicesScore) {
        this.dataServicesScore = dataServicesScore;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }
}
