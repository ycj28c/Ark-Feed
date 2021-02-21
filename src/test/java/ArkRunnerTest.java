import bean.ArkType;
import bean.CompanyData;
import com.slack.api.webhook.Payload;
import org.testng.annotations.Test;
import utils.Analyzer;
import utils.ArkFileUtils;
import utils.S3Utils;
import utils.SlackUtils;

import java.util.List;

public class ArkRunnerTest {

    @Test
    public void arkRunnerTest() {

        //read remote csv file
        for (ArkType arkType : ArkType.values()) {
            String type = arkType.name();
            List<CompanyData> newList = ArkFileUtils.loadRemoteCsv(arkType.getCsvLink());
            //SlackUtils.sendMessage(Payload.builder().text("Start analyzing ARKW Fund...!").build());
            //analyze the result
            if (newList != null && !newList.isEmpty()) {
                //List<CompanyData> oldList = readFromFile(ARKW_OLD_JSON);
                List<CompanyData> oldList = ArkFileUtils.readFromS3File(arkType.getFileName());
                if (oldList != null && !oldList.isEmpty()) {
                    Analyzer.highLevelAnalyze(oldList, newList, type);
                } else {
                    System.out.println("Fail to load " + type + " file from s3.");
                    SlackUtils.sendMessage(Payload.builder().text("Fail to load " + type + " file from s3").build());
                }
                //save the new file into disk
                ArkFileUtils.saveToFile(arkType.getFileName(), newList);
                S3Utils.uploadToS3(arkType.getFileName(), arkType.getFileName());
            } else {
                System.out.println("Fail to load the new fund portfolio file.");
                SlackUtils.sendMessage(Payload.builder().text("Fail to get " + type + " feed.").build());
            }
        }
    }
}
