package com.ktube.uploading.video;

import java.io.IOException;
import java.io.InputStream;

public interface VideoRepository {

    void save(InputStream data, String path) throws IOException;
}
