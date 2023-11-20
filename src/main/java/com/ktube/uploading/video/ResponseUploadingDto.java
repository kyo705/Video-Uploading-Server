package com.ktube.uploading.video;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ResponseUploadingDto {

    private int code;
    private String message;
    private String filePath;
}
