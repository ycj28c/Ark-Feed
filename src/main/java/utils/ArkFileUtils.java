package utils;

import bean.CompanyData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ArkFileUtils {

    public static String readLineByLineJava8(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    public static List<CompanyData> readFromFile(String fileName) {
        try {
            File f = new File(fileName);
            if(!f.exists()){
                return null;
            }
            // create a reader
            String oldJson = readLineByLineJava8(fileName);
            //create ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            if(oldJson == null || oldJson.isEmpty()){
                return null;
            }
            return objectMapper.readValue(oldJson, new TypeReference<List<CompanyData>>() {
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static List<CompanyData> readFromS3File(String fileName) {
        try {
            String dwnFile = S3Utils.downloadS3File(fileName);
            if(dwnFile == null){
                return null;
            }
            // create a reader
            String oldJson = readLineByLineJava8(dwnFile);
            //create ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            if(oldJson == null || oldJson.isEmpty()){
                return null;
            }
            return objectMapper.readValue(oldJson, new TypeReference<List<CompanyData>>() {
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void saveToFile(String fileName, List<CompanyData> list) {
        try {
            File yourFile = new File(fileName);
            yourFile.createNewFile();
            // create a writer
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
            // create ObjectMapper instance
            ObjectMapper mapper = new ObjectMapper();
            // write JSON to file
            writer.write(mapper.writeValueAsString(list));
            //close the writer
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static List<CompanyData> loadRemoteCsv(String baseurl) {
        //String csvFile = "https://ark-funds.com/wp-content/fundsiteliterature/csv/ARK_NEXT_GENERATION_INTERNET_ETF_ARKW_HOLDINGS.csv";
        List<CompanyData> newList = new ArrayList<>();
        try {
            //add this, will able to works, otherwise will display 403 ERROR
            System.setProperty("http.agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.82 Safari/537.36");

            URL url12 = new URL(baseurl);
            URLConnection urlConn = url12.openConnection();
            InputStreamReader inStream = new InputStreamReader(urlConn.getInputStream());
            BufferedReader buff = new BufferedReader(inStream);

            String line = buff.readLine();
            //skip header
            while (line != null) {
                try {
                    line = buff.readLine();
                    System.out.println(line);
                    if (line == null || line.startsWith(",")
                            || line.startsWith("The") || !Character.isDigit(line.charAt(0))) {
                        //end of the line
                        break;
                    } else {
                        String[] strs = line.split(",");
                        CompanyData companyData = new CompanyData();
                        companyData.setDate(strs[0]);
                        companyData.setFund(strs[1]);
                        companyData.setCompany(strs[2]);
                        companyData.setTicker(strs[3]);
                        companyData.setCusip(strs[4]);
                        companyData.setShares(Double.valueOf(strs[5]));
                        companyData.setMarketValue(Double.valueOf(strs[6]));
                        companyData.setWeight(Double.valueOf(strs[7]));
                        //System.out.println(companyData.toString());
                        newList.add(companyData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return newList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
