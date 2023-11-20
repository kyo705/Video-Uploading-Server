package com.ktube.uploading.thumbnail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.UUID;

import static com.ktube.uploading.thumbnail.ThumbnailConstants.FORMAT_SEPARATOR;

@RequiredArgsConstructor
@Service
public class ThumbnailService {

    private final ThumbnailRepository thumbnailRepository;
    private final VideoMetadataRepository videoMetadataRepository;

    public String upload(RequestThumbnailUploadingDto request) {

        String channelId = request.getChannelId();
        String videoId = request.getVideoId();
        MultipartFile thumbnail = request.getMultipartFile();

        String ThumbnailImagePath;
        do {
            ThumbnailImagePath = createUploadingFilePath(thumbnail.getOriginalFilename(), channelId, videoId);
        }
        while (thumbnailRepository.isExistingFilePath(ThumbnailImagePath));

        thumbnailRepository.upload(thumbnail, ThumbnailImagePath);
        videoMetadataRepository.updateThumbnailPath(videoId, ThumbnailImagePath);

        return ThumbnailImagePath;
    }

    private String createUploadingFilePath(String originalFilename, String... directoryPaths) {

        StringBuilder prefixPath = new StringBuilder();
        for(String directoryPath : directoryPaths) {
            prefixPath.append(directoryPath);
        }
        String format = originalFilename.substring(originalFilename.lastIndexOf(FORMAT_SEPARATOR));

        return Paths.get(prefixPath.toString(), UUID.randomUUID() + format).toString();
    }
}
