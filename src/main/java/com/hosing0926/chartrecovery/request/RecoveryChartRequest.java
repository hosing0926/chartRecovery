package com.hosing0926.chartrecovery.request;

import lombok.Data;

@Data
public class RecoveryChartRequest {

    private String symbol;

    private String startTime;

    private String endTime;
}
