import java.io.*;
import java.util.ArrayList;
import java.util.List;
// import java.util.Arrays;

import javax.swing.JOptionPane;

public class FileOperations {

    // Method that returns a boolean when: CSV File exists and follows the correct file structure
    public boolean checkFileStructure(String filePath, int fieldCount, int stringFields) {
        try (BufferedReader fin = new BufferedReader(new FileReader(filePath))) {
            String buffer;
            if((buffer = fin.readLine()) != null) {} // Skip headers
            while((buffer = fin.readLine()) != null) {
                String[] subData = buffer.split(",");
                if (subData.length != fieldCount) {
                    return false;
                }
                for (int j = 0; j < subData.length; j++) {
                    boolean isValidField = (j < stringFields) ? checkIfAlphabet(subData[j]) : checkIfNumber(subData[j]);
                    if (!isValidField) {
                        return false;
                    }      
                }
            }
            fin.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean checkDuplicates(String input, Team[] teams) {
        for(int i = 0; i < teams.length; i++) {
            if(teams[i].getTeamName().equals(input)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkDuplicates(String input, Player[] players) {
        if(players.length == 0) {
            return false;
        }
        for(int i = 0; i < players.length; i++) {
            if(players[i].getPlayerJerseyNum().equals(input)) {
                return true;
            }
        }
        return false;
    }

    public boolean writeToCSVFile(Player player, String fpath, boolean append) {
        try (PrintWriter fout = new PrintWriter(new FileWriter(fpath, append))) {
            if(!append) {
                fout.println("First Name,Last Name,Jersey Number");
            }
            fout.printf(
                "%s,%s,%s,%d,%d,%d,%d,%d\n", 
                player.getPlayerFirstName(), 
                player.getPlayerLastName(), 
                player.getPlayerJerseyNum(),
                player.getPoints(),
                player.getRebounds(),
                player.getAssists(),
                player.getBlocks(),
                player.getSteals()
            );
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Player[] extractPlayerData(String fpath) {
        // Use ArrayList to dynamically add teams
        List<Player> playerList = new ArrayList<>(); 
        String line;
        try (BufferedReader fin = new BufferedReader(new FileReader(fpath))) {
            if ((line = fin.readLine()) != null) {} // Skip Header
            while ((line = fin.readLine()) != null) {
                // Split CSV Data and load into team list using constructors
                String[] tmp = line.split(",");
                if(tmp.length != 8) {
                    continue;
                }
                // Assuming the CSV format is: Name,ID,PlayerCounts,Wins,Losses
                Player player = new Player(tmp[0], tmp[1], tmp[2], Integer.parseInt(tmp[3]), Integer.parseInt(tmp[4]),
                Integer.parseInt(tmp[5]), Integer.parseInt(tmp[6]), Integer.parseInt(tmp[7]));
                playerList.add(player);
            }
            fin.close();
        } catch(IOException e) {
            return new Player[0];
        }
        // Convert list to array
        Player[] players = playerList.toArray(new Player[0]);
        return players;
    }

    public Team[] extractTeamData(String fpath) {
        // Use ArrayList to dynamically add teams
        List<Team> teamList = new ArrayList<>(); 
        String line;
        try (BufferedReader fin = new BufferedReader(new FileReader(fpath))) {
            if ((line = fin.readLine()) != null) {} // Skip Header
            while ((line = fin.readLine()) != null) {
                // Split CSV Data and load into team list using constructors
                String[] tmp = line.split(",");
                // Assuming the CSV format is: Name,ID,PlayerCounts,Wins,Losses
                Team team = new Team(tmp[0], Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]), Integer.parseInt(tmp[3]), Integer.parseInt(tmp[4]));
                teamList.add(team);
            }
            fin.close();
        } catch(IOException e) {
            return new Team[0];
        }
        // Convert list to array
        Team[] teams = teamList.toArray(new Team[0]);
        return teams;
    }

    // Checks if the data has any other inputs other than alphabets case insensitive
    public boolean checkIfAlphabet(String input) {
        String tmp = input.toLowerCase();
        if (input == null || input.length() == 0) return false;
        for(int i = 0; i < tmp.length(); i++) {
            if ((tmp.charAt(i) < 'a' || tmp.charAt(i) > 'z')) {    
                return false; 
            } 
        }
        return true;
    }
        
    // Checks if the data has any other inputs other than numbers
    public boolean checkIfNumber(String input) {
        if (input == null || input.isEmpty()) return false;
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }  

    public boolean saveToCSV(String fPath, Team team) {
        Team[] teams = extractTeamData(fPath);
        boolean append = (teams.length == 0) ? false : true;
        try (PrintWriter fout = new PrintWriter(new FileWriter(fPath, append))){
            if(teams.length == 0) { 
                fout.println("Team Name,Team ID,Player Count,Wins,Losses");
            }
            fout.printf("%s,%d,%d,%d,%d\n", team.getTeamName(), team.getTeamID(), team.getPlayerCount(), team.getWins(), team.getLosses());
            fout.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean checkIfFileExists(String fPath) {
        File file = new File(fPath);
        return file.exists();
    }

    public boolean writeToCSVFile(String fPath, String[] matchList) {
        try (PrintWriter fout = new PrintWriter(new FileWriter(fPath))) {
            fout.println("Team A,Team B,Match Day");
                int matchCount = 1;
                for (int i = 0; i < matchList.length; i++) {
                    String[] teamVersus = matchList[i].split(" ");
                    String team1 = teamVersus[0];
                    String team2 = teamVersus[2];
                    fout.printf("%s,%s,Match %d\n", team1, team2, matchCount);
                    matchCount++;
                }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean saveToCSV(Team[] teams, String fPath) {
        try (PrintWriter fout = new PrintWriter(new FileWriter(fPath))) {
            fout.println("Team Name,Team ID,Player Count,Wins,Losses");
            for(int i = 0 ; i < teams.length; i++) {
                fout.printf("%s,%d,%d,%d,%d\n", teams[i].getTeamName(), teams[i].getTeamID(), teams[i].getPlayerCount(), teams[i].getWins(), teams[i].getLosses());
            }
            fout.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public String[][] readCSVDataSchedules(String filePath) {
        ArrayList<String[]> rows = new ArrayList<>(); 
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Skip the header line
                if (!line.startsWith("Team A")) {
                    rows.add(line.split(",")); 
                }
            }
        } catch (IOException e) {
            new MessageBox("ERROR: Could not read CSV file.", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return rows.toArray(new String[0][]); 
    }

    //FILEPATHOPERATIONS
    public String[][] readCSVPlayerData(String filePath) {
        ArrayList<String[]> rows = new ArrayList<>(); 
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Skip the header line
                if (!line.toLowerCase().startsWith("first name".toLowerCase())) {
                    rows.add(line.split(",")); 
                }
            }
        } catch (IOException e) {
            new MessageBox("ERROR: Could not read CSV file.",JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return rows.toArray(new String[0][]); 
    }

    //FILEPATHOPERATIONS
    public void writePlayerStatsToCSV(String[][] playerStats, String[] tempColumnHeaders, String filePath) {
        String[] columnHeaders = new String[8]; 
    
        columnHeaders[0] = "First Name";
        columnHeaders[1] = "Last Name";
        columnHeaders[2] = "Jersey Number";
        columnHeaders[3] = "POINT/S";
        columnHeaders[4] = "REBOUNDS/S";
        columnHeaders[5] = "ASSIST/S";
        columnHeaders[6] = "BLOCK/S";
        columnHeaders[7] = "STEAL/S";
    
        try (FileWriter output = new FileWriter(filePath)) {
            // Write the headers to the CSV
            for (int i = 0; i < columnHeaders.length; i++) {
                output.write(columnHeaders[i]);
                if (i < columnHeaders.length - 1) {
                    output.write(",");
                }
            }
            output.write("\n");
    
            // Write the player statistics to the CSV
            for (int i = 0; i < playerStats.length; i++) {
                for (int j = 0; j < playerStats[i].length; j++) {
                    output.write(playerStats[i][j]);
                    if (j < playerStats[i].length - 1) {
                        output.write(",");
                    }
                }
                output.write("\n");
            }
            new MessageBox("Player Stats saved to CSV successfully", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            new MessageBox("Unable to write Player Stats file to CSV", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    //FILEPATHOPERATIONS
    public void writeGameReportToCSV(String[] team1, String[] team2, String[] overallMatchStats) {
        String filePath = Constants.DATA_DIR + Constants.STATS_DIR + overallMatchStats[0] + "VS" + overallMatchStats[1] + ".csv"; //Change later to actual file path
        String[] columnHeaders = {
                "TEAM NAME 1", "TEAM NAME 2", "TOTAL POINTS", "LONGEST LEAD", "TOTAL REBOUNDS", 
                "TOTAL BLOCKS", "TOTAL STEALS", "TOTAL ASSISTS",  "TEAM 1 SCORE", "TEAM 2 SCORE", "WINNER"
        };
    
        String[] teamStatHeaders = {
                "TEAM NAME", "Q1 SCORE","Q2 SCORE","Q3 SCORE","Q4 SCORE", 
                "TEAM REBOUNDS", "TEAM BLOCKS", "TEAM STEALS", "TEAM ASSISTS"
        };
     
        try (FileWriter writer = new FileWriter(filePath)) {
    
            writeLine(writer, columnHeaders);
    
            writeLine(writer, overallMatchStats);
            
            writer.write("\n");
    
            writeLine(writer, teamStatHeaders);
            
            writer.write(overallMatchStats[0] + ",");  // Team 1 name
            writeLine(writer, team1);
            
            writer.write(overallMatchStats[1] + ",");  // Team 2 name
            writeLine(writer, team2);
    
            new MessageBox("Game Report saved to CSV successfully", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            new MessageBox("Unable to write Game Report file to CSV", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void writeLine(FileWriter writer, String[] data) throws IOException {
        for (int i = 0; i < data.length; i++) {
            writer.write(data[i]);
            if (i < data.length - 1) {
                writer.write(",");
            }
        }
        writer.write("\n");
    }
}
