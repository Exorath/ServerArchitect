/*
 * Copyright 2016 Exorath
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.exorath.serverarchitect.handler;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.exorath.serverarchitect.lib.StringLoader;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Map;

/**
 * Created by toonsev on 11/25/2016.
 */
public class S3Handler implements ConfigHandler {
    public S3Handler() {

    }

    @Override
    public void loadMaps(Map<String, Object> configSection, File mapsDir) {

    }

    @Override
    public void loadJar(Map<String, Object> configSection, File jarFile) {
        if (configSection == null)
            return;
        if(jarFile == null)
            return;
        if (!configSection.containsKey("bucketName")) {
            System.out.println("No 'bucketName' defined in s3 jar config section. Jar not loaded.");
            System.exit(1);
        }
        if (!configSection.containsKey("objectId")) {
            System.out.println("No 'objectId' defined in s3 jar config section. Jar not loaded.");
            System.exit(1);
        }
        if (!configSection.containsKey("accessKeyId")) {
            System.out.println("No 'accessKeyId' defined in s3 jar config section. Jar not loaded.");
            System.exit(1);
        }
        if (!configSection.containsKey("secretKey")) {
            System.out.println("No 'secretKey' defined in s3 jar config section. Jar not loaded.");
            System.exit(1);
        }
        if (!configSection.containsKey("region")) {
            System.out.println("No 'region' defined in s3 jar config section. Jar not loaded.");
            System.exit(1);
        }
        String bucketName = StringLoader.getValue(configSection, "bucketName");
        String objectId = StringLoader.getValue(configSection, "objectId");
        String accessKeyId = StringLoader.getValue(configSection, "accessKeyId");
        String secretKey = StringLoader.getValue(configSection, "secretKey");
        String regionId = StringLoader.getValue(configSection, "region");
        System.out.println("jar specification: ");
        System.out.println(" bucketName: " + bucketName);
        System.out.println(" objectId: " + objectId);
        System.out.println(" accessKeyId: " + accessKeyId);
        System.out.println(" secretKey==null: " + secretKey == null);
        System.out.println(" region: " + regionId);
        if (bucketName == null || objectId == null || accessKeyId == null || secretKey == null || regionId == null) {
            System.out.println("ServerArchitect: S3 jar config value not found.");
            System.exit(1);
        }
        AmazonS3 s3 = new AmazonS3Client();
        Region region = Region.getRegion(Regions.valueOf(regionId));
        s3.setRegion(region);

        try {
            s3.createBucket(bucketName);
        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() != 409) {//Bucket already created
                e.printStackTrace();
                System.exit(1);
            }
        }
        S3Object s3Object = s3.getObject(new GetObjectRequest(bucketName, objectId));
        if(s3Object == null){
            System.out.println("ServerArchitect: s3 jar not found, terminating.");
            System.exit(1);
        }
        try(InputStream contentInput = s3Object.getObjectContent()) {
            try(OutputStream fileOutput = new FileOutputStream(jarFile)) {
                if (contentInput == null) {
                    System.out.println("ServerArchitect: s3 jar content empty, terminating.");
                    System.exit(1);
                }
                IOUtils.copy(contentInput, fileOutput);
            }
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }
}
