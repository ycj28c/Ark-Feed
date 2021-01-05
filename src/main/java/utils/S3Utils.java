package utils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.UUID;

public class S3Utils {

    //https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/examples-s3.html
    private static String accessKey;
    private static String secretKey;
    private static String awsBase;
    private static final String bucket = "manyfeeds";
    private static final String directory = "arkfeed";

    static {
        accessKey = System.getProperty("access_key");
        secretKey = System.getProperty("secret_key");
        awsBase = System.getProperty("aws_base");
    }

    /**
     * Instantiate S3 client.
     *
     * @return
     * @throws NullPointerException
     */
    private static AmazonS3 getS3Client() throws NullPointerException {
        if (accessKey == null || "".equals(accessKey)) {
            System.out.println("access key for Credentials is empty!");
            throw new NullPointerException("Empty Access Key");
        }
        if (secretKey == null || "".equals(secretKey)) {
            System.out.println("secret key for Credentials  is empty!");
            throw new NullPointerException("Empty Secret Key");
        }
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_2)
                .build();
        return s3;
    }

    /**
     * build the aws file name per environment config
     * @param fileName
     * @return
     */
    private static String buildAWSFileNamePerEnv(String fileName){
        return directory + "/" + fileName;
    }

    public static String uploadToS3(String fileName, String sourceFilePath) {
        return uploadToS3(bucket, fileName, sourceFilePath);
    }

    /**
     * Upload file to S3, so Mailman can download the attachment from AWS.
     *
     * @param bucketName
     * @param fileName
     * @param sourceFilePath
     * @return
     */
    public static String uploadToS3(String bucketName, String fileName, String sourceFilePath) {
        try {
            //AWS s3 is stateless, each upload will be signed connection
            AmazonS3 s3Client = getS3Client();
            System.out.println("start uploadToS3");
            System.out.println("bucketName: " + bucketName);
            System.out.println("fileName: " + fileName);
            System.out.println("filePath: " + sourceFilePath);

            String awsFileName = buildAWSFileNamePerEnv(fileName);
            PutObjectRequest request = new PutObjectRequest(bucketName, awsFileName, new File(sourceFilePath));
            request.withCannedAcl(CannedAccessControlList.PublicRead);
            request.setMetadata(new ObjectMetadata());
            request.getMetadata().setContentDisposition("attachment");

            System.out.println("Uploading S3 file to: " + fileName);
            s3Client.putObject(request);

            String fileUrl = awsBase + "/" + bucketName + "/" + awsFileName;
            System.out.println("The file has uploaded to S3, file url: " + fileUrl);
            return fileUrl;
        } catch (AmazonServiceException ase) {
             System.out.println("Caught an AmazonServiceException, which means your "
                    + "request made it to Amazon S3, but was rejected "
                    + "with an error response for some reason.");
             System.out.println("Error Message:    " + ase.getMessage());
             System.out.println("HTTP Status Code: " + ase.getStatusCode());
             System.out.println("AWS Error Code:   " + ase.getErrorCode());
             System.out.println("Error Type:       " + ase.getErrorType());
             System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
             System.out.println("Caught an AmazonClientException, which means the client "
                    + "encountered an internal error while trying to communicate "
                    + "with S3, such as not being able to access the network.");
             System.out.println("Error Message: " + ace.getMessage());
        } catch(Exception e) {
             System.out.println("Exception: " + e.getMessage());
        }
        return null;
    }

    public static String downloadS3File(String fileName) {
        return downloadS3File(bucket, fileName);
    }

    public static String downloadS3File(String bucketName, String fileName) {
        try {
            //AWS s3 is stateless, each upload will be signed connection
            AmazonS3 s3Client = getS3Client();
            System.out.println("start downloadS3File");
            System.out.println("bucketName: " + bucketName);
            System.out.println("fileName: " + fileName);

            String awsFileName = buildAWSFileNamePerEnv(fileName);
            S3Object s3object = s3Client.getObject(bucketName, awsFileName);
            S3ObjectInputStream inputStream = s3object.getObjectContent();

            String destFilePath = "tmpFile" + UUID.randomUUID().toString().replace("-", "");
            FileUtils.copyInputStreamToFile(inputStream, new File(destFilePath));

            System.out.println("download file path: " + destFilePath);
            return destFilePath;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
