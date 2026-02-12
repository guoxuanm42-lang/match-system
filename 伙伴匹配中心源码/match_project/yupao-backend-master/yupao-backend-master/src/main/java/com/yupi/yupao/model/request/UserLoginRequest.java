package com.yupi.yupao.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author Ethan
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    @NotBlank(message = "账号不能为空")
    @Size(min = 4, max = 256, message = "账号长度不合法")
    private String userAccount;

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 256, message = "密码长度不合法")
    private String userPassword;
}
