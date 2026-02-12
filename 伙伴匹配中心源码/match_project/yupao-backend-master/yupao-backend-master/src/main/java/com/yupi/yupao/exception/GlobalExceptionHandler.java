package com.yupi.yupao.exception;

import com.yupi.yupao.common.BaseResponse;
import com.yupi.yupao.common.ErrorCode;
import com.yupi.yupao.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * 全局异常处理器
 *
 * @author Ethan
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理业务异常（BusinessException）。
     *
     * @param e 业务异常
     * @return 统一返回结构（包含错误码、错误信息、描述）
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.error("businessException: " + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    /**
     * 处理参数校验异常（@RequestBody + @Valid）。
     *
     * @param e 参数校验异常
     * @return 统一返回结构（参数错误）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError == null ? "请求参数错误" : fieldError.getDefaultMessage();
        return ResultUtils.error(ErrorCode.PARAMS_ERROR.getCode(), message, "");
    }

    /**
     * 处理参数绑定/校验异常（@ModelAttribute + @Valid，例如 GET 查询参数绑定到对象）。
     *
     * @param e 参数绑定异常
     * @return 统一返回结构（参数错误）
     */
    @ExceptionHandler(BindException.class)
    public BaseResponse bindExceptionHandler(BindException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError == null ? "请求参数错误" : fieldError.getDefaultMessage();
        return ResultUtils.error(ErrorCode.PARAMS_ERROR.getCode(), message, "");
    }

    /**
     * 处理单参数校验异常（例如 @RequestParam + @Min）。
     *
     * @param e 单参数校验异常
     * @return 统一返回结构（参数错误）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResponse constraintViolationExceptionHandler(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("请求参数错误");
        return ResultUtils.error(ErrorCode.PARAMS_ERROR.getCode(), message, "");
    }

    /**
     * 处理未被捕获的运行时异常（RuntimeException）。
     *
     * @param e 运行时异常
     * @return 统一返回结构（系统错误）
     */
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }
}
