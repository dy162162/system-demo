package com.system.demo.core.security.resolver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.system.demo.core.web.exception.BizException;
import com.system.demo.core.web.response.ResultCode;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class TenantResolverTest {

    private final TenantResolver resolver = new TenantResolver();

    @Test
    void should_resolve_tenant_from_header() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(TenantResolver.HEADER_TENANT_ID, "tenant-a");
        assertEquals("tenant-a", resolver.resolve(request).getTenantId());
    }

    @Test
    void should_throw_when_tenant_missing() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        BizException ex = assertThrows(BizException.class, () -> resolver.resolve(request));
        assertEquals(ResultCode.BAD_REQUEST.code(), ex.getCode());
    }
}
