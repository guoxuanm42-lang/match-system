package com.yupi.yupao.common;

/**
 * 返回工具类（用于快速构造统一返回对象 BaseResponse）。
 *
 * @author Ethan
 */
public class ResultUtils {

    /**
     * 构造成功返回。
     *
     * @param data 返回的数据
     * @param <T> data 的类型
     * @return 统一返回结构（code=0，message=ok）
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 构造失败返回（直接使用错误码枚举）。
     *
     * @param errorCode 错误码枚举
     * @return 统一返回结构（data 为 null）
     */
    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 构造失败返回（自定义 code / message / description）。
     *
     * @param code 状态码
     * @param message 提示信息
     * @param description 详细描述
     * @return 统一返回结构（data 为 null）
     */
    public static BaseResponse error(int code, String message, String description) {
        return new BaseResponse(code, null, message, description);
    }

    /**
     * 构造失败返回（使用错误码的 code，同时自定义 message / description）。
     *
     * @param errorCode 错误码枚举
     * @param message 提示信息
     * @param description 详细描述
     * @return 统一返回结构（data 为 null）
     */
    public static BaseResponse error(ErrorCode errorCode, String message, String description) {
        return new BaseResponse(errorCode.getCode(), null, message, description);
    }

    /**
     * 构造失败返回（使用错误码的 code 和 message，同时自定义 description）。
     *
     * @param errorCode 错误码枚举
     * @param description 详细描述
     * @return 统一返回结构
     */
    public static BaseResponse error(ErrorCode errorCode, String description) {
        return new BaseResponse(errorCode.getCode(), errorCode.getMessage(), description);
    }
}
