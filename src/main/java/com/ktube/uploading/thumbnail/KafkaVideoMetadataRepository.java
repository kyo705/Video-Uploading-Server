package com.ktube.uploading.thumbnail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Repository;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@RequiredArgsConstructor
@Repository
public class KafkaVideoMetadataRepository implements VideoMetadataRepository {

    private static final String UPDATING_THUMBNAIL_PATH_TOPIC = "TOPIC_NAME";
    private static final String UPDATING_VIDEO_STATE_TOPIC = "TOPIC_NAME";

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void updateThumbnailPath(String videoId, String thumbnailImagePath) {

        ListenableFuture<SendResult<String, String>> result =
                kafkaTemplate.send(UPDATING_THUMBNAIL_PATH_TOPIC, videoId, thumbnailImagePath);

        addKafkaSendCallback(result);
    }

    @Override
    public void updateVideoState(String videoId, String videoState) {

        ListenableFuture<SendResult<String, String>> result =
                kafkaTemplate.send(UPDATING_VIDEO_STATE_TOPIC, videoId, videoState);

        addKafkaSendCallback(result);
    }

    private <K,V> void addKafkaSendCallback(ListenableFuture<SendResult<K, V>> result) {

        result.addCallback(new KafkaSendCallback<>() {

            @Override
            public void onSuccess(SendResult<K, V> result) {
                log.info("[success] record producing : " + result.getProducerRecord());
            }

            @Override
            public void onFailure(KafkaProducerException ex) {
                log.error("[failure] record producing : " + ex.getFailedProducerRecord() +
                        "because of " + ex.getMessage());
            }
        });
    }
}
