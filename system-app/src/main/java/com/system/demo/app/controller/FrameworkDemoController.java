package com.system.demo.app.controller;

import com.system.demo.app.service.FrameworkDemoService;
import com.system.demo.core.web.response.ApiResponse;
import com.system.demo.core.web.response.PageQuery;
import com.system.demo.core.web.response.PageResponse;
import com.system.demo.core.web.response.Results;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/framework")
public class FrameworkDemoController {
    private final FrameworkDemoService frameworkDemoService;

    public FrameworkDemoController(FrameworkDemoService frameworkDemoService) {
        this.frameworkDemoService = frameworkDemoService;
    }

    @GetMapping("/success")
    public ApiResponse<String> success() {
        return Results.success("ok");
    }

    @GetMapping("/failure")
    public ApiResponse<Void> failure() {
        return Results.fail(10001, "demo failure");
    }

    @GetMapping("/page")
    public PageResponse<String> page(@Valid PageQuery pageQuery) {
        return Results.page(frameworkDemoService.pageDemo(pageQuery));
    }

    @GetMapping("/raw")
    public String raw() {
        return "raw-ok";
    }

    @GetMapping("/datasource/verify")
    public ApiResponse<Map<String, Object>> verifyDataSource(@RequestParam(value = "key", required = false) String key) {
        return Results.success(frameworkDemoService.dataSourceDemo(key));
    }

    @GetMapping("/redis/verify")
    public ApiResponse<Map<String, Object>> verifyRedis() {
        return Results.success(frameworkDemoService.redisDemo());
    }
}
