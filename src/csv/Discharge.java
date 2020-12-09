package csv;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

class Discharge {
    final String filePath = "src/movementList.csv";
    private ArrayList<String[]> parsedMovementList;
    private BufferedReader bufferedReader;
    private HashMap<String, Double> companies = new HashMap<>();
    Discharge() {
        parsedMovementList = null;
    }

    public void parseCSV() {
        String filePath = this.filePath;
        parsedMovementList = new ArrayList<>();
        bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader( new FileInputStream(filePath), "windows-1251"));
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                parsedMovementList.add(line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<String[]> getParsedMovementList() {
        return parsedMovementList;
    }

    private double parseStringToDouble(String s){
        String str = s.trim();
        if(str.charAt(0) == '\"'){
            str = str.substring(1);
        }
        if (str.charAt(str.length() - 1) == '\"') {
            str = str.substring(0, str.length() - 1);
        }
        return Double.parseDouble(str.replace(',', '.'));
    }

    public HashMap<String, Double> getCompanies(){
        ArrayList<String[]> list = parsedMovementList;
        HashMap<String, Double> buff = new HashMap<>();
        for (int i = 1; i < list.size(); i++) {
            String companyName =  getCompanyName(list.get(i)[5]);
            if(buff.containsKey(companyName)){
                buff.replace(companyName, buff.get(companyName) + parseStringToDouble(list.get(i)[7]));
            }
            else{
                buff.put(companyName, parseStringToDouble(list.get(i)[7]));
            }
        }
        return buff;
    }

    public double getTotalIncome() {
        double buff = 0;
        for (int i = 1; i < parsedMovementList.size(); i++) {
            buff+=parseStringToDouble(parsedMovementList.get(i)[6]);
        }
        return buff;
    }

    public double getTotalConsumption() {
        double buff = 0;
        for (int i = 1; i < parsedMovementList.size(); i++) {
            buff+=parseStringToDouble(parsedMovementList.get(i)[7]);
        }
        return buff;
    }

    private String getCompanyName(String dirtyData) {
        String divider;
        if(dirtyData.contains("/")) divider = "/";
        else if(dirtyData.contains("\\")) divider = "\\\\";
        else divider = "";
        String[] dirtyArray = dirtyData.split(divider);
        int end = 1;
        String appendix = "";
        for(int i = 0; i < dirtyArray.length; i++){
            String s = dirtyArray[i];
            if(s.matches("^.+\\d{2}[.]\\d{2}[.]\\d{2}.+$")){
                end = i;
                appendix = s.split("\\d{2}[.]\\d{2}[.]\\d{2}")[0].strip();
            }
        }
        int start = 1;
        if(dirtyArray[1].matches("^\\d+$")) start = 2;
        String total = "";
        for (int i = start; i < end; i++) {
            total += dirtyArray[i];
        }
        total += appendix;
        return total;
    }

    public static void main(String[] args) {

        Discharge discharge = new Discharge();
        discharge.parseCSV();
        System.out.println("Сумма доходов: " + discharge.getTotalIncome()+ " руб.");
        System.out.println("Сумма расходов: " + discharge.getTotalConsumption() + " руб.");
        for(HashMap.Entry<String, Double> entry: discharge.getCompanies().entrySet()){
            System.out.println(entry.getKey() + " - " + entry.getValue() + " руб.");
        }
    }
}
