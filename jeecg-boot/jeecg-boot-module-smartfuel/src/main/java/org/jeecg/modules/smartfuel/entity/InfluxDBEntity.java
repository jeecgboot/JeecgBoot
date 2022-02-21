package org.jeecg.modules.smartfuel.entity;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Measurement(name="influxdbEntity")
@Getter
@Setter
public class InfluxDBEntity {
    /**
     * 值
     */
    @Column
    double value;
    /**
     * 标签字段
     */
    @Column(tag=true)
    String field;
    /**
     * 时间
     */
    @Column(timestamp = true)
    Instant time;

    public void setTime(Instant time) {
        Instant LocalTime = time.plusMillis(TimeUnit.HOURS.toMillis(8));
        this.time = LocalTime;
    }
}
