# Java SpringBoot 通用基础框架

## 技术基线

- Java: 1.8
- Spring Boot: 2.7.18
- Maven Multi-Module

## 模块说明

- `system-parent`: 父工程与依赖管理
- `system-bom`: 版本统一管理
- `system-common`: 通用基础层
- `system-core`: core-web/core-datasource/core-redis/core-cache/observability/doc
- `system-starter`: 自动装配入口
- `system-app`: 启动模块和框架验证接口

## 已实现能力

- 统一返回协议：`ApiResponse`、`PageResponse`、`Results`
- 全局异常处理：`GlobalExceptionHandler`
- TraceId 拦截器：`TraceIdInterceptor`
- 多数据源契约：`DataSourceRoutingProperties`、`DataSourceContextHolder`
- 多 Redis 路由：`RedisTemplateRouter`
- 缓存门面：`CacheService` + `RedisCacheService`
- 可观测性基线：
  - `Actuator` 健康检查
  - `Prometheus` 指标端点
  - `application` 维度公共指标标签
  - 自定义框架健康项 `FrameworkHealthIndicator`
  - 统一核心线程池 `coreExecutor`
- OpenAPI 文档自动配置

## 启动与验证

1. 构建项目
  - `mvn -DskipTests package`
2. 启动应用
  - `mvn -pl system-app spring-boot:run`
3. 验证接口
  - 健康检查：`/actuator/health`
  - 指标：`/actuator/prometheus`
  - 文档：`/swagger-ui.html`
  - 成功响应：`/api/framework/success`
  - 失败响应：`/api/framework/failure`
  - 分页响应：`/api/framework/page?pageNum=1&pageSize=10`
  - 多数据源验证：`/api/framework/datasource/verify?key=master`
  - 多 Redis 验证：`/api/framework/redis/verify`

## 里程碑映射

- M1 骨架可运行：已完成
- M2 数据能力：已完成基础契约与验证接口
- M3 治理能力：已完成可观测性与线程池基线
- M4 交付能力：已完成 starter 聚合、README 文档；CI 与容器模板待按部署环境补充