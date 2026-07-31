# RBAC 企业级权限管理系统

> 🔗 **在线演示（静态版 · Mock 数据 · 无需后端）：https://yhyhukn.github.io/RBAC-/**

基于 **Spring Boot 3 + Vue 3** 的全栈 RBAC（Role-Based Access Control，角色权限控制）中后台管理系统演示项目。前端采用**赛博朋克科幻主题**（暗紫 + 品红霓虹 / 玻璃拟态 / 动态网格背景）。

## 在线演示

前端通过 GitHub Pages 自动部署（`.github/workflows/deploy.yml`），以 `VITE_MOCK=true` 离线 Mock 模式构建为纯静态站点，**无需启动后端即可完整体验**全部界面（仪表盘、用户/角色/菜单/部门/字典/文件/日志、系统监控、定时任务、公告管理、个人中心）。

🔗 **https://yhyhukn.github.io/RBAC-/**

演示账号见下文「演示账号」一节。

## 技术栈

### 后端（Spring Boot 3.3.5 / Java 21）
- Spring Data JPA (Hibernate) + H2（开发内存库）/ MySQL（生产）
- Spring Security + JWT 鉴权
- Quartz 定时任务调度
- Maven 构建

### 前端（Vue 3.4 / Vite 5）
- Element Plus 2.7 + Pinia 2 状态管理
- ECharts 5 数据可视化
- Axios 请求封装（支持 Mock 离线模式）

## 目录结构

```
rbac-admin/
├── backend/            # Spring Boot 后端
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/rbac/admin/   # 控制器 / 服务 / 实体 / 配置
│       └── resources/application.yml
└── frontend/          # Vue3 前端
    ├── src/
    │   ├── api/        # 接口封装
    │   ├── mock/       # 离线 Mock 数据层
    │   ├── layout/     # 全局布局外壳
    │   ├── views/      # 业务页面（仪表盘/系统/日志/监控/任务/公告...）
    │   ├── styles/main.css  # 赛博朋克设计系统
    │   └── router/     # 路由
    └── vite.config.js
```

## 功能模块

| 模块 | 说明 |
| --- | --- |
| 仪表盘 | 关键指标统计 + 访问趋势 / 角色分布可视化 |
| 用户管理 | 用户增删改查、启用禁用 |
| 角色管理 | 角色维护、菜单权限分配 |
| 菜单管理 | 系统菜单树 |
| 部门管理 | 部门结构维护 |
| 数据字典 | 字典类型与字典数据 |
| 文件管理 | 文件上传 / 列表 / 删除 |
| 操作日志 | 操作审计记录 |
| 系统监控 | CPU / 内存 / 磁盘 / JVM 实时指标 |
| 定时任务 | Quartz 任务增删改查、暂停 / 恢复 / 立即执行 |
| 公告管理 | 公告发布 / 撤回 / 编辑 |
| 个人中心 | 当前用户信息 |

## 快速开始

### 1. 后端

```bash
cd backend
mvn spring-boot:run
# 默认端口 8080，开发环境使用 H2 内存库，启动即自动建表并写入演示数据
```

> 接口文档：健康检查 `GET http://localhost:8080/actuator/health`（如已开启）。

### 2. 前端

```bash
cd frontend
npm install
npm run dev          # 开发模式，默认 http://localhost:5173
# 或
npm run build        # 生产构建，输出 dist/
```

前端默认开启 **Mock 离线模式**（`frontend/.env.production` 中 `VITE_MOCK=true`），可脱离后端独立运行演示。生产对接真实后端时关闭该开关并配置 `vite.config.js` 中的 `/api` 代理即可。

## 演示账号

| 用户名 | 密码 | 角色 |
| --- | --- | --- |
| `admin` | `123456` | 管理员（全部权限） |
| `user` | `123456` | 普通用户 |

> 在登录页也可通过右上角下拉「切换角色」快速体验两种视角。

## 说明

- `backend/src/main/resources/application.yml` 中的 `jwt.secret` 为**演示占位值**，生产部署请替换为高强度密钥。
- 本项目定位为全栈 RBAC 演示，含少量前后端契约待对齐项与零自动化测试，欢迎 Issue / PR。
