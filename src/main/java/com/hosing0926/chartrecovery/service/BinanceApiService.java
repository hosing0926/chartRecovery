package com.hosing0926.chartrecovery.service;

import com.hosing0926.chartrecovery.client.BinanceApiClient;
import com.hosing0926.chartrecovery.entity.mongo.Chart;
import com.hosing0926.chartrecovery.exception.ApiException;
import com.hosing0926.chartrecovery.exception.Error;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BinanceApiService {

    private final BinanceApiClient client;

    public List<Chart> getChart(String symbol, String interval, Long strStringTime, Long endStringTime) {
        try {
            List<Chart> charts = new ArrayList<>();

            if (endStringTime == null) {
                endStringTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) * 1000;
            }

            //TODO chart data mapping
// [
//  [
//            1499040000000,      // Kline open time
//            "0.01634790",       // Open price
//            "0.80000000",       // High price
//            "0.01575800",       // Low price
//            "0.01577100",       // Close price
//            "148976.11427815",  // Volume
//            1499644799999,      // Kline Close time
//            "2434.19055334",    // Quote asset volume
//            308,                // Number of trades
//            "1756.87402397",    // Taker buy base asset volume
//            "28.46694368",      // Taker buy quote asset volume
//            "0"                 // Unused field, ignore.
//  ]
//]
//
            List<Object> binanceCharts = client.getCharts(symbol, interval, strStringTime, endStringTime, 1000);
            for (Object binanceChart : binanceCharts) {
                Chart chart = Chart.builder()
                        .symbol(symbol)
                        .interval(interval)
                        .build();
            }

            return charts;
        } catch (Exception e) {
            log.error("getChart {} {}", symbol, interval);
            e.printStackTrace();
            throw new ApiException(Error.BINANCE_API_ERROR);
        }
    }
}
