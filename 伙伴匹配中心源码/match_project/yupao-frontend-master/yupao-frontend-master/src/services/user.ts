/**
 * 模块用途：封装用户相关的接口调用，并维护当前登录用户的前端缓存。
 *
 * 交互：页面进入时可调用获取当前用户；成功后更新用户状态，失败时返回 null 由页面决定提示/跳转。
 *
 * 数据来源：后端接口 GET /user/current（通过 myAxios 访问，依赖 Cookie Session 登录态）。
 */
import myAxios from "../plugins/myAxios";
import { setCurrentUserState } from "../states/user";
import type { UserType } from "../models/user";

/**
 * 获取当前登录用户。
 *
 * 交互：常在页面 onMounted / watchEffect 中触发；成功后写入前端用户缓存；失败返回 null。
 *
 * 数据来源：后端 GET /user/current（Cookie Session 维持登录态）。
 *
 * @returns 当前用户信息；未登录或请求失败返回 null
 */
export const getCurrentUser = async () => {
    // const currentUser = getCurrentUserState();
    // if (currentUser) {
    //     return currentUser;
    // }
    // 不存在则从远程获取
    const res = await myAxios.get('/user/current');
    if (res.code === 0) {
        setCurrentUserState(res.data);
        return res.data as UserType;
    }
    return null;
}
