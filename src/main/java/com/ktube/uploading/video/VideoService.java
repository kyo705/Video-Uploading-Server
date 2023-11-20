package com.ktube.uploading.video;

import me.desair.tus.server.exception.TusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface VideoService {

    ResponseUploadingDto upload(String channelId, String videoId,
                                HttpServletRequest request, HttpServletResponse response) throws IOException, TusException;

}
