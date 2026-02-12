package com.yupi.yupao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yupao.model.domain.Team;
import com.yupi.yupao.model.domain.User;
import com.yupi.yupao.model.dto.TeamQuery;
import com.yupi.yupao.model.request.TeamJoinRequest;
import com.yupi.yupao.model.request.TeamQuitRequest;
import com.yupi.yupao.model.request.TeamUpdateRequest;
import com.yupi.yupao.model.vo.TeamUserVO;

import java.util.List;

/**
 * 队伍服务（队伍的创建、查询、加入、退出、解散等核心业务）。
 *
 * @author Ethan
 */
public interface TeamService extends IService<Team> {

    /**
     * 创建队伍。
     *
     * @param team 队伍信息（名称、描述、人数上限、状态等）
     * @param loginUser 当前登录用户（创建者）
     * @return 新创建的队伍 id
     */
    long addTeam(Team team, User loginUser);

    /**
     * 查询队伍列表（支持筛选）。
     *
     * @param teamQuery 查询条件
     * @param isAdmin 是否管理员（管理员可查询更多状态的队伍）
     * @return 队伍列表（包含队长信息等扩展字段）
     */
    List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin);

    /**
     * 更新队伍信息。
     *
     * @param teamUpdateRequest 更新队伍请求体
     * @param loginUser 当前登录用户
     * @return true 表示更新成功
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);

    /**
     * 加入队伍。
     *
     * @param teamJoinRequest 加入队伍请求体
     * @param loginUser 当前登录用户
     * @return true 表示加入成功
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);

    /**
     * 退出队伍。
     *
     * @param teamQuitRequest 退出队伍请求体
     * @param loginUser 当前登录用户
     * @return true 表示退出成功
     */
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    /**
     * 删除（解散）队伍。
     *
     * @param id 队伍 id
     * @param loginUser 当前登录用户
     * @return true 表示删除成功
     */
    boolean deleteTeam(long id, User loginUser);
}
