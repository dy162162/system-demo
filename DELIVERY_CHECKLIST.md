# 分阶段实施与验收清单

## M1 骨架可运行
- [x] 完成多模块结构：parent/common/core/starter/app
- [x] 应用可启动（`system-app`）
- [x] 提供统一返回协议、异常处理、基础日志 traceId

## M2 数据能力
- [x] 多数据源配置契约与上下文切换能力
- [x] 多 Redis 路由与统一缓存门面
- [x] 提供多数据源/多 Redis 验证接口

## M3 治理能力
- [x] 集成 Actuator 健康检查
- [x] 集成 Micrometer + Prometheus 指标端点
- [x] 增加框架级健康项（数据源配置、Redis 模板可用性）
- [x] 统一核心线程池配置

## M4 交付能力
- [x] `system-starter` 聚合自动装配
- [x] 提供 README（启动、配置、验证说明）
- [ ] CI 模板（按企业环境选 GitHub Actions / Jenkins）
- [ ] Docker/K8s 模板（按目标环境补充）
