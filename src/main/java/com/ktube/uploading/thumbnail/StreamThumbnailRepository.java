package com.ktube.uploading.thumbnail;

import com.ktube.uploading.error.SavingFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Repository
public class StreamThumbnailRepository implements ThumbnailRepository {

    @Value("${homePath}") private String homePath;

    @Override
    public boolean isExistingFilePath(String filePath) {
        Path realFilePath = Paths.get(homePath, filePath);
        if(!Files.exists(realFilePath)) {
            return false;
        }
        return !Files.isDirectory(realFilePath);
    }

    @Override
    public void upload(MultipartFile thumbnailImage, String uploadingFilePath) {
        Path realUploadingFilePath = Paths.get(homePath, uploadingFilePath);

        try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(realUploadingFilePath.toFile()))) {

            bos.write(uploadingFilePath.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new SavingFileException(e.getMessage());
        }
    }
}
