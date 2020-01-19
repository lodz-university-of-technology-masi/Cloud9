package com.serverless.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.serverless.db.model.Question;
import com.serverless.handlers.form.AddUserToFormHandler;
import com.serverless.handlers.pojo.ResponseListErrorMessagePojo;
import com.serverless.settings.BucketSettings;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BucketService {
    private AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
    private static final Logger LOG = LogManager.getLogger(BucketService.class);
    private ResponseListErrorMessagePojo error = new ResponseListErrorMessagePojo();
    public BucketService() {}

    public List<String> uploadFile(String nameFile) throws IOException {
        nameFile = "private/us-east-1:37f21b3a-fba6-48fc-a06c-9152edc830db/"+nameFile;
        S3Object s3Object = s3.getObject(new GetObjectRequest(BucketSettings.BUCKET_NAME, nameFile));
        InputStream objectData = s3Object.getObjectContent();
        LOG.info(s3Object.getKey());
        BufferedReader reader = new BufferedReader(new InputStreamReader(objectData));
        String line;
        List<String> linses = new ArrayList<String>();
        while ((line = reader.readLine()) != null)
            linses.add(line);

        return linses;
    }

    public void deleteFile(String nameFile) throws  AmazonServiceException {
        s3.deleteObject(BucketSettings.BUCKET_NAME, nameFile);
    }
    public List<Bucket> listBucket(){
        List<Bucket> buckets = s3.listBuckets();
        return buckets;
    }
}
