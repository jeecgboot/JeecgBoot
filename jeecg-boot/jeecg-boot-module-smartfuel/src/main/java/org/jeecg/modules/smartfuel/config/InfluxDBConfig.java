package org.jeecg.modules.smartfuel.config;

import com.influxdb.LogLevel;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * influxdb配置
 */

@Component
public class InfluxDBConfig {
    @Value("${spring.influx.url:''}")
    private String url;
    @Value("${spring.influx.token:''}")
    private String token;
    @Value("${spring.influx.org:''}")
    private String org;
    @Value("${spring.influx.bucket:''}")
    private String bucket;
    @Value("${spring.influx.measurement:''}")
    private String measurement;

    @Bean
    public InfluxDBClient influxDBClient() {
        InfluxDBClient influxDBClient = InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
        influxDBClient.setLogLevel(LogLevel.BASIC);
        return influxDBClient;
    }

//    @Bean
//    public InfluxDBUtil influxDBUtil() {
//        return new InfluxDBUtil(url, token, org, bucket, measurement);
//    }
}
