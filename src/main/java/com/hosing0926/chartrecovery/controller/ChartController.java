package com.hosing0926.chartrecovery.controller;

import com.hosing0926.chartrecovery.entity.mongo.Chart;
import com.hosing0926.chartrecovery.response.ApiResponse;
import com.hosing0926.chartrecovery.service.ChartService;
import com.hosing0926.chartrecovery.request.RecoveryChartRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController("/chart")
public class ChartController {

    private final ChartService chartService;

    @GetMapping
    public ApiResponse getChart(
            @RequestParam String symbol, @RequestParam String interval,
            @RequestParam Long startTime, @RequestParam Long endTime)
    {
        List<Chart> charts = chartService.getChart(symbol, interval, startTime, endTime);

        return ApiResponse.builder().data(charts).build();
    }

    @PostMapping("/recovery")
    public ApiResponse recoveryChartBySymbol(@RequestBody RecoveryChartRequest request) {
        String response = chartService.recoveryChartBySymbol(request);

        return ApiResponse.builder().data(response).build();
    }

    @PostMapping("/recovery/all")
    public ApiResponse recoveryChartAllSymbol(@RequestBody RecoveryChartRequest request) {
        String response = chartService.recoveryChartAllSymbol(request);

        return ApiResponse.builder().data(response).build();
    }
}
