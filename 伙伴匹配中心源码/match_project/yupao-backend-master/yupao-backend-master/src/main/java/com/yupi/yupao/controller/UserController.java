package com.yupi.yupao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.yupao.common.BaseResponse;
import com.yupi.yupao.common.ErrorCode;
import com.yupi.yupao.common.ResultUtils;
import com.yupi.yupao.exception.BusinessException;
import com.yupi.yupao.model.domain.User;
import com.yupi.yupao.model.request.UserLoginRequest;
import com.yupi.yupao.model.request.UserRegisterRequest;
import com.yupi.yupao.model.vo.UserVO;
import com.yupi.yupao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.yupi.yupao.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author Ethan
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 用户注册接口。
     *
     * <p>用途：创建新用户账号，并返回新用户 id。</p>
     *
     * @param userRegisterRequest 注册请求体（账号、密码、确认密码、星球编号）
     * @return 统一返回结构，data 为新用户 id
     * @throws BusinessException 请求参数为空、参数校验不通过时抛出
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录接口。
     *
     * <p>用途：校验账号密码，登录成功后在 Session 中写入登录态，并返回用户信息。</p>
     *
     * @param userLoginRequest 登录请求体（账号、密码）
     * @param request Http 请求对象（用于写入 / 获取 Session）
     * @return 统一返回结构，data 为用户信息（含登录态）
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@Valid @RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    /**
     * 用户退出登录接口。
     *
     * <p>用途：清理 Session 中的登录态。</p>
     *
     * @param request Http 请求对象（用于获取 Session）
     * @return 统一返回结构，data 为整型结果（一般 1 表示成功）
     * @throws BusinessException request 为空时抛出
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户接口。
     *
     * <p>用途：从 Session 中读取登录态，返回脱敏后的当前用户信息。</p>
     *
     * @param request Http 请求对象（用于获取 Session）
     * @return 统一返回结构，data 为当前用户信息（脱敏后）
     * @throws BusinessException 未登录时抛出
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = currentUser.getId();
        // TODO 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    /**
     * 搜索用户接口（管理员）。
     *
     * <p>用途：管理员按用户名关键字搜索用户列表，返回脱敏后的用户信息。</p>
     *
     * @param username 用户名关键字（可选）
     * @param request Http 请求对象（用于判断是否管理员）
     * @return 统一返回结构，data 为用户列表（脱敏后）
     * @throws BusinessException 非管理员访问时抛出
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    /**
     * 按标签搜索用户接口。
     *
     * <p>用途：根据标签列表筛选用户。</p>
     *
     * @param tagNameList 标签名列表（例如 Java / Spring）
     * @return 统一返回结构，data 为匹配到的用户列表
     * @throws BusinessException 标签列表为空时抛出
     */
    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchUsersByTags(@RequestParam(required = false) List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<User> userList = userService.searchUsersByTags(tagNameList);
        return ResultUtils.success(userList);
    }

    /**
     * 推荐用户接口（分页）。
     *
     * <p>用途：按分页获取推荐用户列表。该接口会优先读取 Redis 缓存，缓存没有再查数据库。</p>
     *
     * @param pageSize 每页条数
     * @param pageNum 当前页码
     * @param request Http 请求对象（用于获取当前登录用户）
     * @return 统一返回结构，data 为分页结果
     * @throws BusinessException 未登录时抛出
     */
    @GetMapping("/recommend")
    public BaseResponse<Page<User>> recommendUsers(long pageSize, long pageNum, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        String redisKey = String.format("yupao:user:recommend:%s", loginUser.getId());
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        // 如果有缓存，直接读缓存
        Page<User> userPage = (Page<User>) valueOperations.get(redisKey);
        if (userPage != null) {
            return ResultUtils.success(userPage);
        }
        // 无缓存，查数据库
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        userPage = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
        // 写缓存
        try {
            valueOperations.set(redisKey, userPage, 30000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("redis set key error", e);
        }
        return ResultUtils.success(userPage);
    }


    /**
     * 更新用户接口。
     *
     * <p>用途：更新用户信息（只允许修改允许修改的字段）。</p>
     *
     * @param user 用户对象（包含要更新的字段）
     * @param request Http 请求对象（用于获取当前登录用户）
     * @return 统一返回结构，data 为影响行数（一般 1 表示成功）
     * @throws BusinessException 请求参数为空、未登录、业务校验不通过时抛出
     */
    @PostMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody User user, HttpServletRequest request) {
        // 校验参数是否为空
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        int result = userService.updateUser(user, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 删除用户接口（管理员）。
     *
     * <p>用途：管理员删除指定用户。</p>
     *
     * @param id 用户 id
     * @param request Http 请求对象（用于判断是否管理员）
     * @return 统一返回结构，data 为 true 表示删除成功
     * @throws BusinessException 非管理员、id 不合法时抛出
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 获取最匹配的用户
     *
     * @param num 匹配数量（1-20）
     * @param request Http 请求对象（用于获取当前登录用户）
     * @return 统一返回结构，data 为匹配到的用户列表
     * @throws BusinessException num 不合法、未登录时抛出
     */
    @GetMapping("/match")
    public BaseResponse<List<User>> matchUsers(long num, HttpServletRequest request) {
        if (num <= 0 || num > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.matchUsers(num, user));
    }

}
