package com.hosing0926.chartrecovery.constant;

import java.util.Arrays;
import java.util.List;

public class Constant {

    public static List<String> INTERVALS = Arrays.asList
            (
                    "1m", "3m", "5m", "15m", "30m",
                    "1h", "2h", "4h", "6h", "8h", "12h",
                    "1d", "3d",
                    "1w", "1M"
            );

    public static List<Long> INTERVAL_WIGHT = Arrays.asList
            (
                    60000L, 180000L, 300000L, 600000L, 900000L, 1800000L,
                    3600000L, 7200000L, 14400000L, 21600000L,
                    28800000L, 43200000L,
                    86400000L, 259200000L,
                    604800000L, 2629800000L
            );

    public static String CHART_COLLECTION_NAME = "chart_%s_%s";
}
