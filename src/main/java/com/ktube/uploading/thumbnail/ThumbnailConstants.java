package com.ktube.uploading.thumbnail;

public class ThumbnailConstants {

    public static final String FORMAT_SEPARATOR = ".";
    public static final String IMAGE_CONTENT_TYPE_KEYWORD = "image";
    public static final String THUMBNAIL_UPLOADING_URL = "/api/channel/{channelId}/video/{videoId}/thumbnail";
    public static final boolean SUCCESSFUL_COMPLETION_STATE = true;
    public static final boolean FAIL_COMPLETION_STATE = false;
    public static final String SUCCESSFUL_COMPLETION_MESSAGE = "uploading thumbnail is completed successfully";
    public static final String BAD_THUMBNAIL_FILE_FORMAT_MESSAGE = "not allowed image format";
    public static final String NOT_EXISTING_THUMBNAIL_FILE_MESSAGE = "not existing uploading thumbnail file";
    public static final String FAILURE_UPLOADING_THUMBNAIL_FILE_MESSAGE = "fail during upload thumbnail file";


}
