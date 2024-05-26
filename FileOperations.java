import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileOperations {

    // Method that returns a boolean when: CSV File exists and follows the correct file structure
    public boolean checkFileStructure(String filePath, int fieldCount, int stringFields) {
        try (BufferedReader fin = new BufferedReader(new FileReader(filePath))) {
            String buffer;
            if((buffer = fin.readLine()) == null) {
                return false;
            }
            if ((buffer = fin.readLine()) != null) {} // Skip Header
            while((buffer = fin.readLine()) != null) {
                String[] subData = buffer.split(",");
                if(subData.length != fieldCount) {
                    return false;
                }
                for (int j = 0; j < subData.length; j++) {
                    boolean isValidField = (j > stringFields) ? checkIfAlphabet(subData[j]) : checkIfNumber(subData[j]);
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

    public Team[] extractTeamData(String fpath) {
        // Use ArrayList to dynamically add teams
        List<Team> teamList = new ArrayList<>(); 
        String line;
        try (BufferedReader fin = new BufferedReader(new FileReader(fpath))) {
            if ((line = fin.readLine()) != null) {}
            while ((line = fin.readLine()) != null) {
                System.out.println("extract test: " + line);
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
        if (input == null || input.isEmpty()) return false;
        String tmp = input.toLowerCase();
        if (input.length() == 0) return false;
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
}
