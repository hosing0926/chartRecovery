package com.hosing0926.chartrecovery.service;

import com.hosing0926.chartrecovery.constant.Constant;
import com.hosing0926.chartrecovery.entity.mongo.Chart;
import com.hosing0926.chartrecovery.entity.mysql.Symbol;
import com.hosing0926.chartrecovery.exception.ApiException;
import com.hosing0926.chartrecovery.exception.Error;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChartService {

    private final SymbolService symbolService;
    private final BinanceApiService binanceApiService;

    private final MongoTemplate mongoTemplate;

    @PostConstruct
    public void initChart() {
        List<Symbol> symbols = symbolService.getSymbols();
        for (Symbol symbol : symbols) {
            for (String interval : Constant.INTERVALS) {
                String collectionName = String.format(Constant.CHART_COLLECTION_NAME, symbol.getSymbol(), interval);
                Boolean existsChart = mongoTemplate.collectionExists(collectionName);
                if (existsChart) {
                    continue;
                }

                mongoTemplate.createCollection(collectionName);
                mongoTemplate.indexOps(collectionName).ensureIndex(new Index().on("startTime", Sort.Direction.DESC));
                System.out.println("[Create " + collectionName + " Collection]");
            }
        }
    }

    public boolean resetChart() {
        List<Symbol> symbols = symbolService.getSymbols();
        for (Symbol symbol : symbols) {
            for (String interval : Constant.INTERVALS) {
                try {
                    String collectionName = String.format(Constant.CHART_COLLECTION_NAME, symbol.getSymbol(), interval);
                    Boolean existsChart = mongoTemplate.collectionExists(collectionName);
                    if (!existsChart) {
                        continue;
                    }

                    mongoTemplate.dropCollection(collectionName);
                    System.out.println("[drop " + collectionName + " Collection]");

                    mongoTemplate.createCollection(collectionName);
                    mongoTemplate.indexOps(collectionName).ensureIndex(new Index().on("startTime", Sort.Direction.DESC));
                    System.out.println("[Create " + collectionName + " Collection]");
                } catch (Exception e) {
                    log.error("reset chart");
                }
            }

            System.out.println("[reset " + symbol.getSymbol() + " chart]");
        }

        return true;
    }

    public String recoveryChartBySymbol(String symbol, Long startTime, Long endTime) {
        int successCount = 0;

        Symbol symbolEntity = symbolService.getBySymbol(symbol);
        if (symbolEntity == null) throw new ApiException(Error.NOT_EXIST_SYMBOL);

        List<Chart> charts = recovery1mChart(symbol, startTime, endTime);
        for (int i = 1; i < Constant.INTERVALS.size(); i++) {
            boolean result = recoveryChart(symbol, Constant.INTERVALS.get(i), Constant.INTERVAL_WIGHT.get(i), startTime, endTime, charts);
            if (result) successCount++;
        }

        return "Complete " + symbol + " " + successCount + " Recovery Chart";
    }

    public String recoveryChartAllSymbol(Long startTime, Long endTime) {
        List<Symbol> symbols = symbolService.getSymbols();
        for (Symbol symbol : symbols) {
            recoveryChartBySymbol(symbol.getSymbol(), startTime, endTime);
        }

        return "Complete All Symbol Recovery Chart";
    }

    private List<Chart> recovery1mChart(String symbol, Long startTime, Long endTime) {
        removeChart(startTime, endTime, symbol, "1m");

        List<Chart> oneMinuteCharts = binanceApiService.getChart(symbol, "1m", startTime, endTime);
        return mongoTemplate.insert(oneMinuteCharts, String.format(Constant.CHART_COLLECTION_NAME, symbol, "1m")).stream().collect(Collectors.toList());
    }

    public List<Chart> getChart(String symbol, String interval, Long startTime, Long endTime) {
        Criteria criteria = Criteria.where("startTime").gte(startTime)
                .andOperator(Criteria.where("endTime").lt(endTime));

        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC,"startTime"));
        Query query = new Query(criteria).with(sort);

        return mongoTemplate.find(query, Chart.class, String.format(Constant.CHART_COLLECTION_NAME, symbol, interval));
    }

    private boolean recoveryChart(String symbol, String interval, Long intervalWight, Long startTime, Long endTime, List<Chart> oneMinuteCharts) {
        try {
            removeChart(startTime, endTime, symbol, interval);

            List<Chart> charts = new ArrayList<>();
            Long start = startTime;
            Long end = (start + intervalWight) - 1;
            Chart chart = new Chart(symbol, interval, start, end);
            for (Chart oneMinuteChart : oneMinuteCharts) {
                if (oneMinuteChart.getEndTime().compareTo(chart.getEndTime()) == 0) {
                    charts.add(chart);

                    start = chart.getEndTime() + 1;
                    end = (start + intervalWight) - 1;
                    chart = new Chart(symbol, interval, start, end);
                } else {
                    chart.setOpenPrice(chart.getOpenPrice() == null ? oneMinuteChart.getOpenPrice() : chart.getOpenPrice());
                    chart.setHighPrice(chart.getHighPrice().max(oneMinuteChart.getHighPrice()));
                    chart.setLowPrice(chart.getLowPrice().max(oneMinuteChart.getLowPrice()));
                    chart.setClosePrice(oneMinuteChart.getClosePrice());
                    chart.setVolume(chart.getOpenPrice().add(chart.getVolume()));
                }
            }

            mongoTemplate.insert(charts, String.format(Constant.CHART_COLLECTION_NAME, symbol, interval));
            return true;
        } catch (Exception e) {
            log.error("recoverChart {} message: {}", symbol, e.getMessage());
            return false;
        }
    }

    private void removeChart(Long startTime, Long endTime, String symbol, String x) {
        Criteria criteria = Criteria.where("startTime").gte(startTime);
        if (endTime != null) {
            criteria = criteria.andOperator(Criteria.where("endTime").lt(endTime));
        }

        Query query = new Query(criteria);
        mongoTemplate.remove(query, Chart.class, String.format(Constant.CHART_COLLECTION_NAME, symbol, x));
    }
}
