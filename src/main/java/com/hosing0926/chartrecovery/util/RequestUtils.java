package com.hosing0926.chartrecovery.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class RequestUtils {

    private RequestUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (hasIpAddress(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (hasIpAddress(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (hasIpAddress(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (hasIpAddress(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (hasIpAddress(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (hasIpAddress(ip)) {
            ip = request.getHeader("X-RealIP");
        }
        if (hasIpAddress(ip)) {
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (hasIpAddress(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getQueryString(HttpServletRequest request) {
        String queryString = request.getQueryString();
        if (StringUtils.isEmpty(queryString)) {
            queryString = "-";
        }
        return queryString;
    }

    public static Object getRequestBody(HttpServletRequest request) {
        Object requestBody = request.getAttribute("requestBody");
        if (requestBody == null) {
            requestBody = "-";
        }

        return requestBody;
    }

    private static boolean hasIpAddress(String ip) {
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }
}
