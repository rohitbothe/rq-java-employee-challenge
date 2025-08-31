package com.reliaquest.api.filter;

import com.reliaquest.api.controller.EmployeeControllerImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * @author Rohit Bothe
 */
@Component
public class RequestLogger extends CommonsRequestLoggingFilter {

    private static final Logger log = LoggerFactory.getLogger(EmployeeControllerImpl.class);

    public RequestLogger() {
        setIncludeQueryString(true);
        setIncludePayload(true);
        setMaxPayloadLength(10000);
        setIncludeHeaders(false);
        setIncludeClientInfo(true);
        setBeforeMessagePrefix("");
        setAfterMessagePrefix("");
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        long startTime = (long) request.getAttribute("apiRequestStartTime");
        long endTime = System.currentTimeMillis();
        log.debug(
                "Request Completed. HTTPMethod={}, message={}, TimeTakenToComplete={} ms",
                request.getMethod(),
                message,
                (endTime - startTime));
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        request.setAttribute("apiRequestStartTime", System.currentTimeMillis());
        log.debug("Request started HTTPMethod={}, message={}", request.getMethod(), message);
    }
}
