package com.ktube.uploading.video;

import lombok.Getter;

@Getter
public enum UploadingState {

    READY(0, "uploading is not start yet"),
    RUNNING(1, "uploading is running"),
    STOP(2, "uploading is stopped"),
    COMPLETE(3, "uploading is completed");

    private final int code;
    private final String message;

    UploadingState(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
