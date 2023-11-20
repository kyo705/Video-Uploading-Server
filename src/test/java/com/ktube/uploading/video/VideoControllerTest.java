package com.ktube.uploading.video;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.desair.tus.server.HttpHeader;
import me.desair.tus.server.TusFileUploadService;
import me.desair.tus.server.upload.UploadInfo;
import me.desair.tus.server.upload.UploadStorageService;
import me.desair.tus.server.upload.disk.DiskStorageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.ktube.uploading.video.UploadingConstants.VIDEO_UPLOADING_URL;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
public class VideoControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired private MockMvc mockMvc;
    @Autowired private VideoService videoService;
    @Autowired private TusFileUploadService tusFileUploadService;
    @Value("${tus.data.path}") String storagePath;

    @DisplayName("Tus 프로토콜 초기 연결시 이전에 저장한 파일 일부분이 없다면 404 상태코드를 리턴한다.")
    @Test
    void testConnectingWithNotExistingFileMetadata() throws Exception {

        //HEAD /files/24e533e02ec3bc40c387f1a0e460e216 HTTP/1.1
        //Host: tus.example.org
        //Tus-Resumable: 1.0.0

        //when & then
        mockMvc.perform(
                        head(VIDEO_UPLOADING_URL, 1L, 1L)
                                .header(HttpHeader.TUS_RESUMABLE, TusFileUploadService.TUS_API_VERSION)
                )
                .andExpect(status().isNotFound())
                .andDo(MockMvcRestDocumentation.document("video/upload/initWithNotExistingFileMetadata",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())));
    }

    @DisplayName("Tus 프로토콜 초기 연결시 이전에 저장한 파일 일부분이 없다면 404 상태코드를 리턴한다.")
    @Test
    void testUpload() throws Exception {

        //PATCH /files/24e533e02ec3bc40c387f1a0e460e216 HTTP/1.1
        //Host: tus.example.org
        //Content-Type: application/offset+octet-stream
        //Content-Length: 30
        //Upload-Offset: 70
        //Tus-Resumable: 1.0.0

        UploadInfo uploadInfo = new UploadInfo();
        uploadInfo.setLength(120L);

        UploadStorageService uploadStorageService = new DiskStorageService(storagePath);
        tusFileUploadService.withUploadStorageService(uploadStorageService);

        uploadStorageService.create(uploadInfo, "key1");

        //when & then
        mockMvc.perform(
                        patch(VIDEO_UPLOADING_URL, 1L, 1L)
                                .header(HttpHeader.TUS_RESUMABLE, TusFileUploadService.TUS_API_VERSION)
                                .header(HttpHeader.CONTENT_TYPE, "application/offset+octet-stream")
                                .header(HttpHeader.CONTENT_LENGTH, 30)
                                .header(HttpHeader.UPLOAD_OFFSET, 70)
                                .content(new byte[30])
                )
                .andExpect(status().isNotFound())
                .andDo(MockMvcRestDocumentation.document("video/upload/process",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())));
    }
}
