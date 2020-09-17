package cn.betasoft.dp.metricsend.kafka;

import cn.betasoft.dp.metricsend.protobuf.MetricValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

@Slf4j
public class MetricValueDeserializer implements Deserializer<MetricValue> {

    @Override
    public MetricValue deserialize(String topic, byte[] data) {
        try {
            return MetricValue.parseFrom(data);
        } catch (Exception ex) {
            log.error("Received unparseable message", ex);
            throw new RuntimeException("Received unparseable message " + ex.getMessage(), ex);
        }
    }
}
