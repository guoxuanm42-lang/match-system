# 伙伴匹配中心（match_project）

一个“前后端分离”的伙伴匹配与组队项目：后端提供用户、队伍、匹配与推荐接口；前端提供匹配首页、队伍大厅、个人中心等页面。

## 明确说明【学习/练手项目】

> 本项目为个人学习 / 练手项目。  
> 用于熟悉 Spring Boot + Vue 的完整开发流程。  
> 并非生产级系统。

## 功能清单 / Feature List

### 用户侧

- 用户注册 / 登录（Session 登录态）
- 获取当前登录用户信息
- 修改个人资料
- 退出登录

### 匹配与推荐

- 推荐用户（分页）
- 心动模式：标签匹配推荐
- 标签搜索（前端可选标签）

### 队伍侧

- 创建队伍 / 更新队伍 / 解散队伍
- 加入队伍 / 退出队伍
- 队伍列表（公开 / 加密）
- 我创建的队伍 / 我加入的队伍

### 其他

- 统一返回体（`BaseResponse`）
- 统一异常处理（`GlobalExceptionHandler`）
- Redis 缓存推荐列表
- Redisson 分布式锁（加入队伍场景）

## 业务逻辑说明（重点）

### 1）登录态机制（Session）

- 登录成功后，后端把脱敏用户写入 Session（`USER_LOGIN_STATE`）。
- 浏览器通过 Cookie（`JSESSIONID`）维持登录态。
- 未登录访问需要鉴权的接口会返回 `40100/40101`。

### 2）推荐缓存机制

- 接口：`GET /user/recommend`
- 缓存 key：`yupao:user:recommend:{userId}`
- 逻辑：命中 Redis 直接返回；未命中则查库并写缓存（30 秒过期）。

### 3）心动模式（标签匹配）

- 接口：`GET /user/match`
- 会读取当前用户 `tags` 与其他用户 `tags` 计算相似度（编辑距离）。
- 返回相似度最接近的用户列表。

### 4）队伍流程与校验

#### 创建队伍

- 必须登录
- 队伍人数 1~20、名称 <= 20、描述 <= 512
- 加密队伍必须设置密码（<= 32）
- 过期时间必须晚于当前时间
- 每个用户最多创建 5 个队伍
- 创建成功后自动把队长加入队伍关系表

#### 加入队伍

- 私有队伍禁止加入
- 加密队伍必须校验密码
- 用户最多加入/创建 5 个队伍
- 队伍人数不能超过上限
- 使用 Redisson 锁保证并发安全

#### 退出/解散队伍

- 队伍只剩一人时自动解散
- 队长退出会把队长转移给最早加入的成员

## 整体架构说明

- 前端（`yupao-frontend-master/yupao-frontend-master`）
  - Vue 3 + Vite + Vant
  - 通过 Axios 调用后端 `/api/**`
- 后端（`yupao-backend-master/yupao-backend-master`）
  - Spring Boot + MyBatis-Plus
  - Session 维持登录态（Redis 存储）
  - Redis 缓存推荐列表
- 数据库（MySQL）
  - 核心表：`user`、`team`、`user_team`
  - `tags` 目前存放在 `user.tags` 字段（JSON 字符串）

## 技术栈

- 后端：Spring Boot、MyBatis-Plus、MySQL、Redis、Redisson
- 前端：Vue 3、Vite、Vant、Axios

## 目录结构

- `yupao-backend-master/yupao-backend-master`：后端工程
- `yupao-frontend-master/yupao-frontend-master`：前端工程

## 运行环境

- JDK 21+
- Maven 3.8+
- Node.js 16+（建议 18）
- MySQL 8+
- Redis 6+

## 快速开始（Windows）

### 1）初始化数据库

1. 创建数据库：

   ```sql
   CREATE DATABASE IF NOT EXISTS friend_match DEFAULT CHARACTER SET utf8mb4;
   ```

2. 执行建表脚本：

   - `yupao-backend-master/yupao-backend-master/sql/create_table.sql`

3. （可选）导入示例数据：

   - `yupao-backend-master/yupao-backend-master/sql/seed_data.sql`

### 2）配置后端环境变量

后端默认读取以下变量（见 `application.yml`）：

- `DB_HOST`（默认 `localhost`）
- `DB_PORT`（默认 `3306`）
- `DB_NAME`（默认 `friend_match`）
- `DB_USER`（默认 `root`）
- `DB_PASSWORD`（默认 `root1234!`）
- `REDIS_HOST`（默认 `localhost`）
- `REDIS_PORT`（默认 `6379`）
- `REDIS_DB`（默认 `1`）

### 3）启动后端

```powershell
cd .\yupao-backend-master\yupao-backend-master
mvn -DskipTests spring-boot:run
```

后端地址：`http://localhost:8080/api`

### 4）启动前端

```powershell
cd .\yupao-frontend-master\yupao-frontend-master
npm install
npm run dev
```

前端地址（端口可能自动递增）：

- `http://localhost:3000/`
- `http://localhost:3001/`

## 配置说明

- 后端配置：`yupao-backend-master/yupao-backend-master/src/main/resources/application.yml`
- 前端请求基地址：`yupao-frontend-master/yupao-frontend-master/src/plugins/myAxios.ts`

## 接口文档

详见 `API.md`。

## 常见问题

- **提交无反应/报错**：确保已登录，Redis 正常运行，后端接口可用。
- **跨域错误**：后端 `WebMvcConfg` 已允许 `localhost:3000/3001`，如改端口需同步更新。

