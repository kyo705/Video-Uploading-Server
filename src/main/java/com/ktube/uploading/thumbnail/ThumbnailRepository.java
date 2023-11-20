package com.ktube.uploading.thumbnail;

import org.springframework.web.multipart.MultipartFile;

public interface ThumbnailRepository {

    boolean isExistingFilePath(String filePath);
    void upload(MultipartFile thumbnailImage, String uploadingFilePath);
}
