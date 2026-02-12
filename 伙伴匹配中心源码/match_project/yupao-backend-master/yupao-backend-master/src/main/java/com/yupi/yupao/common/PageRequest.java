package com.yupi.yupao.common;

import lombok.Data;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * 通用分页请求参数
 *
 * @author Ethan
 */
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = -5860707094194210842L;

    /**
     * 页面大小
     */
    @Min(value = 1, message = "pageSize 必须大于等于 1")
    protected int pageSize = 10;

    /**
     * 当前是第几页
     */
    @Min(value = 1, message = "pageNum 必须大于等于 1")
    protected int pageNum = 1;
}
