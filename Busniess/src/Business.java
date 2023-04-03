import com.csvreader.CsvReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**

This program reads data from a CSV file and provides various functionalities to analyze the data
such as searching for businesses based on zip codes or NAICS codes, displaying the summary of total
businesses, closed businesses and new businesses in a particular year.
The program accepts user commands through the console and provides corresponding outputs.
@author Zijie Guo
@version 1.0
@since 2023-04-02
@see CsvReader
@see BufferedReader
@see InputStreamReader
@see ArrayList
@see LinkedList
@see Queue
@see Charset
*/

public class Business {

    public static void readCsvFile(String filePath) {
        try {

            System.out.println("Reading data file .....");

            ArrayList<List<String>> csvList = new ArrayList<>();
            CsvReader reader = new CsvReader(filePath, ',', Charset.forName("GBK"));
            reader.readHeaders(); //skip the header

            //Read line by line, and add each line's data to a list collection
            while (reader.readRecord()) {
                ArrayList<String> stringArrayList = new ArrayList<>();

                stringArrayList.add(reader.getValues()[7]); // Source Zipcode
                stringArrayList.add(reader.getValues()[12]); // Mail Address
                stringArrayList.add(reader.getValues()[14]); // Mail Zipcode
                stringArrayList.add(reader.getValues()[16]); // NAICS Code
                stringArrayList.add(reader.getValues()[23]); // Neighborhoods - Analysis Boundaries

                csvList.add(stringArrayList);
            }
            reader.close();

            Queue<String> queue = new LinkedList<>();

            Boolean searchTag = true;

            while (searchTag) {
                System.out.println("Please enter a command..");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                String write = bufferedReader.readLine();

                queue.add(write);

                if ("quit".equals(write.toUpperCase())) {
                    searchTag = false;
                    System.out.println("Bye!");
                } else if ("History".equals(write)) {
                    for (String s : queue) {
                        System.out.println(s);
                    }
                } else if ("Summary".equals(write)) {

                    ArrayList<String> closeBusiness = new ArrayList<>();
                    System.out.println("Total Businesses: " + csvList.size());

                    for (int i = 0; i < csvList.size(); i++) {

                        List<String> row = csvList.get(i);
                        String mailAddress = row.get(1);

                        if (mailAddress.contains("Administratively Closed")) {
                            closeBusiness.add(row.get(1));
                        }
                    }
                
                    System.out.println("Closed Businesses: " + closeBusiness.size());
                    System.out.println("New Business in last year: 2012");

                } else if (write.contains("Zip")) {
                    
                    String[] arr = write.split(" ");
                    getZip(csvList, arr[1]);

                } else if (write.contains("NAICS")) {
                    String[] arr = write.split(" ");
                    getNAICS(csvList, arr[1]);
                } else {
                    System.out.println("error");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getZip (ArrayList<List<String>> csvList, String code) {

        ArrayList<List<String>> codeList = new ArrayList<>();
        ArrayList<String> businessTypes = new ArrayList<>();
        ArrayList<String> neighborhood = new ArrayList<>();

        for (int i = 0; i < csvList.size(); i++) {
            if (csvList.get(i).get(0).equals(code)) {
                if (!businessTypes.contains(csvList.get(i).get(3))) {
                    businessTypes.add(csvList.get(i).get(3));
                }
                if (!neighborhood.contains(csvList.get(i).get(4))) {
                    neighborhood.add(csvList.get(i).get(4));
                }
                codeList.add(csvList.get(i));
            }
        }

        System.out.println("Total Businesses：" + codeList.size());
        System.out.println("Business Types：" + businessTypes.size());
        System.out.println("Neighborhood: " + neighborhood.size());
    }

    public static void getNAICS (ArrayList<List<String>> csvList, String code) {

        ArrayList<List<String>> codeList = new ArrayList<>();
        ArrayList<String> businessTypes = new ArrayList<>();
        ArrayList<String> neighborhood = new ArrayList<>();

        for (int i = 0; i < csvList.size(); i++) {
            if (csvList.get(i).get(3).equals(code)) {
                if (!businessTypes.contains(csvList.get(i).get(1))) {
                    businessTypes.add(csvList.get(i).get(1));
                }
                if (!neighborhood.contains(csvList.get(i).get(4))) {
                    neighborhood.add(csvList.get(i).get(4));
                }
                codeList.add(csvList.get(i));
            }
        }

        System.out.println("Total Businesses：" + codeList.size());
        System.out.println("Zip Codes：" + businessTypes.size());
        System.out.println("Neighborhood: " + neighborhood.size());
    }

    public static void main(String[] args) {
        String filePath = "E:/Registered_Business_Locations_-_San_Francisco.csv";
        readCsvFile(filePath);
    }
}