package com.ktube.uploading.thumbnail;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktube.uploading.error.SavingFileException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.web.servlet.MockMvc;

import static com.ktube.uploading.thumbnail.ThumbnailConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest(value = {ThumbnailController.class, ThumbnailExceptionHandler.class})
public class ThumbnailControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired private MockMvc mockMvc;
    @MockBean private ThumbnailService thumbnailService;


    @DisplayName("허용된 파일 확장자를 가진 파일을 업로드 요청시 업로드가 성공한다.")
    @Test
    void testUploadWithValidParam() throws Exception {

        //given
        MockMultipartFile thumbnailFile = new MockMultipartFile(
                "thumbnail", "test.jpg", "image/jpg", new byte[10]);

        String thumbnailFilePath = "/1/1/1_thumbnail.jpg";
        BDDMockito.given(thumbnailService.upload(any(), any())).willReturn(thumbnailFilePath);

        //when & then
        mockMvc.perform(
                multipart(THUMBNAIL_UPLOADING_URL,
                        1L, 1L, "1_thumbnail")
                        .file(thumbnailFile)
                )
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, thumbnailFilePath))
                .andDo(result -> {
                    ResponseThumbnailUploadingDto responseBody = objectMapper.readValue(
                            result.getResponse().getContentAsString(), ResponseThumbnailUploadingDto.class);

                    assertThat(responseBody.isCompleted()).isTrue();
                    assertThat(responseBody.getMessage()).isEqualTo(SUCCESSFUL_COMPLETION_MESSAGE);
                })
                .andDo(MockMvcRestDocumentation.document("thumbnail/upload/success",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())));
    }

    @DisplayName("허용되지 않은 확장자를 가진 파일을 업로드 요청시 400 상태코드를 리턴한다.")
    @Test
    void testUploadWithNotAllowedFormat() throws Exception {

        //given
        MockMultipartFile thumbnailFile = new MockMultipartFile(
                "thumbnail", "not_allowed_format_file.mp4", "image/jpg", new byte[10]);

        //when & then
        mockMvc.perform(
                        multipart(THUMBNAIL_UPLOADING_URL,
                                1L, 1L, "1_thumbnail")
                                .file(thumbnailFile)
                )
                .andExpect(status().isBadRequest())
                .andDo(result -> {
                    ResponseThumbnailUploadingDto responseBody = objectMapper.readValue(
                            result.getResponse().getContentAsString(), ResponseThumbnailUploadingDto.class);

                    assertThat(responseBody.isCompleted()).isFalse();
                    assertThat(responseBody.getMessage()).isEqualTo(BAD_THUMBNAIL_FILE_FORMAT_MESSAGE);
                })
                .andDo(MockMvcRestDocumentation.document("thumbnail/upload/notAllowedFormat",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())));
    }

    @DisplayName("존재하지 않는 파일을 업로드 요청시 400 상태코드를 리턴한다.")
    @Test
    void testUploadWithNotExistingThumbnailFile() throws Exception {

        //given
        MockMultipartFile thumbnailFile = new MockMultipartFile(
                "thumbnail", null, null, new byte[0]);

        //when & then
        mockMvc.perform(
                        multipart(THUMBNAIL_UPLOADING_URL,
                                1L, 1L, "1_thumbnail")
                                .file(thumbnailFile)
                )
                .andExpect(status().isBadRequest())
                .andDo(result -> {
                    ResponseThumbnailUploadingDto responseBody = objectMapper.readValue(
                            result.getResponse().getContentAsString(), ResponseThumbnailUploadingDto.class);

                    assertThat(responseBody.isCompleted()).isFalse();
                    assertThat(responseBody.getMessage()).isEqualTo(NOT_EXISTING_THUMBNAIL_FILE_MESSAGE);
                })
                .andDo(MockMvcRestDocumentation.document("thumbnail/upload/notExistingFile",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())));
    }

    @DisplayName("썸네일 파일 저장 중 오류가 발생할 경우 500 상태코드를 리턴한다.")
    @Test
    void testUploadWithUploadingError() throws Exception {

        //given
        MockMultipartFile thumbnailFile = new MockMultipartFile(
                "thumbnail", "test.jpg", "image/jpg", new byte[10]);

        BDDMockito.given(thumbnailService.upload(any(), any()))
                .willThrow(new SavingFileException(FAILURE_UPLOADING_THUMBNAIL_FILE_MESSAGE));

        //when & then
        mockMvc.perform(
                        multipart(THUMBNAIL_UPLOADING_URL,
                                1L, 1L, "1_thumbnail")
                                .file(thumbnailFile)
                )
                .andExpect(status().isInternalServerError())
                .andDo(result -> {
                    ResponseThumbnailUploadingDto responseBody = objectMapper.readValue(
                            result.getResponse().getContentAsString(), ResponseThumbnailUploadingDto.class);

                    assertThat(responseBody.isCompleted()).isFalse();
                    assertThat(responseBody.getMessage()).isEqualTo(FAILURE_UPLOADING_THUMBNAIL_FILE_MESSAGE);
                })
                .andDo(MockMvcRestDocumentation.document("thumbnail/upload/uploadingError",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())));
    }
}
