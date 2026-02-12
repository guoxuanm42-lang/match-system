/**
 * 模块用途：定义队伍相关的前端类型（用于队伍列表渲染、组件 props 与接口返回值约束）。
 *
 * 交互：页面/组件使用 TeamType 展示队伍卡片、创建/更新表单；不会直接触发 UI 行为。
 *
 * 数据来源：字段来自后端 Team 相关接口返回（例如 GET /team/list、GET /team/get）。
 */
import {UserType} from "./user";

export type TeamType = {
    id: number;
    userId?: number;
    name: string;
    description: string;
    expireTime?: Date;
    maxNum: number;
    password?: string,
    // todo 定义枚举值类型，更规范
    status: number;
    createTime: Date;
    updateTime: Date;
    createUser?: UserType;
    hasJoinNum?: number;
    hasJoin?: boolean;
};
