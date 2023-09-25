package com.ktube.uploading.thumbnail;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ResponseThumbnailUploadingDto {

    private boolean isCompleted;
    private String message;
}
