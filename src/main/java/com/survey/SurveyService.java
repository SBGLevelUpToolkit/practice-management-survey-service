package com.survey;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import static com.survey.JsonUtil.json;
import static spark.Spark.*;

public class SurveyService {
    private final static Logger logger = LoggerFactory.getLogger(SurveyService.class);

    private static String[] getDBDetails() {
        try {
            Properties props = new Properties();
            String configFile = System.getProperty("user.dir") + "/config/survey-config.properties";
            InputStream in = new FileInputStream(configFile);
            props.load(in);
            in.close();

            String[] dbDetails = new String[4];
            dbDetails[0] = props.get("DB_URL").toString();
            dbDetails[1] = props.get("DB_USERNAME").toString();
            dbDetails[2] = props.get("DB_PASSWORD").toString();
            dbDetails[3] = props.get("MY_SQL_URL").toString();


            return dbDetails;
        }
        catch (Exception exception){
            logger.error(exception.getMessage());
            return new String[] {"", "", "", ""};
        }
    }

    private static ArrayList<String> getTeamNames(){
        String[] dbDetails = getDBDetails();
        ArrayList<String> teamNames = new ArrayList<String>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            Statement stmt = conn.createStatement();

            String queryStatement = "SELECT DISTINCT teamName from TeamNames";

            ResultSet resultSet = stmt.executeQuery(queryStatement);
            while(resultSet.next()){
                teamNames.add(resultSet.getString("teamName"));
            }
        }
        catch (Exception exception){
            logger.error("Error Code - getTeamNames: " + exception.toString());
        }

        return teamNames;
    }

    private static ArrayList<Surveyee> getAllSurveyees(){
        String[] dbDetails = getDBDetails();
        ArrayList<Surveyee> surveyees = new ArrayList<Surveyee>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            Statement stmt = conn.createStatement();

            String queryStatement = "SELECT * from TeamNames";

            ResultSet resultSet = stmt.executeQuery(queryStatement);
            while(resultSet.next()){
                Surveyee surveyee = new Surveyee();
                surveyee.setSurveyeeName(resultSet.getString("bioName"));
                surveyee.setPortfolio(resultSet.getString("portfolio"));
                surveyee.setTeamName(resultSet.getString("teamName"));
                surveyees.add(surveyee);
            }
        }
        catch (Exception exception){
            logger.error("Error Code - getAllSurveyees: " + exception.toString());
        }

        return surveyees;
    }

    private static int getNumberOfBioForTeam(String teamName){
        String[] dbDetails = getDBDetails();
        int numberOfBIO = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            Statement stmt = conn.createStatement();

            String queryStatement = String.format("SELECT bioName from TeamNames WHERE teamName = '%s'", teamName);

            ResultSet resultSet = stmt.executeQuery(queryStatement);

            while(resultSet.next()){
                numberOfBIO++;
            }

        }
        catch (SQLException exception){
            logger.error("Error Code - sql - getNumberOfBioForTeam: " + exception.toString());
        }
        catch (Exception exception){
            logger.error("Error Code - non-sql - getNumberOfBioForTeam: " + exception.toString());
        }

        return numberOfBIO;
    }

    private static String getPortfolioForTeam(String teamName){
        String[] dbDetails = getDBDetails();
        String portfolio = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            Statement stmt = conn.createStatement();

            String queryStatement = String.format("SELECT portfolio from TeamNames WHERE teamName = '%s'", teamName);

            ResultSet resultSet = stmt.executeQuery(queryStatement);

            while(resultSet.next()){
                portfolio = resultSet.getString("portfolio");
            }

        }
        catch (SQLException exception){
            logger.error("Error Code - sql - getPortfolioForTeam: " + exception.toString());
        }
        catch (Exception exception){
            logger.error("Error Code - non-sql - getPortfolioForTeam: " + exception.toString());
        }

        return portfolio;
    }

    private static ArrayList<SurveyResult> getSurveyResultsForTeam(String teamName){
        String[] dbDetails = getDBDetails();
        ArrayList<SurveyResult> surveyResults = new ArrayList<SurveyResult>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            Statement stmt = conn.createStatement();

            String queryStatement = String.format("SELECT * from SurveyResults where teamName = '%s'", teamName);

            ResultSet resultSet = stmt.executeQuery(queryStatement);
            while(resultSet.next()){
                SurveyResult surveyResult = new SurveyResult();
                surveyResult.setSoftwareScore(resultSet.getString("softwareScore"));
                surveyResult.setAgileCoachingScore(resultSet.getString("agileCoachingScore"));
                surveyResult.setChangeAndReleaseScore(resultSet.getString("changeAndReleaseScore"));
                surveyResult.setQualityEngineeringScore(resultSet.getString("qualityEngineeringScore"));
                surveyResult.setEnterpriseArchitectureScore(resultSet.getString("enterpriseArchitectureScore"));
                surveyResult.setSolutionsArchitectureScore(resultSet.getString("solutionsArchitectureScore"));
                surveyResult.setDataServicesScore(resultSet.getString("dataServicesScore"));
                surveyResult.setBIO(resultSet.getString("bioName"));
                surveyResult.setPeriodOfYear(resultSet.getString("quarter"));
                surveyResult.setTeamName(teamName);
                surveyResults.add(surveyResult);
            }
        }
        catch (Exception exception){
            logger.error("Error Code - getSurveyResultsForTeam: " + exception.toString());
        }
        return surveyResults;
    }

    private static SurveyResult getSurveyResultsForTeamAndSurveyee(String teamName, String surveyee, String quarter){
        String[] dbDetails = getDBDetails();
        SurveyResult surveyResult = new SurveyResult();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            Statement stmt = conn.createStatement();

            String queryStatement = String.format("SELECT * from SurveyResults where teamName = '%s' AND bioName = '%s' AND quarter = '%s'", teamName, surveyee, quarter);

            ResultSet resultSet = stmt.executeQuery(queryStatement);
            while(resultSet.next()){
                surveyResult.setSoftwareScore(resultSet.getString("softwareScore"));
                surveyResult.setAgileCoachingScore(resultSet.getString("agileCoachingScore"));
                surveyResult.setChangeAndReleaseScore(resultSet.getString("changeAndReleaseScore"));
                surveyResult.setQualityEngineeringScore(resultSet.getString("qualityEngineeringScore"));
                surveyResult.setEnterpriseArchitectureScore(resultSet.getString("enterpriseArchitectureScore"));
                surveyResult.setSolutionsArchitectureScore(resultSet.getString("solutionsArchitectureScore"));
                surveyResult.setDataServicesScore(resultSet.getString("dataServicesScore"));
                surveyResult.setBIO(resultSet.getString("bioName"));
                surveyResult.setPeriodOfYear(resultSet.getString("quarter"));
                surveyResult.setRawData(resultSet.getString("rawData"));
                surveyResult.setTeamName(teamName);
            }
        }
        catch (Exception exception){
            logger.error("Error Code - getSurveyResultsForTeamAndSurveyee: " + exception.toString());
        }
        return surveyResult;
    }

    private static ArrayList<SurveyResults> getSurveyResults(String teamName){
        ArrayList<SurveyResults> surveyResults = new ArrayList<SurveyResults>();

        if(teamName != null && !teamName.equals("undefined" )){
            SurveyResults teamSurveyResults = new SurveyResults(teamName, getPortfolioForTeam(teamName), getNumberOfBioForTeam(teamName), getSurveyResultsForTeam(teamName));
            surveyResults.add(teamSurveyResults);
        }
        else {
            ArrayList<String> allTeams = getTeamNames();

            for (String teamNameRetrieved : allTeams) {
                SurveyResults teamSurveyResults = new SurveyResults(teamNameRetrieved, getPortfolioForTeam(teamNameRetrieved), getNumberOfBioForTeam(teamNameRetrieved), getSurveyResultsForTeam(teamNameRetrieved));
                surveyResults.add(teamSurveyResults);
            }
        }

        return surveyResults;
    }

    private static SurveyResult surveyForSurveyee(String surveyeeName, String teamName){
        String quarter = getQuarter();
        return getSurveyResultsForTeamAndSurveyee(teamName, surveyeeName, quarter);
    }

    private static String getColumnName(String BIOName, String columnName){
        String columnNameRetrieved = "";
        String[] dbDetails = getDBDetails();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            Statement stmt = conn.createStatement();

            String queryStatement = String.format("SELECT %s from TeamNames where bioName = '%s'", columnName, BIOName);

            ResultSet resultSet = stmt.executeQuery(queryStatement);

            while(resultSet.next()){
                columnNameRetrieved = resultSet.getString(columnName);
            }
        }
        catch (Exception exception){
            logger.error("Error Code - getColumnName: " + exception.toString());
            return "Error Code: " + exception.toString();
        }
        return columnNameRetrieved;
    }

    private static String getQuarter(){
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(currentDate);

        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        if(month <= 2){
            return "Quarter 1 - " + String.valueOf(year);
        }
        else if(month > 2 && month <= 5){
            return "Quarter 2 - " + String.valueOf(year);
        }
        else if(month > 5 && month <= 8){
            return "Quarter 3 - " + String.valueOf(year);
        }
        else{
            return "Quarter 4 - " + String.valueOf(year);
        }
    }

    public static void main(String[] args) {

        port(8082);

        post("/saveSurvey", new Route() {
            public Object handle(Request request, Response response) throws Exception {
                JSONObject json = new JSONObject(request.body());

                String BIO, softwareScore, agileCoachingScore, changeAndReleaseScore, qualityEngineeringScore,
                        enterpriseArchitectureScore, solutionsArchitectureScore, dataServicesScore, rawData;

                try {
                    BIO = json.get("BIO").toString();
                }
                catch(Exception ex){
                    BIO = "";
                }

                try {
                    softwareScore = json.get("softwareScore").toString();
                }
                catch(Exception ex){
                    softwareScore = "";
                }

                try {
                    agileCoachingScore = json.get("agileCoachingScore").toString();
                }
                catch(Exception ex){
                    agileCoachingScore = "";
                }

                try {
                    changeAndReleaseScore = json.get("changeAndReleaseScore").toString();
                }
                catch(Exception ex){
                    changeAndReleaseScore = "";
                }

                try {
                    qualityEngineeringScore = json.get("qualityEngineeringScore").toString();
                }
                catch(Exception ex){
                    qualityEngineeringScore = "";
                }

                try {
                    enterpriseArchitectureScore = json.get("enterpriseArchitectureScore").toString();
                }
                catch(Exception ex){
                    enterpriseArchitectureScore = "";
                }

                try {
                    solutionsArchitectureScore = json.get("solutionsArchitectureScore").toString();
                }
                catch(Exception ex){
                    solutionsArchitectureScore = "";
                }

                try {
                    dataServicesScore = json.get("dataServicesScore").toString();
                }
                catch(Exception ex){
                    dataServicesScore = "";
                }

                try {
                    rawData = json.get("rawData").toString();
                }
                catch(Exception ex){
                    rawData = "";
                }



                String[] dbDetails = getDBDetails();

                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
                    Statement stmt = conn.createStatement();

                    String sql = String.format("REPLACE INTO SurveyResults " +
                                    "VALUES ('%s','%s', '%s', '%s',%s,%s,%s,%s,%s,%s,%s,'%s')", BIO, getColumnName(BIO, "teamName"),
                            getColumnName(BIO, "portfolio"), getQuarter(), softwareScore, agileCoachingScore,
                            changeAndReleaseScore, qualityEngineeringScore, enterpriseArchitectureScore,
                            solutionsArchitectureScore, dataServicesScore, rawData);

                    int insertedRecord = stmt.executeUpdate(sql);

                    if (insertedRecord > 0) {
                        return "Successfully inserted record";
                    } else {
                        return "Record not inserted";
                    }
                }
                catch (SQLException exception){
                    logger.error("Error Code - sql - saveSurvey: " + exception.toString());
                    return "Error Code: " + exception.toString();
                }

            }
        });

        get("/surveys", new Route() {
            public Object handle(Request request, Response res) throws Exception {
                String teamName = request.queryParams("teamName");
                return getSurveyResults(teamName);
            }
        }, json());


        get("/surveyTaken", new Route() {
            public Object handle(Request request, Response res) throws Exception {
                String surveyeeName = request.queryParams("surveyee");
                String teamName = request.queryParams("teamName");
                return surveyForSurveyee(surveyeeName, teamName);
            }
        }, json());


        get("/surveyees", new Route() {
            public Object handle(Request request, Response res) throws Exception {
                return getAllSurveyees();
            }
        }, json());


        options("/*",
                new Route() {
                    public Object handle(Request request, Response response) throws Exception {

                        String accessControlRequestHeaders = request
                                .headers("Access-Control-Request-Headers");
                        if (accessControlRequestHeaders != null) {
                            response.header("Access-Control-Allow-Headers",
                                    accessControlRequestHeaders);
                        }

                        String accessControlRequestMethod = request
                                .headers("Access-Control-Request-Method");
                        if (accessControlRequestMethod != null) {
                            response.header("Access-Control-Allow-Methods",
                                    accessControlRequestMethod);
                        }

                        return "OK";
                    }
                });

        before(new Filter() {
            public void handle(Request request, Response response) throws Exception {
                response.header("Access-Control-Allow-Origin", "*");
            }
        });


    }

}
