package com.aijobagent.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private boolean mockAi = true;
    private boolean dailyEnabled = true;
    private int matchThreshold = 75;
    private int strongMatchThreshold = 85;
    private String reportDirectory = "./data/reports";
    private String documentDirectory = "./data/documents";
    private List<GreenhouseBoard> greenhouseBoards = new ArrayList<>();
    private List<SmartRecruitersCompany> smartRecruitersCompanies = new ArrayList<>();
    private List<String> targetLocations = new ArrayList<>(List.of("Dubai", "UAE", "Netherlands", "UK"));
    private List<String> targetTitles = new ArrayList<>(List.of("Java Backend Engineer", "Senior Java Engineer", "Lead Java Engineer", "Staff Backend Engineer"));

    public boolean isMockAi() { return mockAi; }
    public void setMockAi(boolean mockAi) { this.mockAi = mockAi; }
    public boolean isDailyEnabled() { return dailyEnabled; }
    public void setDailyEnabled(boolean dailyEnabled) { this.dailyEnabled = dailyEnabled; }
    public int getMatchThreshold() { return matchThreshold; }
    public void setMatchThreshold(int matchThreshold) { this.matchThreshold = matchThreshold; }
    public int getStrongMatchThreshold() { return strongMatchThreshold; }
    public void setStrongMatchThreshold(int strongMatchThreshold) { this.strongMatchThreshold = strongMatchThreshold; }
    public String getReportDirectory() { return reportDirectory; }
    public void setReportDirectory(String reportDirectory) { this.reportDirectory = reportDirectory; }
    public String getDocumentDirectory() { return documentDirectory; }
    public void setDocumentDirectory(String documentDirectory) { this.documentDirectory = documentDirectory; }
    public List<GreenhouseBoard> getGreenhouseBoards() { return greenhouseBoards; }
    public void setGreenhouseBoards(List<GreenhouseBoard> greenhouseBoards) { this.greenhouseBoards = greenhouseBoards; }
    public List<SmartRecruitersCompany> getSmartRecruitersCompanies() { return smartRecruitersCompanies; }
    public void setSmartRecruitersCompanies(List<SmartRecruitersCompany> smartRecruitersCompanies) { this.smartRecruitersCompanies = smartRecruitersCompanies; }
    public List<String> getTargetLocations() { return targetLocations; }
    public void setTargetLocations(List<String> targetLocations) { this.targetLocations = targetLocations; }
    public List<String> getTargetTitles() { return targetTitles; }
    public void setTargetTitles(List<String> targetTitles) { this.targetTitles = targetTitles; }

    public static class GreenhouseBoard {
        private String company;
        private String boardToken;
        public String getCompany() { return company; }
        public void setCompany(String company) { this.company = company; }
        public String getBoardToken() { return boardToken; }
        public void setBoardToken(String boardToken) { this.boardToken = boardToken; }
    }

    public static class SmartRecruitersCompany {
        private String company;
        private String identifier;
        public String getCompany() { return company; }
        public void setCompany(String company) { this.company = company; }
        public String getIdentifier() { return identifier; }
        public void setIdentifier(String identifier) { this.identifier = identifier; }
    }
}
