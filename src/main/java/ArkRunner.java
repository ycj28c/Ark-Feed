import bean.CompanyData;
import com.slack.api.webhook.Payload;
import utils.Analyzer;
import utils.ArkFileUtils;
import utils.S3Utils;
import utils.SlackUtils;

import java.util.List;

/**
 * @author ryang
 * This runner is for local only, because the github action intergration only work for test
 */
public class ArkRunner {

    private static final String ARKW_OLD_JSON = "arkw_old_result.json";
    private static final String ARKG_OLD_JSON = "arkg_old_result.json";
    private static final String ARKW_CSV = "https://ark-funds.com/wp-content/fundsiteliterature/csv/ARK_NEXT_GENERATION_INTERNET_ETF_ARKW_HOLDINGS.csv";
    private static final String ARKG_CSV = "https://ark-funds.com/wp-content/fundsiteliterature/csv/ARK_GENOMIC_REVOLUTION_MULTISECTOR_ETF_ARKG_HOLDINGS.csv";

    public static void main(String[] args) {
        boolean isArkW = true;
        boolean isArkG = true;
        //read remote csv file
        if(isArkW) {
            List<CompanyData> newList = ArkFileUtils.loadRemoteCsv(ARKW_CSV);
            //SlackUtils.sendMessage(Payload.builder().text("Start analyzing ARKW Fund...!").build());
            //analyze the result
            if(newList != null && !newList.isEmpty()){
                //List<CompanyData> oldList = readFromFile(ARKW_OLD_JSON);
                List<CompanyData> oldList = ArkFileUtils.readFromS3File(ARKW_OLD_JSON);
                if(oldList != null && !oldList.isEmpty()){
                    Analyzer.highLevelAnalyze(oldList, newList, "ARKW");
                } else {
                    System.out.println("Fail to load ARKW file from s3.");
                    SlackUtils.sendMessage(Payload.builder().text("Fail to load ARKW file from s3").build());
                }
                //save the new file into disk
                ArkFileUtils.saveToFile(ARKW_OLD_JSON, newList);
                S3Utils.uploadToS3(ARKW_OLD_JSON, ARKW_OLD_JSON);
            } else {
                System.out.println("Fail to load the new fund portfolio file.");
                SlackUtils.sendMessage(Payload.builder().text("Fail to get ARKW feed.").build());
            }
        }

        if(isArkG) {
            List<CompanyData> newList = ArkFileUtils.loadRemoteCsv(ARKG_CSV);
            //SlackUtils.sendMessage(Payload.builder().text("Start analyzing ARKG Fund...!").build());
            //analyze the result
            if (newList != null && !newList.isEmpty()) {
                //List<CompanyData> oldList = readFromFile(ARKG_OLD_JSON);
                List<CompanyData> oldList = ArkFileUtils.readFromS3File(ARKG_OLD_JSON);
                if(oldList != null && !oldList.isEmpty()) {
                    Analyzer.highLevelAnalyze(oldList, newList, "ARKG");
                } else {
                    System.out.println("Fail to load ARKG file from s3.");
                    SlackUtils.sendMessage(Payload.builder().text("Fail to load ARKG file from s3").build());
                }
                //save the new file into disk
                ArkFileUtils.saveToFile(ARKG_OLD_JSON, newList);
                S3Utils.uploadToS3(ARKG_OLD_JSON, ARKG_OLD_JSON);
            } else {
                System.out.println("Fail to load the new fund portfolio file.");
                SlackUtils.sendMessage(Payload.builder().text("Fail to get ARKG feed.").build());
            }
        }

        //System.exit(0);
    }

}
