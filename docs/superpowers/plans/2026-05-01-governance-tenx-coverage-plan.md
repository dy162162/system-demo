# Governance TenX Coverage Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build C+D+E+F governance capabilities for admin-backend and multi-tenant scenarios with a measurable coverage matrix (>=20/24 effective capabilities).

**Architecture:** Add four focused governance modules under `system-core` (`security`, `data`, `cache`, `api`) and wire them through `system-starter` auto-configuration. Keep current Java8/Spring Boot 2.7 multi-module layout unchanged and deliver `system-app` verification endpoints for A+E scenarios.

**Tech Stack:** Java 8, Spring Boot 2.7.18, Maven multi-module, Spring MVC, Spring AOP, Redis, Micrometer, JUnit 5.

---

## File Structure Mapping

- **Modify** `pom.xml` (parent module list)
- **Create** `system-core-security/pom.xml`
- **Create** `system-core-data/pom.xml`
- **Create** `system-core-cache/pom.xml`
- **Create** `system-core-api/pom.xml`
- **Create** `system-core-security/src/main/java/com/system/demo/core/security/...`
- **Create** `system-core-data/src/main/java/com/system/demo/core/data/...`
- **Create** `system-core-cache/src/main/java/com/system/demo/core/cachegovernance/...`
- **Create** `system-core-api/src/main/java/com/system/demo/core/apigovernance/...`
- **Modify** `system-starter/pom.xml`
- **Modify** `system-starter/src/main/java/com/system/demo/starter/config/SystemBaseAutoConfiguration.java`
- **Modify** `system-app/src/main/java/com/system/demo/app/controller/FrameworkDemoController.java`
- **Modify** `system-app/src/main/java/com/system/demo/app/service/FrameworkDemoService.java`
- **Create** `system-app/src/test/java/com/system/demo/app/...`
- **Create** `docs/superpowers/specs/coverage-matrix-governance.md` (matrix tracking doc)

### Task 1: Bootstrap Governance Modules

**Files:**
- Modify: `pom.xml`
- Create: `system-core-security/pom.xml`
- Create: `system-core-data/pom.xml`
- Create: `system-core-cache/pom.xml`
- Create: `system-core-api/pom.xml`
- Test: `mvn -q -DskipTests package`

- [ ] **Step 1: Write failing build expectation (module references missing)**

```bash
mvn -q -DskipTests package
```

Expected: FAIL after adding module names in parent `pom.xml` before child module poms exist.

- [ ] **Step 2: Update parent module list**

```xml
<modules>
    <module>system-bom</module>
    <module>system-common</module>
    <module>system-core</module>
    <module>system-core-security</module>
    <module>system-core-data</module>
    <module>system-core-cache</module>
    <module>system-core-api</module>
    <module>system-starter</module>
    <module>system-app</module>
</modules>
```

- [ ] **Step 3: Create minimal child poms**

```xml
<project ...>
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>com.system.demo</groupId>
    <artifactId>system-parent</artifactId>
    <version>1.0.0-SNAPSHOT</version>
  </parent>
  <artifactId>system-core-security</artifactId>
</project>
```

Repeat with matching `artifactId` for `data`, `cache`, `api`.

- [ ] **Step 4: Run build to verify pass**

Run: `mvn -q -DskipTests package`  
Expected: PASS, all modules are discovered and packaged.

- [ ] **Step 5: Commit**

```bash
git add pom.xml system-core-security/pom.xml system-core-data/pom.xml system-core-cache/pom.xml system-core-api/pom.xml
git commit -m "build: add governance core modules to maven reactor"
```

### Task 2: Security Governance Minimum Vertical Slice

**Files:**
- Create: `system-core-security/src/main/java/com/system/demo/core/security/context/TenantContext.java`
- Create: `system-core-security/src/main/java/com/system/demo/core/security/context/TenantContextHolder.java`
- Create: `system-core-security/src/main/java/com/system/demo/core/security/resolver/TenantResolver.java`
- Create: `system-core-security/src/main/java/com/system/demo/core/security/interceptor/TenantContextInterceptor.java`
- Create: `system-core-security/src/main/java/com/system/demo/core/security/config/SecurityGovernanceAutoConfiguration.java`
- Create: `system-core-security/src/test/java/com/system/demo/core/security/resolver/TenantResolverTest.java`
- Test: `mvn -q -pl system-core-security test`

- [ ] **Step 1: Write failing tenant resolver test**

```java
@Test
void should_resolve_tenant_from_header() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("X-Tenant-Id", "tenant-a");
    TenantContext context = resolver.resolve(request);
    assertEquals("tenant-a", context.getTenantId());
}
```

- [ ] **Step 2: Run test to verify fail**

Run: `mvn -q -pl system-core-security test -Dtest=TenantResolverTest`  
Expected: FAIL with class or method missing.

- [ ] **Step 3: Implement minimal resolver + context holder**

```java
public class TenantResolver {
    public TenantContext resolve(HttpServletRequest request) {
        String tenantId = request.getHeader("X-Tenant-Id");
        if (tenantId == null || tenantId.trim().isEmpty()) {
            throw new BizException("TENANT_CONTEXT_MISSING", "Tenant header is required");
        }
        return new TenantContext(tenantId, request.getHeader("X-Env"), request.getHeader("X-User-Id"));
    }
}
```

- [ ] **Step 4: Run module tests**

Run: `mvn -q -pl system-core-security test`  
Expected: PASS for resolver tests.

- [ ] **Step 5: Commit**

```bash
git add system-core-security/src/main/java system-core-security/src/test/java
git commit -m "feat(security): add tenant context resolver baseline"
```

### Task 3: Data Governance Scope Injection Baseline

**Files:**
- Create: `system-core-data/src/main/java/com/system/demo/core/data/scope/DataScopeRule.java`
- Create: `system-core-data/src/main/java/com/system/demo/core/data/scope/DataScopeInjector.java`
- Create: `system-core-data/src/main/java/com/system/demo/core/data/config/DataGovernanceAutoConfiguration.java`
- Create: `system-core-data/src/test/java/com/system/demo/core/data/scope/DataScopeInjectorTest.java`
- Test: `mvn -q -pl system-core-data test`

- [ ] **Step 1: Write failing data-scope test**

```java
@Test
void should_append_tenant_condition() {
    QuerySpec query = QuerySpec.of("select * from t_user where status=1");
    QuerySpec scoped = injector.applyTenantScope(query, "tenant-a");
    assertTrue(scoped.getSql().contains("tenant_id = ?"));
}
```

- [ ] **Step 2: Run test to confirm fail**

Run: `mvn -q -pl system-core-data test -Dtest=DataScopeInjectorTest`  
Expected: FAIL with missing injector implementation.

- [ ] **Step 3: Implement minimal scope injector**

```java
public QuerySpec applyTenantScope(QuerySpec query, String tenantId) {
    if (tenantId == null) {
        throw new BizException("TENANT_CONTEXT_MISSING", "tenant is required");
    }
    return query.appendCondition("tenant_id = ?", tenantId);
}
```

- [ ] **Step 4: Run module tests**

Run: `mvn -q -pl system-core-data test`  
Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add system-core-data/src/main/java system-core-data/src/test/java
git commit -m "feat(data): add tenant data-scope injection baseline"
```

### Task 4: CacheGovernanceFacade and Isolation Key Policy

**Files:**
- Create: `system-core-cache/src/main/java/com/system/demo/core/cachegovernance/CacheGovernanceFacade.java`
- Create: `system-core-cache/src/main/java/com/system/demo/core/cachegovernance/model/CacheRequest.java`
- Create: `system-core-cache/src/main/java/com/system/demo/core/cachegovernance/policy/CacheKeyPolicy.java`
- Create: `system-core-cache/src/main/java/com/system/demo/core/cachegovernance/impl/RedisCacheGovernanceFacade.java`
- Modify: `system-core/src/main/java/com/system/demo/core/cache/CacheService.java` (bridge compatibility only)
- Create: `system-core-cache/src/test/java/com/system/demo/core/cachegovernance/CacheGovernanceFacadeTest.java`
- Test: `mvn -q -pl system-core-cache test`

- [ ] **Step 1: Write failing key isolation test**

```java
@Test
void should_build_tenant_isolated_key() {
    String key = keyPolicy.build("USER_PROFILE", "u-1", "tenant-a", "prod");
    assertEquals("prod:tenant-a:USER_PROFILE:u-1", key);
}
```

- [ ] **Step 2: Run test to verify fail**

Run: `mvn -q -pl system-core-cache test -Dtest=CacheGovernanceFacadeTest`  
Expected: FAIL due missing key policy/facade.

- [ ] **Step 3: Implement minimal facade getOrLoad**

```java
public <T> Optional<T> getOrLoad(CacheRequest request, Supplier<T> loader) {
    String key = keyPolicy.build(request.getNamespace(), request.getBizKey(), request.getTenantId(), request.getEnv());
    T cached = cacheService.get(key, request.getType());
    if (cached != null) {
        return Optional.of(cached);
    }
    T loaded = loader.get();
    if (loaded != null || request.isAllowNullValue()) {
        cacheService.put(key, loaded, request.getTtl());
    }
    return Optional.ofNullable(loaded);
}
```

- [ ] **Step 4: Run module tests**

Run: `mvn -q -pl system-core-cache test`  
Expected: PASS, includes key policy test + getOrLoad behavior.

- [ ] **Step 5: Commit**

```bash
git add system-core-cache/src/main/java system-core-cache/src/test/java system-core/src/main/java/com/system/demo/core/cache/CacheService.java
git commit -m "feat(cache): add governance facade and tenant-isolated key policy"
```

### Task 5: API Governance (Error Code Registry + Idempotency)

**Files:**
- Create: `system-core-api/src/main/java/com/system/demo/core/apigovernance/error/ErrorCodeRegistry.java`
- Create: `system-core-api/src/main/java/com/system/demo/core/apigovernance/idempotency/IdempotencyService.java`
- Create: `system-core-api/src/main/java/com/system/demo/core/apigovernance/filter/ApiGovernanceFilter.java`
- Create: `system-core-api/src/main/java/com/system/demo/core/apigovernance/config/ApiGovernanceAutoConfiguration.java`
- Modify: `system-core/src/main/java/com/system/demo/core/web/advice/GlobalExceptionHandler.java`
- Create: `system-core-api/src/test/java/com/system/demo/core/apigovernance/idempotency/IdempotencyServiceTest.java`
- Test: `mvn -q -pl system-core-api test`

- [ ] **Step 1: Write failing idempotency test**

```java
@Test
void should_reject_duplicate_request_key_in_ttl_window() {
    boolean first = idempotencyService.acquire("tenant-a", "req-1", Duration.ofSeconds(30));
    boolean second = idempotencyService.acquire("tenant-a", "req-1", Duration.ofSeconds(30));
    assertTrue(first);
    assertFalse(second);
}
```

- [ ] **Step 2: Run test to verify fail**

Run: `mvn -q -pl system-core-api test -Dtest=IdempotencyServiceTest`  
Expected: FAIL with missing implementation.

- [ ] **Step 3: Implement minimal idempotency + error mapping**

```java
public boolean acquire(String tenantId, String requestKey, Duration ttl) {
    String key = "idem:" + tenantId + ":" + requestKey;
    Boolean success = redisTemplate.opsForValue().setIfAbsent(key, "1", ttl);
    return Boolean.TRUE.equals(success);
}
```

Update exception handler mapping:

```java
@ExceptionHandler(IdempotencyConflictException.class)
public ApiResponse<Void> handleIdempotency(IdempotencyConflictException ex) {
    return Results.fail("IDEMPOTENT_CONFLICT", ex.getMessage());
}
```

- [ ] **Step 4: Run module tests**

Run: `mvn -q -pl system-core-api test`  
Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add system-core-api/src/main/java system-core-api/src/test/java system-core/src/main/java/com/system/demo/core/web/advice/GlobalExceptionHandler.java
git commit -m "feat(api): add idempotency service and governance error mapping"
```

### Task 6: Starter Wiring and Feature Flags

**Files:**
- Modify: `system-starter/pom.xml`
- Modify: `system-starter/src/main/java/com/system/demo/starter/config/SystemBaseAutoConfiguration.java`
- Create: `system-starter/src/main/java/com/system/demo/starter/config/properties/GovernanceProperties.java`
- Create: `system-starter/src/test/java/com/system/demo/starter/config/SystemBaseAutoConfigurationTest.java`
- Test: `mvn -q -pl system-starter test`

- [ ] **Step 1: Write failing auto-config enable/disable test**

```java
@Test
void should_disable_cache_governance_when_flag_false() {
    contextRunner
      .withPropertyValues("system.governance.cache.enabled=false")
      .run(context -> assertThat(context).doesNotHaveBean(CacheGovernanceFacade.class));
}
```

- [ ] **Step 2: Run test to verify fail**

Run: `mvn -q -pl system-starter test -Dtest=SystemBaseAutoConfigurationTest`  
Expected: FAIL before new properties and conditions exist.

- [ ] **Step 3: Implement starter imports and conditional config**

```java
@Import({
    WebMvcCoreConfig.class,
    DataSourceCoreAutoConfiguration.class,
    RedisCoreAutoConfiguration.class,
    OpenApiAutoConfiguration.class,
    ObservabilityAutoConfiguration.class,
    SecurityGovernanceAutoConfiguration.class,
    DataGovernanceAutoConfiguration.class,
    CacheGovernanceAutoConfiguration.class,
    ApiGovernanceAutoConfiguration.class
})
public class SystemBaseAutoConfiguration {}
```

- [ ] **Step 4: Run module tests**

Run: `mvn -q -pl system-starter test`  
Expected: PASS with flag behavior validated.

- [ ] **Step 5: Commit**

```bash
git add system-starter/pom.xml system-starter/src/main/java system-starter/src/test/java
git commit -m "feat(starter): wire governance modules with feature flags"
```

### Task 7: System-App Verification Endpoints and Coverage Matrix

**Files:**
- Modify: `system-app/src/main/java/com/system/demo/app/controller/FrameworkDemoController.java`
- Modify: `system-app/src/main/java/com/system/demo/app/service/FrameworkDemoService.java`
- Create: `system-app/src/test/java/com/system/demo/app/controller/GovernanceDemoControllerTest.java`
- Create: `docs/superpowers/specs/coverage-matrix-governance.md`
- Modify: `README.md`
- Test: `mvn -q -pl system-app test`

- [ ] **Step 1: Write failing endpoint tests for A+E scenarios**

```java
@Test
void should_reject_when_tenant_header_missing() throws Exception {
    mockMvc.perform(get("/api/framework/governance/cache-profile").param("userId", "u-1"))
           .andExpect(status().is4xxClientError());
}
```

- [ ] **Step 2: Run tests to verify fail**

Run: `mvn -q -pl system-app test -Dtest=GovernanceDemoControllerTest`  
Expected: FAIL because endpoints do not exist.

- [ ] **Step 3: Implement demo endpoints + matrix document**

```java
@GetMapping("/governance/cache-profile")
public ApiResponse<Object> cacheProfile(@RequestParam String userId) {
    return Results.success(frameworkDemoService.cacheProfile(userId));
}
```

Coverage matrix doc must include all 24 capability rows and status columns:
- `Capability`
- `Module`
- `Config Flag`
- `Demo Endpoint/Test`
- `Doc Link`
- `Status`

- [ ] **Step 4: Run app tests and smoke verification**

Run: `mvn -q -pl system-app test`  
Expected: PASS.  
Run: `mvn -q -pl system-app spring-boot:run` then verify `/api/framework/governance/*` endpoints manually.

- [ ] **Step 5: Commit**

```bash
git add system-app/src/main/java system-app/src/test/java docs/superpowers/specs/coverage-matrix-governance.md README.md
git commit -m "feat(app): add governance verification endpoints and coverage matrix"
```

### Task 8: Full Verification and Delivery Gate

**Files:**
- Modify: `DELIVERY_CHECKLIST.md`
- Create: `docs/superpowers/specs/2026-05-01-governance-acceptance-report.md`
- Test: root verification commands

- [ ] **Step 1: Write failing acceptance check script (missing artifacts)**

```bash
test -f docs/superpowers/specs/coverage-matrix-governance.md
test -f docs/superpowers/specs/2026-05-01-governance-acceptance-report.md
```

Expected: FAIL before acceptance report is created.

- [ ] **Step 2: Execute full verification suite**

Run:

```bash
mvn -q clean test
mvn -q -DskipTests package
```

Expected: PASS for all modules.

- [ ] **Step 3: Write acceptance report with 20/24 threshold check**

```markdown
## Result
- Completed capabilities: 21/24
- A scenario E2E: PASS
- E scenario E2E: PASS
- Gate decision: PASS
```

- [ ] **Step 4: Re-run final smoke checks**

Run:

```bash
mvn -q -pl system-app spring-boot:run
```

Manual checks:
- `/actuator/health`
- `/api/framework/governance/cache-profile`
- `/api/framework/governance/idempotency/verify`

Expected: All return successful/expected responses.

- [ ] **Step 5: Commit**

```bash
git add DELIVERY_CHECKLIST.md docs/superpowers/specs/2026-05-01-governance-acceptance-report.md
git commit -m "docs: finalize governance acceptance report and delivery gate"
```

## Self-Review

- **Spec coverage:** Covers module split, C/D/E/F capability implementation, starter wiring, app-level validation, and measurable matrix gate.
- **Placeholder scan:** No `TODO`/`TBD` placeholders used in task steps.
- **Type consistency:** Uses consistent names (`TenantContext`, `CacheGovernanceFacade`, `IdempotencyService`, coverage matrix 24-row model) across tasks.

