package com.hosing0926.chartrecovery.service;

import com.hosing0926.chartrecovery.constant.Constant;
import com.hosing0926.chartrecovery.entity.mongo.Chart;
import com.hosing0926.chartrecovery.entity.mysql.Symbol;
import com.hosing0926.chartrecovery.exception.ApiException;
import com.hosing0926.chartrecovery.exception.Error;
import com.hosing0926.chartrecovery.request.RecoveryChartRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChartService {

    private final SymbolService symbolService;
    private final BinanceApiService binanceApiService;

    private final MongoTemplate mongoTemplate;

    public String recoveryChartBySymbol(RecoveryChartRequest request) {
        int successCount = 0;

        Symbol symbol = symbolService.getBySymbol(request.getSymbol());
        if (symbol == null) throw new ApiException(Error.NOT_EXIST_SYMBOL);

        for (String interval : Constant.INTERVALS) {
            boolean result = recoveryChart(symbol.getSymbol(), interval, request.getStartTime(), request.getEndTime());
            if (result) successCount++;
        }

        return "Complete " + request.getSymbol() + " " + successCount + " Recovery Chart";
    }

    public String recoveryChartAllSymbol(RecoveryChartRequest request) {
        int successCount = 0;

        List<Symbol> symbols = symbolService.getSymbols();
        for (Symbol symbol : symbols) {
            for (String interval : Constant.INTERVALS) {
                boolean result = recoveryChart(symbol.getSymbol(), interval, request.getStartTime(), request.getEndTime());
                if (result) successCount++;
            }
        }

        return "Complete " + successCount + "Recovery Chart";
    }

    private boolean recoveryChart(String symbol, String interval, String strStringTime, String endStringTime) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            LocalDateTime startTime = LocalDateTime.parse(strStringTime, formatter);
            Long startTimestamp = startTime.toEpochSecond(ZoneOffset.UTC) * 1000;
            Criteria criteria = Criteria.where("startTime").gte(startTimestamp);

            Long endTimestamp = null;
            if (!Strings.isEmpty(endStringTime)) {
                LocalDateTime endTime = LocalDateTime.parse(endStringTime, formatter);
                endTimestamp = endTime.toEpochSecond(ZoneOffset.UTC) * 1000;
                criteria = criteria.andOperator(Criteria.where("endTime").lt(endTimestamp));
            }

            Query query = new Query(criteria);
            mongoTemplate.remove(query, Chart.class, String.format(Constant.CHART_COLLECTION_NAME, symbol, interval));

            List<Chart> charts = binanceApiService.getChart(symbol, interval, startTimestamp, endTimestamp);
            mongoTemplate.insert(charts, String.format(Constant.CHART_COLLECTION_NAME, symbol, interval));

            return true;
        } catch (Exception e) {
            log.error("recoverChart {} message: {}", symbol, e.getMessage());
            return false;
        }
    }

    public List<Chart> getChart(String symbol, String interval, Long startTime, Long endTime) {
        Criteria criteria = Criteria.where("startTime").gte(startTime)
                .andOperator(Criteria.where("endTime").lt(endTime));
        Query query = new Query(criteria);

        return mongoTemplate.find(query, Chart.class, String.format(Constant.CHART_COLLECTION_NAME, symbol, interval));
    }
}
