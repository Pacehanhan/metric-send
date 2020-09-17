package cn.betasoft.dp.metricsend.kafka;

import cn.betasoft.dp.metricsend.protobuf.MetricValue;
import org.apache.kafka.common.serialization.Serializer;

public class MetricValueSerializer implements Serializer<MetricValue> {

    @Override
    public byte[] serialize(String topic, MetricValue metricValue) {
        return metricValue.toByteArray();
    }
}
