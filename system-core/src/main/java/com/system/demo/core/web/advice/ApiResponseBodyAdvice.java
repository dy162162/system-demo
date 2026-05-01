package com.system.demo.core.web.advice;

import com.system.demo.core.web.response.ApiResponse;
import com.system.demo.core.web.response.Results;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import com.system.demo.core.web.response.PageResponse;

@RestControllerAdvice(basePackages = "com.system.demo")
public class ApiResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    private final ObjectMapper objectMapper;

    public ApiResponseBodyAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {
        if (body == null) {
            return Results.success();
        }
        if (body instanceof ApiResponse || body instanceof PageResponse) {
            return body;
        }
        if (body instanceof String) {
            ((ServletServerHttpResponse) response).getServletResponse().setContentType(MediaType.APPLICATION_JSON_VALUE);
            try {
                return objectMapper.writeValueAsString(Results.success(body));
            } catch (JsonProcessingException ex) {
                return "{\"code\":500,\"message\":\"serialization error\",\"timestamp\":" + System.currentTimeMillis() + "}";
            }
        }
        return Results.success(body);
    }
}
