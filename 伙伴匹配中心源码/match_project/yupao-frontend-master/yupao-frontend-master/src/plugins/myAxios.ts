/**
 * 模块用途：封装项目统一的 Axios 实例，用于前端调用后端 /api 接口。
 *
 * 交互：页面/组件通过 myAxios 发起请求；当后端返回未登录/无权限时自动跳转登录页并携带 redirect。
 *
 * 数据来源：运行环境 import.meta.env.DEV 决定 baseURL；响应数据遵循后端统一返回结构（code/data/description）。
 */
import axios, { AxiosInstance } from "axios";

export type ApiResponse<T = any> = {
  code: number;
  data: T;
  description?: string;
};

type MyAxiosInstance = Omit<AxiosInstance, 'get' | 'post'> & {
  get<T = any>(url: string, config?: any): Promise<ApiResponse<T>>;
  post<T = any>(url: string, data?: any, config?: any): Promise<ApiResponse<T>>;
};

const isDev = import.meta.env.DEV;

const myAxios = axios.create({
    baseURL: isDev ? 'http://localhost:8080/api' : '线上地址',
}) as MyAxiosInstance;

myAxios.defaults.withCredentials = true; // 配置为true

// Add a request interceptor
myAxios.interceptors.request.use(function (config) {
    console.log('我要发请求啦', config)
    // Do something before request is sent
    return config;
}, function (error) {
    // Do something with request error
    return Promise.reject(error);
});

// Add a response interceptor
myAxios.interceptors.response.use(function (response) {
    console.log('我收到你的响应啦', response)
    // 未登录或无权限则跳转到登录页
    if (response?.data?.code === 40100 || response?.data?.code === 40101) {
        const redirectUrl = window.location.href;
        if (!window.location.pathname.startsWith('/user/login')) {
            window.location.href = `/user/login?redirect=${redirectUrl}`;
        }
    }
    // Do something with response data
    return response.data;
}, function (error) {
    // Do something with response error
    return Promise.reject(error);
});

export default myAxios;
