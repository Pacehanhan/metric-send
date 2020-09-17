package cn.betasoft.dp.metricsend.kafka;

import cn.betasoft.dp.metricsend.protobuf.MetricValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Service
public class KafkaProducerService {

    private KafkaTemplate kafkaTemplate;

    public KafkaProducerService(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessageAsync(String topic, String key, MetricValue metricValue) {
        ProducerRecord producerRecord = new ProducerRecord(topic, null, metricValue.getSampleTime(), key, metricValue);
        ListenableFuture<SendResult<Integer, String>> future = kafkaTemplate.send(producerRecord);

        // 设置异步发送消息获取发送结果后执行的动作
        ListenableFutureCallback listenableFutureCallback = new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                log.info("success");
            }

            @Override
            public void onFailure(Throwable ex) {
                log.info("failure");
            }
        };

        // 将listenableFutureCallback与异步发送消息对象绑定
        future.addCallback(listenableFutureCallback);
    }

}
