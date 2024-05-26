public class Team {
    private String teamName;
    private int teamId;
    private int playerCount;
    private int wins;
    private int losses;
    
    // Team Constructor
    public Team() {}
    public Team(String teamName, int teamId, int playerCount, int wins, int losses) {
        this.teamName = teamName;
        this.teamId = teamId;
        this.playerCount = playerCount;
        this.wins = wins;
        this.losses = losses;
    }

    // Setter for the teamName attribute for duplicate checking
    public void setNewTeamName(String teamName) {
        this.teamName = teamName;
    }
    public void updateTeamWins(int wins) {
        this.wins = wins;
    }
    public void updateTeamLosses(int losses) {
        this.losses = losses;
    }
    // Getters
    public int getWins() {
        return this.wins;
    }
    public int getLosses() {
        return this.losses;
    }
    public int getTeamID() {
        return this.teamId;
    }
    public int getPlayerCount() {
        return this.playerCount;
    }
    public String getTeamName() {
        return this.teamName;
    }
}

class Player {
    private String lastName;
    private String firstName;
    private String jerseyNum;
    private int points;
    private int rebounds;
    private int assists;
    private int blocks;
    private int steals;

    // Player Constructor
    public Player(String lastName, String firstName, String jerseyNum, int points, int rebounds, int assists, int blocks, int steals) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.jerseyNum = jerseyNum;
        this.points = points;
        this.rebounds = rebounds;
        this.assists = assists;
        this.blocks = blocks;
        this.steals = steals;
    }

    // Getters for different player attributes for value checking
    public String getPlayerLastName() {
        return this.lastName;
    }
    public String getPlayerFirstName() {
        return this.firstName;
    }
    public String getPlayerJerseyNum() {
        return this.jerseyNum;
    }
    public int getPoints() {
        return this.points;
    }
    public int getRebounds() {
        return this.rebounds;
    }
    public int getAssists() {
        return this.assists;
    }
    public int getBlocks() {
        return this.blocks;
    }
    public int getSteals() {
        return this.steals;
    }

    // Setters for different player attributes for duplicate checking
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastname(String lastName) {
        this.lastName = lastName;
    }
    public void setJerseyNumber(String jerseyNum) {
        this.jerseyNum = jerseyNum;
    }
    public void setPoints(int points) {
        this.points = points;
    }
    public void setRebounds(int rebounds) {
        this.rebounds = rebounds;
    }
    public void setAssists(int assists) {
        this.assists = assists;
    }
    public void setBlocks(int blocks) {
        this.blocks = blocks;
    }
    public void setSteals(int steals) {
        this.steals = steals;
    }
}