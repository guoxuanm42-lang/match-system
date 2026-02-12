package com.yupi.yupao.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用删除请求（用于只传一个 id 的删除类操作）。
 *
 * @author Ethan
 */
@Data
public class DeleteRequest implements Serializable {

    private static final long serialVersionUID = -5860707094194210842L;

    private long id;
}
