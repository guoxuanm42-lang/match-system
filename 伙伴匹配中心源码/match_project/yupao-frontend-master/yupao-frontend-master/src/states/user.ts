/**
 * 模块用途：提供简单的“当前用户”前端内存态缓存。
 *
 * 交互：登录态相关页面会在进入时写入 currentUser；其他页面读取用于展示/权限判断。
 *
 * 数据来源：来自 services/user.ts 的 getCurrentUser 调用结果，或页面主动更新。
 */
import {UserType} from "../models/user";

let currentUser: UserType;

/**
 * 写入当前用户缓存。
 *
 * 交互：通常在成功获取当前用户后触发；不会产生 UI 行为，由调用方决定 Toast/跳转等反馈。
 *
 * 数据来源：参数 user 来自后端接口（例如 GET /user/current）或页面更新表单后的回写。
 *
 * @param user 当前用户信息
 */
const setCurrentUserState = (user: UserType) => {
    currentUser = user;
}

/**
 * 读取当前用户缓存。
 *
 * 交互：页面渲染/权限判断时调用；不直接触发 UI 行为。
 *
 * 数据来源：从内存变量 currentUser 读取，需先由 setCurrentUserState 写入。
 *
 * @returns 当前用户信息
 */
const getCurrentUserState = () : UserType => {
    return currentUser;
}

export {
    setCurrentUserState,
    getCurrentUserState,
}
