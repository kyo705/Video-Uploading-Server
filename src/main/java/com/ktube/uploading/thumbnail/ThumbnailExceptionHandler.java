package com.ktube.uploading.thumbnail;

import com.ktube.uploading.error.SavingFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice(assignableTypes = ThumbnailController.class)
public class ThumbnailExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ResponseThumbnailUploadingDto> handleIllegalArgumentException(IllegalArgumentException ex){

        log.error(ex.getMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setDate(System.currentTimeMillis());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .headers(headers)
                .body(ResponseThumbnailUploadingDto
                        .builder()
                        .isCompleted(ThumbnailConstants.FAIL_COMPLETION_STATE)
                        .message(ex.getMessage())
                        .build()
                );
    }

    @ExceptionHandler({SavingFileException.class})
    public ResponseEntity<ResponseThumbnailUploadingDto> handleSavingFileException(SavingFileException ex){

        log.error(ex.getMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setDate(System.currentTimeMillis());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .headers(headers)
                .body(ResponseThumbnailUploadingDto
                        .builder()
                        .isCompleted(ThumbnailConstants.FAIL_COMPLETION_STATE)
                        .message(ex.getMessage())
                        .build()
                );
    }
}
