# 接口文档（v1）

## 基本信息

- Base URL：`http://localhost:8080/api`
- 认证方式：Session + Cookie
- 返回结构：`BaseResponse<T>`

```json
{
  "code": 0,
  "data": {},
  "message": "ok",
  "description": ""
}
```

## 错误码

- `0`：成功
- `40000`：请求参数错误
- `40001`：请求数据为空
- `40100`：未登录
- `40101`：无权限
- `40301`：禁止操作
- `50000`：系统内部异常

## 用户模块

### 注册

- `POST /user/register`

请求体：

```json
{
  "userAccount": "string",
  "userPassword": "string",
  "checkPassword": "string",
  "planetCode": "string"
}
```

返回：`data` 为注册用户 ID。

### 登录

- `POST /user/login`

请求体：

```json
{
  "userAccount": "string",
  "userPassword": "string"
}
```

返回：`data` 为用户信息（含登录态）。

### 退出登录

- `POST /user/logout`

返回：`data` 为整型结果。

### 当前登录用户

- `GET /user/current`

返回：`data` 为当前用户信息。

### 搜索用户（管理员）

- `GET /user/search?username=xxx`

返回：`data` 为用户列表。

### 按标签搜索用户

- `GET /user/search/tags?tagNameList=Java&tagNameList=Spring`

返回：`data` 为用户列表。

### 推荐用户

- `GET /user/recommend?pageSize=10&pageNum=1`

返回：`data` 为分页结果。

### 更新用户

- `POST /user/update`

请求体：`User` 对象（部分字段可选）。

### 删除用户（管理员）

- `POST /user/delete`

请求体：`long` 类型用户 ID。

### 匹配用户

- `GET /user/match?num=10`

返回：`data` 为匹配到的用户列表。

## 队伍模块

### 创建队伍

- `POST /team/add`

请求体：

```json
{
  "name": "string",
  "description": "string",
  "maxNum": 3,
  "expireTime": "2026-02-06T14:00:00.000Z",
  "status": 0,
  "password": "string"
}
```

返回：`data` 为队伍 ID。

### 更新队伍

- `POST /team/update`

请求体：

```json
{
  "id": 1,
  "name": "string",
  "description": "string",
  "expireTime": "2026-02-06T14:00:00.000Z",
  "status": 0,
  "password": "string"
}
```

返回：`data` 为布尔值。

### 队伍详情

- `GET /team/get?id=1`

返回：`data` 为队伍信息。

### 队伍列表（带筛选）

- `GET /team/list`

可选参数（`TeamQuery` + `PageRequest`）：

- `id` / `idList`
- `searchText` / `name` / `description`
- `maxNum`
- `userId`
- `status`（0 公开 / 1 私有 / 2 加密）
- `pageSize` / `pageNum`

返回：`data` 为 `TeamUserVO` 列表。

### 队伍分页列表

- `GET /team/list/page`

参数同 `TeamQuery` + `PageRequest`。

### 加入队伍

- `POST /team/join`

请求体：

```json
{
  "teamId": 1,
  "password": "string"
}
```

返回：`data` 为布尔值。

### 退出队伍

- `POST /team/quit`

请求体：

```json
{
  "teamId": 1
}
```

返回：`data` 为布尔值。

### 删除队伍

- `POST /team/delete`

请求体：

```json
{
  "id": 1
}
```

返回：`data` 为布尔值。

### 我创建的队伍

- `GET /team/list/my/create`

参数同 `TeamQuery`。

### 我加入的队伍

- `GET /team/list/my/join`

参数同 `TeamQuery`。

