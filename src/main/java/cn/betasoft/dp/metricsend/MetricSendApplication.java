package cn.betasoft.dp.metricsend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MetricSendApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MetricSendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
