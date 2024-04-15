import java.io.*;

public class FileOperations {
    private static final String DATA_DIR = "CSVFolder/";
    // Method to extract CSV Data and return as an array of Teams
    public Team[] extractTeamData() {
        String fpath = DATA_DIR + "Teams.csv";
        File dir = new File(fpath);
        if (!dir.exists()) {
            return new Team[0];
        }
        int size = 0;
        Team[] teamData = new Team[size];
        String line;
        try (BufferedReader fin = new BufferedReader(new FileReader(dir))) {
            while((line = fin.readLine()) != null) {
                size++;
                // TODO: Validate CSV Data and load into players array
            }
            fin.close();
        } catch (IOException e) {
            return new Team[0];
        }
        return teamData;
    }
        // Checks if the data has any other inputs other than alphabets
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
            for (int i = 0; i < input.length(); i++) {
                if (input.charAt(0) == '-' || input.charAt(0) == '+') continue;
                if (!(input.charAt(i) >= '0' && input.charAt(i) <= '9')) return false;
            }
            return true;
        }  
}
