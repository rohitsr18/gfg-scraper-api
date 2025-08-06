package com.rohitsr18.gfgscraperapi.model;

public class GfgProfile {
    private String username;
    private String globalRank;
    private int problemsSolved;
    private String codingScore;
    private String contestRating;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGlobalRank() {
        return globalRank;
    }

    public void setGlobalRank(String globalRank) {
        this.globalRank = globalRank;
    }

    public int getProblemsSolved() {
        return problemsSolved;
    }

    public void setProblemsSolved(int problemsSolved) {
        this.problemsSolved = problemsSolved;
    }

    public String getCodingScore() {
        return codingScore;
    }

    public void setCodingScore(String codingScore) {
        this.codingScore = codingScore;
    }

    public String getContestRating() {
        return contestRating;
    }

    public void setContestRating(String contestRating) {
        this.contestRating = contestRating;
    }
}
