package com.ktube.uploading.thumbnail;

public interface VideoMetadataRepository {

    void updateThumbnailPath(String videoId, String thumbnailImagePath);

    void updateVideoState(String videoId, String videoState);
}
