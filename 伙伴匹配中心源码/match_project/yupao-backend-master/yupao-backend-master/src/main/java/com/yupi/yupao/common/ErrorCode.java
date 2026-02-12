package com.yupi.yupao.common;

/**
 * 错误码枚举（用于统一定义接口返回的错误类型）。
 *
 * @author Ethan
 */
public enum ErrorCode {

    SUCCESS(0, "ok", ""),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001, "请求数据为空", ""),
    NOT_LOGIN(40100, "未登录", ""),
    NO_AUTH(40101, "无权限", ""),
    FORBIDDEN(40301, "禁止操作", ""),
    SYSTEM_ERROR(50000, "系统内部异常", "");

    private final int code;

    /**
     * 状态码信息
     */
    private final String message;

    /**
     * 状态码描述（详情）
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    /**
     * 获取错误码的 code。
     *
     * @return 状态码
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取错误码的提示信息。
     *
     * @return 提示信息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 获取错误码的详细描述。
     *
     * @return 详细描述
     */
    public String getDescription() {
        return description;
    }
}
