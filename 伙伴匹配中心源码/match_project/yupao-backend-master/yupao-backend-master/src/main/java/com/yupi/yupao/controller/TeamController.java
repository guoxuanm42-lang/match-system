package com.yupi.yupao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.yupao.common.BaseResponse;
import com.yupi.yupao.common.DeleteRequest;
import com.yupi.yupao.common.ErrorCode;
import com.yupi.yupao.common.ResultUtils;
import com.yupi.yupao.exception.BusinessException;
import com.yupi.yupao.model.domain.Team;
import com.yupi.yupao.model.domain.User;
import com.yupi.yupao.model.domain.UserTeam;
import com.yupi.yupao.model.dto.TeamQuery;
import com.yupi.yupao.model.request.TeamAddRequest;
import com.yupi.yupao.model.request.TeamJoinRequest;
import com.yupi.yupao.model.request.TeamQuitRequest;
import com.yupi.yupao.model.request.TeamUpdateRequest;
import com.yupi.yupao.model.vo.TeamUserVO;
import com.yupi.yupao.service.TeamService;
import com.yupi.yupao.service.UserService;
import com.yupi.yupao.service.UserTeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 队伍接口
 *
 * @author Ethan
 */
@RestController
@RequestMapping("/team")
@Validated
@Slf4j
public class TeamController {

    @Resource
    private UserService userService;

    @Resource
    private TeamService teamService;

    @Resource
    private UserTeamService userTeamService;

    /**
     * 创建队伍接口。
     *
     * <p>用途：创建一个队伍，并返回新队伍的 id。</p>
     *
     * @param teamAddRequest 创建队伍请求体（队伍名称、描述、人数上限、过期时间、状态、密码等）
     * @param request Http 请求对象（用于获取当前登录用户）
     * @return 统一返回结构，data 为新创建的队伍 id
     * @throws BusinessException 请求参数为空、未登录、业务校验不通过时抛出
     */
    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
        if (teamAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Team team = new Team();
        BeanUtils.copyProperties(teamAddRequest, team);
        long teamId = teamService.addTeam(team, loginUser);
        return ResultUtils.success(teamId);
    }

    /**
     * 更新队伍接口。
     *
     * <p>用途：更新队伍信息（例如名称、描述、过期时间、状态、密码等）。</p>
     *
     * @param teamUpdateRequest 更新队伍请求体（包含队伍 id 以及要更新的字段）
     * @param request Http 请求对象（用于获取当前登录用户）
     * @return 统一返回结构，data 为 true 表示更新成功
     * @throws BusinessException 请求参数为空、未登录、无权限、更新失败时抛出
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest, HttpServletRequest request) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.updateTeam(teamUpdateRequest, loginUser);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 获取队伍详情接口。
     *
     * <p>用途：根据队伍 id 查询队伍信息。</p>
     *
     * @param id 队伍 id
     * @return 统一返回结构，data 为队伍信息
     * @throws BusinessException id 不合法、队伍不存在时抛出
     */
    @GetMapping("/get")
    public BaseResponse<Team> getTeamById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = teamService.getById(id);
        if (team == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(team);
    }

    /**
     * 查询队伍列表接口（支持条件筛选）。
     *
     * <p>用途：按条件查询队伍列表，并附带“我是否已加入”和“已加入人数”等信息。</p>
     *
     * @param teamQuery 查询条件（可选：id、关键词、最大人数、队长用户 id、状态、分页参数等）
     * @param request Http 请求对象（用于判断是否管理员、获取当前登录用户）
     * @return 统一返回结构，data 为队伍列表（TeamUserVO）
     * @throws BusinessException 请求参数为空时抛出
     */
    @GetMapping("/list")
    public BaseResponse<List<TeamUserVO>> listTeams(@Valid TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean isAdmin = userService.isAdmin(request);
        // 1、查询队伍列表
        List<TeamUserVO> teamList = teamService.listTeams(teamQuery, isAdmin);
        final List<Long> teamIdList = teamList.stream().map(TeamUserVO::getId).collect(Collectors.toList());
        // 2、判断当前用户是否已加入队伍
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        try {
            User loginUser = userService.getLoginUser(request);
            userTeamQueryWrapper.eq("userId", loginUser.getId());
            userTeamQueryWrapper.in("teamId", teamIdList);
            List<UserTeam> userTeamList = userTeamService.list(userTeamQueryWrapper);
            // 已加入的队伍 id 集合
            Set<Long> hasJoinTeamIdSet = userTeamList.stream().map(UserTeam::getTeamId).collect(Collectors.toSet());
            teamList.forEach(team -> {
                boolean hasJoin = hasJoinTeamIdSet.contains(team.getId());
                team.setHasJoin(hasJoin);
            });
        } catch (Exception e) {
        }
        // 3、查询已加入队伍的人数
        QueryWrapper<UserTeam> userTeamJoinQueryWrapper = new QueryWrapper<>();
        userTeamJoinQueryWrapper.in("teamId", teamIdList);
        List<UserTeam> userTeamList = userTeamService.list(userTeamJoinQueryWrapper);
        // 队伍 id => 加入这个队伍的用户列表
        Map<Long, List<UserTeam>> teamIdUserTeamList = userTeamList.stream().collect(Collectors.groupingBy(UserTeam::getTeamId));
        teamList.forEach(team -> team.setHasJoinNum(teamIdUserTeamList.getOrDefault(team.getId(), new ArrayList<>()).size()));
        return ResultUtils.success(teamList);
    }

    /**
     * 分页查询队伍接口。
     *
     * <p>用途：按条件分页查询队伍基础信息（未封装 TeamUserVO）。</p>
     *
     * @param teamQuery 查询条件（包含 pageNum / pageSize）
     * @return 统一返回结构，data 为分页结果
     * @throws BusinessException 请求参数为空时抛出
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> listTeamsByPage(@Valid TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtils.copyProperties(teamQuery, team);
        Page<Team> page = new Page<>(teamQuery.getPageNum(), teamQuery.getPageSize());
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> resultPage = teamService.page(page, queryWrapper);
        return ResultUtils.success(resultPage);
    }

    /**
     * 加入队伍接口。
     *
     * <p>用途：当前登录用户加入指定队伍（加密队伍需要密码）。</p>
     *
     * @param teamJoinRequest 加入队伍请求体（teamId + password）
     * @param request Http 请求对象（用于获取当前登录用户）
     * @return 统一返回结构，data 为 true 表示加入成功
     * @throws BusinessException 请求参数为空、未登录、队伍状态不允许加入、人数已满等情况抛出
     */
    @PostMapping("/join")
    public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest, HttpServletRequest request) {
        if (teamJoinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.joinTeam(teamJoinRequest, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 退出队伍接口。
     *
     * <p>用途：当前登录用户退出指定队伍；如果队长退出，会按业务逻辑转让/解散队伍。</p>
     *
     * @param teamQuitRequest 退出队伍请求体（teamId）
     * @param request Http 请求对象（用于获取当前登录用户）
     * @return 统一返回结构，data 为 true 表示退出成功
     * @throws BusinessException 请求参数为空、未登录、未加入该队伍等情况抛出
     */
    @PostMapping("/quit")
    public BaseResponse<Boolean> quitTeam(@RequestBody TeamQuitRequest teamQuitRequest, HttpServletRequest request) {
        if (teamQuitRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.quitTeam(teamQuitRequest, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 删除队伍接口。
     *
     * <p>用途：删除指定队伍（通常仅队长或管理员可操作）。</p>
     *
     * @param deleteRequest 删除请求体（包含队伍 id）
     * @param request Http 请求对象（用于获取当前登录用户）
     * @return 统一返回结构，data 为 true 表示删除成功
     * @throws BusinessException 请求参数不合法、未登录、无权限、删除失败时抛出
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.deleteTeam(id, loginUser);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return ResultUtils.success(true);
    }


    /**
     * 获取我创建的队伍列表接口。
     *
     * <p>用途：查询当前登录用户作为队长创建的队伍列表。</p>
     *
     * @param teamQuery 查询条件（可选；常用是分页参数）
     * @param request Http 请求对象（用于获取当前登录用户）
     * @return 统一返回结构，data 为队伍列表（TeamUserVO）
     * @throws BusinessException 请求参数为空、未登录时抛出
     */
    @GetMapping("/list/my/create")
    public BaseResponse<List<TeamUserVO>> listMyCreateTeams(@Valid TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        teamQuery.setUserId(loginUser.getId());
        List<TeamUserVO> teamList = teamService.listTeams(teamQuery, true);
        return ResultUtils.success(teamList);
    }


    /**
     * 获取我加入的队伍列表接口。
     *
     * <p>用途：查询当前登录用户加入过的队伍列表。</p>
     *
     * @param teamQuery 查询条件（可选；常用是分页参数）
     * @param request Http 请求对象（用于获取当前登录用户）
     * @return 统一返回结构，data 为队伍列表（TeamUserVO）
     * @throws BusinessException 请求参数为空、未登录时抛出
     */
    @GetMapping("/list/my/join")
    public BaseResponse<List<TeamUserVO>> listMyJoinTeams(@Valid TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", loginUser.getId());
        List<UserTeam> userTeamList = userTeamService.list(queryWrapper);
        // 取出不重复的队伍 id
        // teamId userId
        // 1, 2
        // 1, 3
        // 2, 3
        // result
        // 1 => 2, 3
        // 2 => 3
        Map<Long, List<UserTeam>> listMap = userTeamList.stream()
                .collect(Collectors.groupingBy(UserTeam::getTeamId));
        List<Long> idList = new ArrayList<>(listMap.keySet());
        teamQuery.setIdList(idList);
        List<TeamUserVO> teamList = teamService.listTeams(teamQuery, true);
        return ResultUtils.success(teamList);
    }
}
























