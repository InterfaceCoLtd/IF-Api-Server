package xyz.interfacesejong.interfaceapi.global.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoggingInterceptor implements HandlerInterceptor {
    private final Logger LOGGER = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIP = request.getRemoteAddr();
        String requestURI = request.getRequestURI();
        LOGGER.info("[{}] request from IP: {}", requestURI, clientIP);
        return true;
    }
}
