package utils;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;

import java.io.IOException;

/**
 * https://slack.dev/java-slack-sdk/guides/incoming-webhooks
 */
public class SlackUtils {

    private static Slack slack = Slack.getInstance();

    //example: "https://hooks.slack.com/services/xxxx/xxxx/xxxxxx";
    private static String webhookUrl;
    static {
        webhookUrl = System.getProperty("webhookUrl");
    }

    /**
     * send slack message
     *
     * @param payload "{\"text\":\"Hello, World!\"}";
     */
    public static void sendMessage(Payload payload) {
        try {
            WebhookResponse response = slack.send(webhookUrl, payload);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
