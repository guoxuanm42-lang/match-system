package com.yupi.yupao.exception;

import com.yupi.yupao.common.ErrorCode;

/**
 * 业务异常（用于在业务校验不通过时，携带错误码返回给前端）。
 *
 * @author Ethan
 */
public class BusinessException extends RuntimeException {

    private final int code;

    private final String description;

    /**
     * 构造业务异常（自定义 message / code / description）。
     *
     * @param message 错误提示信息
     * @param code 错误码
     * @param description 详细描述
     */
    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    /**
     * 构造业务异常（使用错误码枚举）。
     *
     * @param errorCode 错误码枚举
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    /**
     * 构造业务异常（使用错误码枚举并覆盖 description）。
     *
     * @param errorCode 错误码枚举
     * @param description 详细描述
     */
    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    /**
     * 获取错误码。
     *
     * @return 错误码
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取详细描述。
     *
     * @return 详细描述
     */
    public String getDescription() {
        return description;
    }
}
