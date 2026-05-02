package com.system.demo.core.security.resolver;

import com.system.demo.core.security.context.TenantContext;
import com.system.demo.core.web.exception.BizException;
import com.system.demo.core.web.response.ResultCode;
import javax.servlet.http.HttpServletRequest;

public class TenantResolver {

    public static final String HEADER_TENANT_ID = "X-Tenant-Id";
    public static final String HEADER_ENV = "X-Env";
    public static final String HEADER_USER_ID = "X-User-Id";

    public TenantContext resolve(HttpServletRequest request) {
        String tenantId = request.getHeader(HEADER_TENANT_ID);
        if (tenantId == null || tenantId.trim().isEmpty()) {
            throw new BizException(ResultCode.BAD_REQUEST.code(), "Tenant header is required");
        }
        return new TenantContext(
                tenantId.trim(),
                trimToNull(request.getHeader(HEADER_ENV)),
                trimToNull(request.getHeader(HEADER_USER_ID)));
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
