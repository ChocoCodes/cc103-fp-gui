import java.io.*;

public class FileOperations {
    
    private static final String DATA_DIR = "CSVFolder/";
    // Method that returns a boolean when: CSV File exists and follows the correct file structure
    public boolean checkFileStructure(String filePath, int fieldCount) {
        filePath = DATA_DIR + filePath;
        int n = 0;
        String[] fileStructure = new String[n];
        // Extract Preserved CSV Structure
        try (BufferedReader fin = new BufferedReader(new FileReader(filePath))) {
            String buffer;
            while((buffer = fin.readLine()) != null) {
                n++;
                String[] tmp = new String[n];
                for(int i = 0; i < fileStructure.length; i++) {
                    tmp[i] = fileStructure[i];
                }
                tmp[tmp.length - 1] = buffer;
                fileStructure = tmp;
            }
            fin.close();
        } catch (IOException e) {
            return false;
        }
        // TODO: Revamp according to numeric fields
        // Evaluate CSV Structure
        for(int i = 0; i < fileStructure.length; i++) {
            String[] subData = fileStructure[i].split(",");
            if (subData.length != fieldCount) return false;
            for (int j = 0; j < subData.length; j++) {
                boolean isValidField = (j == 1) ? checkIfAlphabet(subData[j]) : checkIfNumber(subData[j]);
                if (!isValidField) return false;
            }
        }
        return true;
    }

    // Method to extract CSV Data and return an array of Teams
    public Team[] extractTeamData() throws IOException {
        String fpath = DATA_DIR + "Teams.csv";
        int size = 0;
        Team[] teams = new Team[size];
        String line;
        BufferedReader fin = new BufferedReader(new FileReader(fpath));
        while((line = fin.readLine()) != null) {
            size++;
            // Split CSV Data and load into team array using getters and setters
            String[] tmp = line.split(",");
            for(int i = 0; i < teams.length; i++) {
                teams[i] = new Team(tmp[0], Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]), Integer.parseInt(tmp[3]), Integer.parseInt(tmp[4]));
            }
        }
        fin.close();
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
