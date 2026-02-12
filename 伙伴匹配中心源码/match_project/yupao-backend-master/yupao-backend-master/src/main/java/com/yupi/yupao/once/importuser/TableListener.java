package com.yupi.yupao.once.importuser;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;

/**
 * Excel 读取监听器（用于接收 EasyExcel 的解析回调）。
 *
 * @author Ethan
 */
@Slf4j
public class TableListener implements ReadListener<XingQiuTableUserInfo> {

    /**
     * 逐行解析回调（每解析到一行都会触发）。
     *
     * @param data 当前行数据
     * @param context 解析上下文
     */
    @Override
    public void invoke(XingQiuTableUserInfo data, AnalysisContext context) {
        System.out.println(data);
    }

    /**
     * 解析完成回调（所有数据解析完成后触发）。
     *
     * @param context 解析上下文
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        System.out.println("已解析完成");
    }
}
