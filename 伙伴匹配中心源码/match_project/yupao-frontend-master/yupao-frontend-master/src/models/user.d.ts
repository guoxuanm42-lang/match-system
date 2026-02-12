/**
 * 模块用途：定义用户相关的前端类型（用于页面渲染、组件 props 与接口返回值约束）。
 *
 * 交互：页面/组件使用 UserType 展示用户卡片与个人信息；不会直接触发 UI 行为。
 *
 * 数据来源：字段来自后端 User 实体及其接口返回结构（例如 GET /user/recommend、GET /user/current）。
 */
export type UserType = {
    id: number;
    username: string;
    userAccount: string;
    avatarUrl?: string;
    profile?: string;
    gender:number;
    phone: string;
    email: string;
    userStatus: number;
    userRole: number;
    planetCode: string;
    tags: string;
    createTime: Date;
};
