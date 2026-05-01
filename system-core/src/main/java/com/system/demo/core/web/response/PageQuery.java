package com.system.demo.core.web.response;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class PageQuery {
    @Min(value = 1, message = "pageNum must be >= 1")
    private Long pageNum = 1L;
    @Min(value = 1, message = "pageSize must be >= 1")
    @Max(value = 500, message = "pageSize must be <= 500")
    private Long pageSize = 10L;

    public long safePageNum() {
        return pageNum == null || pageNum < 1 ? 1L : pageNum;
    }

    public long safePageSize() {
        if (pageSize == null || pageSize < 1) {
            return 10L;
        }
        return Math.min(pageSize, 500L);
    }

    public Long getPageNum() {
        return pageNum;
    }

    public void setPageNum(Long pageNum) {
        this.pageNum = pageNum;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }
}
