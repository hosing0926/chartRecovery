package com.hosing0926.chartrecovery.entity.mongo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Builder
@Data
@Document(collection = "chart")
public class Chart {

    private String symbol;

    private String interval;

    private BigDecimal openPrice;

    private BigDecimal lowPrice;

    private BigDecimal highPrice;

    private BigDecimal closePrice;

    private BigDecimal change;

    private BigDecimal percentChange;

    private BigDecimal volume;

    private String startTime;

    private String endTime;
}
