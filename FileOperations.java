import java.io.*;
import java.util.ArrayList;
import java.util.List;
// import java.util.Arrays;

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
}
