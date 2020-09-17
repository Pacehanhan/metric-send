package cn.betasoft.dp.metricsend.kafka;

import cn.betasoft.dp.metricsend.protobuf.MetricValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "#{'${message.topic.name}'.split(',')}", groupId = "alarm")
    public void consumer(List<ConsumerRecord<String, MetricValue>> data) {
        for (int i = 0; i < data.size(); i++) {
            ConsumerRecord<String, MetricValue> record = data.get(i);
            Optional<MetricValue> kafkaMessage = Optional.ofNullable(record.value());
            Long threadId = Thread.currentThread().getId();
            if (kafkaMessage.isPresent()) {
                MetricValue metricValue = kafkaMessage.get();
                log.info("consumer: --> message:{}, topic:{}, partition:{}, key:{}, offset:{}, threadId:{}", metricValue.toString(), record.topic(), record.partition(), record.key(), record.offset(), threadId);
            }
        }
    }
}
