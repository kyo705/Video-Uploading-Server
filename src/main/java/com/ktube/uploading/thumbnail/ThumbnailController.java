package com.ktube.uploading.thumbnail;

import lombok.RequiredArgsConstructor;
import me.desair.tus.server.HttpHeader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.ktube.uploading.thumbnail.ThumbnailConstants.*;

@RequiredArgsConstructor
@Controller
public class ThumbnailController {

    private final ThumbnailService thumbnailService;

    @PostMapping(THUMBNAIL_UPLOADING_URL)
    public ResponseEntity<ResponseThumbnailUploadingDto> upload(@PathVariable String channelId,
                                 @PathVariable String videoId,
                                 @PathVariable String thumbnailId,
                                 @RequestParam MultipartFile thumbnail) throws IOException {

        validateFormat(thumbnail);

        String directoryPath = Paths.get(channelId, videoId, thumbnailId).toString();
        String thumbnailFilePath = thumbnailService.upload(directoryPath, thumbnail);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeader.LOCATION, thumbnailFilePath)
                .body(ResponseThumbnailUploadingDto
                        .builder()
                        .isCompleted(SUCCESSFUL_COMPLETION_STATE)
                        .message(SUCCESSFUL_COMPLETION_MESSAGE)
                        .build()
                );
    }

    private void validateFormat(MultipartFile thumbnail) throws IOException {

        String originalFilename = thumbnail.getOriginalFilename();
        if(originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException(NOT_EXISTING_THUMBNAIL_FILE_MESSAGE);
        }
        if(!Files.probeContentType(Paths.get(originalFilename)).startsWith(IMAGE_CONTENT_TYPE_KEYWORD)) {
            throw new IllegalArgumentException(BAD_THUMBNAIL_FILE_FORMAT_MESSAGE);
        }
    }
}
