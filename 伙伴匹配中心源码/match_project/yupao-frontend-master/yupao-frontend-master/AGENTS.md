# Frontend Code Documentation Specification

本项目所有前端代码（Vue 3 + Vite + TypeScript + Vant + Axios）必须严格遵循以下注释规范。

## 一、文件头注释规范

以下文件必须包含标准 JSDoc/TSDoc 风格“文件头注释”，用于说明该模块核心职责：

- `src/pages/**`：页面组件
- `src/components/**`：复用组件
- `src/services/**`：接口请求封装（调用 `myAxios`）
- `src/plugins/**`：插件封装（例如 `myAxios`）
- `src/states/**`：状态管理（例如当前用户缓存）
- `src/config/**`、`src/constants/**`：配置与常量
- `src/models/**`：类型定义（`.d.ts` / `.ts`）

推荐格式：

```ts
/**
 * 模块用途：简要描述该文件负责的业务能力。
 *
 * 交互：说明用户或上层如何触发该模块能力，以及 UI/路由层面的反馈（Toast、跳转、弹窗等）。
 *
 * 数据来源：说明数据从哪里来（后端接口、route、props、state、本地存储、常量等）。
 */
```

规则：

1. 必须使用 JSDoc/TSDoc 风格（/** */）
2. 必须描述模块主要职责
3. 必须包含「用途 / 交互 / 数据来源」三要素
4. 禁止生成无意义描述（例如“定义变量”“发请求”）

## 二、方法注释规范

以下函数/方法必须添加 JSDoc/TSDoc 注释：

- 所有 `export` 导出的函数
- 页面或组件关键交互入口函数（例如 `onSubmit`、`onSearch`、`toRegister` 等）
- 会产生副作用的函数（发起请求、写入 state、路由跳转、写入本地存储等）
- 组件对外暴露能力（props/emits/expose 相关的方法或适配函数）

推荐格式：

```ts
/**
 * 方法用途：说明该方法解决什么问题。
 *
 * 交互：说明触发方式（按钮点击/表单提交/页面进入等）与 UI 行为（Toast、跳转、loading 等）。
 *
 * 数据来源：说明参数来源（表单/route/state/props）以及会调用的后端接口（如有：METHOD /path）。
 *
 * @param paramName 参数说明（必须逐一写全）
 * @returns 返回值说明（有返回值必须写，Promise 也必须写）
 * @throws 异常说明（可能抛错/Promise reject 时必写；不抛错可省略）
 */
```

规则：

1. 必须说明方法功能（用途/交互/数据来源）
2. 必须为所有参数添加 `@param`，且逐一对应函数签名参数
3. 有返回值必须添加 `@returns`（返回 `Promise` 也必须说明 resolve 值含义）
4. 抛出异常或会向上抛错必须添加 `@throws`；不抛错可省略
5. 禁止生成无意义描述

## 三、页面组件额外要求（src/pages）

页面组件的文件头注释必须额外说明：

- 页面用途（该页面提供什么能力）
- 关键交互（表单提交、跳转、筛选/搜索等）
- 数据来源（明确后端接口路径、route 参数、state 等）

## 四、Service 层额外要求（src/services）

每个导出函数必须明确写清：

- 后端接口：HTTP 方法 + 路径（例如 `GET /user/current`、`POST /user/login`）
- 登录态/权限：是否依赖 Cookie Session（本项目 `myAxios.defaults.withCredentials = true`）
- 返回结构：返回是否为统一结构（例如 `code/data/description`）
- 错误策略：失败时是兜底返回（例如 `null/false`）还是继续抛出异常

## 五、示例（结合本项目）

### 5.1 Service 示例：获取当前用户

```ts
/**
 * 获取当前登录用户。
 *
 * 交互：通常在页面进入时触发；成功后更新用户状态；未登录时可能触发跳转登录页。
 *
 * 数据来源：后端 GET /user/current（通过 myAxios 访问）。
 *
 * @returns 当前用户信息；失败返回 null
 */
export const getCurrentUser = async () => {
  // ...
};
```

### 5.2 页面示例：登录提交

```ts
/**
 * 提交登录表单。
 *
 * 交互：用户点击提交触发；成功 Toast 并跳转；失败 Toast 提示。
 *
 * 数据来源：表单输入；后端 POST /user/login；route.query.redirect。
 *
 * @returns Promise<void>
 */
const onSubmit = async () => {
  // ...
};
```
