package com.ktube.uploading.thumbnail;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class RequestThumbnailUploadingDto {

    private String channelId;
    private String videoId;
    private MultipartFile multipartFile;
}
