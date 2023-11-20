package com.ktube.uploading.config;

import me.desair.tus.server.TusFileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.ktube.uploading.video.UploadingConstants.TUS_VIDEO_UPLOADING_URL;

@Configuration
public class UploadingConfig {

    @Value("${tus.data.path}")
    private String tusDataPath;

    @Value("${tus.data.expiration}")
    private Long tusDataExpiration;

    @Bean
    public TusFileUploadService tus() {

        return new TusFileUploadService()
                .withStoragePath(tusDataPath)
                .withDownloadFeature()
                .withUploadExpirationPeriod(tusDataExpiration)
                .withThreadLocalCache(true)
                .withUploadURI(TUS_VIDEO_UPLOADING_URL);
    }

}
