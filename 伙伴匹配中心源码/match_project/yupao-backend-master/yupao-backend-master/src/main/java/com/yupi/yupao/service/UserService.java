package com.yupi.yupao.service;

import com.yupi.yupao.common.BaseResponse;
import com.yupi.yupao.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yupao.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务（用户注册、登录、鉴权、资料更新、匹配等业务）。
 *
 * @author Ethan
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @param planetCode    星球编号
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode);

    /**
     * 用户登录。
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request Http 请求对象（用于写入/读取 Session 登录态）
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏（去掉密码等敏感字段）。
     *
     * @param originUser 原始用户对象
     * @return 脱敏后的用户对象
     */
    User getSafetyUser(User originUser);

    /**
     * 用户注销（退出登录）。
     *
     * @param request Http 请求对象（用于清理 Session 登录态）
     * @return 整型结果（一般 1 表示成功）
     */
    int userLogout(HttpServletRequest request);

    /**
     * 根据标签搜索用户。
     *
     * @param tagNameList 标签名列表
     * @return 匹配到的用户列表
     */
    List<User> searchUsersByTags(List<String> tagNameList);

    /**
     * 更新用户信息。
     *
     * @param user 要更新的用户信息（包含要修改的字段）
     * @param loginUser 当前登录用户
     * @return 影响行数（一般 1 表示成功）
     */
    int updateUser(User user, User loginUser);

    /**
     * 获取当前登录用户信息。
     *
     * @param request Http 请求对象（用于获取 Session 登录态）
     * @return 当前登录用户
     * @throws BusinessException request 为空或未登录时抛出
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 判断当前请求对应的用户是否为管理员。
     *
     * @param request Http 请求对象
     * @return true 表示管理员
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 判断指定用户是否为管理员。
     *
     * @param loginUser 用户
     * @return true 表示管理员
     */
    boolean isAdmin(User loginUser);

    /**
     * 匹配用户（根据标签等维度返回最匹配的用户）。
     *
     * @param num 匹配数量
     * @param loginUser 当前登录用户
     * @return 匹配到的用户列表
     */
    List<User> matchUsers(long num, User loginUser);
}
