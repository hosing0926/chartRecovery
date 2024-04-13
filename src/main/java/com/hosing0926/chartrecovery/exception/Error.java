package com.hosing0926.chartrecovery.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Error {

    NOT_EXIST_SYMBOL(10001, "NOT EXIST SYMBOL"),
    BINANCE_API_ERROR(20001, "BINANCE API ERROR");

    private int code;
    private String message;
}
