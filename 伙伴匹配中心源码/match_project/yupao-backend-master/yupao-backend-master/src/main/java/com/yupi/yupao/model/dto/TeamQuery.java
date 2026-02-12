package com.yupi.yupao.model.dto;

import com.yupi.yupao.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;


/**
 * 队伍查询封装类
 *
 * @author Ethan
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TeamQuery extends PageRequest {
    /**
     * id
     */
    @Min(value = 1, message = "id 必须大于等于 1")
    private Long id;

    /**
     * id 列表
     */
    private List<Long> idList;

    /**
     * 搜索关键词（同时对队伍名称和描述搜索）
     */
    private String searchText;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */
    @Min(value = 1, message = "maxNum 必须大于等于 1")
    @Max(value = 20, message = "maxNum 必须小于等于 20")
    private Integer maxNum;

    /**
     * 用户id
     */
    @Min(value = 1, message = "userId 必须大于等于 1")
    private Long userId;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    @Min(value = 0, message = "status 不合法")
    @Max(value = 2, message = "status 不合法")
    private Integer status;
}
