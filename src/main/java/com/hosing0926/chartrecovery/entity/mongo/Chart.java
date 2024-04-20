package com.hosing0926.chartrecovery.entity.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "chart")
public class Chart {

    private String symbol;

    private String interval;

    private BigDecimal openPrice;

    private BigDecimal highPrice;

    private BigDecimal lowPrice;

    private BigDecimal closePrice;

    private BigDecimal volume;

    private Long startTime;

    private Long endTime;

    public Chart(String symbol, String interval, Long startTime, Long endTime) {
        this.symbol = symbol;
        this.interval = interval;
        this.highPrice = BigDecimal.ZERO;
        this.lowPrice = BigDecimal.ZERO;
        this.closePrice = BigDecimal.ZERO;
        this.volume = BigDecimal.ZERO;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
