package com.hosing0926.chartrecovery.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "BinanceApiClient", url = "https://api.binance.com", path = "/api")
public interface BinanceApiClient {

    @GetMapping("/v3/klines")
    List<Object> getCharts(
            @RequestParam String symbol,
            @RequestParam String interval,
            @RequestParam Long startTime,
            @RequestParam Long endTime,
            @RequestParam int limit
    );
}
