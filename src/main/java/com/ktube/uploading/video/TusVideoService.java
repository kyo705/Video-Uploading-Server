package com.ktube.uploading.video;

import com.ktube.uploading.thumbnail.VideoMetadataRepository;
import lombok.RequiredArgsConstructor;
import me.desair.tus.server.TusFileUploadService;
import me.desair.tus.server.exception.TusException;
import me.desair.tus.server.upload.UploadInfo;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.UUID;

import static com.ktube.uploading.video.UploadingConstants.FILE_FORMAT_SEPARATOR;
import static com.ktube.uploading.video.UploadingConstants.ORIGINAL_FILE_PATH;
import static com.ktube.uploading.video.UploadingState.*;

@RequiredArgsConstructor
@Service
public class TusVideoService implements VideoService {

    private final TusFileUploadService tusFileUploadService;
    private final VideoRepository videoRepository;
    private final VideoMetadataRepository videoMetadataRepository;

    @Override
    public ResponseUploadingDto upload(String channelId, String videoId,
                                       HttpServletRequest request, HttpServletResponse response) throws IOException, TusException {

        tusFileUploadService.process(request, response);

        UploadInfo uploadInfo = tusFileUploadService.getUploadInfo(request.getRequestURI());

        if(uploadInfo == null) { //업로딩 상태 없는 경우
            return getResponseUploadingDto(READY, null);
        }
        if(uploadInfo.isUploadInProgress()) {  //업로딩 진행 중인 경우
            return getResponseUploadingDto(RUNNING, null);
        }
        // 업로딩 완료된 경우
        String originalFilePath = createOriginalFilePath(channelId, videoId, uploadInfo.getFileName());
        InputStream data = tusFileUploadService.getUploadedBytes(request.getRequestURI());

        videoRepository.save(data, originalFilePath);
        tusFileUploadService.deleteUpload(request.getRequestURI()); // 기존 분할 파일 삭제

        //queue에 상태 값 저장
        videoMetadataRepository.updateVideoState(videoId, VideoState.SAVED.name());

        return getResponseUploadingDto(COMPLETE, originalFilePath);
    }

    private String createOriginalFilePath(String channelId, String videoId, String fileName) {

        String directoryPath = String.format(ORIGINAL_FILE_PATH, channelId, videoId);
        String[] fileNameSplits = fileName.split("[.]");
        String format = fileNameSplits[fileNameSplits.length-1];

        return Paths.get(directoryPath, UUID.randomUUID() + FILE_FORMAT_SEPARATOR + format).toString();
    }


    private ResponseUploadingDto getResponseUploadingDto(UploadingState state, String filePath) {

        return ResponseUploadingDto.builder()
                .filePath(filePath)
                .message(state.getMessage())
                .code(state.getCode())
                .build();
    }
}
