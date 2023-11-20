package com.ktube.uploading.video;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Repository
public class FileVideoRepository implements VideoRepository {

    @Value("${home_path}")
    private String homePath;

    @Override
    public void save(InputStream data, String filePath) {

        try {
            Path result = Files.createFile(Paths.get(homePath, filePath));
            FileUtils.copyInputStreamToFile(data, result.toFile());
        } catch (IOException e) {
            throw new IllegalStateException("fail to creating file");
        }
    }
}
