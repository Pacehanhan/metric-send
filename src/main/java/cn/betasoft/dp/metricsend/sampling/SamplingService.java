package cn.betasoft.dp.metricsend.sampling;

import cn.betasoft.dp.metricsend.kafka.KafkaConsumerService;
import cn.betasoft.dp.metricsend.kafka.KafkaProducerService;
import cn.betasoft.dp.metricsend.protobuf.MetricValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class SamplingService {

    @Value(value = "${message.topic.name}")
    private String topicName;

    private KafkaProducerService kafkaProducerService;

    public SamplingService(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostConstruct
    public void start() {
        log.info("start sampling");
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            Map<String, String> tags = Stream.of(new String[][]{
                    {"domain", "defaultEngine"},
                    {"category", "host"},
                    {"name", "30.0.11.123"}
            }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

            MetricValue metricValue = MetricValue.newBuilder()
                    .setMetricName("CPU_Load")
                    .setMoType("Windows")
                    .setMoId("00001")
                    .setMetricValue(new Double(10.0))
                    .setSampleTime(Instant.now().toEpochMilli())
                    .putAllMetricTags(tags)
                    .build();

            String key = String.join("_",metricValue.getMetricName(), metricValue.getMoType());
            kafkaProducerService.sendMessageAsync(topicName, key, metricValue);
            // log.info(metricValue.toString());

        }, 0, 5, TimeUnit.SECONDS);
    }
}
