package com.hosing0926.chartrecovery.entity.redis;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.math.BigDecimal;

@Data
@RedisHash("price")
public class Price {

    private String symbol;

    private BigDecimal openPrice;

    private BigDecimal lowPrice;

    private BigDecimal highPrice;

    private BigDecimal closePrice;

    private BigDecimal change;

    private BigDecimal percentChange;

    private BigDecimal coinVolume;

    private BigDecimal BaseVolume;

    private String openTime;

    private String closeTime;

    private String lastUpdateTime;
}
