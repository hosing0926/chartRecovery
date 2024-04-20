package com.hosing0926.chartrecovery.service;

import com.hosing0926.chartrecovery.client.BinanceApiClient;
import com.hosing0926.chartrecovery.entity.mongo.Chart;
import com.hosing0926.chartrecovery.entity.mysql.Symbol;
import com.hosing0926.chartrecovery.exception.ApiException;
import com.hosing0926.chartrecovery.exception.Error;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BinanceApiService {

    private final BinanceApiClient client;

    public List<Chart> getChart(String symbol, String interval, Long startTime, Long endTime) {
        try {
            List<Chart> charts = new ArrayList<>();

            if (endTime == null) {
                endTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) * 1000;
            }

            List<Object[]> chartDatas = new ArrayList<>();
            List<Object[]> binanceCharts = client.getCharts(symbol, interval, startTime, endTime, 1000);
            chartDatas.addAll(binanceCharts);

            while (binanceCharts.size() >= 1000) {
                Object[] chart = binanceCharts.get(binanceCharts.size() - 1);
                startTime = (Long) chart[6] + 1;
                System.out.println(startTime);
                binanceCharts = client.getCharts(symbol, interval, startTime, endTime, 1000);
                chartDatas.addAll(binanceCharts);
            }

            for (Object[] chartData : chartDatas) {
                Chart chart = Chart.builder()
                        .symbol(symbol)
                        .interval(interval)
                        .openPrice(new BigDecimal((String) chartData[1]))
                        .highPrice(new BigDecimal((String) chartData[2]))
                        .lowPrice(new BigDecimal((String) chartData[3]))
                        .closePrice(new BigDecimal((String) chartData[4]))
                        .volume(new BigDecimal((String) chartData[5]))
                        .startTime((Long) chartData[0])
                        .endTime((Long) chartData[6])
                        .build();

                charts.add(chart);
            }

            return charts;
        } catch (Exception e) {
            log.error("getChart {} {}", symbol, interval);
            e.printStackTrace();
            throw new ApiException(Error.BINANCE_API_ERROR);
        }
    }
}
