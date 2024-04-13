package com.hosing0926.chartrecovery.controller;

import com.hosing0926.chartrecovery.service.ChartService;
import com.hosing0926.chartrecovery.request.RecoveryChartRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("/chart/recovery")
public class ChartController {

    private final ChartService chartService;

    @PostMapping
    public String recoveryChartBySymbol(@RequestBody RecoveryChartRequest request) {
        return chartService.recoveryChartBySymbol(request);
    }

    @PostMapping("/all")
    public String recoveryChartAllSymbol(@RequestBody RecoveryChartRequest request) {
        return chartService.recoveryChartAllSymbol(request);
    }
}
