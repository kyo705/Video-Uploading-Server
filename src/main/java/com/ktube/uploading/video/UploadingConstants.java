package com.ktube.uploading.video;

public class UploadingConstants {

    /**
     * /video/{channel-id}/{video-id}/original/{video-id}.{file-format}
     */
    public static final String ORIGINAL_FILE_PATH = "/video/%s/%s/original";

    public static final String VIDEO_UPLOADING_URL = "/api/channel/{channelId}/video/{videoId}";
    public static final String TUS_VIDEO_UPLOADING_URL = "/api/channel/[0-9]+/video/[0-9]+";

    public static final String FILE_FORMAT_SEPARATOR = ".";
}
