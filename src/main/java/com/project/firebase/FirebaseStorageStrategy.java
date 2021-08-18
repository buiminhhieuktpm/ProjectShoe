package com.project.firebase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
@PropertySource("classpath:application.properties")
public class FirebaseStorageStrategy implements StorageStrategy{

    private static final Logger logger = LoggerFactory.getLogger(FirebaseStorageStrategy.class);

    @Autowired
    private Environment environment;

    private Storage storage;

    @EventListener
    public void init(ApplicationReadyEvent event) {
        try {
            ClassPathResource serviceAccount = new ClassPathResource("serviceAccountKey.json");
            storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
                    .setProjectId(environment.getRequiredProperty("FIREBASE_PROJECT_ID")).build().getService();
        } catch (Exception ex) {
            logger.error("Cannot init ", ex);
        }
    }

    //upload file to firebase
    @Override
    public String saveImage(MultipartFile file, String path) {
        try {
            if ("jpg,png,jpeg".contains(Objects.requireNonNull(getExtension(file.getOriginalFilename())))) {

                String imageName = generateFileName(file.getOriginalFilename());
                String tokens = org.apache.commons.lang3.StringUtils.substringBeforeLast(imageName, ".");

                Map<String, String> map = new HashMap<>();
                map.put(environment.getRequiredProperty("FIREBASE_DOWLOAD_TOKENS"), tokens);

                BlobId blobId = BlobId.of(environment.getRequiredProperty("FIREBASE_BUCKET_NAME"), path + "/" + imageName);
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                        .setMetadata(map)
                        .setContentType(file.getContentType())
                        .build();

                Blob blob = storage.create(blobInfo, file.getBytes());
                return imageName;
            }
        } catch (IOException e) {
            logger.error("Cannot upload file", e);
        }
        return null;
    }

    //name file
    private String generateFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "." + getExtension(originalFileName);
    }

    //type file
    private String getExtension(String originalFileName) {
        return StringUtils.getFilenameExtension(originalFileName);
    }

}
