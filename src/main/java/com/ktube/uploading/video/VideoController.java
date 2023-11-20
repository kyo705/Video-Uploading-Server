package com.ktube.uploading.video;

import lombok.RequiredArgsConstructor;
import me.desair.tus.server.exception.TusException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.ktube.uploading.video.UploadingConstants.VIDEO_UPLOADING_URL;

@RequiredArgsConstructor
@RestController
public class VideoController {

    private final VideoService videoService;

    @RequestMapping(VIDEO_UPLOADING_URL)
    public ResponseUploadingDto upload(@PathVariable String channelId,
                                       @PathVariable String videoId,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws TusException, IOException {

        return videoService.upload(channelId, videoId, request, response);
    }
}
