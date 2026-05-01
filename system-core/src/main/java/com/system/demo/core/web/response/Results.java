package com.system.demo.core.web.response;

import java.util.Collections;
import java.util.List;

public final class Results {
    private Results() {
    }

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<T>();
        response.setCode(ResultCode.SUCCESS.code());
        response.setMessage(ResultCode.SUCCESS.message());
        response.setData(data);
        response.setTimestamp(System.currentTimeMillis());
        return response;
    }

    public static ApiResponse<Void> success() {
        return success(null);
    }

    public static ApiResponse<Void> fail(ResultCode code) {
        return fail(code.code(), code.message());
    }

    public static ApiResponse<Void> fail(int code, String message) {
        ApiResponse<Void> response = new ApiResponse<Void>();
        response.setCode(code);
        response.setMessage(message);
        response.setData(null);
        response.setTimestamp(System.currentTimeMillis());
        return response;
    }

    public static <T> PageResponse<T> page(List<T> records, long total, long pageNum, long pageSize) {
        PageResponse<T> response = new PageResponse<T>();
        response.setCode(ResultCode.SUCCESS.code());
        response.setMessage(ResultCode.SUCCESS.message());
        response.setRecords(records == null ? Collections.<T>emptyList() : records);
        response.setTotal(total);
        response.setPageNum(pageNum);
        response.setPageSize(pageSize);
        long totalPages = pageSize <= 0 ? 0 : (total + pageSize - 1) / pageSize;
        response.setTotalPages(totalPages);
        response.setHasNext(pageNum < totalPages);
        response.setTimestamp(System.currentTimeMillis());
        return response;
    }

    public static <T> PageResponse<T> page(PageResult<T> pageResult) {
        return page(pageResult.getRecords(), pageResult.getTotal(), pageResult.getPageNum(), pageResult.getPageSize());
    }
}
