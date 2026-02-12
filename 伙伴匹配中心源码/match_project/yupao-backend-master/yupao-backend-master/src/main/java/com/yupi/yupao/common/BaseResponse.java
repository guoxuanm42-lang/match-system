package com.yupi.yupao.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @param <T> data 字段的数据类型
 * @author Ethan
 */
@Data
public class BaseResponse<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    private String description;

    /**
     * 构造统一返回对象（完整字段）。
     *
     * @param code 状态码（0 表示成功，其他表示失败）
     * @param data 返回数据
     * @param message 提示信息（例如 ok / 错误提示）
     * @param description 详细描述（可为空字符串）
     */
    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    /**
     * 构造统一返回对象（不传 description 时默认为空）。
     *
     * @param code 状态码
     * @param data 返回数据
     * @param message 提示信息
     */
    public BaseResponse(int code, T data, String message) {
        this(code, data, message, "");
    }

    /**
     * 构造统一返回对象（只传 code 和 data）。
     *
     * @param code 状态码
     * @param data 返回数据
     */
    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }

    /**
     * 构造失败返回对象（从错误码中读取 code / message / description）。
     *
     * @param errorCode 错误码枚举
     */
    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }
}
